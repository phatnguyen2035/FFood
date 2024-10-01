package com.example.du_an_1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.du_an_1.databinding.ActivityMainBinding;

import dao.ShopDAO;
import dao.UserDAO;
import fragment.CartBuyFragment;
import fragment.CartSellFragment;
import fragment.HomeFragment;
import fragment.ManageAdminFragment;
import fragment.ManageBoothFragment;
import fragment.ManageBuyerFragment;
import fragment.ManageCategoryFragment;
import fragment.ManageSellerFragment;
import fragment.SearchFragment;
import fragment.SettingFragment;
import fragment.ShopIsLockedFragment;
import fragment.StatisticalAdminFragment;
import fragment.StatisticalSellFragment;
import model.Shop;
import model.User;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int role = getRoleFromSharedPreferences();
        ShopDAO shopDAO = new ShopDAO(MainActivity.this);

        Shop shop = shopDAO.getShopByIdUser(getIdUserFromSharedPreferences());


        checkRole();

        Intent intent = getIntent();
        String fragmentName = null;
        if (intent.hasExtra("CartBuyFragment")) {
            fragmentName = intent.getStringExtra("CartBuyFragment");
        } else if (intent.hasExtra("SearchFragment")) {
            fragmentName = intent.getStringExtra("SearchFragment");
        } else if (intent.hasExtra("SettingFragment")) {
            fragmentName = intent.getStringExtra("SettingFragment");
        }

        if (fragmentName != null && !fragmentName.isEmpty()) {
            replaceFragment(getFragmentByName(fragmentName));
            binding.bottomNavigation.setSelectedItemId(getMenuItemIdByFragmentName(fragmentName));
        } else {
            if (role == 0) {
                replaceFragment(new HomeFragment());
            } else if (role == 1) {
                replaceFragment(new ManageSellerFragment());
                binding.bottomNavigation.setSelectedItemId(R.id.nav_search);
            } else if (role == 2) {
                replaceFragment(new HomeFragment());

            }
        }

        // Handle bottom navigation item selection
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            //tim kiem
            if (item.getItemId() == R.id.nav_home) {
                if (role == 1) {
                    replaceFragment(new SearchFragment());
                } else {
                    replaceFragment(new HomeFragment());
                }

            }


            //shop
            else if (item.getItemId() == R.id.nav_search) {
                if (role == 1) {
                    if (shop.getStatus() == 0) {
                        replaceFragment(new ShopIsLockedFragment());
                    } else {
                        replaceFragment(new ManageSellerFragment());
                    }
                } else {
                    replaceFragment(new SearchFragment());
                }
            }

            //thong ke
            else if (item.getItemId() == R.id.nav_cart) {
                if (role == 1) {
                    replaceFragment(new CartSellFragment());
                } else if (role == 2) {
                    replaceFragment(new CartBuyFragment());
                } else if (role == 0) {
                    replaceFragment(new ManageAdminFragment());
                }
            }
            //don hang
            else if (item.getItemId() == R.id.nav_shop) {
                if (role == 1) {
                    replaceFragment(new StatisticalSellFragment());

                } else if (role == 0) {
                    replaceFragment(new StatisticalAdminFragment());

                } else if (role == 2) {
                    replaceFragment(new ManageBuyerFragment());
                }
            }
            //cai dat
            else if (item.getItemId() == R.id.nav_setting) {
                replaceFragment(new SettingFragment());
            }


            return true;
        });

        int[][] states = new int[][]{new int[]{android.R.attr.state_checked}, // trạng thái được chọn
                new int[]{-android.R.attr.state_checked} // trạng thái không được chọn
        };

        int[] colors = new int[]{getResources().getColor(R.color.orange), getResources().getColor(R.color.navbottombar_nochoose)};

        ColorStateList colorStateList = new ColorStateList(states, colors);

        // Thiết lập màu sắc cho các mục trong BottomNavigationView
        binding.bottomNavigation.setItemIconTintList(colorStateList);
        binding.bottomNavigation.setItemTextColor(colorStateList);
    }

    // Method to get Fragment by name
    private Fragment getFragmentByName(String fragmentName) {
        switch (fragmentName) {
            case "CartSellFragment":
                return new CartSellFragment();
            case "CartBuyFragment":
                return new CartBuyFragment();
            case "ManageAdminFragment":
                return new ManageAdminFragment();
            case "ManageBuyerFragment":
                return new ManageBuyerFragment();
            case "ManageSellerFragment":
                return new ManageSellerFragment();
            case "SearchFragment":
                return new SearchFragment();
            case "SettingFragment":
                return new SettingFragment();
            case "HomeFragment":
            default:
                return new HomeFragment();
        }
    }

    // Method to get MenuItem ID by Fragment name
    private int getMenuItemIdByFragmentName(String fragmentName) {
        switch (fragmentName) {
            case "CartSellFragment":
                return R.id.nav_cart;
            case "CartBuyFragment":
                return R.id.nav_cart;
            case "ManageAdminFragment":
            case "ManageBuyerFragment":
            case "ManageSellerFragment":
                return R.id.nav_shop;
            case "SearchFragment":
                return R.id.nav_search;
            case "SettingFragment":
                return R.id.nav_setting;
            case "HomeFragment":
            default:
                return R.id.nav_home;
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    public int getRoleFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("User_Login", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("role", -1);
    }

    public int getIdUserFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("User_Login", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("idUser", -1);
    }

    public void checkRole() {
        int role = getRoleFromSharedPreferences();

        switch (role) {
            case 0: // admin
                MenuItem nav_search1 = binding.bottomNavigation.getMenu().findItem(R.id.nav_search);
                if (nav_search1 != null) {
                    nav_search1.setTitle("Tìm kiếm");
                    nav_search1.setIcon(R.drawable.ic_item_search);
                }
                MenuItem nav_cart = binding.bottomNavigation.getMenu().findItem(R.id.nav_cart);
                if (nav_cart != null) {
                    nav_cart.setTitle("Quản lý");
                    nav_cart.setIcon(R.drawable.ic_item_manage);
                }

                MenuItem nav_shop = binding.bottomNavigation.getMenu().findItem(R.id.nav_shop);
                if (nav_shop != null) {
                    nav_shop.setTitle("Thống kê");
                    nav_shop.setIcon(R.drawable.icon_stacked_bar_chart_24);
                }

                break;
            case 1: // seller

                MenuItem nav_home = binding.bottomNavigation.getMenu().findItem(R.id.nav_home);
                if (nav_home != null) {
                    nav_home.setTitle("Tìm kiếm");
                    nav_home.setIcon(R.drawable.ic_item_search);
                }

                MenuItem nav_search = binding.bottomNavigation.getMenu().findItem(R.id.nav_search);
                if (nav_search != null) {
                    nav_search.setTitle("Shop");
                    nav_search.setIcon(R.drawable.ic_item_shop);
                }

                MenuItem nav_cart1 = binding.bottomNavigation.getMenu().findItem(R.id.nav_cart);
                if (nav_cart1 != null) {
                    nav_cart1.setTitle("Đơn hàng");
                    nav_cart1.setIcon(R.drawable.ic_item_cart);
                }

                MenuItem nav_shop3 = binding.bottomNavigation.getMenu().findItem(R.id.nav_shop);
                if (nav_shop3 != null) {
                    nav_shop3.setTitle("Thống kê");
                    nav_shop3.setIcon(R.drawable.icon_stacked_bar_chart_24);
                }


                break;
            case 2: // buyer
                MenuItem nav_shop4 = binding.bottomNavigation.getMenu().findItem(R.id.nav_shop);
                nav_shop4.setVisible(false);
                break;

            default:
                break;
        }
    }
}