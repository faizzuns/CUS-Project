package user.com.cus.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import user.com.cus.Adapter.PaymentAdapter;
import user.com.cus.DataModel.History.HistoryItem;
import user.com.cus.DataModel.History.HistoryResult;
import user.com.cus.DataModel.Payment.ItemList;
import user.com.cus.R;
import user.com.cus.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailHistoryActivity extends AppCompatActivity {

    @BindView(R.id.root_estimasi_detail) RelativeLayout rootEstimasi;
    @BindView(R.id.txt_place_detail) TextView txtPlace;
    @BindView(R.id.txt_address_detail) TextView txtAddress;
    @BindView(R.id.txt_phone_detail) TextView txtPhoneDetail;
    @BindView(R.id.rv_item) RecyclerView rvItem;
    @BindView(R.id.txt_total_price_detail) TextView txtTotalPrice;
    @BindView(R.id.txt_estimasi_detail) TextView estimationTime;
    @BindView(R.id.btn_call) TextView txtCall;
    @BindView(R.id.btn_navigate) TextView txtNavigate;
    @BindView(R.id.root_rating_detail) LinearLayout rootRatingDetail;
    @BindView(R.id.btn_cancel) Button btnCancel;
    @BindView(R.id.card_cancel) CardView cardCancel;
    @BindView(R.id.rate_1) ImageView rate1;
    @BindView(R.id.rate_2) ImageView rate2;
    @BindView(R.id.rate_3) ImageView rate3;
    @BindView(R.id.rate_4) ImageView rate4;
    @BindView(R.id.rate_5) ImageView rate5;

    List<ItemList> listItem;
    PaymentAdapter adapter;
    HistoryResult historyResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("History");

        String data = getIntent().getStringExtra("historyObject");
        try {
            historyResult = HistoryResult.toObject(data);
        } catch (Exception e) {
            Toast.makeText(this, "Something was error", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d("cekDetailHistory", "onCreate: " + historyResult.toString());

        checkCallPhonePermission();

        setDataDetail();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                setResult();
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkCallPhonePermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                Log.d("cekLocation", "location permission granted");
                setDataDetail();
            } else {
                ActivityCompat.requestPermissions(DetailHistoryActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE}, Utils.PERMISSION_FOR_CALL_PHONE);
            }
        } else {
            Log.d("cekLocation", "location permission granted");
            setDataDetail();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Utils.PERMISSION_FOR_CALL_PHONE){
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                setDataDetail();
            }else{
                Toast.makeText(this, "Permission Denied to access your Phone", Toast.LENGTH_SHORT).show();
                setResult();
                finish();
            }
        }
    }

    public void setDataDetail() {
        txtPlace.setText(historyResult.getToko().get(0).getName());
        txtAddress.setText(historyResult.getToko().get(0).getAddress());
        txtPhoneDetail.setText(historyResult.getToko().get(0).getPhone());
        int total = 0;
        for (HistoryItem historyItem : historyResult.getList()) {
            total += historyItem.getPrice();
        }
        txtTotalPrice.setText(Utils.convertToRupiahFormat(total));
        if (historyResult.getStatus() == 0) {
            rootEstimasi.setVisibility(View.VISIBLE);
            rootRatingDetail.setVisibility(View.VISIBLE);
            setRating(0);
            estimationTime.setText(historyResult.getEstimation());
            cardCancel.setVisibility(View.VISIBLE);
        } else {
            rootRatingDetail.setVisibility(View.VISIBLE);
            rootEstimasi.setVisibility(View.GONE);
            setRating(historyResult.getRating());
            cardCancel.setVisibility(View.GONE);
        }

        final String phone = historyResult.getToko().get(0).getPhone();

        txtCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        });

        txtNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //https://www.google.com/maps/dir/?api=1&origin=Google+Pyrmont+NSW&destination=QVB&travelmode=driving
                String data = historyResult.getToko().get(0).getLatitude() + "," + historyResult.getToko().get(0).getLongitude();
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr="+data));
                startActivity(intent);
            }
        });

        //rincian pembelian
        listItem = new ArrayList<>();
        for (HistoryItem historyItem : historyResult.getList()){
            listItem.add(new ItemList(historyItem.getName(), historyItem.getId(), historyItem.getId(), historyItem.getQuantity(), historyItem.getPrice()));
        }

        rvItem.setHasFixedSize(true);
        final LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext()){
            @Override
            public boolean canScrollVertically(){
                return false;
            }
        };
        rvItem.setLayoutManager(llm);

        adapter = new PaymentAdapter(listItem);
        rvItem.setAdapter(adapter);


    }

    private void setRating(int rating) {
        ImageView[] rate = {
                rate1,
                rate2,
                rate3,
                rate4,
                rate5
        };

        for (int i = 0; i < 5; i++){
            if ( i < rating){
                rate[i].setImageResource(R.drawable.rated);
            }else{
                rate[i].setImageResource(R.drawable.not_rated);
            }
        }
    }

    @Override
    public void onBackPressed() {
        setResult();
        super.onBackPressed();
    }

    public void setResult(){
        Intent returnIntent = new Intent();
        int data = 0;
        if (historyResult.getStatus() != 0){
            data = 1;
        }
        returnIntent.putExtra("result",data);
        setResult(Activity.RESULT_OK,returnIntent);
    }
}
