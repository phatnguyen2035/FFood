package com.example.du_an_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import dao.OrderDetailsDAO;
import dao.UserDAO;
import model.User;

public class LoginActivity extends AppCompatActivity {
    private int role, idUser;
    private String user, pass, name;
    private long cccd, phone;
    private OrderDetailsDAO orderDetailsDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ImageView btnBack = findViewById(R.id.btnBack);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView txtChangePass = findViewById(R.id.txtChangePass);
        TextView txtSignIn = findViewById(R.id.txtSignIn);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkUserPass();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, StarActivity.class);
                startActivity(intent);
            }
        });

        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SigninActivity.class);
                startActivity(intent);
            }
        });


    }


    public void checkUserPass() {
        EditText edtUser = findViewById(R.id.edtUsername);
        EditText edtPass = findViewById(R.id.edtPassword);

        String user = edtUser.getText().toString();
        String pass = edtPass.getText().toString();
        UserDAO userDAO = new UserDAO(LoginActivity.this);

        // Check user pass
        int check = userDAO.login(user, pass);

        switch (check) {
            case -1:
                Toast.makeText(LoginActivity.this, "Tài khoản bị khoá", Toast.LENGTH_SHORT).show();
                return;
            case 1:
                Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                break;
            case 0:
                Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                sharedPreferences();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
        }
    }

    // Save user information in SharedPreferences
    public void sharedPreferences() {
        EditText edtUser = findViewById(R.id.edtUsername);
        EditText edtPass = findViewById(R.id.edtPassword);

        String username = edtUser.getText().toString();
        String password = edtPass.getText().toString();

        UserDAO userDAO = new UserDAO(LoginActivity.this);
        User user1 = userDAO.getUser(new User(username, password));

        idUser = user1.getIdUser();
        user = user1.getUser();
        pass = user1.getPass();
        name = user1.getPass();
        phone = user1.getPhone();
        cccd = user1.getCccd();
        role = user1.getRole();

        // Save rol
        SharedPreferences sharedPreferences = getSharedPreferences("User_Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("idUser", idUser);
        editor.putString("user", user);
        editor.putString("name", name);
        editor.putString("pass", pass);
        editor.putLong("phone", phone);
        editor.putLong("cccd", cccd);
        editor.putInt("role", role);
        editor.apply();
    }


//    public int getRoleFromSharedPreferences() {
//        SharedPreferences sharedPreferences = getSharedPreferences("User_Login", Context.MODE_PRIVATE);
//        return sharedPreferences.getInt("role", -1);
//    }

//    int role = getRoleFromSharedPreferences();


}