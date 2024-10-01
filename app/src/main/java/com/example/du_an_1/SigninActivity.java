package com.example.du_an_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import dao.UserDAO;
import data.base.DbHelper;
import model.User;

import model.User;

public class SigninActivity extends AppCompatActivity {

    EditText edtUser,edtPass,edtRePass,edtFullName,edtPhone,edtCCCD;

    Button btnSignin;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        ImageView btnBack = findViewById(R.id.btnBack);

        // back to starActivity
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SigninActivity.this, StarActivity.class);
                startActivity(intent);
            }
        });
        // code register
        edtUser = findViewById(R.id.edtUsername);
        edtPass = findViewById(R.id.edtPassword);
        edtRePass = findViewById(R.id.edtRePassword);
        edtPhone = findViewById(R.id.edtPhone);

        edtFullName = findViewById(R.id.edtFullName);
        edtCCCD = findViewById(R.id.edtCCCD);

        btnSignin = findViewById(R.id.btnSignin);


        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = edtUser.getText().toString();
                String pass = edtPass.getText().toString();
                String rePass = edtRePass.getText().toString();
                String phone = edtPhone.getText().toString();
                String name = edtFullName.getText().toString();
                String cccd = edtCCCD.getText().toString();

                //check
                if (user.isEmpty() || pass.isEmpty() || rePass.isEmpty() || phone.isEmpty() || name.isEmpty() || cccd.isEmpty()) {
                    Toast.makeText(SigninActivity.this, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else if (!rePass.equals(pass)) {
                    Toast.makeText(SigninActivity.this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    UserDAO userDAO = new UserDAO(SigninActivity.this);
                    User user1 = new User();
                    user1.setUser(user);
                    user1.setPass(pass);
                    user1.setName(name);
                    user1.setPhone(Long.parseLong(phone));
                    user1.setCccd(Long.parseLong(cccd));
                    user1.setRole(2);
                    user1.setAddress("");
                    user1.setStatus(1);
                    int addUser = userDAO.addUser(user1);
                    if (addUser == 0) {
                        Toast.makeText(SigninActivity.this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                    } else if (addUser == -1) {
                        Toast.makeText(SigninActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(SigninActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SigninActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }

                }
            }
        });

    }
}