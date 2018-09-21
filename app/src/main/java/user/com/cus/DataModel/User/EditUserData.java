package user.com.cus.DataModel.User;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 31/01/2018.
 */

public class EditUserData {
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("current_password")
    @Expose
    private String currentPassword;
    @SerializedName("password_1")
    @Expose
    private String password;
    @SerializedName("password_2")
    @Expose
    private String password2;

    public EditUserData(int userId, String name, String phone, String email, String currentPassword, String password, String password2) {
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.currentPassword = currentPassword;
        this.password = password;
        this.password2 = password2;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }
}
