package fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.du_an_1.R;
import com.example.du_an_1.databinding.FragmentStatisticalAdminBinding;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import adapter.ShopItemAdapter;
import adapter.ShopSelectAdapter;
import dao.OrderDAO;
import dao.ShopDAO;
import model.Order;
import model.Shop;

public class StatisticalAdminFragment extends Fragment implements ShopSelectAdapter.OnItemClickListener {
    private FragmentStatisticalAdminBinding binding;
    private int idShop;
    private Dialog dialog;
    private ShopDAO shopDAO;
    private Shop shop;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStatisticalAdminBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.txtMenuStatistical.setText("Ngày");
        binding.txtDateStatistical.setText("dd/mm/yyyy");
        binding.llSelectShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_show_select_shop);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                RecyclerView recyclerView = dialog.findViewById(R.id.rcv_select_shop);
                StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                shopDAO = new ShopDAO(getContext());
                List<Shop> shopList = shopDAO.getlitsShopIsActive();
                ShopSelectAdapter shopSelectAdapter = new ShopSelectAdapter(getContext(), shopList);
                shopSelectAdapter.setOnItemClickListener(StatisticalAdminFragment.this);
                recyclerView.setAdapter(shopSelectAdapter);
                dialog.show();
            }
        });

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
        OrderDAO orderDAO = new OrderDAO(getContext());
        if (shop == null) {
            return;
        }

        switch (x) {
            case 1:
                List<Order> orderListConfirm = orderDAO.getOrderByIdShopStatus(shop.getIdShop(), 2);
                List<Order> orderListCanel = orderDAO.getOrderByIdShopStatus(shop.getIdShop(), 3);
                List<Order> orderListAllConfrim = orderDAO.getOrderByStatus(2);
                List<Order> orderListAllCannel = orderDAO.getOrderByStatus(3);
                List<Order> orderListStatus2And3 = orderDAO.getOrderByStatus2And3();




                int quantityOrderConfirm = orderListConfirm.size();
                int quantityQrderCanel = orderListCanel.size();

                double totalPriceTong = 0.0;


                double totalPrice = 0;
                for (Order order : orderListConfirm) {
                    totalPrice += order.getTotalPrice();
                }

                //time
                if (orderListStatus2And3 != null && !orderListStatus2And3.isEmpty()) {
                    String date1 = orderListStatus2And3.get(0).getDate();
                    SimpleDateFormat inputFormat1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    SimpleDateFormat outputFormat1 = new SimpleDateFormat("dd/MM/yyyy");

                    String date2 = orderListStatus2And3.get((orderListStatus2And3.size() - 1)).getDate();
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

                for (Order order : orderListAllConfrim) {
                    totalPriceTong += order.getTotalPrice();
                }


                DecimalFormat decimalFormat = new DecimalFormat("#,###,###" + " đ");
                binding.txtQuantityOrderConfirm.setText(String.valueOf(quantityOrderConfirm));
                binding.txtQuantityOrderCanel.setText(String.valueOf(quantityQrderCanel));
                binding.txtQuantityOrder.setText(String.valueOf(quantityOrderConfirm + quantityQrderCanel));
                binding.txtTotalPrice.setText(decimalFormat.format(totalPrice));

                binding.txtTotalPriceAllShop.setText(decimalFormat.format(totalPriceTong));
                binding.txtQuantityOrderCanelSum.setText(String.valueOf(orderListAllCannel.size()));
                binding.txtQuantityOrderConfirmSum.setText(String.valueOf(orderListAllConfrim.size()));
                binding.txtQuantityOrderSum.setText(String.valueOf(orderListAllConfrim.size() + orderListAllCannel.size()));

                break;

            case 2:
                List<Order> orderListConfirm1 = orderDAO.getOrderByIdShopStatus(shop.getIdShop(), 2);
                List<Order> orderListCanel1 = orderDAO.getOrderByIdShopStatus(shop.getIdShop(), 3);
                List<Order> orderListAllConfrim1 = orderDAO.getOrderByStatus(2);
                List<Order> orderListAllCannel1 = orderDAO.getOrderByStatus(3);


                Date today = Calendar.getInstance().getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String formattedToday = formatter.format(today);

                SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

                // lay so luong don huy ngay hom nay theo shop
                int quantityQrderCanelToday = 0;
                for (Order order : orderListCanel1) {
                    String dateS = order.getDate();
                    try {
                        Date dateObj = inputFormat.parse(dateS);
                        String dateDDMMYYYY = outputFormat.format(dateObj);

                        if (dateDDMMYYYY.equals(formattedToday)) {
                            quantityQrderCanelToday ++;
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                // lay so luong don thanh cong va tong tien trong hom nay theo shop
                int quantityQrderConfirmToday = 0;
                double totalPriceToday = 0.0;

                for (Order order : orderListConfirm1) {
                    String dateS = order.getDate();
                    try {
                        Date dateObj = inputFormat.parse(dateS);
                        String dateDDMMYYYY = outputFormat.format(dateObj);

                        if (dateDDMMYYYY.equals(formattedToday)) {
                            quantityQrderConfirmToday ++;
                            totalPriceToday += order.getTotalPrice();
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                //lay so luong don thanh cong va tong tien hom nay, tat ca shop
                int quantityQrderConfirmAllToday = 0;
                double priceQrderConfirmAllToday = 0.0;
                for (Order order : orderListAllConfrim1) {
                    String dateS = order.getDate();
                    try {
                        Date dateObj = inputFormat.parse(dateS);
                        String dateDDMMYYYY = outputFormat.format(dateObj);

                        if (dateDDMMYYYY.equals(formattedToday)) {
                            quantityQrderConfirmAllToday ++;
                            priceQrderConfirmAllToday+= order.getTotalPrice();
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                //lay so luong don huy hom nay
                int quantityQrderCanelAllToday = 0;
                for (Order order : orderListAllCannel1) {
                    String dateS = order.getDate();
                    try {
                        Date dateObj = inputFormat.parse(dateS);
                        String dateDDMMYYYY = outputFormat.format(dateObj);

                        if (dateDDMMYYYY.equals(formattedToday)) {
                            quantityQrderCanelAllToday ++;
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }


                DecimalFormat decimalFormat1 = new DecimalFormat("#,###,###" + " đ");
                //ngay hom nay
                binding.txtDateStatistical.setText(formattedToday);
                //so luong don huy, thanh cong hom nay cua 1 shop
                binding.txtQuantityOrderConfirm.setText(String.valueOf(quantityQrderConfirmToday));
                binding.txtQuantityOrderCanel.setText(String.valueOf(quantityQrderCanelToday));
                binding.txtQuantityOrder.setText(String.valueOf(quantityQrderConfirmToday + quantityQrderCanelToday));
                binding.txtTotalPrice.setText(decimalFormat1.format(totalPriceToday));
                //so luong don huy, thanh cong tat ca cac shop
                binding.txtTotalPriceAllShop.setText(decimalFormat1.format(priceQrderConfirmAllToday));
                binding.txtQuantityOrderSum.setText(String.valueOf(quantityQrderCanelAllToday+quantityQrderConfirmAllToday));
                binding.txtQuantityOrderConfirmSum.setText(String.valueOf(quantityQrderConfirmAllToday));
                binding.txtQuantityOrderCanelSum.setText(String.valueOf(quantityQrderCanelAllToday));
                break;
            case 3:
                break;
        }
    }

    @Override
    public void onItemClick(Shop shop1) {
        shop = shop1;
        getStatistical(2);
        binding.txtMenuStatistical.setText("Hôm nay");
        Toast.makeText(getContext(), "Bạn đã chọn shop" + shop.getName(), Toast.LENGTH_SHORT).show();
        dialog.dismiss();

    }

}
