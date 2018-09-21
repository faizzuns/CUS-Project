package user.com.cus.DataModel.User;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by User on 25/01/2018.
 */

public class UserModel {
    @SerializedName("user_id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("favourite")
    @Expose
    private List<FavouriteModel> listFavourite;

    public UserModel(int id, String name, String phone, String email, List<FavouriteModel> listFavourite) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.listFavourite = listFavourite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<FavouriteModel> getListFavourite() {
        return listFavourite;
    }

    public void setListFavourite(List<FavouriteModel> listFavourite) {
        this.listFavourite = listFavourite;
    }
}
