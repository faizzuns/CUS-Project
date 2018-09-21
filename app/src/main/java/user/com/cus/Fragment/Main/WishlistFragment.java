package user.com.cus.Fragment.Main;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.Select;
import user.com.cus.Activity.ListItemActivity;
import user.com.cus.Adapter.ItemAdapter;
import user.com.cus.DataModel.Item.ItemSaved;
import user.com.cus.DataModel.User.FavouriteModel;
import user.com.cus.DataModel.User.UserSaved;
import user.com.cus.DataModel.User.UserSaved_Table;
import user.com.cus.Listener.ItemListener;
import user.com.cus.R;
import user.com.cus.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class WishlistFragment extends Fragment {

    private final String TAG = "cekWishlistFragment";

    @BindView(R.id.rv_item) RecyclerView rvItem;
//    @BindView(R.id.root_total) RelativeLayout rootTotal;
//    @BindView(R.id.txt_total_price) TextView txtTotalPrice;
//    @BindView(R.id.btn_next) Button btnNext;
    @BindView(R.id.txt_no_wishlist) TextView txtNoWishlist;

    ItemAdapter itemAdapter;

    List<ItemSaved> listItem;

    List<FavouriteModel> listFavourite;
    public static List<ItemSaved> listItemBuyed;

    int countItem;
    int totalPrice;

    public WishlistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_wishlist, container, false);
        ButterKnife.bind(this,v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        countItem = 0;
        totalPrice = 0;
        WishlistFragment.listItemBuyed = new ArrayList<>();

        listItem = new ArrayList<>();
        rvItem.setHasFixedSize(true);
        final LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvItem.setLayoutManager(llm);

        itemAdapter = new ItemAdapter(listItem, new ItemListener() {
            @Override
            public void onFavouriteClicked(ItemSaved item) {
                if (item.isWishlist()){
                    countItem -= item.getCount();
                    listItem.remove(item);
                    item.delete();

                    if (WishlistFragment.listItemBuyed.contains(item)){
                        WishlistFragment.listItemBuyed.remove(item);
                    }

                    listFavourite = Utils.sharedUser.getListFavourite();
                    int idxDelete = Utils.checkIdItemSaved(item.getId());
                    if (idxDelete != -1){
                        listFavourite.remove(idxDelete);
                    }
                    Utils.sharedUser.setListFavourite(listFavourite);
                    UserSaved userSaved = new Select()
                            .from(UserSaved.class)
                            .where(UserSaved_Table.idLogin.is(1))
                            .querySingle();
                    if (userSaved != null) {
                        userSaved.setListFavourite(Utils.listFavouriteToString(listFavourite));
                        userSaved.save();
                    }

                    checkDataCountItem();

                    itemAdapter.refreshData(listItem);
                }
            }

            @Override
            public void onDecreaseClicked(ItemSaved item) {
                int cnt = item.getCount();
                item.setCount(cnt-1);

                if (item.isWishlist()){
                    item.save();
                    listFavourite = Utils.sharedUser.getListFavourite();
                    int idxFavourite = Utils.checkIdItemSaved(item.getId());
                    if (idxFavourite == -1){
                        Toast.makeText(getContext(), "something was error with your local database.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    listFavourite.get(idxFavourite).setCount(cnt-1);
                    Utils.sharedUser.setListFavourite(listFavourite);
                    UserSaved userSaved = new Select()
                            .from(UserSaved.class)
                            .where(UserSaved_Table.idLogin.is(1))
                            .querySingle();
                    if (userSaved != null) {
                        userSaved.setListFavourite(Utils.listFavouriteToString(listFavourite));
                        userSaved.save();
                        Log.d("cekList", "onFavouriteClicked: ");
                    }
                }

                if (cnt-1 == 0){
                    WishlistFragment.listItemBuyed.remove(item);
                }

                countItem --;
                checkDataCountItem();
                itemAdapter.refreshData(listItem);
            }

            @Override
            public void onIncreaseClicked(ItemSaved item) {
                int cnt = item.getCount();
                item.setCount(cnt+1);

                if (item.isWishlist()){
                    item.save();
                    listFavourite = Utils.sharedUser.getListFavourite();
                    int idxFavourite = Utils.checkIdItemSaved(item.getId());
                    if (idxFavourite == -1){
                        Toast.makeText(getContext(), "something was error with your local database.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    listFavourite.get(idxFavourite).setCount(cnt+1);
                    Utils.sharedUser.setListFavourite(listFavourite);
                    UserSaved userSaved = new Select()
                            .from(UserSaved.class)
                            .where(UserSaved_Table.idLogin.is(1))
                            .querySingle();
                    if (userSaved != null) {
                        userSaved.setListFavourite(Utils.listFavouriteToString(listFavourite));
                        userSaved.save();
                    }
                }

                if (cnt+1 == 1){
                    WishlistFragment.listItemBuyed.add(item);
                }
                countItem++;
                checkDataCountItem();
                itemAdapter.refreshData(listItem);
            }

            @Override
            public void onItemClick(ItemSaved item) {
                Intent intent = new Intent(getContext(), ListItemActivity.class);
                Log.d(TAG, "onItemClick: "+item.getIdToko());
                intent.putExtra("idPlace", item.getIdToko());
                intent.putExtra("imgUrl",item.getImgUrlToko());
                intent.putExtra("placeName",item.getNameToko());
                intent.putExtra("placeAddress",item.getAddressToko());
                intent.putExtra("placeContact",item.getPhoneToko());
                startActivity(intent);
            }
        }, getContext());

        rvItem.setAdapter(itemAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        callItemData();
    }

    private void callItemData() {
        listItem = new ArrayList<>();

        listItem = new Select()
                .from(ItemSaved.class)
                .queryList();

        for (int i =0; i<listItem.size(); i++){
            if (listItem.get(i).getCount() != 0){
                WishlistFragment.listItemBuyed.add(listItem.get(i));
                countItem += listItem.get(i).getCount();
            }
        }

        checkDataCountItem();
        itemAdapter.refreshData(listItem);
    }

    private void checkDataCountItem() {
        if (listItem.size() == 0){
            txtNoWishlist.setVisibility(View.VISIBLE);
        }else{
            txtNoWishlist.setVisibility(View.GONE);
        }
//        if (countItem > 0){
//            rootTotal.setVisibility(View.VISIBLE);
//            int total = 0;
//            for (int i = 0; i<WishlistFragment.listItemBuyed.size(); i++){
//                total += WishlistFragment.listItemBuyed.get(i).getPrice() * WishlistFragment.listItemBuyed.get(i).getCount();
//            }
//            totalPrice = total;
//            txtTotalPrice.setText("Total : "+ Utils.convertToRupiahFormat(total));
//        }else{
//            rootTotal.setVisibility(View.GONE);
//        }
    }
}
