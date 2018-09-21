package user.com.cus.DataModel.Place;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import user.com.cus.DataModel.ErrorModel;

import java.util.List;

/**
 * Created by User on 26/01/2018.
 */

public class PlaceModel {
    @SerializedName("error")
    @Expose
    private ErrorModel error;
    @SerializedName("result")
    @Expose
    private List<PlaceResult> result;

    public PlaceModel(ErrorModel error, List<PlaceResult> result) {
        this.error = error;
        this.result = result;
    }

    public ErrorModel getError() {
        return error;
    }

    public void setError(ErrorModel error) {
        this.error = error;
    }

    public List<PlaceResult> getResult() {
        return result;
    }

    public void setResult(List<PlaceResult> result) {
        this.result = result;
    }
}
