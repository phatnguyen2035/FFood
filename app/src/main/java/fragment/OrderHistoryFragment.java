package fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.du_an_1.R;

import java.util.List;

import adapter.OrderAdapter;
import dao.OrderDAO;
import model.Order;

public class OrderHistoryFragment extends Fragment {

    private OrderDAO orderDAO;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;
    private RecyclerView recyclerView;
    private int idUser;
    private int status;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);

        TextView txt_order_confirmation = view.findViewById(R.id.txt_order_confirmation);
        TextView txt_order_in_delivery = view.findViewById(R.id.txt_order_in_delivery);
        TextView txt_order_delivered = view.findViewById(R.id.txt_order_delivered);
        TextView txt_order_cancelled = view.findViewById(R.id.txt_order_cancelled);

        idUser = getIdUserFromSharedPreferences();
        status = 0;

        recyclerView = view.findViewById(R.id.rcv_lits_order_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loaddata();
        txt_order_confirmation.setBackgroundResource(R.drawable.bg_lichsu_xanh);
        txt_order_confirmation.setTextColor(ContextCompat.getColor(getContext(), R.color.black));


        txt_order_confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = 0;
                txt_order_confirmation.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                txt_order_in_delivery.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                txt_order_delivered.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                txt_order_cancelled.setTextColor(ContextCompat.getColor(getContext(), R.color.black));

                txt_order_confirmation.setBackgroundResource(R.drawable.bg_lichsu_xanh);
                txt_order_in_delivery.setBackgroundResource(R.drawable.bg_lichsu_xam);
                txt_order_delivered.setBackgroundResource(R.drawable.bg_lichsu_xam);
                txt_order_cancelled.setBackgroundResource(R.drawable.bg_lichsu_xam);
                loaddata();

            }
        });

        txt_order_in_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = 1;

                txt_order_confirmation.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                txt_order_in_delivery.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                txt_order_delivered.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                txt_order_cancelled.setTextColor(ContextCompat.getColor(getContext(), R.color.black));

                txt_order_confirmation.setBackgroundResource(R.drawable.bg_lichsu_xam);
                txt_order_in_delivery.setBackgroundResource(R.drawable.bg_lichsu_xanh);
                txt_order_delivered.setBackgroundResource(R.drawable.bg_lichsu_xam);
                txt_order_cancelled.setBackgroundResource(R.drawable.bg_lichsu_xam);
                loaddata();
            }
        });
        txt_order_delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = 2;

                txt_order_confirmation.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                txt_order_in_delivery.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                txt_order_delivered.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                txt_order_cancelled.setTextColor(ContextCompat.getColor(getContext(), R.color.black));

                txt_order_confirmation.setBackgroundResource(R.drawable.bg_lichsu_xam);
                txt_order_in_delivery.setBackgroundResource(R.drawable.bg_lichsu_xam);
                txt_order_delivered.setBackgroundResource(R.drawable.bg_lichsu_xanh);
                txt_order_cancelled.setBackgroundResource(R.drawable.bg_lichsu_xam);
                loaddata();
            }
        });
        txt_order_cancelled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = 3;

                txt_order_confirmation.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                txt_order_in_delivery.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                txt_order_delivered.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                txt_order_cancelled.setTextColor(ContextCompat.getColor(getContext(), R.color.black));

                txt_order_confirmation.setBackgroundResource(R.drawable.bg_lichsu_xam);
                txt_order_in_delivery.setBackgroundResource(R.drawable.bg_lichsu_xam);
                txt_order_delivered.setBackgroundResource(R.drawable.bg_lichsu_xam);
                txt_order_cancelled.setBackgroundResource(R.drawable.bg_lichsu_xanh);
                loaddata();
            }
        });


        return view;
    }

    public int getIdUserFromSharedPreferences() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("User_Login", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("idUser", -1);
    }

    public void loaddata() {
        orderDAO = new OrderDAO(getContext());
        orderList = orderDAO.getOrderByIdUserStatus(idUser, status);
        orderAdapter = new OrderAdapter(getContext(), orderList, orderDAO);
        recyclerView.setAdapter(orderAdapter);
    }

}