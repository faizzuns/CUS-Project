package user.com.cus.Services.ApiHelper;

import user.com.cus.DataModel.Place.PlaceData;
import user.com.cus.DataModel.Place.PlaceModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by User on 27/01/2018.
 */

public interface ListPlaceService {
    @POST("toko/explore")
    Call<PlaceModel> callListPlace(@Header("Content-Type") String contentType, @Header("authorization") String header, @Body PlaceData placeData);
}
