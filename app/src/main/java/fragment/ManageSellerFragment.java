package fragment;


import static android.app.Activity.RESULT_OK;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.du_an_1.R;
import com.example.du_an_1.databinding.FragmentManageSellerBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import dao.ProductDAO;
import dao.ShopDAO;
import dao.UserDAO;
import model.Product;
import model.Shop;
import model.User;

public class ManageSellerFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST_1 = 1;
    private Bitmap bitmap;
    private ImageView img_shop;
    private LinearLayout ll_content, ll_menu_booth, ll_parent;
    private FragmentManageSellerBinding binding;
    private ShopDAO shopDAO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManageSellerBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // layout
        ll_menu_booth = view.findViewById(R.id.ll_menu);
        ll_content = view.findViewById(R.id.ll_content);
        ll_parent = view.findViewById(R.id.ll_parent);
        setHeight();

        //fragment
        replaceFragment(new ManageProductFragment());
        binding.imgProduct.setVisibility(View.VISIBLE);
        binding.imgCategory.setVisibility(View.GONE);


        binding.txtProduct.setOnClickListener(view1 -> {
            replaceFragment(new ManageProductFragment());
            binding.imgProduct.setVisibility(View.VISIBLE);
            binding.imgCategory.setVisibility(View.GONE);
        });
        binding.txtCategory.setOnClickListener(view1 -> {
            replaceFragment(new ManageCategoryFragment());
            binding.imgProduct.setVisibility(View.GONE);
            binding.imgCategory.setVisibility(View.VISIBLE);
        });
        getShop();

        binding.imgInformatinonShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateShop();
            }
        });

        binding.llStatusProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Xác nhận");
                builder.setMessage("Bạn có muốn ngừng shop?");
                Shop shop = shopDAO.getShopByIdUser(getIdUserFromSharedPreferences());
                // Nút "Có"
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ProductDAO productDAO = new ProductDAO(getContext());
                        List<Product> productList = productDAO.getProductsByIdShop(shop.getIdShop());
                        for(Product product:productList){
                            product.setStatus(0);
                            boolean check = productDAO.upProduct(product);
                        }
                        productList.clear();
                        
                    }
                });

                // Nút "Không"
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Hủy hộp thoại khi người dùng chọn "Không"
                        dialog.dismiss();
                    }
                });

                // Hiển thị hộp thoại
                AlertDialog dialog = builder.create();
                dialog.show();            }
        });

        return view;
    }

    public void setHeight() {
        ll_parent.post(new Runnable() {
            @Override
            public void run() {
                int heightMax = ll_parent.getHeight();
                int menuHeight = ll_menu_booth.getHeight();
                int heightContent = heightMax - menuHeight;
                ViewGroup.LayoutParams params = ll_content.getLayoutParams();
                params.height = heightContent;
                ll_content.setLayoutParams(params);
            }
        });

        binding.imgInformatinonShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateShop();
            }
        });
    }

    // Replace fragment method
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragme_manage_sell, fragment);
        fragmentTransaction.commit();
    }

    public void getShop() {
        int idUser = getIdUserFromSharedPreferences();
        if (idUser != -1) {
            shopDAO = new ShopDAO(requireContext());
            Shop shop = shopDAO.getShopByIdUser(idUser);
            User user = new UserDAO(requireContext()).getUserByID(idUser);
            if (shop != null) {
                binding.txtNameShop.setText(shop.getName());
                binding.txtAddressShop.setText(shop.getAddress());
                binding.imgShop.setImageBitmap(convertByteArrayToBitmap(shop.getImage()));
                binding.txtPhoneShop.setText("0" + String.valueOf(user.getPhone()));
            }
        }
    }

    public void updateShop() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_update_shop);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        img_shop = dialog.findViewById(R.id.img_image_editShop);
        TextView txt_select = dialog.findViewById(R.id.txt_select_edit_shop);
        TextView txt_phone = dialog.findViewById(R.id.txt_phone_edit_shop);
        TextView txt_address = dialog.findViewById(R.id.txt_address_edit_shop);
        TextView txt_canel = dialog.findViewById(R.id.txt_cancel_edit_shop);
        TextView txt_update = dialog.findViewById(R.id.txt_update_edit_address);

        int idUser = getIdUserFromSharedPreferences();
        shopDAO = new ShopDAO(requireContext());
        UserDAO userDAO = new UserDAO(requireContext());

        Shop shop = shopDAO.getShopByIdUser(idUser);
        User user = userDAO.getUserByID(getIdUserFromSharedPreferences());

        if (shop.getImage() != null) {
            img_shop.setImageBitmap(convertByteArrayToBitmap(shop.getImage()));
        } else {
            img_shop.setImageResource(R.drawable.side_nav_bar);
        }
        txt_phone.setText(String.valueOf(user.getPhone()));
        txt_address.setText(user.getAddress());

        txt_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });

        txt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sPhone = txt_phone.getText().toString();
                String Address = txt_address.getText().toString();

                if (!sPhone.isEmpty() && !Address.isEmpty()) {
                    long phone = Long.parseLong(sPhone);

                    if (bitmap != null) {
                        int targetWidth = 600;
                        int targetHeight = 600;
                        bitmap = resizeBitmap(bitmap, targetWidth, targetHeight);
                        byte[] imageBytes = getBitmapAsByteArray(bitmap);
                        shop.setImage(imageBytes);
                    }
                    user.setAddress(Address);
                    user.setPhone(phone);
                    shop.setAddress(Address);

                    boolean updateUser = userDAO.upDateUser(user);
                    boolean updateShop = shopDAO.UpdateShop(shop);

                    if (updateUser && updateShop) {
                        Toast.makeText(getContext(), "Cập nhập Shop thành công", Toast.LENGTH_SHORT).show();
                    }
                    getShop();
                    dialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });
        txt_canel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public int getIdUserFromSharedPreferences() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("User_Login", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("idUser", -1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Giải phóng binding để tránh rò rỉ bộ nhớ
        binding = null;
    }

    private Bitmap convertByteArrayToBitmap(byte[] imageBytes) {
        if (imageBytes != null && imageBytes.length > 0) {
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        } else {
            return BitmapFactory.decodeResource(getContext().getResources(), R.drawable.side_nav_bar);
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST_1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST_1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                if (img_shop != null) {
                    img_shop.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(getContext(), "ImageView is null", Toast.LENGTH_SHORT).show();
                }
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
        return Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, false);
    }

//    public void updateUser() {
//        final UserDAO userDAO = new UserDAO(getContext());
////        final User user = userDAO.getUserByID(idUser);
//
////        binding.txtNameOrderCurent.setText(user.getName());
////        binding.txtPhoneOrderCurent.setText("0" + String.valueOf(user.getPhone()));
////        binding.txtAddressOrderCurent.setText(user.getAddress());

}