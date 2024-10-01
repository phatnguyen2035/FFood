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
import model.User;

public class ChangepassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepass);
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        EditText user = findViewById(R.id.edtUsername);
        EditText passCu = findViewById(R.id.edtPassword);
        EditText passnew = findViewById(R.id.edtNewPassword);
        EditText confirmPass = findViewById(R.id.edtConfirmNewPassword);
        Button btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userText = user.getText().toString();
                String passCuText = passCu.getText().toString();
                String passnewText = passnew.getText().toString();
                String confirmPassText = confirmPass.getText().toString();

                if (userText.isEmpty() || passCuText.isEmpty() || passnewText.isEmpty() || confirmPassText.isEmpty()) {
                    Toast.makeText(ChangepassActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else if (!passnewText.equals(confirmPassText)) {
                    Toast.makeText(ChangepassActivity.this, "Mật khẩu mới không giống nhau", Toast.LENGTH_SHORT).show();
                } else {
                    UserDAO userDAO = new UserDAO(ChangepassActivity.this);
                    User user1 = new User();
                    user1.setUser(userText);
                    user1.setPass(passCuText);

                    int check = userDAO.upDateUserByUser(user1, passnewText);
                    if (check == 0) {
                        Toast.makeText(ChangepassActivity.this, "Tài khoản hoặc mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                    } else if (check == -1) {
                        Toast.makeText(ChangepassActivity.this, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                    } else if (check == 1) {
                        Intent intent = new Intent(ChangepassActivity.this, LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(ChangepassActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}