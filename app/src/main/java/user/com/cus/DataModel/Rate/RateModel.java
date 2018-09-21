package user.com.cus.DataModel.Rate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import user.com.cus.DataModel.ErrorModel;

/**
 * Created by User on 22/02/2018.
 */

public class RateModel {
    @SerializedName("error")
    @Expose
    private ErrorModel errorModel;
    @SerializedName("result")
    @Expose
    private Object result;

    public RateModel(ErrorModel errorModel, Object result) {
        this.errorModel = errorModel;
        this.result = result;
    }

    public ErrorModel getErrorModel() {
        return errorModel;
    }

    public void setErrorModel(ErrorModel errorModel) {
        this.errorModel = errorModel;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
