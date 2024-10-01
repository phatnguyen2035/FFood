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
import com.example.du_an_1.databinding.FragmentCartSellBinding;

public class CartSellFragment extends Fragment {
    private FragmentCartSellBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCartSellBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        replaceFragment(new OrderHistorySellFragment());
        binding.txtCurrentOrdersSell.setTypeface(null, Typeface.BOLD);

        return view;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_oder_sell, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Giải phóng binding để tránh rò rỉ bộ nhớ
        binding = null;
    }
}