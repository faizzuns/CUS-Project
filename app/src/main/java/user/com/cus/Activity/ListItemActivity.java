package user.com.cus.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.squareup.picasso.Picasso;
import user.com.cus.Adapter.ItemAdapter;
import user.com.cus.DataModel.Item.ItemData;
import user.com.cus.DataModel.Item.ItemModel;
import user.com.cus.DataModel.Item.ItemResult;
import user.com.cus.DataModel.Item.ItemSaved;
import user.com.cus.DataModel.User.FavouriteModel;
import user.com.cus.DataModel.User.UserSaved;
import user.com.cus.DataModel.User.UserSaved_Table;
import user.com.cus.Listener.ItemListener;
import user.com.cus.R;
import user.com.cus.Services.RetrofitServices;
import user.com.cus.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListItemActivity extends AppCompatActivity {

    private static final String TAG = "cekListItemActivity";
    @BindView(R.id.rv_item) RecyclerView rvItem;
    @BindView(R.id.root_total) RelativeLayout rootTotal;
    @BindView(R.id.txt_total_price) TextView txtTotalPrice;
    @BindView(R.id.btn_next) Button btnNext;

    @BindView(R.id.img_place) ImageView imgPlace;
    @BindView(R.id.txt_place_name) TextView txtPlaceName;
    @BindView(R.id.txt_address) TextView txtAddress;
    @BindView(R.id.txt_contact) TextView txtContact;

    int idPlace;
    ItemAdapter itemAdapter;

    Call<ItemModel> callItem;
    List<ItemSaved> listItem;

    List<FavouriteModel> listFavourite;
    public static List<ItemSaved> listItemBuyed;

    int countItem;
    int totalPrice;

    String imgUrl;
    String place;
    String address;
    String contact;

    private boolean canAccessPayment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("placeName"));
        countItem = 0;
        totalPrice = 0;
        ListItemActivity.listItemBuyed = new ArrayList<>();

        rootTotal.setVisibility(View.GONE);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canAccessPayment){
                    Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                    intent.putExtra("total",Utils.convertToRupiahFormat(totalPrice));
                    intent.putExtra("isFromWishlist",false);
                    startActivity(intent);
                }else{
                    Toast.makeText(ListItemActivity.this, "Store is closed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setDataToko();

        idPlace = getIntent().getIntExtra("idPlace",0);
        Log.d(TAG, "onCreate: "+idPlace);
        listItem = new ArrayList<>();
        rvItem.setHasFixedSize(true);
        final LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext()){
            @Override
            public boolean canScrollVertically(){
                return false;
            }
        };
        rvItem.setLayoutManager(llm);

        itemAdapter = new ItemAdapter(listItem, new ItemListener() {
            @Override
            public void onFavouriteClicked(ItemSaved item) {
                if (listItem.contains(item)){
                    int idx = listItem.indexOf(item);

                    if (!item.isWishlist()){
                        listItem.get(idx).setWishlist(true);
                        item.save();
                        listFavourite = Utils.sharedUser.getListFavourite();
                        listFavourite.add(new FavouriteModel(item.getId(), item.getCount()));
                        if (listFavourite == null){
                            Log.d("cekListItem", "onFavouriteClicked: list null");
                        }
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
                    }else{
                        listItem.get(idx).setWishlist(false);
                        item.delete();
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
                            Log.d("cekList", "onFavouriteClicked: ");
                        }
                    }

                    itemAdapter.refreshData(listItem);
                }
            }

            @Override
            public void onDecreaseClicked(ItemSaved item) {
                if (listItem.contains(item)){
                    int idx = listItem.indexOf(item);
                    int cnt = listItem.get(idx).getCount();
                    listItem.get(idx).setCount(cnt-1);

                    if (listItem.get(idx).isWishlist()){
                        item.save();
                        listFavourite = Utils.sharedUser.getListFavourite();
                        int idxFavourite = Utils.checkIdItemSaved(item.getId());
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
                        ListItemActivity.listItemBuyed.remove(item);
                    }

                    countItem --;
                    checkDataCountItem();
                    itemAdapter.refreshData(listItem);
                }
            }

            @Override
            public void onIncreaseClicked(ItemSaved item) {
                if (listItem.contains(item)){
                    int idx = listItem.indexOf(item);
                    int cnt = listItem.get(idx).getCount();
                    listItem.get(idx).setCount(cnt+1);

                    if (listItem.get(idx).isWishlist()){
                        item.save();
                        listFavourite = Utils.sharedUser.getListFavourite();
                        int idxFavourite = Utils.checkIdItemSaved(item.getId());
                        listFavourite.get(idxFavourite).setCount(cnt+1);
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

                    if (cnt+1 == 1){
                        ListItemActivity.listItemBuyed.add(item);
                    }

                    countItem ++;
                    checkDataCountItem();
                    itemAdapter.refreshData(listItem);
                }
            }

            @Override
            public void onItemClick(ItemSaved item) {

            }
        },getApplicationContext());

        rvItem.setAdapter(itemAdapter);
        callItemData(idPlace);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setDataToko() {
        imgUrl = getIntent().getStringExtra("imgUrl");
        place = getIntent().getStringExtra("placeName");
        address = getIntent().getStringExtra("placeAddress");
        contact =getIntent().getStringExtra("placeContact");

        Picasso.with(getApplicationContext()).load(imgUrl).into(imgPlace);
        txtPlaceName.setText(place);
        txtAddress.setText(address);
        txtContact.setText(contact);
    }

    private void callItemData(final int idPlace) {
        listItem = new ArrayList<>();

        //call api
        callItem = RetrofitServices.sendListItemRequest().callListItem(Utils.sharedJsonHeader, Utils.sharedHeader, idPlace);
        callItem.enqueue(new Callback<ItemModel>() {
            @Override
            public void onResponse(Call<ItemModel> call, Response<ItemModel> response) {
                if (!callItem.isCanceled()){
                    if (response.body() != null){
                        if (response.body().getError() != null){
                            Toast.makeText(ListItemActivity.this, response.body().getError().getMsg(), Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onResponse: "+response.body().getError().getMsg());
                        }else{
                            Log.d(TAG, "onResponse: reso");
                            for (ItemResult itemResult : response.body().getResult().getListResult()){
                                Log.d(TAG, "onResponse: "+itemResult.getIdToko());
                                boolean check = Utils.checkIdItemSaved(itemResult.getId()) != -1;
                                int idx = 0;
                                if (check){
                                    idx = Utils.sharedUser.getListFavourite().get(Utils.checkIdItemSaved(itemResult.getId())).getCount();
                                    countItem += idx;
                                }
                                listItem.add(new ItemSaved(itemResult.getId(),idPlace,itemResult.getName(),itemResult.getDescription(),itemResult.getPrice(),itemResult.getImgUrl(),check,idx,imgUrl,place, address, contact));
                                if (idx != 0){
                                    ListItemActivity.listItemBuyed.add(listItem.get(0));
                                }
                            }

                            canAccessPayment = !response.body().getResult().isClose();

                            checkDataCountItem();

                            itemAdapter.refreshData(listItem);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ItemModel> call, Throwable t) {
                Toast.makeText(ListItemActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: "+t.toString());
            }
        });
    }

    private void checkDataCountItem() {
        if (countItem > 0){
            rootTotal.setVisibility(View.VISIBLE);
            int total = 0;
            for (int i = 0; i<ListItemActivity.listItemBuyed.size(); i++){
                total += ListItemActivity.listItemBuyed.get(i).getPrice() * ListItemActivity.listItemBuyed.get(i).getCount();
            }
            totalPrice = total;
            txtTotalPrice.setText(Utils.convertToRupiahFormat(total));
        }else{
            rootTotal.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        if (callItem != null){
            callItem.cancel();
        }
        super.onDestroy();
    }
}
