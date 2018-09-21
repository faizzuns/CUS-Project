package user.com.cus.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Select;
import user.com.cus.DataModel.User.UserModel;
import user.com.cus.DataModel.User.UserSaved;
import user.com.cus.DataModel.User.UserSaved_Table;
import user.com.cus.MainActivity;
import user.com.cus.R;
import user.com.cus.Utils.Utils;

public class SplashScreenActivity extends AppCompatActivity {

    private final String TAG = "cekSplashScreenActivity";
    ImageView img;

    private static int SPLASH_TIME_OUT = 1500;

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        img = (ImageView) findViewById(R.id.img_splash);

        //inisialisasi database lokal menggunakan dbFlow
        FlowManager.init(new FlowConfig.Builder(this).build());

        boolean isAlreadyLogin = checkUserInLocalDatabase();

        if (isAlreadyLogin){
            goToMain();
        }else{
            goToLoginRegister();
        }

    }

    private boolean checkUserInLocalDatabase() {
        final UserSaved userSaved = new Select()
                .from(UserSaved.class)
                .where(UserSaved_Table.idLogin.is(1))
                .querySingle();

        if (userSaved == null){
            return false;
        }else{
            Log.d(TAG, "checkUserInLocalDatabase: "+userSaved.getListFavourite());
            UserModel userModel = new UserModel(userSaved.getId(),userSaved.getName(),userSaved.getPhone(),userSaved.getEmail(),Utils.stringToListFavourite(userSaved.getListFavourite()));
            Utils.printProfileData(userModel, TAG);
            Utils.sharedUser = userModel;
            Utils.syncDataFavourite();
            return true;
        }
    }

    private void goToLoginRegister() {
        img.animate().alpha(0f).setDuration(SPLASH_TIME_OUT);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), LoginRegisterActivity.class));
                finish();
            }
        };
        handler.postDelayed(runnable,SPLASH_TIME_OUT);
    }

    private void goToMain(){
        img.animate().alpha(0f).setDuration(SPLASH_TIME_OUT);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        };
        handler.postDelayed(runnable,SPLASH_TIME_OUT);
    }

    @Override
    protected void onStop() {
        if (runnable != null){
            handler.removeCallbacks(runnable);
        }
        super.onStop();
    }
}
