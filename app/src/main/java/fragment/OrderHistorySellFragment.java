package fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.du_an_1.R;
import com.example.du_an_1.databinding.FragmentOrderHistorySellBinding;

import java.util.List;

import adapter.OrderSellAdapter;
import dao.OrderDAO;
import dao.ShopDAO;
import model.Order;


public class OrderHistorySellFragment extends Fragment {
    private FragmentOrderHistorySellBinding binding;
    private OrderDAO orderDAO;
    private OrderSellAdapter sellAdapter;
    private List<Order> orderList;
    private RecyclerView recyclerView;
    private int idUser;
    private int status;
    private int idShop;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrderHistorySellBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        TextView txt_order_confirmation = view.findViewById(R.id.txt_order_confirmation);
        TextView txt_order_in_delivery = view.findViewById(R.id.txt_order_in_delivery);
        TextView txt_order_delivered = view.findViewById(R.id.txt_order_delivered);
        TextView txt_order_cancelled = view.findViewById(R.id.txt_order_cancelled);



        idUser = getIdUserFromSharedPreferences();
        ShopDAO shopDAO = new ShopDAO(getContext());
        idShop = shopDAO.getIdShop(idUser);
        status = 0;

        recyclerView = view.findViewById(R.id.rcv_lits_order_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loaddata();
        txt_order_confirmation.setBackgroundResource(R.drawable.bg_lichsu_xanh);

        txt_order_confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = 0;
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
        orderList = orderDAO.getOrderByIdShopStatus(idShop, status);
        sellAdapter = new OrderSellAdapter(getContext(), orderList, orderDAO);
        recyclerView.setAdapter(sellAdapter);
    }
}