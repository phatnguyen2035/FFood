package fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.du_an_1.R;
import com.example.du_an_1.databinding.FragmentOrdersCurrentBinding;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import CustomToast.CustomToast;
import adapter.OrderDetailsAdapter;
import dao.OrderDAO;
import dao.OrderDetailsDAO;
import dao.UserDAO;
import model.Order;
import model.OrderDetails;
import model.User;

public class OrdersCurrentFragment extends Fragment {
    private FragmentOrdersCurrentBinding binding;
    private OrderDetailsDAO orderDetailsDAO;
    private OrderDetailsAdapter adapter;
    private List<OrderDetails> orderDetailsList;
    private Order order;
    private OrderDAO orderDAO;
    private List<Order> orderList;
    private int idUser;
    private Double priceTotal;
    private int quantityProduct;
    private Handler handler = new Handler();
    private Runnable runnable;
    private int idShopShare;

    private Dialog currentDialog; // Biến để lưu trữ dialog hiện tại

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrdersCurrentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        idUser = getIdUserFromSharedPreferences();
        User user = new User();
        UserDAO userDAO = new UserDAO(getContext());
        user = userDAO.getUserByID(idUser);
        binding.txtNameOrderCurent.setText(user.getName());
        binding.txtPhoneOrderCurent.setText("0" + String.valueOf(user.getPhone()));
        binding.txtAddressOrderCurent.setText(user.getAddress());

        // Initialize DAO
        orderDetailsDAO = new OrderDetailsDAO(getContext());
        orderDAO = new OrderDAO(getContext());

        // Initialize lists
        orderList = new ArrayList<>();
        orderDetailsList = new ArrayList<>();

        // Set up RecyclerView
        loadList();

        // Address
        loadAddress();

