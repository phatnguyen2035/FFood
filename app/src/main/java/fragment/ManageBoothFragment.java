package fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.du_an_1.R;

import java.util.List;

import adapter.ShopAdapter;
import dao.ShopDAO;
import model.Shop;

public class ManageBoothFragment extends Fragment {
    private ShopAdapter adapter;
    private ShopDAO shopDAO;
    private RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_manage_booth, container, false);

        recyclerView = rootView.findViewById(R.id.rcv_fragment_manage_booth);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadShopList();

        return rootView;
    }


    private void loadShopList() {
        ShopDAO shopDao = new ShopDAO(getContext());
        List<Shop> shopList = shopDao.getAllShops();
        adapter = new ShopAdapter(shopList, getContext());
        recyclerView.setAdapter(adapter);
    }


}
