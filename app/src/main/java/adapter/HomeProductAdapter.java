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

import com.example.du_an_1.ProductActivity;
import com.example.du_an_1.R;
import com.example.du_an_1.ShopActivity;

import java.text.DecimalFormat;
import java.util.List;

import dao.ProductDAO;
import dao.ShopDAO;
import model.Product;
import model.Shop;

public class HomeProductAdapter extends RecyclerView.Adapter<HomeProductAdapter.ViewHolder> {
    Context context;
    List<Product> productList;
    ProductDAO productDAO;
    ShopDAO shopDAO;

    public HomeProductAdapter(Context context, List<Product> productList, ProductDAO productDAO, ShopDAO shopDAO) {
        this.context = context;
        this.productList = productList;
        this.productDAO = productDAO;
        this.shopDAO = shopDAO;
    }

    @NonNull
    @Override
    public HomeProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_home,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeProductAdapter.ViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.img_product_home.setImageBitmap(convertByteArrayToBitmap(product.getImage()));
        holder.txt_name.setText(product.getName());
        DecimalFormat decimalFormat = new DecimalFormat("#,### đ");
        holder.txt_price.setText(decimalFormat.format(product.getPrice()));

        int idShop= product.getIdShop();
        ShopDAO shopDAO = new ShopDAO(context);
        Shop shop = shopDAO.getShopByIdShop(idShop);
        holder.txt_name_shop.setText(shop.getName());
        holder.txt_luotban_shop_home.setText(String.valueOf(product.getSold())+" lượt bán");
        holder.txt_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("productId", product.getIdProduct());
                context.startActivity(intent);
            }
        });
        holder.img_product_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("productId", product.getIdProduct());
                context.startActivity(intent);
            }
        });
        holder.txt_name_shop.setOnClickListener(new View.OnClickListener() {
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
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name, txt_price, txt_luotban_shop_home,txt_name_shop;
        ImageView img_product_home;
        LinearLayout ll_item_product_search;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name_product_home);
            txt_price = itemView.findViewById(R.id.txt_price_product_home);
            txt_name_shop = itemView.findViewById(R.id.txt_name_shop_home);
            img_product_home = itemView.findViewById(R.id.img_product_home);
            txt_luotban_shop_home = itemView.findViewById(R.id.txt_luotban_shop_home);
            ll_item_product_search = itemView.findViewById(R.id.ll_item_product_search);
        }
    }

    //img
    private Bitmap convertByteArrayToBitmap(byte[] imageBytes) {
        if (imageBytes != null && imageBytes.length > 0) {
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        } else {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.side_nav_bar);
        }
    }
}