        // Check if orderDetailsList is null or empty
        binding.txtEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAdded() || isRemoving()) return;

                currentDialog = new Dialog(getContext());
                currentDialog.setContentView(R.layout.diaglog_edit_address);

                // Tìm các view trong layout dialog
                final EditText editName = currentDialog.findViewById(R.id.txt_name_edit_address);
                final EditText editPhone = currentDialog.findViewById(R.id.txt_phone_edit_address);
                final EditText editAddress = currentDialog.findViewById(R.id.txt_address_edit_address);
                final TextView txtCancel = currentDialog.findViewById(R.id.txt_cancel_edit_address);
                final TextView txtUpdate = currentDialog.findViewById(R.id.txt_update_edit_address);

                UserDAO userDAO = new UserDAO(getContext());
                User user = userDAO.getUserByID(idUser);

                // Set data
                editName.setText(user.getName());
                editPhone.setText(String.valueOf(user.getPhone()));
                editAddress.setText(user.getAddress());

                // Thiết lập độ rộng của dialog
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(currentDialog.getWindow().getAttributes());
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                currentDialog.getWindow().setAttributes(layoutParams);

                txtUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editName.getText().toString().isEmpty() || editPhone.getText().toString().isEmpty() || editAddress.getText().toString().isEmpty()) {
                            CustomToast.show(getContext(), "Nhập đầy đủ thông tin", R.mipmap.image_logo_admin);
                            return;
                        }
                        if ((!editPhone.getText().toString().matches("\\d+")) || (editPhone.getText().toString().length() != 10)) {
                            CustomToast.show(getContext(), "Số điện thoại không hợp lệ", R.mipmap.image_logo_admin);
                            return;
                        }

                        user.setName(editName.getText().toString());
                        user.setPhone(Long.parseLong(editPhone.getText().toString()));
                        user.setAddress(editAddress.getText().toString());

                        boolean check = userDAO.upDateAddress(user);
                        if (check) {
                            loadAddress();
                            CustomToast.show(getContext(), "Cập nhật thông tin thành công", R.mipmap.image_logo_admin);
                        } else {
                            CustomToast.show(getContext(), "Cập nhật thông tin thất bại", R.mipmap.image_logo_admin);
                        }

                        currentDialog.dismiss();
                    }
                });

                txtCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentDialog.dismiss();
                    }
                });

                // Hiển thị dialog
                currentDialog.show();
            }
        });

        binding.llOrderDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderDetailsList == null || orderDetailsList.isEmpty()) {
                    Toast.makeText(getContext(),"Giỏ hàng trống", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isAdded() || isRemoving()) return;

                currentDialog = new Dialog(getContext());
                currentDialog.setContentView(R.layout.dialog_confirm_order);

                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(currentDialog.getWindow().getAttributes());
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                currentDialog.getWindow().setAttributes(layoutParams);

                currentDialog.show();

                TextView txt_cancel = currentDialog.findViewById(R.id.txt_canel);
                TextView txt_confirm = currentDialog.findViewById(R.id.txt_confirm);
                EditText txt_note = currentDialog.findViewById(R.id.txt_note);

                txt_note.setText("");
                txt_note.setHint("Hãy nhắn gì đó cho cửa hàng");

                txt_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isAdded() || isRemoving()) return;

                        // Tạo đối tượng Order và set các thuộc tính
                        Order order = new Order();
                        OrderDetails orderDetails1 = orderDetailsList.get(0);
                        idShopShare = orderDetails1.getIdShop();

                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        String formattedDate = sdf.format(date);

                        UserDAO userDAO = new UserDAO(getContext());
                        User user = userDAO.getUserByID(idUser);

                        String note = txt_note.getText().toString();

                        order.setIdShop(idShopShare);
                        order.setIdUser(idUser);
                        order.setQuantity(quantityProduct);
                        order.setTotalPrice(priceTotal);
                        order.setDate(formattedDate);
                        order.setNote(note);
                        order.setName(user.getName());
                        order.setPhone(user.getPhone());
                        order.setAddress(user.getAddress());
                        order.setStatus(0);

                        long orderId = orderDAO.addOrder(order.getIdShop(), order.getIdUser(), order.getQuantity(), order.getTotalPrice(), order.getDate(), order.getNote(), order.getName(), order.getPhone(), order.getAddress(), order.getStatus());

                        if (orderId != -1) {
                            order.setIdOrder((int) orderId);
                            orderList.add(order);
                            for (OrderDetails orderDetails : orderDetailsList) {
                                orderDetails.setIdOrder(order.getIdOrder());
                                orderDetails.setStatus(1);

                                boolean isUpdated = orderDetailsDAO.updateOrderDetailsToOrder(orderDetails);
                                if (!isUpdated) {
                                    Toast.makeText(getContext(),"Cập nhật idOder trong OrderDetails thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }
                            adapter.notifyDataSetChanged();
                            orderDetailsList.clear();
                            loadList();
                            Toast.makeText(getContext(),"Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(),"Đặt hàng thất bại", Toast.LENGTH_SHORT).show();
                        }
                        currentDialog.dismiss();
                    }
                });

                txt_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        currentDialog.dismiss();
                    }
                });
            }
        });

        // Update total price
        updateTotalPrice();

        // Setup the runnable to update the price periodically
        runnable = new Runnable() {
            @Override
            public void run() {
                updateTotalPrice();
                handler.postDelayed(this, 500);
            }
        };
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.post(runnable);
        loadList();
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
        if (currentDialog != null && currentDialog.isShowing()) {
            currentDialog.dismiss();
        }
    }

    private int getIdUserFromSharedPreferences() {
        if (getContext() != null) {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("User_Login", Context.MODE_PRIVATE);
            return sharedPreferences.getInt("idUser", -1);
        }
        return -1;
    }

    private void updateTotalPrice() {
        priceTotal = 0.0;
        for (OrderDetails orderDetails : orderDetailsList) {
            priceTotal += orderDetails.getTotalPrice();
        }

        DecimalFormat decimalFormat = new DecimalFormat("#,###,### đ");
        binding.txtTotalPrice.setText(decimalFormat.format(priceTotal));

        quantityProduct = 0;
        for (OrderDetails orderDetails : orderDetailsList) {
            quantityProduct += orderDetails.getQuantity();
            binding.txtQuantityProduct.setText("Tổng đơn hàng: " + String.valueOf(quantityProduct));
        }
        if (orderDetailsList == null || orderDetailsList.isEmpty() || orderDetailsList.size() == 0) {
            binding.imgCardEmpty.setVisibility(View.VISIBLE);
        } else {
            binding.imgCardEmpty.setVisibility(View.GONE);
        }
    }

    public void loadList() {
        RecyclerView recyclerView = binding.rcvLitsOrderDetails;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize data and adapter
        orderDetailsList = orderDetailsDAO.getOrderDetailsIdUserStatus(idUser, 0);
        adapter = new OrderDetailsAdapter(getContext(), orderDetailsList, orderDetailsDAO);
        recyclerView.setAdapter(adapter);
        if (orderDetailsList.isEmpty() || (orderDetailsList == null)) {
            binding.txtQuantityProduct.setText("Tổng đơn hàng: 0");
        }
        if (orderDetailsList == null || orderDetailsList.isEmpty() || orderDetailsList.size() == 0) {
            binding.imgCardEmpty.setVisibility(View.VISIBLE);
        } else {
            binding.imgCardEmpty.setVisibility(View.GONE);
        }
    }

    public void loadAddress() {
        final UserDAO userDAO = new UserDAO(getContext());
        final User user = userDAO.getUserByID(idUser);

        binding.txtNameOrderCurent.setText(user.getName());
        binding.txtPhoneOrderCurent.setText("0" + String.valueOf(user.getPhone()));
        binding.txtAddressOrderCurent.setText(user.getAddress());
    }
}