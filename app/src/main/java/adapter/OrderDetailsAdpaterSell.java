package adapter;

import android.content.Context;
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

import java.text.DecimalFormat;
import java.util.List;

import dao.OrderDetailsDAO;
import dao.ShopDAO;
import model.OrderDetails;
import model.Shop;

public class OrderDetailsAdpaterSell extends RecyclerView.Adapter<OrderDetailsAdpaterSell.ViewHolder> {

    private Context context;
    private List<OrderDetails> orderDetailsList;
    private OrderDetailsDAO orderDetailsDAO;

    public OrderDetailsAdpaterSell(Context context, List<OrderDetails> orderDetailsList, OrderDetailsDAO orderDetailsDAO) {
        this.context = context;
        this.orderDetailsList = orderDetailsList;
        this.orderDetailsDAO = orderDetailsDAO;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDetails orderDetails = orderDetailsList.get(position);

        // Set image
        Bitmap productImage = convertByteArrayToBitmap(orderDetails.getImage());
        if (productImage != null) {
            holder.img_product.setImageBitmap(productImage);
        } else {
            holder.img_product.setImageResource(R.drawable.side_nav_bar);
        }

        Shop shop = new Shop();
        ShopDAO shopDAO = new ShopDAO(context);
        shop = shopDAO.getShopByIdShop(orderDetails.getIdShop());

        // Set text fields
        holder.txt_name_product.setText(orderDetails.getName());
        DecimalFormat decimalFormat = new DecimalFormat("#,###,### đ");
        holder.txt_price_product.setText(decimalFormat.format(orderDetails.getTotalPrice()));
        holder.txt_quantity_product.setTypeface(holder.txt_quantity_product.getTypeface(), holder.txt_quantity_product.getTypeface().ITALIC);
        holder.txt_quantity_product.setText("sl: "+String.valueOf(orderDetails.getQuantity()));
        holder.txt_shop_product_details.setText(shop.getName());

        // Hide quantity controls for seller
        holder.img_minus_quantity_product.setVisibility(View.GONE);
        holder.img_plus_quantity_product.setVisibility(View.GONE);
        holder.img_back_details.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return orderDetailsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_product, img_minus_quantity_product, img_plus_quantity_product,img_back_details;
        TextView txt_name_product, txt_price_product, txt_quantity_product,txt_shop_product_details;
        LinearLayout ll_order_details;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product = itemView.findViewById(R.id.img_product_details);
            txt_name_product = itemView.findViewById(R.id.txt_name_product_details);
            txt_price_product = itemView.findViewById(R.id.txt_price_product_details);
            txt_quantity_product = itemView.findViewById(R.id.txt_quantity_product_details);
            img_minus_quantity_product = itemView.findViewById(R.id.img_minus_quantity_product_details);
            img_plus_quantity_product = itemView.findViewById(R.id.img_plus_quantity_product_details);
            ll_order_details = itemView.findViewById(R.id.ll_order_details);
            img_back_details = itemView.findViewById(R.id.img_back_details);
            txt_shop_product_details = itemView.findViewById(R.id.txt_shop_product_details);
        }
    }

    private Bitmap convertByteArrayToBitmap(byte[] imageBytes) {
        if (imageBytes != null && imageBytes.length > 0) {
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        }
        return null;
    }
}