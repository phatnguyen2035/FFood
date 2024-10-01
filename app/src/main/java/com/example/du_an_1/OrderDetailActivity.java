package com.example.du_an_1;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.du_an_1.databinding.ActivityOrderDetailsBinding;

import java.text.DecimalFormat;
import java.util.List;

import adapter.OrderDetailsAdpaterSell;
import dao.OrderDAO;
import dao.OrderDetailsDAO;
import model.Order;
import model.OrderDetails;

public class OrderDetailActivity extends AppCompatActivity {

    private ActivityOrderDetailsBinding binding;
    private OrderDetailsDAO orderDetailsDAO;
    private List<OrderDetails> orderDetailsList;
    private OrderDetailsAdpaterSell orderDetailsAdapterSell; // Sửa tên lớp nếu cần
    private int idOrder;
    private double quantity;
    private double totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        idOrder = getIntent().getIntExtra("idOrder", 0);

        RecyclerView recyclerView = binding.rcvLitsOrderDetails;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderDetailsDAO = new OrderDetailsDAO(this);
        orderDetailsList = orderDetailsDAO.getOrderDetailsIdOrder(idOrder);

        orderDetailsAdapterSell = new OrderDetailsAdpaterSell(this, orderDetailsList, orderDetailsDAO);
        recyclerView.setAdapter(orderDetailsAdapterSell);

        totalPrice = 0;
        quantity = 0;
        for (OrderDetails orderDetails1 : orderDetailsList) {
            totalPrice += orderDetails1.getTotalPrice();
            quantity += orderDetails1.getQuantity();
        }

        DecimalFormat decimalFormat = new DecimalFormat("#,###,### đ");
        DecimalFormat decimalFormat1 = new DecimalFormat("#");
        binding.txtQuantityOrder.setText("Số lượng: " + String.valueOf(decimalFormat1.format(quantity)));

        binding.txtTotalPriceOrder.setText("Tổng tiền: " + String.valueOf(decimalFormat.format(totalPrice)));

        Order order = new Order();
        OrderDAO orderDAO = new OrderDAO(OrderDetailActivity.this);
        order = orderDAO.getOrderByIdOrder(idOrder);

        binding.txtNameOrderCurent.setText(order.getName());
        binding.txtPhoneOrderCurent.setText(String.valueOf(order.getPhone()));
        binding.txtAddressOrderCurent.setText(order.getAddress());

        binding.txtDateOrder.setText("Ngày đặt: " + order.getDate());
        binding.txtNoteOrder.setText("Lời nhắn: "+order.getNote());
        binding.mid.setText("Đơn hàng số: "+ order.getIdOrder());
        if (order.getStatus() == 0) {
            binding.txtStatusOrder.setText("Trạng Thái: Chờ xác nhận");
        }
        if (order.getStatus() == 1) {
            binding.txtStatusOrder.setText("Trạng Thái: Đang giao hàng");
        }
        if (order.getStatus() == 2) {
            binding.txtStatusOrder.setText("Trạng Thái: Đã giao hàng");
        }
        if (order.getStatus() == 3) {
            binding.txtStatusOrder.setText("Trạng Thái: Đã hủy");
        }

        binding.imgBackOrderDetails.setOnClickListener(view -> {
            finish();
        });

    }
}