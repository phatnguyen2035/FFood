package adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.du_an_1.R;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import dao.UserDAO;
import model.Shop;
import model.User;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.AdapterViewHolder> {
    private List<User> userList;
    private Context context;
    private UserDAO userDAO;
    private int checkUser = 1;

    public AdminAdapter(List<User> userList, Context context, UserDAO userDAO) {
        this.userList = userList;
        this.context = context;
        this.userDAO = userDAO;
    }

    @NonNull
    @Override
    public AdminAdapter.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_manage_user, parent, false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminAdapter.AdapterViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_manage_user;
        private TextView txt_idUser;
        private TextView txt_user;
        private TextView txt_pass;
        private TextView txt_name;
        private TextView txt_phone;
        private TextView txt_cccd;
        private TextView txt_role;
        private ImageView img_image;
        private int roleUpdate;

        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_manage_user = itemView.findViewById(R.id.txt_manage_user);
            txt_idUser = itemView.findViewById(R.id.txt_id_user);
            txt_name = itemView.findViewById(R.id.txt_name_user);
            txt_phone = itemView.findViewById(R.id.txt_phone_user);
            txt_role = itemView.findViewById(R.id.txt_role_user);
            img_image = itemView.findViewById(R.id.image_user);

            txt_manage_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        User user = userList.get(position);
                        showUser(user);
                    }
                }
            });
        }

        public void bind(User user) {
            String sRole = "";
            if (user.getRole() == 0) {
                sRole = "admin";
                img_image.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.image_logo_admin));

            } else if (user.getRole() == 1) {
                sRole = "shop";
                img_image.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.image_shop_default));
            } else if (user.getRole() == 2) {
                sRole = "user";
                img_image.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.image_user_default));
            }

            if (user.getStatus() == 0) {
                sRole = "Tài khoản bị khoá";
                img_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.baseline_remove_24));
            }
            txt_idUser.setText(String.valueOf(user.getIdUser()));
            txt_name.setText(user.getName());
            txt_phone.setText(String.valueOf(user.getPhone()));

            txt_role.setText(sRole);
        }

        //dialog user
        private void showUser(User user) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogView = inflater.inflate(R.layout.dialog_information_user, null);
            builder.setView(dialogView);

            ///
            EditText edt_id_user = dialogView.findViewById(R.id.edt_id_user);
            EditText edt_user = dialogView.findViewById(R.id.edt_user);
            EditText edt_pass = dialogView.findViewById(R.id.edt_pass);
            EditText edt_name = dialogView.findViewById(R.id.edt_name);
            EditText edt_phone = dialogView.findViewById(R.id.edt_phone);
            EditText edt_cccd = dialogView.findViewById(R.id.edt_cccd);
            EditText edt_role = dialogView.findViewById(R.id.edt_role);
            Switch switch_role = dialogView.findViewById(R.id.switch_role);

            Button btn_suspend_account = dialogView.findViewById(R.id.btn_suspend_account);
            Button btn_update = dialogView.findViewById(R.id.btn_update);

            // set edt
            edt_id_user.setEnabled(false);
            edt_user.setEnabled(false);
            edt_pass.setEnabled(false);
