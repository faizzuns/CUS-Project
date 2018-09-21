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
import user.com.cus.DataModel.Login.LoginData;
import user.com.cus.DataModel.Login.LoginModel;
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
public class LoginFragment extends Fragment {

    private final String TAG = "cekLoginFragment";

    public LoginFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.edt_email) EditText edtEmail;
    @BindView(R.id.edt_password) EditText edtPassword;
    @BindView(R.id.btn_login) Button btnLogin;
    @BindView(R.id.btn_register) TextView btnRegister;
    @BindView(R.id.progress_login) ProgressBar progressLogin;
    @BindView(R.id.card_login) CardView cardLogin;

    Call<LoginModel> callLogin;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
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

                if (TextUtils.isEmpty(edtEmail.getText())) {
                    Toast.makeText(getContext(), "You must fill your email", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(edtPassword.getText())) {
                    Toast.makeText(getContext(), "You must fill your password", Toast.LENGTH_SHORT).show();
                }else if (edtPassword.getText().toString().length() < 6){
                    Toast.makeText(getContext(), "Password at least 6 character", Toast.LENGTH_SHORT).show();
                }else if (!isEmailValid(edtEmail.getText().toString())){
                    Toast.makeText(getContext(), "Email doesnt valid", Toast.LENGTH_SHORT).show();
                }else{
                    cardLogin.setVisibility(View.GONE);
                    progressLogin.setVisibility(View.VISIBLE);

                    String email = edtEmail.getText().toString();
                    final String password = edtPassword.getText().toString();
                    LoginData loginData = new LoginData(email,password,null,null);

                    callLogin = RetrofitServices.sendLoginRequest().callLogin(Utils.sharedJsonHeader, Utils.sharedHeader, loginData);
                    callLogin.enqueue(new Callback<LoginModel>() {
                        @Override
                        public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {

                            if (!callLogin.isCanceled()){
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
                                        Log.d(TAG, "onResponse: "+Utils.listFavouriteToString(userModel.getListFavourite()));
                                        userSaved.save();

                                        //go To MainActivity
                                        ((LoginRegisterActivity)getActivity()).goToMain();
                                    }
                                }else{
                                    Log.d(TAG, "onResponse: callLogin has null data");
                                }
                            }

                            Utils.closeSoftKeyboard(getContext(), ((LoginRegisterActivity)getActivity()).getCurrentFocus());
                            cardLogin.setVisibility(View.VISIBLE);
                            progressLogin.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<LoginModel> call, Throwable t) {
                            Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onFailure: "+t.toString());
                            cardLogin.setVisibility(View.VISIBLE);
                            progressLogin.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginRegisterActivity)getActivity()).goToRegisterFragment();
            }
        });
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches();}


    @Override
    public void onDestroyView() {
        if (callLogin != null){
            callLogin.cancel();
        }
        super.onDestroyView();
    }
}
