package user.com.cus.Activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import user.com.cus.Adapter.PaymentAdapter;
import user.com.cus.DataModel.Item.ItemSaved;
import user.com.cus.DataModel.Payment.ItemList;
import user.com.cus.DataModel.Payment.PaymentData;
import user.com.cus.DataModel.Payment.PaymentModel;
import user.com.cus.Fragment.Added.FillPhoneFragment;
import user.com.cus.Fragment.Main.WishlistFragment;
import user.com.cus.MainActivity;
import user.com.cus.R;
import user.com.cus.Services.RetrofitServices;
import user.com.cus.Utils.Utils;

public class PaymentActivity extends AppCompatActivity {

    @BindView(R.id.rv_data_payment) RecyclerView rvDataPayment;
    @BindView(R.id.txt_total_price) TextView txtTotalPrice;
    @BindView(R.id.btn_confirm) Button btnConfirm;
    @BindView(R.id.root_time) LinearLayout rootTime;
    @BindView(R.id.txt_hour) TextView txtHour;
    @BindView(R.id.txt_minute) TextView txtMinute;
    @BindView(R.id.progress_confirmation) ProgressBar progressConfirmation;
    @BindView(R.id.edt_notes) EditText edtNotes;

    List<ItemList> listItem;
    PaymentAdapter adapter;
    Call<PaymentModel> callPayment;

    int hourPicked = -1;
    int minutePicked = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        txtTotalPrice.setText(getIntent().getStringExtra("total"));
        boolean isFromWishlist = getIntent().getBooleanExtra("isFromWishlist",false);

        callDataItemBuyed(isFromWishlist);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (hourPicked < 0){
                    Toast.makeText(PaymentActivity.this, "Estimated time not fill yet", Toast.LENGTH_SHORT).show();
                }else if (Utils.sharedUser.getPhone() == null){
                    goToFillPhone();
                }else{
                    if (Utils.sharedUser.getPhone().length() == 0){
                        goToFillPhone();
                    }else{
                        //goToFillPhone();
                        goToPayment();
                    }
                }
            }
        });

        rootTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(PaymentActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (selectedHour <= hour){
                            if (selectedHour < hour){
                                //keluar
                                Toast.makeText(PaymentActivity.this, "Invalid Time!", Toast.LENGTH_SHORT).show();
                                return;
                            }else{
                                if (selectedMinute <= minute){
                                    //keluar
                                    Toast.makeText(PaymentActivity.this, "Invalid Time!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        }
                        if (selectedHour >= 10){
                            txtHour.setText(String.valueOf(selectedHour));
                        }else{
                            txtHour.setText("0"+selectedHour);
                        }
                        if (selectedMinute >= 10){
                            txtMinute.setText(String.valueOf(selectedMinute));
                        }else{
                            txtMinute.setText("0"+selectedMinute);
                        }
                        hourPicked = selectedHour;
                        minutePicked = selectedMinute;
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
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

    private void goToFillPhone() {
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.frame_container,
                        new FillPhoneFragment())
                .addToBackStack(null).commit();
    }

    public void goToPayment(){
        btnConfirm.setVisibility(View.GONE);
        progressConfirmation.setVisibility(View.VISIBLE);
        PaymentData paymentData = new PaymentData(Utils.sharedUser.getId(), listItem, hourPicked, minutePicked, edtNotes.getText().toString());
        callPayment = RetrofitServices.sendPaymentRequest().callPayment(Utils.sharedJsonHeader, Utils.sharedHeader, paymentData);
        callPayment.enqueue(new Callback<PaymentModel>() {
            @Override
            public void onResponse(Call<PaymentModel> call, Response<PaymentModel> response) {
                if (!call.isCanceled()){
                    if (response.body() != null){
                        if (response.body().getError() != null){
                            Toast.makeText(getApplicationContext(), response.body().getError().getMsg(), Toast.LENGTH_SHORT).show();
                            Log.d("cekPaymentActivity", "onResponse: "+response.body().getError().getMsg());
                        }else{
                            SuccessPayment();
                        }
                    }
                    btnConfirm.setVisibility(View.VISIBLE);
                    progressConfirmation.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<PaymentModel> call, Throwable t) {
                Toast.makeText(PaymentActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                Log.d("cekPaymentActivity", "onFailure: "+t.toString());
                btnConfirm.setVisibility(View.VISIBLE);
                progressConfirmation.setVisibility(View.GONE);
            }
        });
    }

    private void SuccessPayment() {
        Toast.makeText(PaymentActivity.this, "success", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void callDataItemBuyed(boolean isFromWishlist) {
        listItem = new ArrayList<>();

        List<ItemSaved> listItemBuyed;
        if (isFromWishlist){
            listItemBuyed = WishlistFragment.listItemBuyed;
        }else{
            listItemBuyed = ListItemActivity.listItemBuyed;
        }

        for (ItemSaved item : listItemBuyed){
            listItem.add(new ItemList(item.getName(), item.getIdToko(), item.getId(), item.getCount(), (item.getPrice() * item.getCount())));
        }

        rvDataPayment.setHasFixedSize(true);
        final LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext()){
            @Override
            public boolean canScrollVertically(){
                return false;
            }
        };
        rvDataPayment.setLayoutManager(llm);

        adapter = new PaymentAdapter(listItem);
        rvDataPayment.setAdapter(adapter);

    }
}
