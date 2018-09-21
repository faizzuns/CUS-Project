package user.com.cus.Fragment.Added;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.Select;
import user.com.cus.Activity.PaymentActivity;
import user.com.cus.DataModel.Login.LoginModel;
import user.com.cus.DataModel.User.EditUserData;
import user.com.cus.DataModel.User.UserModel;
import user.com.cus.DataModel.User.UserSaved;
import user.com.cus.DataModel.User.UserSaved_Table;
import user.com.cus.R;
import user.com.cus.Services.RetrofitServices;
import user.com.cus.Utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FillPhoneFragment extends Fragment {


    private static final String TAG = "cekFillPhoneFragment";

    public FillPhoneFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.root_fill_phone) RelativeLayout rootFillPhone;
    @BindView(R.id.edt_phone) EditText edtPhone;
    @BindView(R.id.btn_save_fill_phone) Button btnSaveFillPhone;
    @BindView(R.id.card_fill_phone) CardView cardFillPhone;
    @BindView(R.id.progress_fill_phone) ProgressBar progressFillPhone;

    Call<LoginModel> call;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fill_phone, container, false);
        ButterKnife.bind(this,v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setClickedView();
    }

    private void setClickedView() {
        rootFillPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btnSaveFillPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtPhone.getText())){
                    Toast.makeText(getContext(), "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }else{
                    progressFillPhone.setVisibility(View.VISIBLE);
                    cardFillPhone.setVisibility(View.GONE);
                    EditUserData editUserData = new EditUserData(Utils.sharedUser.getId(),null, edtPhone.getText().toString(),null,null,null,null);
                    call = RetrofitServices.sendEditProfileRequest().callEditProfile(Utils.sharedJsonHeader, Utils.sharedHeader,Utils.sharedUser.getId() , editUserData);
                    call.enqueue(new Callback<LoginModel>() {
                        @Override
                        public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                            progressFillPhone.setVisibility(View.GONE);
                            cardFillPhone.setVisibility(View.VISIBLE);

                            if (!call.isCanceled()){
                                if (response.body() != null){
                                    if (response.body().getError() != null){
                                        Toast.makeText(getContext(), response.body().getError().getMsg(), Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "onResponse: "+response.body().getError().getMsg());
                                    }else{
                                        UserSaved userSaved = new Select()
                                                .from(UserSaved.class)
                                                .where(UserSaved_Table.idLogin.is(1))
                                                .querySingle();

                                        UserModel userModel = response.body().getUserModel();
                                        Utils.printProfileData(userModel, TAG);
                                        Utils.sharedUser.setName(userModel.getName());
                                        Utils.sharedUser.setEmail(userModel.getEmail());
                                        Utils.sharedUser.setPhone(userModel.getPhone());

                                        userSaved.setName(userModel.getName());
                                        userSaved.setEmail(userModel.getEmail());
                                        userSaved.setPhone(userModel.getPhone());
                                        userSaved.save();

                                        ((PaymentActivity)getActivity()).goToPayment();
                                        getFragmentManager().popBackStack();
                                        Toast.makeText(getContext(), "Saved!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginModel> call, Throwable t) {
                            Toast.makeText(getContext(), "something was error", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onFailure: "+t.toString());
                            progressFillPhone.setVisibility(View.GONE);
                            cardFillPhone.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        if (call != null){
            call.cancel();
        }
        super.onDestroyView();
    }
}
