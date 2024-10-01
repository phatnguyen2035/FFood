package fragment;

import static com.google.android.material.internal.ViewUtils.hideKeyboard;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.du_an_1.R;
import com.example.du_an_1.databinding.FragmentSearchBinding;

import java.util.List;

import adapter.SearchAdapter;
import adapter.ShopItemAdapter;
import dao.ProductDAO;
import dao.ShopDAO;
import model.Product;
import model.Shop;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;
    private ProductDAO productDAO;
    private SearchAdapter searchAdapter;
    ShopItemAdapter shopItemAdapter;
    private ShopDAO shopDAO;
    private RecyclerView recyclerView;
    private boolean isProductSearch;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        loadProductList();


        binding.showMenu1Textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), v, Gravity.RIGHT);
                popupMenu.getMenuInflater().inflate(R.menu.menu_search, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.item1) {
                            binding.showMenu1Textview.setText("Sản Phẩm");
                            loadProductList();
                        } else if (item.getItemId() == R.id.item3) {
                            binding.showMenu1Textview.setText("Đồ ăn");
                            loadProductEat();

                        } else if (item.getItemId() == R.id.item4) {
                            binding.showMenu1Textview.setText("Nước uống");
                            loadProductDrink();

                        } else if (item.getItemId() == R.id.item2) {
                            binding.showMenu1Textview.setText("Shop");
                            loadShopList();
                        }else if (item.getItemId() == R.id.item5) {
                            binding.showMenu1Textview.setText("Mới Nhất");
                            loadProductListNew();
                        } else if (item.getItemId() == R.id.item6) {
                            binding.showMenu1Textview.setText("Giá Thấp Nhất");
                            loadProductListMin();
                        } else if (item.getItemId() == R.id.item7) {
                            binding.showMenu1Textview.setText("Giá Cao Nhất");
                            loadProductListMax();
                        } else if (item.getItemId() == R.id.item8) {
                            binding.showMenu1Textview.setText("Lượt Bán");
                            loadProductListSold();
                        } else if (item.getItemId() == R.id.item9) {
                            binding.showMenu1Textview.setText("A-Z");
                            loadProductList();
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        binding.showMenu2Textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), v, Gravity.RIGHT);
                popupMenu.getMenuInflater().inflate(R.menu.menu_search_2, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.item1) {
                            loadProduct1();
                            binding.showMenu2Textview.setText("Cơm");
                        } else if (item.getItemId() == R.id.item2) {
                            loadProduct2();
                            binding.showMenu2Textview.setText("Mỳ");
                        } else if (item.getItemId() == R.id.item3) {
                            loadProduct3();
                            binding.showMenu2Textview.setText("Bánh Mỳ");
                        } else if (item.getItemId() == R.id.item4) {
                            loadProduct4();
                            binding.showMenu2Textview.setText("Đồ Ăn Vặt");
                        } else if (item.getItemId() == R.id.item5) {
                            loadProduct5();
                            binding.showMenu2Textview.setText("Đồ Ăn Khác");
                        } else if (item.getItemId() == R.id.item6) {
                            loadProduct6();
                            binding.showMenu2Textview.setText("Trà Sữa");
                        } else if (item.getItemId() == R.id.item7) {
                            loadProduct7();
                            binding.showMenu2Textview.setText("Cà Phê");
                        } else if (item.getItemId() == R.id.item8) {
                            loadProduct8();
                            binding.showMenu2Textview.setText("Nước Ngọt");
                        } else if (item.getItemId() == R.id.item9) {
                            loadProduct9();
                            binding.showMenu2Textview.setText("Sữa");
                        } else if (item.getItemId() == R.id.item10) {
                            loadProduct10();
                            binding.showMenu2Textview.setText("Nước Khác");
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });


        binding.svSearchProduct.setIconified(false);
        binding.svSearchProduct.requestFocus();
        binding.svSearchProduct.clearFocus();
        binding.svSearchProduct.setQueryHint("Tìm kiếm sản phẩm");


        setupSearchView();


        return view;
    }


    private void filterProductList(String query) {
        List<Product> filteredList = productDAO.getProductsByName(query);
        searchAdapter.updateProductList(filteredList);
    }

    private void filterShopList(String query) {
        List<Shop> filteredList = shopDAO.getShopByName(query);
        shopItemAdapter.updateShopList(filteredList);
    }

    private void setupSearchView() {
        binding.svSearchProduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (isProductSearch) {
                    filterProductList(query);
                } else {
                    filterShopList(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (isProductSearch) {
                    filterProductList(newText);
                } else {
                    filterShopList(newText);
                }
                return false;
            }
        });
    }

    public void loadShopList() {
        recyclerView = binding.rcvProductSearch;
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        shopDAO = new ShopDAO(getContext());
        List<Shop> shopList = shopDAO.getlitsShopIsActive();
        shopItemAdapter = new ShopItemAdapter(getContext(), shopList, shopDAO);
        recyclerView.setAdapter(shopItemAdapter);
        isProductSearch = false;
    }

    public void loadProductList() {
        recyclerView = binding.rcvProductSearch;
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        productDAO = new ProductDAO(getContext());
        List<Product> productList = productDAO.getProductsListAll();
        searchAdapter = new SearchAdapter(getContext(), productList, productDAO);
        recyclerView.setAdapter(searchAdapter);
        isProductSearch = true;


    }

    public void loadProductListNew() {
        recyclerView = binding.rcvProductSearch;
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        productDAO = new ProductDAO(getContext());
        List<Product> productList = productDAO.getProductsListNew();
        searchAdapter = new SearchAdapter(getContext(), productList, productDAO);
        recyclerView.setAdapter(searchAdapter);

    }

    public void loadProductListMax() {
        recyclerView = binding.rcvProductSearch;
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        productDAO = new ProductDAO(getContext());
        List<Product> productList = productDAO.getProductsListPriceMax();
        searchAdapter = new SearchAdapter(getContext(), productList, productDAO);
        recyclerView.setAdapter(searchAdapter);
        isProductSearch = true;

    }

    public void loadProductListMin() {
        recyclerView = binding.rcvProductSearch;
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        productDAO = new ProductDAO(getContext());
        List<Product> productList = productDAO.getProductsListPriceMin();
        searchAdapter = new SearchAdapter(getContext(), productList, productDAO);
        recyclerView.setAdapter(searchAdapter);
        isProductSearch = true;

    }

    public void loadProductListSold() {
        recyclerView = binding.rcvProductSearch;
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        productDAO = new ProductDAO(getContext());
        List<Product> productList = productDAO.getProductsListSold();
        searchAdapter = new SearchAdapter(getContext(), productList, productDAO);
        recyclerView.setAdapter(searchAdapter);
        isProductSearch = true;

    }

    public void loadProduct1() {
        recyclerView = binding.rcvProductSearch;
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        productDAO = new ProductDAO(getContext());
        List<Product> productList = productDAO.getProductsListByIdCategories(1);
        searchAdapter = new SearchAdapter(getContext(), productList, productDAO);
        recyclerView.setAdapter(searchAdapter);
        isProductSearch = true;

    }

    public void loadProduct2() {
        recyclerView = binding.rcvProductSearch;
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        productDAO = new ProductDAO(getContext());
        List<Product> productList = productDAO.getProductsListByIdCategories(2);
        searchAdapter = new SearchAdapter(getContext(), productList, productDAO);
        recyclerView.setAdapter(searchAdapter);
        isProductSearch = true;

    }

    public void loadProduct3() {
        recyclerView = binding.rcvProductSearch;
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        productDAO = new ProductDAO(getContext());
        List<Product> productList = productDAO.getProductsListByIdCategories(3);
        searchAdapter = new SearchAdapter(getContext(), productList, productDAO);
        recyclerView.setAdapter(searchAdapter);
        isProductSearch = true;

    }

    public void loadProduct4() {
        recyclerView = binding.rcvProductSearch;
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        productDAO = new ProductDAO(getContext());
        List<Product> productList = productDAO.getProductsListByIdCategories(4);
        searchAdapter = new SearchAdapter(getContext(), productList, productDAO);
        recyclerView.setAdapter(searchAdapter);
        isProductSearch = true;

    }

    public void loadProduct5() {
        recyclerView = binding.rcvProductSearch;
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        productDAO = new ProductDAO(getContext());
        List<Product> productList = productDAO.getProductsListByIdCategories(5);
        searchAdapter = new SearchAdapter(getContext(), productList, productDAO);
        recyclerView.setAdapter(searchAdapter);
        isProductSearch = true;

    }

    public void loadProduct6() {
        recyclerView = binding.rcvProductSearch;
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        productDAO = new ProductDAO(getContext());
        List<Product> productList = productDAO.getProductsListByIdCategories(6);
        searchAdapter = new SearchAdapter(getContext(), productList, productDAO);
        recyclerView.setAdapter(searchAdapter);
        isProductSearch = true;

    }

    public void loadProduct7() {
        recyclerView = binding.rcvProductSearch;
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        productDAO = new ProductDAO(getContext());
        List<Product> productList = productDAO.getProductsListByIdCategories(7);
        searchAdapter = new SearchAdapter(getContext(), productList, productDAO);
        recyclerView.setAdapter(searchAdapter);
        isProductSearch = true;

    }

    public void loadProduct8() {
        recyclerView = binding.rcvProductSearch;
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        productDAO = new ProductDAO(getContext());
        List<Product> productList = productDAO.getProductsListByIdCategories(8);
        searchAdapter = new SearchAdapter(getContext(), productList, productDAO);
        recyclerView.setAdapter(searchAdapter);
        isProductSearch = true;

    }

    public void loadProduct9() {
        recyclerView = binding.rcvProductSearch;
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        productDAO = new ProductDAO(getContext());
        List<Product> productList = productDAO.getProductsListByIdCategories(9);
        searchAdapter = new SearchAdapter(getContext(), productList, productDAO);
        recyclerView.setAdapter(searchAdapter);

        isProductSearch = true;
    }

    public void loadProduct10() {
        recyclerView = binding.rcvProductSearch;
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        productDAO = new ProductDAO(getContext());
        List<Product> productList = productDAO.getProductsListByIdCategories(10);
        searchAdapter = new SearchAdapter(getContext(), productList, productDAO);
        recyclerView.setAdapter(searchAdapter);

        isProductSearch = true;
    }
    public void loadProductEat() {
        recyclerView = binding.rcvProductSearch;
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        productDAO = new ProductDAO(getContext());
        List<Product> productList = productDAO.getProductsListEat();
        searchAdapter = new SearchAdapter(getContext(), productList, productDAO);
        recyclerView.setAdapter(searchAdapter);

        isProductSearch = true;
    }
    public void loadProductDrink() {
        recyclerView = binding.rcvProductSearch;
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        productDAO = new ProductDAO(getContext());
        List<Product> productList = productDAO.getProductsListDrinks();
        searchAdapter = new SearchAdapter(getContext(), productList, productDAO);
        recyclerView.setAdapter(searchAdapter);

        isProductSearch = true;
    }


    public int getRoleFromSharedPreferences() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("User_Login", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("role", -1);
    }

//    public void hideKeyboard() {
//        if (getView() != null) {
//            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
//            if (imm != null) {
//                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
//            }
//        }
//    }

//    public void reloadFragment() {
//        FragmentManager fragmentManager = getParentFragmentManager(); // Hoặc getChildFragmentManager() nếu nó là một fragment con
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        // Tạo một thể hiện mới của SearchFragment
//        SearchFragment newFragment = new SearchFragment();
//
//        // Thay thế fragment hiện tại bằng fragment mới
//        fragmentTransaction.replace(R.id.fragment_container, newFragment);
//        fragmentTransaction.addToBackStack(null); // Optional: Thêm vào back stack nếu bạn muốn có thể quay lại fragment cũ
//        fragmentTransaction.commit();
//    }

}