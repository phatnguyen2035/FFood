package adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.du_an_1.R;

import java.util.List;

import dao.ProductDAO;
import dao.ShopDAO;
import dao.UserDAO;
import model.Product;
import model.Shop;
import model.User;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {

    private Context context;
    private List<Shop> shopList;
    private ShopDAO shopDAO;
    private UserDAO userDAO;

    public ShopAdapter(List<Shop> shopList, Context context) {
        this.shopList = shopList;
        this.context = context;
        this.shopDAO = new ShopDAO(context);
        this.userDAO = new UserDAO(context);
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_manage_booth, parent, false);
        return new ShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        Shop shop = shopList.get(position);
        holder.bind(shop);

        holder.txtKhoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int updateStatusShop = 0;
                final Shop shop1 = new Shop(shop.getIdShop(), shop.getIdUser(), shop.getName(), shop.getAddress(), shop.getImage(), updateStatusShop);

                // Tạo Dialog
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn có muốn khoá Shop id là " + shop.getIdShop() + ". Name là " + shop.getName() + " không?");
                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Cập nhật trạng thái shop
                        boolean check = shopDAO.UpdateShop(shop1);
                        if (check) {
                            Toast.makeText(context, "Khoá Shop thành công", Toast.LENGTH_SHORT).show();

                            // Cập nhật trạng thái các sản phẩm thuộc shop
                            ProductDAO productDAO = new ProductDAO(context);
                            List<Product> productList = productDAO.getProductsByIdShop(shop.getIdShop());
                            for (Product product : productList) {
                                product.setStatus(0);
                                boolean checkProduct = productDAO.upProduct(product);
                            }
                            productList.clear();

                            // Tải lại dữ liệu danh sách shop
                            shopList.clear();
                            shopList.addAll(shopDAO.getAllShops());
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, "Khoá Shop thất bại", Toast.LENGTH_SHORT).show();
                        }
                        // Đóng dialog
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });

        holder.txtMo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check role
                int updateStatusShop = 1;
                final Shop shop1 = new Shop(shop.getIdShop(), shop.getIdUser(), shop.getName(), shop.getAddress(), shop.getImage(), updateStatusShop);

                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                builder.setTitle("Thông Báo");
                builder.setMessage("Bạn có muốn mở Shop id là " + shop.getIdShop() + ". Name là " + shop.getName() + " không ?");
                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // update
                        boolean check = shopDAO.UpdateShop(shop1);
                        if (check) {
                            Toast.makeText(context, "Mở Shop thành công", Toast.LENGTH_SHORT).show();
                            // Load data

                            ProductDAO productDAO = new ProductDAO(context);
                            List<Product> productList = productDAO.getProductsByIdShop(shop.getIdShop());
                            for (Product product : productList) {
                                product.setStatus(1);
                                boolean checkProduct = productDAO.upProduct(product);
                            }
                            productList.clear();


                            //tai lai shop
                            shopList.clear();
                            shopList.addAll(shopDAO.getAllShops());
                            notifyDataSetChanged();


                        } else {
                            Toast.makeText(context, "Mở Shop thất bại", Toast.LENGTH_SHORT).show();
                        }
                        // exit dialog
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // exit dialog
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
        private TextView txtAddressShop, txtStatus, txtKhoa, txtMo;
        private ImageView imgShop;

        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);
            txtKhoa = itemView.findViewById(R.id.txt_manage_canel_shop);
            txtMo = itemView.findViewById(R.id.txt_manage_ok_shop);
            txtIdShop = itemView.findViewById(R.id.txt_idShop);
            txtIdUser = itemView.findViewById(R.id.txt_idUser);
            txtNameShop = itemView.findViewById(R.id.txt_nameShop);
            txtAddressShop = itemView.findViewById(R.id.txt_addressShop);
            imgShop = itemView.findViewById(R.id.img_image_shop);
            txtStatus = itemView.findViewById(R.id.txt_StatusShop);
        }

        public void bind(Shop shop) {
            txtIdShop.setText("idShop: " + shop.getIdShop());
            txtIdUser.setText("idUser: " + shop.getIdUser());
            txtNameShop.setText("Name: " + shop.getName());
            txtAddressShop.setText("Address: " + shop.getAddress());

            if (shop.getImage() == null) {
                imgShop.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.image_shop_default));
            } else {
                imgShop.setImageBitmap(convertByteArrayToBitmap(shop.getImage()));
            }

            User user = userDAO.getUserByID(shop.getIdUser());
            if (user != null) {
                if (shop.getStatus() == 0) {
                    txtStatus.setText("Shop không hoạt động");
                } else if (user.getRole() == 2) {
                    txtStatus.setText("Shop chờ xác nhận");
                } else if (shop.getStatus() == 1) {
                    txtStatus.setText("Shop đang hoạt động");
                } else if (shop.getStatus() == -1) {
                    txtStatus.setText("Shop đã khoá");
                }
            }
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