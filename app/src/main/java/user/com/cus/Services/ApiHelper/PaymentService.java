package user.com.cus.Services.ApiHelper;

import user.com.cus.DataModel.Payment.PaymentData;
import user.com.cus.DataModel.Payment.PaymentModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by User on 03/02/2018.
 */

public interface PaymentService {
    @POST("payment/purchase")
    Call<PaymentModel> callPayment(@Header("Content-Type") String contentType, @Header("authorization") String header, @Body PaymentData paymentData);
}
