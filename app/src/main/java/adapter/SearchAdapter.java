package adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;
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

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    Context context;
    List<Product> productList;
    ProductDAO productDAO;

    public SearchAdapter(Context context, List<Product> productList, ProductDAO productDAO) {
        this.context = context;
        this.productList = productList;
        this.productDAO = productDAO;
    }

    public void updateProductList(List<Product> newProductList) {
        this.productList = newProductList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.img.setImageBitmap(convertByteArrayToBitmap(product.getImage()));
        holder.txt_name.setText(product.getName());
        DecimalFormat decimalFormat = new DecimalFormat("#,###,### đ");
        holder.txt_price.setText(decimalFormat.format(product.getPrice()));

        int idShop = product.getIdShop();
        ShopDAO shopDAO = new ShopDAO(context);
        Shop shop = shopDAO.getShopByIdShop(idShop);
        int heightInPixels;
        if (product.getIdProduct() % 2 == 0) {
            heightInPixels = dpToPx(300, holder.img.getContext()); // Đặt chiều cao cố định 250dp cho ImageView
        } else {
            heightInPixels = dpToPx(250, holder.img.getContext()); // Đặt chiều cao cố định khác cho các sản phẩm khác
        }

        // Lấy LayoutParams của ImageView
        ViewGroup.LayoutParams layoutParams = holder.img.getLayoutParams();
        layoutParams.height = heightInPixels; // Đặt chiều cao cố định cho ImageView

        // Áp dụng lại LayoutParams cho ImageView
        holder.img.setLayoutParams(layoutParams);

        // Yêu cầu ImageView cập nhật lại bố cục
        holder.img.requestLayout();



        holder.txt_name_shop.setText(shop.getName());
        holder.txt_sold_shop_home.setText(String.valueOf(product.getSold()) + " lượt bán");

        View.OnClickListener productClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("productId", product.getIdProduct());
                context.startActivity(intent);
            }
        };

        holder.txt_name.setOnClickListener(productClickListener);
        holder.img.setOnClickListener(productClickListener);

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
        TextView txt_name, txt_price, txt_name_shop, txt_sold_shop_home;
        ImageView img;
        LinearLayout item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name_product_search);
            txt_price = itemView.findViewById(R.id.txt_price_product_search);
            img = itemView.findViewById(R.id.img_product_search);
            txt_name_shop = itemView.findViewById(R.id.txt_name_shop_search);
            item = itemView.findViewById(R.id.ll_item_product_search);
            txt_sold_shop_home = itemView.findViewById(R.id.txt_sold_shop_home);
        }
    }

    private Bitmap convertByteArrayToBitmap(byte[] imageBytes) {
        if (imageBytes != null && imageBytes.length > 0) {
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        } else {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.side_nav_bar);
        }
    }
    private int dpToPx(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
