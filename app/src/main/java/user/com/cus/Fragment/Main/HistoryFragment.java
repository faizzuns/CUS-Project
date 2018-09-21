package user.com.cus.Fragment.Main;


import android.os.Bundle;
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
import android.widget.Toast;

import user.com.cus.Fragment.Main.History.CompletedFragment;
import user.com.cus.Fragment.Main.History.OnGoingFragment;
import user.com.cus.MainActivity;
import user.com.cus.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.pager)
    ViewPager pager;

    ViewPagerAdapter adapter;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this,v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        setupViewPager();
        pager.setCurrentItem(MainActivity.fromDetailHistory);
        MainActivity.fromDetailHistory = 0;
    }

    private void setupViewPager() {
        adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new OnGoingFragment(), "On Going");
        adapter.addFragment(new CompletedFragment(), "Completed");

        pager.setAdapter(adapter);
        pager.setCurrentItem(0);
        tabLayout.setupWithViewPager(pager);
        tabLayout.setTabTextColors(getResources().getColor(android.R.color.white),
                getResources().getColor(R.color.yellowButton));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    public void afterRate() {
        Log.d("cekHistory", "afterRate: ada di History");
        CompletedFragment completedFragment = (CompletedFragment) adapter.getItem(1);
        completedFragment.callHistoryData();
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
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("cekHistoryFragment", "onDestroyView: ");
        if (tabLayout != null){
            tabLayout.removeAllTabs();
        }
    }
}
