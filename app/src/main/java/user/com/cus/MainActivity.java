package user.com.cus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import user.com.cus.Activity.DetailHistoryActivity;
import user.com.cus.DataModel.History.HistoryResult;
import user.com.cus.Fragment.Main.History.CompletedFragment;
import user.com.cus.Fragment.Main.History.RateFragment;
import user.com.cus.Fragment.Main.HistoryFragment;
import user.com.cus.Fragment.Main.HomeFragment;
import user.com.cus.Fragment.Main.SettingsFragment;
import user.com.cus.Fragment.Main.WishlistFragment;
import user.com.cus.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "cekMainActivity";

    @BindView(R.id.pager) ViewPager pager;
    @BindView(R.id.tabs) TabLayout tabs;

    ViewPagerAdapter adapter;

    public static boolean isHistoryOpen = false;
    public static int fromDetailHistory = 0;

    private int[] tabIcon = {
            R.drawable.ic_home,
            R.drawable.ic_history,
            R.drawable.ic_wishlist,
            R.drawable.ic_settings
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        int selection = getIntent().getIntExtra("selection",0);
        setupViewPager(selection);
    }

    private void setupViewPager(int selection){
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new HomeFragment(), "Home");
        adapter.addFragment(new HistoryFragment(), "History");
        adapter.addFragment(new WishlistFragment(), "Wishlist");
        adapter.addFragment(new SettingsFragment(), "Settings");

        pager.setAdapter(adapter);
        pager.setCurrentItem(selection);
        tabs.setupWithViewPager(pager);
        tabs.setTabTextColors(getResources().getColor(android.R.color.white),
                getResources().getColor(R.color.yellowButton));

        tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        tabs.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i<tabIcon.length; i++){
            tabs.getTabAt(i).setIcon(tabIcon[i]);
            if (i == selection){
                tabs.getTabAt(i).getIcon().setColorFilter(getResources().getColor(R.color.yellowButton), PorterDuff.Mode.SRC_IN);
            }else{
                tabs.getTabAt(i).getIcon().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
            }
        }
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getResources().getColor(R.color.yellowButton), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void afterRateFragment() {
        Log.d(TAG, "afterRateFragment: di MainActivity");
        HistoryFragment historyFragment = (HistoryFragment) adapter.getItem(1);
        historyFragment.afterRate();
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

    public void openRatingFragment(HistoryResult historyResult){
        RateFragment rateFragment = new RateFragment(historyResult);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_added,
                        rateFragment)
                .addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        if (isHistoryOpen){
            Toast.makeText(getApplicationContext(), "You must rate your order first!", Toast.LENGTH_SHORT).show();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Utils.DETAIL_HISTORY){
            Log.d(TAG, "onActivityResult: masuk ke detil history");
            if (resultCode == Activity.RESULT_OK){
                //Toast.makeText(this, "setuju", Toast.LENGTH_SHORT).show();
                int hasil = data.getIntExtra("result",0);
                fromDetailHistory = hasil;
            }else{
            }
        }
    }

    public void goToDetailHistory(HistoryResult tempHistory){
        Intent intent = new Intent(getApplicationContext(), DetailHistoryActivity.class);
        intent.putExtra("historyObject",tempHistory.toString());
        startActivityForResult(intent, Utils.DETAIL_HISTORY);
    }
}
