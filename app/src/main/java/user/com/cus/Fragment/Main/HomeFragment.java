package user.com.cus.Fragment.Main;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import user.com.cus.Activity.ListPlaceActivity;
import user.com.cus.Activity.PointMapsActivity;
import user.com.cus.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private final String TAG = "cekHomeFragment";

    public HomeFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.root_food_and_beverages)
    ImageView rootFoodAndBeverages;
    @BindView(R.id.root_mart) ImageView rootMart;
    @BindView(R.id.root_printing) ImageView rootPrint;
    @BindView(R.id.edt_search) EditText edtSearch;
    @BindView(R.id.pager_iklan) ViewPager pagerIklan;
    @BindView(R.id.tabs_dot) TabLayout tabsDot;

    ViewPagerAdapter adapter;

    List<Integer> listIklan;

    private Handler handler;
    private Runnable runnable;
    private int idxIklan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        callIklanData();
        setClickedView();
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpViewPager();
    }

    private void setUpViewPager() {
        adapter = new ViewPagerAdapter(getChildFragmentManager());

        for (int iklan : listIklan){
            adapter.addFragment(new IklanFragment(iklan), "");
        }

        pagerIklan.setAdapter(adapter);
        pagerIklan.setCurrentItem(0);
        idxIklan = 0;
        tabsDot.setupWithViewPager(pagerIklan);
        pagerIklan.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "onPageScrolled: "+position);
                idxIklan = position;
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable,5000);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected: "+position);
                idxIklan = position;
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable,5000);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                idxIklan = (idxIklan + 1) % listIklan.size();
                pagerIklan.setCurrentItem(idxIklan, true);
                handler.postDelayed(runnable,5000);
            }
        };
        handler.postDelayed(runnable,5000);
    }

    private void callIklanData() {
        listIklan = new ArrayList<>();
        listIklan.add(R.drawable.iklan_1);
        listIklan.add(R.drawable.iklan_2);
        listIklan.add(R.drawable.iklan_3);
        listIklan.add(R.drawable.iklan_4);
    }


    private void setClickedView() {
        rootPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToListPlace(1,"Stationery");
            }
        });
        rootFoodAndBeverages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToListPlace(0, "Food & Beverages");
            }
        });
        rootMart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToListPlace(2, "Mart");
            }
        });
//        rootNearMe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                goToPointPlace();
//            }
//        });
        edtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPointPlace();
            }
        });
    }

    private void goToPointPlace() {
        Intent intent = new Intent(getContext(), PointMapsActivity.class);
        int[] data = new int[]{1,2};
        intent.putExtra("category",data);
        startActivity(intent);
    }

    private void goToListPlace(int i,String name) {
        Intent intent = new Intent(getContext(), ListPlaceActivity.class);
        intent.putExtra("category",i);
        intent.putExtra("categoryName",name);
        startActivity(intent);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragmentList = new ArrayList<>();
        private List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        handler.removeCallbacks(runnable);
        if (tabsDot != null){
            tabsDot.removeAllTabs();
        }
    }

}
