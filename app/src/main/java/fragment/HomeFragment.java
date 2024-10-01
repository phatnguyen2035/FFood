package fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.du_an_1.MainActivity;
import com.example.du_an_1.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.List;

import adapter.CategoriesHomeAdapter;
import adapter.HomeProductAdapter;
import adapter.PagerHomeAdapter;
import adapter.ShopItemAdapter;
import bottomRecycleview.RightSpaceItemDecoration;
import dao.CategoriesDao;
import dao.ProductDAO;
import dao.ShopDAO;
import dao.UserDAO;
import model.Categories;
import model.Product;
import model.Shop;
import model.User;

public class HomeFragment extends Fragment {
    private HomeProductAdapter homeProductAdapter;
    private CategoriesHomeAdapter categoriesHomeAdapter;
    private ProductDAO productDAO;
    private CategoriesDao categoriesDao;
    private ShopDAO shopDAO;
    private RecyclerView recyclerView, recyclerView1, recyclerView2, recyclerView3;
    private Handler handler;
    private Runnable runnable;
    private final int PAGE_DELAY = 3000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initializeViews(view);
        setupViewPager(view);
        setupRecyclerViews(view);
        loadProductData();
        startAutoSlide();

        return view;
    }

    private void initializeViews(View view) {
        ImageView img_search = view.findViewById(R.id.img_search_home);

        img_search.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.putExtra("SearchFragment", "SearchFragment");
            startActivity(intent);
        });

        UserDAO userDAO = new UserDAO(getContext());
        User user = userDAO.getUserByID(getIdUserFromSharedPreferences());
    }

    private void setupViewPager(View view) {
        ViewPager2 viewPager = view.findViewById(R.id.viewPager);
        TabLayout tabLayout = view.findViewById(R.id.tabDots);

        List<Integer> imageList = Arrays.asList(R.mipmap.banner_1, R.mipmap.banner_2, R.mipmap.slide3);
        PagerHomeAdapter adapter = new PagerHomeAdapter(imageList);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Cài đặt tiêu đề hoặc biểu tượng cho các tab nếu cần
        }).attach();
    }

    private void setupRecyclerViews(View view) {
        recyclerView = view.findViewById(R.id.rcv_eat_home);
        recyclerView1 = view.findViewById(R.id.rcv_drink_home);
        recyclerView2 = view.findViewById(R.id.rcv_categories_home);
        recyclerView3 = view.findViewById(R.id.rcv_shop_home);

        StaggeredGridLayoutManager layoutManagerEat = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        StaggeredGridLayoutManager layoutManagerDrink = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        StaggeredGridLayoutManager layoutManagerCategories = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        StaggeredGridLayoutManager layoutManagerShop = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManagerEat);
        recyclerView1.setLayoutManager(layoutManagerDrink);
        recyclerView2.setLayoutManager(layoutManagerCategories);
        recyclerView3.setLayoutManager(layoutManagerShop);

        int rightSpaceWidth = getResources().getDimensionPixelSize(R.dimen.right_space_width);
        recyclerView.addItemDecoration(new RightSpaceItemDecoration(rightSpaceWidth));
        recyclerView1.addItemDecoration(new RightSpaceItemDecoration(rightSpaceWidth));
        recyclerView2.addItemDecoration(new RightSpaceItemDecoration(rightSpaceWidth));
    }

    private void loadProductData() {
        productDAO = new ProductDAO(getContext());
        shopDAO = new ShopDAO(getContext());
        categoriesDao = new CategoriesDao(getContext());

        loadProductList();
        loadProductList1();
        loadProductList2();
        loadProductList3();
    }

    private void loadProductList() {
        List<Product> productList = productDAO.getProductsListEat();
        homeProductAdapter = new HomeProductAdapter(getContext(), productList, productDAO, shopDAO);
        recyclerView.setAdapter(homeProductAdapter);
    }

    private void loadProductList1() {
        List<Product> productList = productDAO.getProductsListDrinks();
        homeProductAdapter = new HomeProductAdapter(getContext(), productList, productDAO, shopDAO);
        recyclerView1.setAdapter(homeProductAdapter);
    }

    private void loadProductList2() {
        List<Categories> categoriesList = categoriesDao.getAllCategories();
        categoriesHomeAdapter = new CategoriesHomeAdapter(getContext(), categoriesList, categoriesDao);
        recyclerView2.setAdapter(categoriesHomeAdapter);
    }

    private void loadProductList3() {
        List<Shop> shopList = shopDAO.getTop5ShopsSoldProducts();
        ShopItemAdapter shopItemAdapter = new ShopItemAdapter(getContext(), shopList, shopDAO);
        recyclerView3.setAdapter(shopItemAdapter);
    }

    private void startAutoSlide() {
        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                ViewPager2 viewPager = getView().findViewById(R.id.viewPager);
                int currentItem = viewPager.getCurrentItem();
                int nextItem = (currentItem + 1) % 3; // assuming you have 3 images
                viewPager.setCurrentItem(nextItem, true);
                handler.postDelayed(this, PAGE_DELAY);
            }
        };

        handler.postDelayed(runnable, PAGE_DELAY);
    }

    private int getIdUserFromSharedPreferences() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("User_Login", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("idUser", -1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }
}