package user.com.cus.DataModel.Register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import user.com.cus.DataModel.ErrorModel;
import user.com.cus.DataModel.User.UserModel;

/**
 * Created by User on 25/01/2018.
 */

public class RegisterModel {
    @SerializedName("error")
    @Expose
    private ErrorModel error;
    @SerializedName("result")
    @Expose
    private UserModel userModel;

    public RegisterModel(ErrorModel error, UserModel userModel) {
        this.error = error;
        this.userModel = userModel;
    }

    public ErrorModel getError() {
        return error;
    }

    public void setError(ErrorModel error) {
        this.error = error;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }
}
