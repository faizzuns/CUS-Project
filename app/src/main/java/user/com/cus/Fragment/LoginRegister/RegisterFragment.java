package user.com.cus.Fragment.LoginRegister;


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
import android.widget.TextView;
import android.widget.Toast;

import user.com.cus.Activity.LoginRegisterActivity;
import user.com.cus.DataModel.Register.RegisterData;
import user.com.cus.DataModel.Register.RegisterModel;
import user.com.cus.DataModel.User.FavouriteModel;
import user.com.cus.DataModel.User.UserModel;
import user.com.cus.DataModel.User.UserSaved;
import user.com.cus.R;
import user.com.cus.Services.RetrofitServices;
import user.com.cus.Utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private final String TAG = "cekRegsiterFragment";

    public RegisterFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.edt_name) EditText edtName;
    @BindView(R.id.edt_email) EditText edtEmail;
    @BindView(R.id.edt_phone) EditText edtPhone;
    @BindView(R.id.edt_password) EditText edtPassword;
    @BindView(R.id.edt_confirm_password) EditText edtConfirmPassword;
    @BindView(R.id.btn_login)
    TextView btnLogin;
    @BindView(R.id.btn_register) Button btnRegister;
    @BindView(R.id.progress_register) ProgressBar progressRegister;
    @BindView(R.id.card_register) CardView cardRegister;

    Call<RegisterModel> callRegister;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this,v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setClickedView();
    }

    private void setClickedView() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginRegisterActivity)getActivity()).goToLoginFragment();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtName.getText()) ||
                        TextUtils.isEmpty(edtEmail.getText()) ||
                        TextUtils.isEmpty(edtPhone.getText()) ||
                        TextUtils.isEmpty(edtPassword.getText()) ||
                        TextUtils.isEmpty(edtConfirmPassword.getText())){
                    Toast.makeText(getContext(), "You must fill all data", Toast.LENGTH_SHORT).show();
                }else if (edtPassword.getText().toString().length() < 6){
                    Toast.makeText(getContext(), "Password at least 6 character", Toast.LENGTH_SHORT).show();
                }else if (!isEmailValid(edtEmail.getText().toString())){
                    Toast.makeText(getContext(), "Email doesnt valid", Toast.LENGTH_SHORT).show();
                }else{
                    cardRegister.setVisibility(View.GONE);
                    progressRegister.setVisibility(View.VISIBLE);

                    String name = edtName.getText().toString();
                    String email = edtEmail.getText().toString();
                    String phone = edtPhone.getText().toString();
                    final String password = edtPassword.getText().toString();
                    String confirmPassword = edtConfirmPassword.getText().toString();
                    RegisterData registerData = new RegisterData(name,phone,email,password,confirmPassword);

                    callRegister = RetrofitServices.sendRegisterRequest().callRegister(Utils.sharedJsonHeader, Utils.sharedHeader, registerData);
                    callRegister.enqueue(new Callback<RegisterModel>() {
                        @Override
                        public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {
                            if (!callRegister.isCanceled()){
                                if (response.body() != null){
                                    if (response.body().getError() != null){
                                        Log.d(TAG, "onResponse: "+response.body().getError().getMsg());
                                        Toast.makeText(getContext(), response.body().getError().getMsg(), Toast.LENGTH_SHORT).show();
                                    }else{
                                        UserModel userModel = response.body().getUserModel();
                                        userModel.setListFavourite(new ArrayList<FavouriteModel>());
                                        Utils.printProfileData(userModel, TAG);
                                        Utils.sharedUser = userModel;
                                        UserSaved userSaved = new UserSaved(1,
                                                userModel.getId(),
                                                userModel.getName(),
                                                userModel.getPhone(),
                                                userModel.getEmail(),
                                                password,
                                                Utils.listFavouriteToString(userModel.getListFavourite()));
                                        userSaved.save();
                                        Utils.closeSoftKeyboard(getContext(), ((LoginRegisterActivity)getActivity()).getCurrentFocus());

                                        //go To MainActivity
                                        ((LoginRegisterActivity)getActivity()).goToMain();
                                    }
                                }else{
                                    Log.d(TAG, "onResponse: callLogin has null data");
                                }
                            }

                            cardRegister.setVisibility(View.VISIBLE);
                            progressRegister.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<RegisterModel> call, Throwable t) {
                            Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onFailure: "+t.toString());
                            cardRegister.setVisibility(View.VISIBLE);
                            progressRegister.setVisibility(View.GONE);
                        }
                    });

                }
            }
        });
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches();}

    @Override
    public void onDestroyView() {
        if (callRegister != null){
            callRegister.cancel();
        }
        super.onDestroyView();
    }
}
