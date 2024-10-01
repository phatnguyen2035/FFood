package com.example.du_an_1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.du_an_1.databinding.ActivityShopBinding;

import java.util.List;

import adapter.SearchAdapter;
import dao.OrderDetailsDAO;
import dao.ProductDAO;
import dao.ShopDAO;
import dao.UserDAO;
import model.OrderDetails;
import model.Product;
import model.Shop;
import model.User;

public class ShopActivity extends AppCompatActivity {
    private ActivityShopBinding binding;
    private RecyclerView rcvEat, rcvDrink;
    private ProductDAO productDAO;
    private SearchAdapter searchAdapter1, searchAdapter2;
    private int idUser, idShop;
    private ShopDAO shopDAO;
    private Runnable runnable;
    private Handler handler = new Handler(); // Initialize handler
    private int quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityShopBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            binding.getRoot().setPadding(systemBarsInsets.left, systemBarsInsets.top, systemBarsInsets.right, systemBarsInsets.bottom);
            return insets;
        });

        int role = getRoleUserFromSharedPreferences();
        if (role != 2) {
            binding.rlCartOrder.setVisibility(View.GONE);
        }

        Intent intent = getIntent();
        idShop = intent.getIntExtra("idShop", -1);

        rcvEat = binding.rcvShopEatBuy;
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rcvEat.setLayoutManager(layoutManager);

        rcvDrink = binding.rcvShopDrinkBuy;
        StaggeredGridLayoutManager layoutManager1 = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rcvDrink.setLayoutManager(layoutManager1);

        loadProductList();
        loadProductList1();

        binding.imgBackProduct.setOnClickListener(view -> finish());

        shopDAO = new ShopDAO(this);
        Shop shop = shopDAO.getShopByIdShop(idShop);

        idUser = shop.getIdUser();
        UserDAO userDAO = new UserDAO(this);
        User user = userDAO.getUserByID(idUser);

        binding.txtQuantityOrder.setText(String.valueOf(quantity));
        binding.txtAddressShopBuy.setText("Địa chỉ: " + shop.getAddress());
        binding.txtPhoneShopBuy.setText(String.valueOf("Hotline: " + user.getPhone()));
        binding.txtNameShopBuy.setText(shop.getName());
        binding.imgImageShopBuy.setImageBitmap(convertByteArrayToBitmap(shop.getImage()));

        binding.rlCartOrder.setOnClickListener(view -> {
            Intent intent1 = new Intent(ShopActivity.this, MainActivity.class);
            intent1.putExtra("CartBuyFragment", "CartBuyFragment");
            startActivity(intent1);
        });

        // Setup the runnable to update the price periodically
        runnable = new Runnable() {
            @Override
            public void run() {
                loadQuantity();
                handler.postDelayed(this, 500);
            }
        };
    }

    public void loadProductList() {
        productDAO = new ProductDAO(this);
        List<Product> productList = productDAO.getProductsListEatShop(idShop);
        searchAdapter1 = new SearchAdapter(this, productList, productDAO);
        rcvEat.setAdapter(searchAdapter1);
    }

    public void loadProductList1() {
        productDAO = new ProductDAO(this);
        List<Product> productList = productDAO.getProductsListDrinksShop(idShop);
        searchAdapter2 = new SearchAdapter(this, productList, productDAO);
        rcvDrink.setAdapter(searchAdapter2);
    }

    private Bitmap convertByteArrayToBitmap(byte[] imageBytes) {
        if (imageBytes != null && imageBytes.length > 0) {
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        } else {
            return BitmapFactory.decodeResource(ShopActivity.this.getResources(), R.drawable.side_nav_bar);
        }
    }

    public int getidUserFromSharedPreferences() {
        SharedPreferences sharedPreferences = ShopActivity.this.getSharedPreferences("User_Login", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("idUser", -1);
    }

    public int getRoleUserFromSharedPreferences() {
        SharedPreferences sharedPreferences = ShopActivity.this.getSharedPreferences("User_Login", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("role", -1);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.post(runnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    public void loadQuantity() {
        OrderDetailsDAO orderDetailsDAO = new OrderDetailsDAO(ShopActivity.this); // Correctly initialized
        List<OrderDetails> orderDetailsList = orderDetailsDAO.getOrderDetailsIdUserStatus(getidUserFromSharedPreferences(), 0);
        quantity = 0; // Reset quantity before calculating
        for (OrderDetails orderDetail : orderDetailsList) {
            quantity += orderDetail.getQuantity();
        }
        binding.txtQuantityOrder.setText(String.valueOf(quantity)); // Update the quantity on the UI
    }
}