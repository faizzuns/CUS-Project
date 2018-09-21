package user.com.cus.Services.ApiHelper;

import user.com.cus.DataModel.Register.RegisterData;
import user.com.cus.DataModel.Register.RegisterModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by User on 25/01/2018.
 */

public interface RegisterService {

    @POST("account/create")
    Call<RegisterModel> callRegister(@Header("Content-Type") String contentType, @Header("authorization") String header, @Body RegisterData registerData);
}
