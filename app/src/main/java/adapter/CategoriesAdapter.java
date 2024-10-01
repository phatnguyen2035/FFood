package adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.du_an_1.R;
import dao.CategoriesDao;
import model.Categories;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
    private Context context;
    private List<Categories> categoriesList;
    private CategoriesDao categoriesDao;
    private int idUser;

    public CategoriesAdapter(Context context, List<Categories> categoriesList) {
        this.context = context;
        this.categoriesList = categoriesList;
        this.categoriesDao = new CategoriesDao(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_categories_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Categories categories = categoriesList.get(position);
        holder.txtCategories.setText(categories.getName());
        holder.imgCategories.setImageBitmap(convertByteArrayToBitmap(categories.getImage()));

        int idCategories = categories.getIdCategories();
        idUser = getIdUserFromSharedPreferences();
        int idShop = categoriesDao.getIdShop(idUser);

        holder.ll_item_categories.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Category");
                builder.setMessage("Are you sure you want to delete this category?");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int adapterPosition = holder.getAdapterPosition();
                        if (adapterPosition != RecyclerView.NO_POSITION) {
                            Categories categoryToDelete = categoriesList.get(adapterPosition);
                            boolean check = categoriesDao.deleteCategories(idCategories,idShop);
                            if (check) {
                                categoriesList.remove(adapterPosition); // Remove from list
                                notifyItemRemoved(adapterPosition); // Notify adapter about the removed item
                                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Delete Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            }


        });
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCategories;
        TextView txtCategories;
        LinearLayout ll_item_categories;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_item_categories = itemView.findViewById(R.id.ll_item_categories);
            imgCategories = itemView.findViewById(R.id.img_categories);
            txtCategories = itemView.findViewById(R.id.txt_categories);
        }
    }

    // Helper method to convert byte[] to Bitmap
    private Bitmap convertByteArrayToBitmap(byte[] imageBytes) {
        if (imageBytes != null && imageBytes.length > 0) {
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        } else {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.side_nav_bar);
        }
    }

    // Method to update the dataset with new categories
    public void updateData(List<Categories> newCategoriesList) {
        categoriesList.clear();
        categoriesList.addAll(newCategoriesList);
        notifyDataSetChanged();
    }

    public int getIdUserFromSharedPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("User_Login", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("idUser", -1);
    }
}

