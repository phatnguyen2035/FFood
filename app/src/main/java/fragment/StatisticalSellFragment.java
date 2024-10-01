package fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.du_an_1.R;
import com.example.du_an_1.databinding.FragmentStatisticalSellBinding;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import dao.OrderDAO;
import dao.ShopDAO;
import model.Order;
import model.Shop;

public class StatisticalSellFragment extends Fragment {
    private FragmentStatisticalSellBinding binding;
    private int idUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatisticalSellBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        getStatistical(2);


        binding.txtMenuStatistical.setText("Hôm nay");
        binding.txtMenuStatistical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), view, Gravity.RIGHT);
                popupMenu.getMenuInflater().inflate(R.menu.menu_statistics, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.item1) {
                            getStatistical(2);
                            binding.txtMenuStatistical.setText("Hôm nay");
                        } else if (item.getItemId() == R.id.item2) {
                            getStatistical(1);
                            binding.txtMenuStatistical.setText("Tất cả");
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });


        return view;
    }

    public void getStatistical(int x) {


        ShopDAO shopDAO = new ShopDAO(getContext());
        OrderDAO orderDAO = new OrderDAO(getContext());
        Shop shop = shopDAO.getShopByIdUser(getIdUserFromSharedPreferences());

        switch (x) {

            case 1:
                List<Order> orderListConfirm = orderDAO.getOrderByIdShopStatus(shop.getIdShop(), 2);
                List<Order> orderListCanel = orderDAO.getOrderByIdShopStatus(shop.getIdShop(), 3);
                int quantityOrderConfirm = orderListConfirm.size();
                int quantityQrderCanel = orderListCanel.size();

                double totalPrice = 0;
                for (Order order : orderListConfirm) {
                    totalPrice += order.getTotalPrice();
                }

                if (orderListConfirm != null && !orderListConfirm.isEmpty()) {
                    String date1 = orderListConfirm.get(0).getDate();
                    SimpleDateFormat inputFormat1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    SimpleDateFormat outputFormat1 = new SimpleDateFormat("dd/MM/yyyy");

                    String date2 = orderListConfirm.get((orderListConfirm.size() - 1)).getDate();
                    SimpleDateFormat inputFormat2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    SimpleDateFormat outputFormat2 = new SimpleDateFormat("dd/MM/yyyy");

                    try {
                        Date dateObj1 = inputFormat1.parse(date1);
                        String formattedDate1 = outputFormat1.format(dateObj1);

                        Date dateObj2 = inputFormat2.parse(date2);
                        String formattedDate2 = outputFormat2.format(dateObj2);
                        binding.txtDateStatistical.setText(formattedDate2 + " - " + formattedDate1);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    binding.txtDateStatistical.setText("N/A");
                }
                DecimalFormat decimalFormat = new DecimalFormat("#,###,###" + "VND");
                binding.txtQuantityOrderConfirm.setText(String.valueOf(quantityOrderConfirm));
                binding.txtQuantityOrderCanel.setText(String.valueOf(quantityQrderCanel));
                binding.txtQuantityOrder.setText(String.valueOf(quantityOrderConfirm + quantityQrderCanel));
                binding.txtTotalPrice.setText(decimalFormat.format(totalPrice));

                break;

            case 2:
                List<Order> orderListConfirm1 = orderDAO.getOrderByIdShopStatus(shop.getIdShop(), 2);
                List<Order> orderListCanel1 = orderDAO.getOrderByIdShopStatus(shop.getIdShop(), 3);

                int quantityOrderConfirm1 = 0;
                int quantityQrderCanel1 = 0;
                Double totalPrice1 = 0.0;

                Date today = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
                String formattedToday = dateFormatter.format(today);

                SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

                for (Order order : orderListCanel1) {
                    String dateS = order.getDate();
                    try {
                        Date dateObj = inputFormat.parse(dateS);
                        String dateDDMMYYYY = outputFormat.format(dateObj);

                        if (dateDDMMYYYY.equals(formattedToday)) {
                            quantityQrderCanel1 ++;
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                for (Order order : orderListConfirm1) {
                    String dateS = order.getDate();
                    try {
                        Date dateObj = inputFormat.parse(dateS);
                        String dateDDMMYYYY = outputFormat.format(dateObj);

                        if (dateDDMMYYYY.equals(formattedToday)) {
                            quantityOrderConfirm1 ++;
                            totalPrice1 += order.getTotalPrice();
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                DecimalFormat decimalFormat1 = new DecimalFormat("#,###,###" + " VND");
                binding.txtDateStatistical.setText(formattedToday);
                binding.txtQuantityOrderConfirm.setText(String.valueOf(quantityOrderConfirm1));
                binding.txtQuantityOrderCanel.setText(String.valueOf(quantityQrderCanel1));
                binding.txtQuantityOrder.setText(String.valueOf(quantityOrderConfirm1 + quantityQrderCanel1));
                binding.txtTotalPrice.setText(decimalFormat1.format(totalPrice1));
                break;


            case 3:
                break;
        }
    }


    public int getIdUserFromSharedPreferences() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("User_Login", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("idUser", -1);
    }
}