package user.com.cus.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import user.com.cus.Fragment.LoginRegister.LoginFragment;
import user.com.cus.Fragment.LoginRegister.PickFragment;
import user.com.cus.Fragment.LoginRegister.RegisterFragment;
import user.com.cus.MainActivity;
import user.com.cus.R;

public class LoginRegisterActivity extends AppCompatActivity {

    private boolean onPickFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        goToPickFragment();
    }

    private void goToPickFragment(){
        onPickFragment = true;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container,
                        new PickFragment(),
                        PickFragment.class.getSimpleName()).commit();
    }
    public void goToLoginFragment() {
        onPickFragment = false;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container,
                        new LoginFragment(),
                        LoginFragment.class.getSimpleName())
                .commit();
    }
    public void goToRegisterFragment() {
        onPickFragment = false;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container,
                        new RegisterFragment(),
                        RegisterFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (!onPickFragment){
            goToPickFragment();
        }else{
            super.onBackPressed();
        }
    }

    public void goToMain() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
