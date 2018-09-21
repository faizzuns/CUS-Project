package user.com.cus.Services.ApiHelper;

import user.com.cus.DataModel.Login.LoginModel;
import user.com.cus.DataModel.User.EditUserData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by User on 31/01/2018.
 */

public interface EditProfileServie {
    @POST("account/{user_id}/edit")
    Call<LoginModel> callEditProfile(@Header("Content-Type") String contentType, @Header("authorization") String header, @Path("user_id") int userId, @Body EditUserData editUserData);
}
