package fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.du_an_1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import adapter.ItemCategoriesAddProduct;
import adapter.ProductAdapter;
import dao.CategoriesDao;
import dao.ProductDAO;
import model.Categories;
import model.Product;

public class ManageProductFragment extends Fragment implements ItemCategoriesAddProduct.OnItemClickListener {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ProductDAO productDAO;
    private FloatingActionButton floatingActionButton;
    private Context context;
    private Bitmap bitmap;
    private ImageView imageView;
    private int idShop;
    private int idUser;
    private int idCategories;
    private String sidCategories;
    private String nameCategories;
    private List<Product> productList;
    private List<Categories> categoriesList;
    private RecyclerView recyclerView, recyclerView1;
    private ProductAdapter adapter;
    private ItemCategoriesAddProduct adapter1;
    private CategoriesDao categoriesDao;
    private TextView txt_categories;
    private int statusProduct;
    private Dialog dialog1;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_manage_product, container, false);

        productDAO = new ProductDAO(context);
        categoriesDao = new CategoriesDao(context);
        floatingActionButton = view.findViewById(R.id.fab_add_product);
        recyclerView = view.findViewById(R.id.rcv_manage_product);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));

        loadProductList();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(context, androidx.appcompat.R.style.Theme_AppCompat_Light);
                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogView = inflater.inflate(R.layout.dialog_information_product, null);

                dialog.setContentView(dialogView);

                idUser = getIdUserFromSharedPreferences();
                idShop = categoriesDao.getIdShop(idUser);

                EditText edt_name = dialogView.findViewById(R.id.edt_name_product);
                EditText edt_price = dialogView.findViewById(R.id.edt_price_product);
                EditText edt_note = dialogView.findViewById(R.id.edt_note_product);
                imageView = dialogView.findViewById(R.id.img_selected);
                ImageView img_back = dialogView.findViewById(R.id.img_back_information);

                txt_categories = dialogView.findViewById(R.id.txt_categories_product);
                Button btn_select_image = dialogView.findViewById(R.id.btn_select_image);
                Button btn_save = dialogView.findViewById(R.id.btn_save_product);
                Switch switch1 = dialogView.findViewById(R.id.sw_status_product);

                img_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                txt_categories.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1 = new Dialog(context, androidx.appcompat.R.style.Theme_AppCompat_Light);
                        LayoutInflater inflater = LayoutInflater.from(context);
                        View dialogView = inflater.inflate(R.layout.dialog_add_categories_product, null);
                        dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialog1.setContentView(dialogView);


                        ImageView img_back1 = dialogView.findViewById(R.id.img_add_categories_back);
                        img_back1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog1.dismiss();
                            }
                        });

                        recyclerView1 = dialogView.findViewById(R.id.rcv_item_categories_add_product);
                        recyclerView1.setLayoutManager(new GridLayoutManager(context, 1));

                        categoriesList = categoriesDao.getAllCategories();
                        adapter1 = new ItemCategoriesAddProduct(context, categoriesList);
                        adapter1.setOnItemClickListener(ManageProductFragment.this);
                        recyclerView1.setAdapter(adapter1);

                        dialog1.show();

                    }

                });


                btn_select_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openImagePicker();
                    }
                });

                switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (switch1.isChecked()) {
                            statusProduct = 1;
                            switch1.setThumbTintList(ContextCompat.getColorStateList(context, R.color.green_color));
                            switch1.setTrackTintList(ContextCompat.getColorStateList(context, R.color.green_color));
                            Toast.makeText(context, "Còn hàng", Toast.LENGTH_SHORT).show();
                        } else {
                            statusProduct = 0;
                            switch1.setThumbTintList(ContextCompat.getColorStateList(context, R.color.default_color));
                            switch1.setTrackTintList(ContextCompat.getColorStateList(context, R.color.default_color));
                            Toast.makeText(context, "Hết hàng", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                btn_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String productName = edt_name.getText().toString();
                        String sProductPrice = edt_price.getText().toString();
                        String productNote = edt_note.getText().toString();
                        String sStatusProduct = String.valueOf(statusProduct);

                        // Kiểm tra điều kiện đầu vào
                        if (bitmap != null && !productName.isEmpty() && !sProductPrice.isEmpty() && !sidCategories.isEmpty() && !sStatusProduct.isEmpty()) {
                            try {
                                double productPrice = Double.parseDouble(sProductPrice);
                                int sold = 0;

                                int targetWidth = 600;
                                int targetHeight = 600;
                                bitmap = resizeBitmap(bitmap, targetWidth, targetHeight);
                                byte[] imageBytes = getBitmapAsByteArray(bitmap);

                                Product product = new Product();
                                product.setName(productName);
                                product.setImage(imageBytes);
                                product.setStatus(statusProduct);
                                product.setPrice(productPrice);
                                product.setIdShop(idShop);
                                product.setNote(productNote);
                                product.setSold(sold);
                                product.setIdCategories(idCategories);

                                long check = productDAO.addProduct(idCategories, idShop, productName, imageBytes, productPrice, productNote, statusProduct, sold);

                                if (check > 0) {
                                    productList.add(product);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(context, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                                    loadProductList();
                                } else {
                                    Toast.makeText(context, "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                            } catch (NumberFormatException e) {
                                // Xử lý lỗi khi giá trị không phải số hợp lệ
                                Toast.makeText(context, "Giá sản phẩm không hợp lệ", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        }
                    }
                });                dialog.show();
                loadProductList();
            }
        });


        return view;
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

    public Bitmap resizeBitmap(Bitmap originalBitmap, int newWidth, int newHeight) {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, false);
        return resizedBitmap;
    }

    public int getIdUserFromSharedPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("User_Login", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("idUser", -1);
    }

    private void loadProductList() {
        idUser = getIdUserFromSharedPreferences();
        idShop = categoriesDao.getIdShop(idUser);
        productList = productDAO.getProductsByIdShop(idShop);
        adapter = new ProductAdapter(context, productList, productDAO);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(Categories categories) {
        idCategories = categories.getIdCategories();
        nameCategories = categories.getName();
        txt_categories.setText(nameCategories);
        sidCategories = String.valueOf(idCategories);
        Toast.makeText(context, "Bạn đã chọn Id thể loại: " + idCategories + " Tên: " + nameCategories, Toast.LENGTH_SHORT).show();
        dialog1.dismiss();

    }

    @Override
    public void onResume() {
        super.onResume();
        loadProductList();
    }


}
