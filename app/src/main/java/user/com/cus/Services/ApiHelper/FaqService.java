package user.com.cus.Services.ApiHelper;

import user.com.cus.DataModel.Faq.FaqModel;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by User on 01/02/2018.
 */

public interface FaqService {
    @GET("faq/get")
    Call<FaqModel> callFaq();
}
