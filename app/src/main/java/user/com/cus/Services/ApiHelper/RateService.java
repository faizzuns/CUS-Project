package user.com.cus.Services.ApiHelper;

import user.com.cus.DataModel.Rate.RateData;
import user.com.cus.DataModel.Rate.RateModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by User on 21/02/2018.
 */

public interface RateService {
    @POST("payment/{transaction_id}/rate")
    Call<RateModel> callRate(@Header("Content-Type") String contentType, @Header("authorization") String header, @Path("transaction_id") String transactionId, @Body RateData rateData);
}
