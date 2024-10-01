package com.example.du_an_1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.du_an_1.databinding.ActivityProductBinding;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import adapter.OrderDetailsAdapter;
import dao.OrderDetailsDAO;
import dao.ProductDAO;
import dao.ShopDAO;
import model.OrderDetails;
import model.Product;
import model.Shop;


public class ProductActivity extends AppCompatActivity {
    private ProductDAO productDAO;
    private Product product;
    private OrderDetailsDAO orderDetailsDAO;
    private ActivityProductBinding binding;
    private List<OrderDetails> orderDetailsList;
    private OrderDetailsAdapter adapter;
    private int quantity = 1;
    private double price;
    private double totalPrice;
    private int idUser;
    private int idShopList;
    private int idRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        idRole = getRoleUserFromSharedPreferences();
        if (idRole != 2) {
            binding.btnAddProductCart.setVisibility(View.GONE);
        }


        Intent intent = getIntent();
        int productId = intent.getIntExtra("productId", -1);


        if (productId != -1) {
            productDAO = new ProductDAO(this);
            product = productDAO.getProductById(productId);
            binding.txtNameProduct.setText(product.getName());
            DecimalFormat decimalFormat = new DecimalFormat("#,###,### đ");
            String formattedPrice = decimalFormat.format(product.getPrice());
            price = product.getPrice();
            totalPrice = quantity * price;
            binding.txtPriceProduct.setText(formattedPrice);
            binding.imgProduct.setImageBitmap(convertByteArrayToBitmap(product.getImage()));
            binding.txtNoteProduct.setText(product.getNote());
            binding.txtSoldProduct.setText(String.valueOf(product.getSold() + " lượt mua"));

            Shop shop = new Shop();
            ShopDAO shopDAO = new ShopDAO(ProductActivity.this);
            shop = shopDAO.getShopByIdShop(product.getIdShop());
            binding.txtNameShop.setText("Shop " + shop.getName());
            binding.txtNameShop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(ProductActivity.this, ShopActivity.class);
                    intent1.putExtra("idShop", product.getIdShop());
                    startActivity(intent1);
                    finish();
                }
            });
        }

        binding.imgMinusQuantityProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity > 1) {
                    quantity--;
                    if (price > 0) {
                        totalPrice = quantity * price;
                    }
                }
                DecimalFormat decimalFormat = new DecimalFormat("#,###,### đ");
                String formattedTotalPrice = decimalFormat.format(totalPrice);
                binding.txtQuantityProduct.setText(String.valueOf(quantity));
                binding.txtPriceProduct.setText(formattedTotalPrice);
            }
        });
        binding.imgPlusQuantityProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity++;
                if (price > 0) {
                    totalPrice = quantity * price;
                }

                DecimalFormat decimalFormat = new DecimalFormat("#,###,### đ");
                String formattedTotalPrice = decimalFormat.format(totalPrice);
                binding.txtQuantityProduct.setText(String.valueOf(quantity));
                binding.txtPriceProduct.setText(formattedTotalPrice);
            }
        });


        binding.imgBackProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.btnAddProductCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderDetailsList == null) {
                    orderDetailsList = new ArrayList<>();
                }
                if (adapter == null) {
                    adapter = new OrderDetailsAdapter(ProductActivity.this, orderDetailsList, orderDetailsDAO);
                }

                orderDetailsDAO = new OrderDetailsDAO(ProductActivity.this);
                idUser = getIdUserFromSharedPreferences();
                int idOrder = idUser;
                int statusOderDetail = 0;
                idShopList = -1;

                orderDetailsList = orderDetailsDAO.getOrderDetailsIdUserStatus(idUser, 0);
                if (orderDetailsList != null && !orderDetailsList.isEmpty()) {
                    OrderDetails firstOrderDetail = orderDetailsList.get(0);
                    idShopList = firstOrderDetail.getIdShop();
                    if (firstOrderDetail.getIdShop() != product.getIdShop()) {
                        Toast.makeText(ProductActivity.this, "Bạn phải thêm sản phẩm cùng 1 shop", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                OrderDetails orderDetailsUp = new OrderDetails();

                orderDetailsUp.setIdShop(product.getIdShop());
                orderDetailsUp.setIdOrder(idOrder);
                orderDetailsUp.setIdProduct(product.getIdProduct());
                orderDetailsUp.setQuantity(quantity);
                orderDetailsUp.setPrice(product.getPrice());
                orderDetailsUp.setTotalPrice(totalPrice);
                orderDetailsUp.setImage(product.getImage());
                orderDetailsUp.setName(product.getName());
                orderDetailsUp.setStatus(0);

                long check = orderDetailsDAO.addOrderDetails(product.getIdShop(), idOrder, product.getIdProduct(), quantity, product.getPrice(), totalPrice, product.getImage(), product.getName(), statusOderDetail);

                if (check > 0) {
                    orderDetailsList.add(orderDetailsUp);
                    Toast.makeText(ProductActivity.this, "Thêm sản phẩm vào giỏ hàng", Toast.LENGTH_SHORT).show();

                    if (product.getIdShop() != idShopList) {
                        Intent intent1 = new Intent(ProductActivity.this, ShopActivity.class);
                        intent1.putExtra("idShop", product.getIdShop());
                        startActivity(intent1);

                    } else {
                        finish();
                    }
                } else if (check == 0) {
                    for (OrderDetails orderDetail : orderDetailsList) {
                        if (orderDetail.getIdProduct() == product.getIdProduct()) {
                            orderDetail.setQuantity(orderDetail.getQuantity() + orderDetailsUp.getQuantity());
                            orderDetail.setTotalPrice(orderDetail.getTotalPrice() + orderDetailsUp.getTotalPrice());
                            orderDetailsDAO.updateOrderDetails(orderDetail);
                            Toast.makeText(ProductActivity.this, "Sản phẩm + " + orderDetailsUp.getQuantity(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(ProductActivity.this, "Lỗi khi thêm sản phẩm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public int getIdUserFromSharedPreferences() {
        SharedPreferences sharedPreferences = ProductActivity.this.getSharedPreferences("User_Login", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("idUser", -1);
    }

    public int getRoleUserFromSharedPreferences() {
        SharedPreferences sharedPreferences = ProductActivity.this.getSharedPreferences("User_Login", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("role", -1);
    }


    private Bitmap convertByteArrayToBitmap(byte[] imageBytes) {
        if (imageBytes != null && imageBytes.length > 0) {
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        } else {
            return BitmapFactory.decodeResource(this.getResources(), R.drawable.side_nav_bar);
        }
    }

    public byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }


}