package fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.du_an_1.R;

import java.util.List;

import adapter.ShopNewAdapter;
import dao.ShopDAO;
import model.Shop;

public class ManegeBoothNewFragment extends Fragment {
    private ShopNewAdapter adapter;
    private ShopDAO shopDAO;
    private RecyclerView rcv_shopListNew;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manege_booth_new, container, false);

        rcv_shopListNew = view.findViewById(R.id.rcv_fragment_manage_booth_new);
        rcv_shopListNew.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        load();
    }

    public void load() {
        ShopDAO shopDao = new ShopDAO(getContext());
        List<Shop> shopList = shopDao.getAllShopsNew();

        adapter = new ShopNewAdapter(shopList, getContext());
        rcv_shopListNew.setAdapter(adapter);
    }
}