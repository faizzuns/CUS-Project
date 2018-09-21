package user.com.cus.DataModel.Item;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import user.com.cus.DataModel.ErrorModel;
import user.com.cus.DataModel.Place.PlaceResult;

import java.util.List;

/**
 * Created by User on 27/01/2018.
 */

public class ItemModel {
    @SerializedName("error")
    @Expose
    private ErrorModel error;
    @SerializedName("result")
    @Expose
    private ItemModelResult result;

    public ItemModel(ErrorModel error, ItemModelResult result) {
        this.error = error;
        this.result = result;
    }

    public ErrorModel getError() {
        return error;
    }

    public void setError(ErrorModel error) {
        this.error = error;
    }

    public ItemModelResult getResult() {
        return result;
    }

    public void setResult(ItemModelResult result) {
        this.result = result;
    }
}
