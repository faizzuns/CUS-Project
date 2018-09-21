package user.com.cus.DataModel.Payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import user.com.cus.DataModel.ErrorModel;

/**
 * Created by User on 02/02/2018.
 */

public class PaymentModel {
    @SerializedName("error")
    @Expose
    private ErrorModel error;

    public PaymentModel(ErrorModel error) {
        this.error = error;
    }

    public ErrorModel getError() {
        return error;
    }

    public void setError(ErrorModel error) {
        this.error = error;
    }
}
