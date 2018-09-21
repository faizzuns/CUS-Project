package user.com.cus.Services.ApiHelper;

import user.com.cus.DataModel.History.HistoryData;
import user.com.cus.DataModel.History.HistoryModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by User on 21/02/2018.
 */

public interface HistoryService {
    @POST("payment/history")
    Call<HistoryModel> callHistory(@Header("Content-Type") String contentType, @Header("authorization") String header, @Body HistoryData historyData);
}
