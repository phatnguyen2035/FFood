package fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.du_an_1.R;

import java.util.List;

import adapter.AdminAdapter;
import dao.UserDAO;
import model.User;

public class ManageUserFragment extends Fragment {
    private AdminAdapter adapter;
    private UserDAO userDAO;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_manage_user, container, false);

        recyclerView = rootView.findViewById(R.id.rcv_fragment_manage_user);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadUserList();
        return rootView;
    }

    private void loadUserList() {
        UserDAO userDAO = new UserDAO(getContext());
        List<User> userList = userDAO.getListUser();

        adapter = new AdminAdapter(userList, getContext(), userDAO);
        recyclerView.setAdapter(adapter);
    }
}
