package adapter;

import android.content.Context;
import android.content.DialogInterface;
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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.du_an_1.R;

import java.util.List;

import dao.ShopDAO;
import dao.UserDAO;
import model.Shop;
import model.User;

public class ShopNewAdapter extends RecyclerView.Adapter<ShopNewAdapter.ShopViewHolder> {
    private List<Shop> shopList;
    private Context context;
    private ShopDAO shopDAO;
    private UserDAO userDAO;

    public ShopNewAdapter(List<Shop> shopList, Context context) {
        this.shopList = shopList;
        this.context = context;
        this.shopDAO = new ShopDAO(context);
        this.userDAO = new UserDAO(context);
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_manage_booth_new, parent, false);
        return new ShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        Shop shop = shopList.get(position);

        holder.txtIdShop.setText("idShop: " + shop.getIdShop());
        holder.txtIdUser.setText("idUser: " + shop.getIdUser());
        holder.txtNameShop.setText("Name: " + shop.getName());
        holder.txtAddressShop.setText("Address: " + shop.getAddress());

        if (shop.getImage() == null) {
            holder.image.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.image_shop_default));
        } else {
            holder.image.setImageBitmap(convertByteArrayToBitmap(shop.getImage()));
        }

        holder.txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idUser = shop.getIdUser();
                int roleUpdate = 1;
                User user = userDAO.getUserByID(idUser);
                if (user != null) {
                    final User user2 = new User(user.getIdUser(), user.getUser(), user.getPass(), user.getName(), user.getPhone(), user.getCccd(), roleUpdate,null);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Thông báo");
                    builder.setMessage("Bạn có đồng ý cho User này thành nhà bán hàng ?");
                    builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            boolean check = userDAO.SuspendAccount(user2);
                            if (check) {
                                Toast.makeText(context, "Cập nhật người dùng thành công", Toast.LENGTH_SHORT).show();
                                loadShops();
                            } else {
                                Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    Toast.makeText(context, "Không tìm thấy người dùng", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.txtCanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Thông Báo");
                builder.setMessage("Bạn không cho phép user này thành nhà bán hàng");
                builder.setPositiveButton("Đồng Ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int adapterPosition = holder.getAdapterPosition();
                        if (adapterPosition != RecyclerView.NO_POSITION) {
                            boolean check = shopDAO.deleteShop(shop.getIdShop());
                            if (check) {
                                Toast.makeText(context, "Xoá shop thành công", Toast.LENGTH_SHORT).show();
                                loadShops();
                            } else {
                                Toast.makeText(context, "Xoá shop thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    public class ShopViewHolder extends RecyclerView.ViewHolder {
        private TextView txtIdShop;
        private TextView txtIdUser;
        private TextView txtNameShop;
        private TextView txtAddressShop;
        private ImageView image;
        private TextView txtOk;
        private TextView txtCanel;

        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);
            txtIdShop = itemView.findViewById(R.id.txt_idShop_new);
            txtIdUser = itemView.findViewById(R.id.txt_idUser_new);
            txtNameShop = itemView.findViewById(R.id.txt_nameShop_new);
            txtAddressShop = itemView.findViewById(R.id.txt_addressShop_new);
            image = itemView.findViewById(R.id.img_image_shop_new);
            txtOk = itemView.findViewById(R.id.txt_manage_ok_shop_new);
            txtCanel = itemView.findViewById(R.id.txt_manage_canel_shop_new);
        }
    }

    private Bitmap convertByteArrayToBitmap(byte[] imageBytes) {
        if (imageBytes != null && imageBytes.length > 0) {
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        } else {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.side_nav_bar);
        }
    }

    private void loadShops() {
        shopList.clear();
        shopList.addAll(shopDAO.getAllShopsNew());
        notifyDataSetChanged();
    }
}