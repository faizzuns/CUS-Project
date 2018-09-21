package user.com.cus.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.Select;
import user.com.cus.DataModel.Login.LoginModel;
import user.com.cus.DataModel.User.EditUserData;
import user.com.cus.DataModel.User.UserModel;
import user.com.cus.DataModel.User.UserSaved;
import user.com.cus.DataModel.User.UserSaved_Table;
import user.com.cus.MainActivity;
import user.com.cus.R;
import user.com.cus.Services.RetrofitServices;
import user.com.cus.Utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = "cekEditProfileActivity";
    @BindView(R.id.edt_name) EditText edtName;
    @BindView(R.id.edt_email) EditText edtEmail;
    @BindView(R.id.edt_phone) EditText edtPhone;
    @BindView(R.id.edt_password) EditText edtPassword;
    @BindView(R.id.edt_current_password) EditText edtCurrentPassword;
    @BindView(R.id.edt_confirm_password) EditText edtConfirmPassword;

    @BindView(R.id.btn_save)
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Profile");

        edtName.setText(Utils.sharedUser.getName());
        edtEmail.setText(Utils.sharedUser.getEmail());
        edtPhone.setText(Utils.sharedUser.getPhone());
        setClickedView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                goToHome();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setClickedView() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = Utils.sharedUser.getId();

                String name = edtName.getText().toString();
                if (name.length() == 0) name = null;
                String email = edtEmail.getText().toString();
                if (email.length() == 0) email = null;
                String phone = edtPhone.getText().toString();
                if (phone.length() == 0) phone = null;
                String currentPassword = edtCurrentPassword.getText().toString();
                if (currentPassword.length() == 0) currentPassword = null;
                String password = edtPassword.getText().toString();
                if (password.length() == 0) password = null;
                String confirmPassword = edtConfirmPassword.getText().toString();
                if (confirmPassword.length() == 0) confirmPassword = null;

                if (name == null && email == null && phone == null && currentPassword == null && password == null && confirmPassword == null){
                    Toast.makeText(EditProfileActivity.this, "No data to update!", Toast.LENGTH_SHORT).show();
                    return;
                }

                UserSaved userSaved = new Select()
                        .from(UserSaved.class)
                        .where(UserSaved_Table.idLogin.is(1))
                        .querySingle();
                if (currentPassword != null || password != null || confirmPassword != null){
                    if (currentPassword == null || password == null || confirmPassword == null){
                        Toast.makeText(EditProfileActivity.this, "You must fill all password data", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!currentPassword.equals(userSaved.getPassword())){
                        Toast.makeText(EditProfileActivity.this, "wrong password", Toast.LENGTH_SHORT).show();
                        return;
                    }else if(!password.equals(confirmPassword)){
                        Toast.makeText(EditProfileActivity.this, "password doesnt match", Toast.LENGTH_SHORT).show();
                        return;
                    }else if (password.length() < 6){
                        Toast.makeText(EditProfileActivity.this, "Password minimal 6 character", Toast.LENGTH_SHORT).show();
                    }
                }

                EditUserData editUserData = new EditUserData(id,name,phone,email,currentPassword,password,confirmPassword);
                Call<LoginModel> call = RetrofitServices.sendEditProfileRequest().callEditProfile(Utils.sharedJsonHeader, Utils.sharedHeader,id , editUserData);
                call.enqueue(new Callback<LoginModel>() {
                    @Override
                    public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                        if (response.body() != null){
                            if (response.body().getError() != null){
                                Toast.makeText(EditProfileActivity.this, response.body().getError().getMsg(), Toast.LENGTH_SHORT).show();
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

                                goToHome();
                                Toast.makeText(EditProfileActivity.this, "saved!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginModel> call, Throwable t) {
                        Toast.makeText(EditProfileActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: "+t.toString());
                    }
                });
            }
        });
    }

    public void goToHome() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("selection", 3);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        goToHome();
    }
}