//            edt_name.setEnabled(false);
//            edt_phone.setEnabled(false);
//            edt_cccd.setEnabled(false);
            edt_role.setEnabled(false);

            // check role

            if (0 == user.getRole()) {
                switch_role.setEnabled(false);
                btn_suspend_account.setEnabled(false);
            }

            if (user.getRole() == 1) {
                switch_role.setThumbTintList(ContextCompat.getColorStateList(context, R.color.green_color));
                switch_role.setTrackTintList(ContextCompat.getColorStateList(context, R.color.green_color));
                edt_role.setText("shop");
            }

            roleUpdate = user.getRole();
            if (roleUpdate == 1) {
                switch_role.setChecked(true);
            }

            int idShop = userDAO.getIdShop(user.getIdUser());
            if (idShop == -1) {
                switch_role.setEnabled(false);
            }

            switch_role.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    roleUpdate = 1;
                    Toast.makeText(context, "Thay đổi thành shop bán hàng", Toast.LENGTH_SHORT).show();
                    switch_role.setThumbTintList(ContextCompat.getColorStateList(context, R.color.green_color));
                    switch_role.setTrackTintList(ContextCompat.getColorStateList(context, R.color.green_color));
                    edt_role.setText("shop");


                } else {
                    roleUpdate = 2;
                    Toast.makeText(context, "Thay đổi thành người mua hàng", Toast.LENGTH_SHORT).show();
                    switch_role.setThumbTintList(ContextCompat.getColorStateList(context, R.color.nav_background_color));
                    switch_role.setTrackTintList(ContextCompat.getColorStateList(context, R.color.default_color));
                    edt_role.setText("user");
                }
            });


            edt_id_user.setText(String.valueOf(user.getIdUser()));
            edt_user.setText((user.getUser()));
            edt_pass.setText((user.getPass()));
            edt_name.setText(user.getName());
            edt_phone.setText(String.valueOf(user.getPhone()));
            edt_cccd.setText(String.valueOf(user.getCccd()));
            String sRole = "";
            if (user.getRole() == 0) {
                sRole = "admin";
            } else if (user.getRole() == 1) {
                sRole = "shop";
            } else if (user.getRole() == 2) {
                sRole = "user";
            }

            if (user.getStatus() == 0) {
                sRole = "Tài khoản đã khoá";
                btn_suspend_account.setText("Mở Tài Khoản");
                checkUser = 0;
            }


            edt_role.setText(sRole);
            //
            AlertDialog alertDialog11 = builder.create();
            alertDialog11.show();

            // back diaglog
            ImageView iv_Back = dialogView.findViewById(R.id.iv_back);
            iv_Back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog11.dismiss();
                }
            });

            //update user
            btn_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int idUser = user.getIdUser();
                    String user = edt_user.getText().toString();
                    String pass = edt_pass.getText().toString();
                    String name = edt_name.getText().toString();
                    String phoneS = edt_phone.getText().toString();
                    String cccdS = edt_cccd.getText().toString();
                    String roleS = String.valueOf(roleUpdate);

                    // check role not int
                    try {
                        long phone = Long.parseLong(phoneS);
                        long cccd = Long.parseLong(cccdS);
                        int role = Integer.parseInt(roleS);

                        //
                        if (user.isEmpty() || pass.isEmpty() || name.isEmpty() || roleS.isEmpty()) {
                            Toast.makeText(context, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        } else {
                            final User user1 = new User(idUser, user, pass, name, phone, cccd, roleUpdate, null);

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Thông Báo");
                            builder.setMessage("Bạn có muốn cập nhập User?");
                            builder.setPositiveButton("Đồng Ý", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // update
                                    boolean check = userDAO.upDateUser(user1);
                                    if (check) {
                                        Toast.makeText(context, "Cập nhập người dùng thành công", Toast.LENGTH_SHORT).show();
                                        // Load data
                                        userList.clear();
                                        userList.addAll(userDAO.getListUser());
                                        notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(context, "Cập nhập thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                    // exit dialog
                                    dialog.dismiss();
                                    alertDialog11.dismiss();

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
                    } catch (NumberFormatException e) {
                        Toast.makeText(context, "Role must be an integer", Toast.LENGTH_SHORT).show();
                    }

                }
            });


            btn_suspend_account.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // check role
                    if (checkUser == 1) {
                        int statusSuspendAccount = 0;

                        user.setStatus(statusSuspendAccount);

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Thông Báo");
                        builder.setMessage("Bạn có muốn tạm ngưng tài khoản này khoản với id là " + user.getIdUser() + " Name là " + user.getName() + " không ?");
                        builder.setPositiveButton("Đồng Ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // update
                                boolean check = userDAO.SuspendAccount(user);
                                if (check) {
                                    Toast.makeText(context, "Ngưng tài khoản thành công", Toast.LENGTH_SHORT).show();
                                    // Load data
                                    userList.clear();
                                    userList.addAll(userDAO.getListUser());
                                    notifyDataSetChanged();
                                } else {
                                    Toast.makeText(context, "Ngưng tài khoản thất bại", Toast.LENGTH_SHORT).show();
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
                        alertDialog11.dismiss();
                    } else if (checkUser == 0) {

                        int statusSuspendAccount = 1;

                        user.setStatus(statusSuspendAccount);

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Thông Báo");
                        builder.setMessage("Bạn có muốn Mở tài khoản này khoản với id là " + user.getIdUser() + " Name là " + user.getName() + " không ?\n Sau khi mở nhớ cập nhập lại  role bán hàng");
                        builder.setPositiveButton("Đồng Ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // update
                                boolean check = userDAO.SuspendAccount(user);
                                if (check) {
                                    Toast.makeText(context, "Mở tài khoản thành công", Toast.LENGTH_SHORT).show();
                                    // Load data
                                    userList.clear();
                                    userList.addAll(userDAO.getListUser());
                                    notifyDataSetChanged();
                                } else {
                                    Toast.makeText(context, "Mở tài khoản thất bại", Toast.LENGTH_SHORT).show();
                                }
                                // exit dialog
                                dialog.dismiss();
                                alertDialog11.dismiss();
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
                }
            });

        }

        // get role
        public int getRoleFromSharedPreferences() {
            SharedPreferences sharedPreferences = context.getSharedPreferences("User_Login", Context.MODE_PRIVATE);
            return sharedPreferences.getInt("role", -1);
        }
    }

}