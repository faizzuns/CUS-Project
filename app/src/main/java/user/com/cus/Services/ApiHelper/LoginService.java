package user.com.cus.Services.ApiHelper;

import user.com.cus.DataModel.Login.LoginData;
import user.com.cus.DataModel.Login.LoginModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by User on 25/01/2018.
 */

public interface LoginService {

    @POST("account/verify")
    Call<LoginModel> callLogin(@Header("Content-Type") String contentType, @Header("authorization") String header, @Body LoginData loginData);
}
