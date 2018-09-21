package user.com.cus.Fragment.Main;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.raizlabs.android.dbflow.sql.language.Select;
import user.com.cus.Activity.EditProfileActivity;
import user.com.cus.Activity.LoginRegisterActivity;
import user.com.cus.Adapter.FaqAdapter;
import user.com.cus.DataModel.Faq.FaqModel;
import user.com.cus.DataModel.Faq.FaqResult;
import user.com.cus.DataModel.Item.ItemSaved;
import user.com.cus.DataModel.User.UserSaved;
import user.com.cus.DataModel.User.UserSaved_Table;
import user.com.cus.MainActivity;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    private static final String TAG = "cekSettingFragment";
    @BindView(R.id.arrow_profile) ImageView arrowProfile;
    @BindView(R.id.txt_name_profile) TextView txtNameProfile;
    @BindView(R.id.txt_phone_profile) TextView txtPhoneProfile;
    @BindView(R.id.txt_email_profile) TextView txtEmailProfile;
    @BindView(R.id.btn_logout) Button btnLogout;
    @BindView(R.id.btn_edit_profile) Button btnEditProfile;
    @BindView(R.id.root_data_profile) LinearLayout rootDataProfile;
    @BindView(R.id.root_profile) LinearLayout rootProfile;

    @BindView(R.id.arrow_faq) ImageView arrowFaq;
    @BindView(R.id.root_faq) RelativeLayout rootFaq;
    @BindView(R.id.rv_faq) RecyclerView rvFaq;

    @BindView(R.id.txt_version) TextView txtVersion;

    Call<FaqModel> callFaq;
    List<FaqResult> listFaq;
    FaqAdapter adapter;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this,v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setProfileData();
        setDataFaq();
        txtVersion.setText(Utils.version);

        arrowProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrowProfile.animate().rotationBy(180).setDuration(200);
                if (rootDataProfile.getVisibility() == View.VISIBLE){
                    rootDataProfile.setVisibility(View.GONE);
                }else{
                    rootDataProfile.setVisibility(View.VISIBLE);
                }
            }
        });
        arrowFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrowFaq.animate().rotationBy(180).setDuration(200);
                if (rootFaq.getVisibility() == View.VISIBLE){
                    rootFaq.setVisibility(View.GONE);
                }else{
                    rootFaq.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setDataFaq() {
        rootFaq.setVisibility(View.GONE);
        listFaq = new ArrayList<>();

        callFaq = RetrofitServices.sendFaqRequest().callFaq();
        callFaq.enqueue(new Callback<FaqModel>() {
            @Override
            public void onResponse(Call<FaqModel> call, Response<FaqModel> response) {
                if (!callFaq.isCanceled()){
                    if (response.body() != null){
                        if (response.body().getError() != null){
                            Toast.makeText(getContext(), response.body().getError().getMsg(), Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onResponse: "+response.body().getError().getMsg());
                        }else{
                            for (FaqResult result : response.body().getListFaq()){
                                result.setShow(false);
                                listFaq.add(result);
                            }
                            rvFaq.setHasFixedSize(true);
                            final LinearLayoutManager llm = new LinearLayoutManager(getContext()) {
                                @Override
                                public boolean canScrollVertically(){
                                    return false;
                                }
                            };
                            rvFaq.setLayoutManager(llm);

                            adapter = new FaqAdapter(listFaq, getContext());
                            rvFaq.setAdapter(adapter);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<FaqModel> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.toString());
            }
        });
    }

    private void setProfileData() {
        txtNameProfile.setText(Utils.sharedUser.getName());
        if (Utils.sharedUser.getPhone() != null){
            if (Utils.sharedUser.getPhone().length() != 0){
                txtPhoneProfile.setVisibility(View.VISIBLE);
                txtPhoneProfile.setText(Utils.sharedUser.getPhone());
            }else{
                txtPhoneProfile.setVisibility(View.GONE);
            }
        }else{
            txtPhoneProfile.setVisibility(View.GONE);
        }
        txtEmailProfile.setText(Utils.sharedUser.getEmail());

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.sharedUser = null;
                UserSaved userSaved = new Select()
                        .from(UserSaved.class)
                        .where(UserSaved_Table.idLogin.is(1))
                        .querySingle();
                if (userSaved != null) {
                    userSaved.delete();
                }

                List<ItemSaved> itemSaveds = new Select()
                        .from(ItemSaved.class)
                        .queryList();
                for (ItemSaved itemSaved : itemSaveds){
                    itemSaved.delete();
                }

                signOutGoogle();

                startActivity(new Intent(getContext(), LoginRegisterActivity.class));
                ((MainActivity)getActivity()).finish();
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EditProfileActivity.class));
                ((MainActivity)getActivity()).finish();
            }
        });
    }

    private void signOutGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(((MainActivity) getActivity()), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "onComplete: success");
                    }
                });
    }

    @Override
    public void onDestroyView() {
        if (callFaq != null){
            callFaq.cancel();
        }
        super.onDestroyView();
    }
}
