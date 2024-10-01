package com.example.du_an_1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import dao.ShopDAO;

public class RegisterShopActivity extends AppCompatActivity {

    private int idUser;
    private ShopDAO shopDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_shop);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);


            shopDAO = new ShopDAO(RegisterShopActivity.this);

            //get User
            getUserFromSharedPreferences();

            // Find the buttons and checkbox after the view is created
            Button btn_register_booth1 = findViewById(R.id.btn_register_booth1);


            btn_register_booth1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterShopActivity.this);
                    View dialogView = getLayoutInflater().inflate(R.layout.dialog_booth_register, null);
                    builder.setView(dialogView);

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                    //dialog Register
                    Button btn_register_booth2 = dialogView.findViewById(R.id.btn_register_booth2);
                    CheckBox cb_register = dialogView.findViewById(R.id.cb_booth_register);
                    EditText edt_name_register = dialogView.findViewById(R.id.edt_name_shop_register);
                    EditText edt_address_register = dialogView.findViewById(R.id.edt_address_shop_register);

                    //checkbox
                    boolean isChecked = cb_register.isChecked();
                    btn_register_booth2.setEnabled(cb_register.isChecked());
                    cb_register.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            btn_register_booth2.setEnabled(isChecked);
                        }
                    });
                    // register
                    btn_register_booth2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String name = edt_name_register.getText().toString();
                            String address = edt_address_register.getText().toString();

                            if (name.length() == 0 || address.length() == 0) {
                                Toast.makeText(RegisterShopActivity.this, "Nhập thông tin đầy đủ.", Toast.LENGTH_SHORT).show();
                            } else {

                                int check = shopDAO.addShop(idUser, name, address, null, 1);
                                switch (check) {
                                    case 1:

                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(RegisterShopActivity.this);
                                        builder1.setTitle("Thông báo");
                                        builder1.setMessage("Đăng ký thành công");

                                        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                                alertDialog.dismiss();
                                            }
                                        });
                                        AlertDialog alertDialog1 = builder1.create();
                                        alertDialog1.show();
                                        break;
                                    case 0:
                                        Toast.makeText(RegisterShopActivity.this, "Tài khoản đã đăng ký cửa hàng", Toast.LENGTH_SHORT).show();
                                        break;
                                    case -1:
                                        Toast.makeText(RegisterShopActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    });


                }
            });

            ImageView img_back = findViewById(R.id.img_back);
            img_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(RegisterShopActivity.this, MainActivity.class);
                    intent.putExtra("SettingFragment", "SettingFragment");
                    startActivity(intent);
                }
            });

            return insets;

        });
    }

    public void getUserFromSharedPreferences() {
        SharedPreferences share = this.getSharedPreferences("User_Login", Context.MODE_PRIVATE);
        idUser = share.getInt("idUser", -1);


    }
}