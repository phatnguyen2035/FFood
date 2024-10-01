package adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.du_an_1.R;
import com.example.du_an_1.ShopActivity;

import java.util.List;

import dao.ProductDAO;
import dao.ShopDAO;
import model.Product;
import model.Shop;

public class ShopItemAdapter extends RecyclerView.Adapter<ShopItemAdapter.ViewHolder> {
    private Context context;
    private List<Shop> shopList;
    private ShopDAO shopDAO;

    public ShopItemAdapter(Context context, List<Shop> shopList, ShopDAO shopDAO) {
        this.context = context;
        this.shopList = shopList;
        this.shopDAO = shopDAO;
    }

    public void updateShopList(List<Shop> newShopList) {
        this.shopList = newShopList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Shop shop = shopList.get(position);

        ProductDAO productDAO = new ProductDAO(context);
        List<Product> productList = productDAO.getProductsByIdShop(shop.getIdShop());
        int totalQuantitySold = 0;
        for (Product product : productList) {
            totalQuantitySold += product.getSold();
        }

        if (totalQuantitySold > 0) {
            holder.txt_sold_shop.setVisibility(View.VISIBLE);
            holder.txt_sold_shop.setText("Đã bán: " + totalQuantitySold);
        } else {
            holder.txt_sold_shop.setVisibility(View.GONE);
        }

        // Set the image for the shop
        if (shop.getImage() != null && shop.getImage().length > 0) {
            holder.img_image_shop.setImageBitmap(convertByteArrayToBitmap(shop.getImage()));
        } else {
            holder.img_image_shop.setImageResource(R.mipmap.image_shop_default); // Default image if no image data
        }

        holder.txt_name_shop.setText(shop.getName());

        holder.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShopActivity.class);
                intent.putExtra("idShop", shop.getIdShop());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_item;
        ImageView img_image_shop;
        TextView txt_name_shop, txt_sold_shop;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_item = itemView.findViewById(R.id.ll_item_shop);
            img_image_shop = itemView.findViewById(R.id.img_image_shop);
            txt_name_shop = itemView.findViewById(R.id.txt_name_shop);
            txt_sold_shop = itemView.findViewById(R.id.txt_sold_shopppp);
        }
    }

    private Bitmap convertByteArrayToBitmap(byte[] imageBytes) {
        if (imageBytes != null && imageBytes.length > 0) {
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        } else {
            return BitmapFactory.decodeResource(context.getResources(), R.mipmap.image_shop_default); // Default image if conversion fails
        }
    }
}