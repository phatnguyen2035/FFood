package adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.du_an_1.OrderDetailActivity;
import com.example.du_an_1.R;

import java.text.DecimalFormat;
import java.util.List;

import dao.OrderDAO;
import dao.ShopDAO;
import model.Order;
import model.Shop;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private Context context;
    private List<Order> orderList;
    private OrderDAO orderDAO;

    public OrderAdapter(Context context, List<Order> orderList, OrderDAO orderDAO) {
        this.context = context;
        this.orderList = orderList;
        this.orderDAO = orderDAO;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        Order order = orderList.get(position);

        Shop shop = new Shop();

        ShopDAO shopDAO = new ShopDAO(context);

        shop = shopDAO.getShopByIdShop(order.getIdShop());

        shop.setIdShop(order.getIdShop());

        holder.txt_shop_order.setText(shop.getName());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("idOrder", order.getIdOrder());
                context.startActivity(intent);
            }
        });

        int status = order.getStatus();
        if (status == 0) {
            holder.txt_status_order.setText("Đơn hàng đang chờ xác nhận");
            holder.btn_confirm_order.setVisibility(View.GONE);
            holder.txt_note_order.setVisibility(View.GONE);
        } else if (status == 1) {
            holder.txt_status_order.setText("Đơn hàng đang được giao");
            holder.btn_canel_order.setVisibility(View.GONE);
            holder.btn_confirm_order.setVisibility(View.GONE);
        } else if (status == 2) {
            holder.txt_status_order.setText("Đơn hàng giao thành công");
            holder.btn_canel_order.setVisibility(View.GONE);
            holder.btn_confirm_order.setVisibility(View.GONE);
        } else if (status == 3) {
            holder.txt_status_order.setText("Đơn hàng bị huỷ");
            holder.btn_confirm_order.setVisibility(View.INVISIBLE);
            holder.btn_canel_order.setVisibility(View.INVISIBLE);

        }

        holder.txt_id_order.setText("Đơn hàng: " + String.valueOf(order.getIdOrder()));
        holder.txt_name_user_order.setText("Người Mua: " + order.getName());
        holder.txt_quantity_order.setText("SL: " + String.valueOf(order.getQuantity()));
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        holder.txt_total_price_order.setText("Tổng Tiền: " + String.valueOf(decimalFormat.format(order.getTotalPrice())) + " đ");
        holder.txt_date_order.setText("Ngày: " + String.valueOf(order.getDate()));
        holder.img_image_order.setImageBitmap(convertByteArrayToBitmap(shop.getImage()));
        holder.txt_note_order.setText(order.getNote());

        if (status == 0) {
            holder.btn_canel_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_canel_order);

                    Window window = dialog.getWindow();
                    if (window != null) {
                        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                        layoutParams.copyFrom(window.getAttributes());
                        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT; // Chiều rộng đầy màn hình
                        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT; // Chiều cao vừa đủ nội dung
                        window.setAttributes(layoutParams);
                    }

                    TextView txt_note = dialog.findViewById(R.id.txt_note);
                    TextView txt_canel = dialog.findViewById(R.id.txt_canel);
                    TextView txt_confirm = dialog.findViewById(R.id.txt_confirm);

                    txt_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String note = txt_note.getText().toString();
                            String noteUp = "Lý do huỷ: Người mua-" + note;
                            if (note.isEmpty()) {
                                Toast.makeText(context, "Hãy ghi lý do huỷ đơn hàng", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (note.length() < 10) {
                                Toast.makeText(context, "nội dung ít nhất 10 kí tự", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            int statusUpdate = 3;
                            Order updatedOrder = new Order(order.getIdOrder(), order.getIdShop(), order.getIdUser(), order.getQuantity(), order.getTotalPrice(), order.getDate(), noteUp, order.getName(), order.getPhone(), order.getAddress(), statusUpdate);

                            boolean check = orderDAO.updateOrder(updatedOrder);
                            if (check) {
                                int position = holder.getAdapterPosition();
                                orderList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, getItemCount());

                                Toast.makeText(context, "Huỷ đơn hàng thành công", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(context, "Huỷ đơn hàng thất bại", Toast.LENGTH_SHORT).show();
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
            });

        }

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_shop_order, txt_status_order, txt_id_order, txt_name_user_order, txt_name_order, txt_quantity_order, txt_total_price_order, txt_date_order, txt_note_order;
        ImageView img_image_order;
        Button btn_canel_order, btn_confirm_order;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_shop_order = itemView.findViewById(R.id.txt_shop_order);
            txt_status_order = itemView.findViewById(R.id.txt_status_order);
            txt_id_order = itemView.findViewById(R.id.txt_id_order);
            txt_name_user_order = itemView.findViewById(R.id.txt_name_user_order);
            txt_quantity_order = itemView.findViewById(R.id.txt_quantity_order);
            txt_total_price_order = itemView.findViewById(R.id.txt_total_price_order);
            txt_date_order = itemView.findViewById(R.id.txt_date_order);
            img_image_order = itemView.findViewById(R.id.img_image_order);
            btn_canel_order = itemView.findViewById(R.id.btn_canel_order);
            btn_confirm_order = itemView.findViewById(R.id.btn_confirm_order);
            txt_note_order = itemView.findViewById(R.id.txt_note_order);
            linearLayout = itemView.findViewById(R.id.linear_layout_order);
        }
    }

    private Bitmap convertByteArrayToBitmap(byte[] imageBytes) {
        if (imageBytes != null && imageBytes.length > 0) {
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        } else {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.side_nav_bar);
        }
    }
}
