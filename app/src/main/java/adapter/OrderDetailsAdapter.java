package adapter;

import android.content.Context;
import android.content.Intent;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.du_an_1.ProductActivity;
import com.example.du_an_1.R;
import com.example.du_an_1.ShopActivity;

import java.text.DecimalFormat;
import java.util.List;

import dao.OrderDetailsDAO;
import dao.ShopDAO;
import model.OrderDetails;
import model.Shop;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {
    private Context context;
    private List<OrderDetails> orderDetailsList;
    private OrderDetailsDAO orderDetailsDAO;
    private int idUser;

    public OrderDetailsAdapter(Context context, List<OrderDetails> orderDetailsList, OrderDetailsDAO orderDetailsDAO) {
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


        holder.img_product.setImageBitmap(convertByteArrayToBitmap(orderDetails.getImage()));
        holder.txt_name_product.setText(orderDetails.getName());
        DecimalFormat decimalFormat = new DecimalFormat("#,###,### đ");
        holder.txt_price_product.setText(decimalFormat.format(orderDetails.getTotalPrice()));
        holder.txt_quantity_product.setText(String.valueOf(orderDetails.getQuantity()));
        Shop shop = new Shop();
        ShopDAO shopDAO = new ShopDAO(context);
        shop = shopDAO.getShopByIdShop(orderDetails.getIdShop());
        holder.txt_shop_product_details.setText(shop.getName());

        holder.txt_name_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("productId", orderDetails.getIdProduct());
                context.startActivity(intent);
            }
        });
        holder.img_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("productId", orderDetails.getIdProduct());
                context.startActivity(intent);
            }
        });

        holder.txt_shop_product_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShopActivity.class);
                intent.putExtra("idShop", orderDetails.getIdShop());
                context.startActivity(intent);
            }
        });

        idUser = getIdUserFromSharedPreferences();

        holder.img_minus_quantity_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int[] quantity = {orderDetails.getQuantity()};
                final double[] totalPrice = {orderDetails.getTotalPrice()};
                if (quantity[0] > 1) {
                    quantity[0]--;
                    totalPrice[0] = totalPrice[0] - orderDetails.getPrice();

                    // Cập nhật TextView
                    holder.txt_price_product.setText(decimalFormat.format(totalPrice[0]));
                    holder.txt_quantity_product.setText(String.valueOf(quantity[0]));

                    // Cập nhật đối tượng OrderDetails
                    orderDetails.setQuantity(quantity[0]);
                    orderDetails.setTotalPrice(totalPrice[0]);

                    // Cập nhật cơ sở dữ liệu
                    boolean isUpdated = orderDetailsDAO.updateOrderDetails(orderDetails);
                    if (!isUpdated) {
                        Toast.makeText(view.getContext(), "Không cập nhật được chi tiết đơn hàng trong cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        holder.img_plus_quantity_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int[] quantity = {orderDetails.getQuantity()};
                final double[] totalPrice = {orderDetails.getTotalPrice()};
                if (quantity[0] > 0) {
                    quantity[0]++;
                    totalPrice[0] = totalPrice[0] + orderDetails.getPrice();

                    // Cập nhật TextView
                    holder.txt_price_product.setText(decimalFormat.format(totalPrice[0]));
                    holder.txt_quantity_product.setText(String.valueOf(quantity[0]));

                    // Cập nhật đối tượng OrderDetails
                    orderDetails.setQuantity(quantity[0]);
                    orderDetails.setTotalPrice(totalPrice[0]);


                    boolean isUpdated = orderDetailsDAO.updateOrderDetails(orderDetails);
                    if (!isUpdated) {
                        Toast.makeText(view.getContext(), "Giảm số lượng sản phẩm không thành công", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        int currentPosition = holder.getAdapterPosition();
        holder.img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isDeleted = orderDetailsDAO.deleteOrderDetails(orderDetails.getIdOrderDetails());
                if (isDeleted) {
                    orderDetailsList.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                    notifyItemRangeChanged(currentPosition, orderDetailsList.size());
                } else {
                    Toast.makeText(view.getContext(), "Xoá sản phẩm thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });


   }

    @Override
    public int getItemCount() {
        return orderDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_product, img_back, img_minus_quantity_product, img_plus_quantity_product;
        TextView txt_name_product, txt_price_product, txt_quantity_product,txt_shop_product_details;
        LinearLayout ll_order_details;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product = itemView.findViewById(R.id.img_product_details);
            txt_name_product = itemView.findViewById(R.id.txt_name_product_details);
            txt_price_product = itemView.findViewById(R.id.txt_price_product_details);
            img_back = itemView.findViewById(R.id.img_back_details);
            txt_quantity_product = itemView.findViewById(R.id.txt_quantity_product_details);
            img_minus_quantity_product = itemView.findViewById(R.id.img_minus_quantity_product_details);
            img_plus_quantity_product = itemView.findViewById(R.id.img_plus_quantity_product_details);
            txt_shop_product_details = itemView.findViewById(R.id.txt_shop_product_details);
            ll_order_details = itemView.findViewById(R.id.ll_order_details);
        }
    }


    private Bitmap convertByteArrayToBitmap(byte[] imageBytes) {
        if (imageBytes != null && imageBytes.length > 0) {
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        } else {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.side_nav_bar);
        }
    }

    public int getIdUserFromSharedPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("User_Login", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("idUser", -1);
    }
}
