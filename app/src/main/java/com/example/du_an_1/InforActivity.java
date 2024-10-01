package com.example.du_an_1;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import dao.OrderDetailsDAO;
import model.OrderDetails;

public class InforActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor);

        Button button = findViewById(R.id.btnLoginm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Khởi tạo DAO để truy xuất dữ liệu
                OrderDetailsDAO orderDetailsDAO = new OrderDetailsDAO(InforActivity.this);
                List<OrderDetails> orderDetailsList = orderDetailsDAO.getOrderDetailsListStatus(1);

                if (orderDetailsList.isEmpty()) {
                    // Thông báo cho người dùng nếu không có dữ liệu
                    Toast.makeText(InforActivity.this, "Không có chi tiết đơn hàng với trạng thái 2", Toast.LENGTH_SHORT).show();
                } else {
                    // Ghi log thông tin các chi tiết đơn hàng
                    int i = 0;
                    for (OrderDetails orderDetails : orderDetailsList) {
                        i++;
                        Log.d(TAG, "Order " + i + ": " + orderDetails.getIdOrderDetails() + " - Quantity: " + orderDetails.getQuantity());
                    }
                }
            }
        });
    }
}
