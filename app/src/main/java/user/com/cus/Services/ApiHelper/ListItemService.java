package user.com.cus.Services.ApiHelper;

import user.com.cus.DataModel.Item.ItemData;
import user.com.cus.DataModel.Item.ItemModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by User on 01/02/2018.
 */

public interface ListItemService {
    @POST("toko/{toko_id}/item")
    Call<ItemModel> callListItem(@Header("Content-Type") String contentType, @Header("authorization") String header, @Path("toko_id") int tokoId);
}
