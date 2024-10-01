package fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.du_an_1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import adapter.CategoriesAdapter;
import dao.CategoriesDao;
import model.Categories;

public class ManageCategoryFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private CategoriesDao categoriesDao;
    private FloatingActionButton floatingActionButton;
    private Context context;
    private Bitmap bitmap;
    private ImageView imageView;
    private int idShop;
    private int idUser;
    private List<Categories> categoriesList;
    private RecyclerView recyclerView;
    private CategoriesAdapter adapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_category, container, false);

        categoriesDao = new CategoriesDao(context);
        floatingActionButton = view.findViewById(R.id.fab_add_catagory);

        recyclerView = view.findViewById(R.id.rcv_manage_categories);
        recyclerView.setLayoutManager(new LinearLayoutManager(context)); // Thiết lập LinearLayoutManager
        idUser = getIdUserFromSharedPreferences();

        idShop = categoriesDao.getIdShop(idUser);
        loadCategoriesList();

        floatingActionButton.setVisibility(View.GONE);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCategoryDialog();
            }
        });

        return view;
    }

    private void showCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_information_category, null);
        builder.setView(dialogView);

        EditText edt_name = dialogView.findViewById(R.id.edt_name_categories);
        imageView = dialogView.findViewById(R.id.img_selected); // Khởi tạo imageView
        Button btn_select_image = dialogView.findViewById(R.id.btn_select_image);
        Button btn_save = dialogView.findViewById(R.id.btn_save_categories);

        btn_select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName = edt_name.getText().toString();
                if (bitmap != null && !categoryName.isEmpty()) {
                    // Resize bitmap if needed
                    int targetWidth = 800; // Example target width
                    int targetHeight = 600; // Example target height
                    bitmap = resizeBitmap(bitmap, targetWidth, targetHeight);

                    // Convert resized bitmap to byte array
                    byte[] imageBytes = getBitmapAsByteArray(bitmap);

                    Categories newCategory = new Categories();
                    newCategory.setName(categoryName);
                    newCategory.setImage(imageBytes);
                    newCategory.setIdShop(idShop);

                    long check = categoriesDao.addCategories(idShop, categoryName, imageBytes);

                    if (check == 1) {
                        categoriesList.add(newCategory);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(context, "Category saved successfully!", Toast.LENGTH_SHORT).show();
                    } else if (check == -1) {
                        Toast.makeText(context, "Failed to save category! The category name already exists.", Toast.LENGTH_SHORT).show();
                    }
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(context, "Please select an image and enter a category name.", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    private void loadCategoriesList() {
        categoriesList = categoriesDao.getCategories(idShop);
        adapter = new CategoriesAdapter(getContext(), categoriesList);
        recyclerView.setAdapter(adapter);
    }
}
