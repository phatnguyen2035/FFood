package fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.du_an_1.R;
import com.example.du_an_1.databinding.FragmentCartBuyBinding;

public class CartBuyFragment extends Fragment {
    private FragmentCartBuyBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCartBuyBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        replaceFragment(new OrdersCurrentFragment());

        binding.txtCurrentOrders.setTypeface(null, Typeface.BOLD); // Đặt kiểu chữ Bold

        // Handle TextView clicks to change fragments

        binding.txtCurrentOrders.setOnClickListener(v -> {
            replaceFragment(new OrdersCurrentFragment());

            binding.txtCurrentOrders.setTypeface(null, Typeface.BOLD); // Đặt kiểu chữ Bold
            binding.txtHistoryOrders.setTypeface(null, Typeface.NORMAL);

        });

        binding.txtHistoryOrders.setOnClickListener(v -> {
            replaceFragment(new OrderHistoryFragment());
            binding.txtCurrentOrders.setTypeface(null, Typeface.NORMAL); // Đặt kiểu chữ Bold
            binding.txtHistoryOrders.setTypeface(null, Typeface.BOLD);

        });
        
        return view;

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_oder, fragment);
        fragmentTransaction.commit();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Giải phóng binding để tránh rò rỉ bộ nhớ
        binding = null;
    }
}