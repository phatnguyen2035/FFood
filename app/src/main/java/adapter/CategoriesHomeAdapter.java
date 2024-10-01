package adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.du_an_1.MainActivity;
import com.example.du_an_1.R;

import java.util.List;

import dao.CategoriesDao;
import fragment.SearchFragment;
import model.Categories;

public class CategoriesHomeAdapter extends RecyclerView.Adapter<CategoriesHomeAdapter.ViewHolder> {
    private Context context;
    private List<Categories> categoriesList;
    private CategoriesDao categoriesDao;

    public CategoriesHomeAdapter(Context context, List<Categories> categoriesList, CategoriesDao categoriesDao) {
        this.context = context;
        this.categoriesList = categoriesList;
        this.categoriesDao = categoriesDao;
    }

    @NonNull
    @Override
    public CategoriesHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_categories_image,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesHomeAdapter.ViewHolder holder, int position) {

        Categories categories = categoriesList.get(position);
        holder.img_image.setImageBitmap(convertByteArrayToBitmap(categories.getImage()));
        holder.txt_name.setText(categories.getName());
        holder.rl_item_categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("SearchFragment", "SearchFragment");
                intent.putExtra("idCategories", categories.getIdCategories());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_image;
        TextView txt_name;
        RelativeLayout rl_item_categories;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_image = itemView.findViewById(R.id.img_categories_img);
            txt_name = itemView.findViewById(R.id.txt_name_categories_img);
            rl_item_categories = itemView.findViewById(R.id.rl_item_categories);
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
}
