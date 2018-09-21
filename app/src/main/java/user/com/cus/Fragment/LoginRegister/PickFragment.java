package user.com.cus.Fragment.LoginRegister;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
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
public class PickFragment extends Fragment {

    private static final String TAG = "cekPickFragment";
    @BindView(R.id.btn_login) Button btnLogin;
    @BindView(R.id.btn_register) Button btnRegister;
    @BindView(R.id.sign_in_button) Button btnGoogle;
    @BindView(R.id.progress_google) ProgressBar progressGoogle;
    @BindView(R.id.card_google) CardView cardGoogle;

    GoogleSignInClient mGoogleSignInClient;

    public PickFragment() {
        // Required empty public constructor
    }

    Call<LoginModel> callLogin;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pick, container, false);
        ButterKnife.bind(this,v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setClickedView();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);



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
                ((LoginRegisterActivity)getActivity()).goToRegisterFragment();
            }
        });
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, Utils.RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == Utils.RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(getContext(), "signInResult:failed code=" + e.getStatusCode(), Toast.LENGTH_SHORT).show();
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null){
            progressGoogle.setVisibility(View.VISIBLE);
            cardGoogle.setVisibility(View.GONE);

            final String userId = account.getId();
            String name = account.getDisplayName();
            String email = account.getEmail();

            LoginData loginData = new LoginData(email,userId,name,"google");
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
                                        userId,
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
                    cardGoogle.setVisibility(View.VISIBLE);
                    progressGoogle.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<LoginModel> call, Throwable t) {
                    Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: "+t.toString());
                    cardGoogle.setVisibility(View.VISIBLE);
                    progressGoogle.setVisibility(View.GONE);
                }
            });


            Log.d(TAG, "updateUI: "+userId+", "+name+", "+email);
        }
    }

    @Override
    public void onDestroyView() {
        if (callLogin != null){
            callLogin.cancel();
        }
        super.onDestroyView();
    }
}
