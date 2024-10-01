package fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.du_an_1.R;
import com.example.du_an_1.databinding.FragmentManageAdminBinding;


public class ManageAdminFragment extends Fragment {
    private FragmentManageAdminBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentManageAdminBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Load the default fragment
        replaceFragment(new ManegeBoothNewFragment());
        binding.ivBoothNew.setVisibility(View.VISIBLE);
        binding.ivUser.setVisibility(View.GONE);
        binding.ivBooth.setVisibility(View.GONE);

        // Handle TextView clicks to change fragments
        binding.txtBoothNewManage.setOnClickListener(v -> {
            replaceFragment(new ManegeBoothNewFragment());
            binding.ivBoothNew.setVisibility(View.VISIBLE);
            binding.ivUser.setVisibility(View.GONE);
            binding.ivBooth.setVisibility(View.GONE);
        });

        binding.txtBoothManage.setOnClickListener(v -> {
            replaceFragment(new ManageBoothFragment());
            binding.ivBooth.setVisibility(View.VISIBLE);
            binding.ivUser.setVisibility(View.GONE);
            binding.ivBoothNew.setVisibility(View.GONE);

        });

        binding.txtUserManage.setOnClickListener(v -> {
            replaceFragment(new ManageUserFragment());
            binding.ivBooth.setVisibility(View.GONE);
            binding.ivUser.setVisibility(View.VISIBLE);
            binding.ivBoothNew.setVisibility(View.GONE);

        });
        return view;
    }

    // Replace fragment method
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout_mangane, fragment);
        fragmentTransaction.commit();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Giải phóng binding để tránh rò rỉ bộ nhớ
        binding = null;
    }

}
