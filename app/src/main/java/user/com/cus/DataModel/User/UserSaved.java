package user.com.cus.DataModel.User;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import user.com.cus.Utils.MyDatabase;

/**
 * Created by User on 25/01/2018.
 */

@Table( database = MyDatabase.class)
public class UserSaved extends BaseModel{
    @PrimaryKey
    @Column
    private int idLogin;
    @Column
    private int id;
    @Column
    private String name;
    @Column
    private String phone;
    @Column
    private String email;
    @Column
    private String password;
    @Column
    private String listFavourite;

    public UserSaved(){

    }

    public UserSaved(int idLogin, int id, String name, String phone, String email, String password, String listFavourite) {
        this.idLogin = idLogin;
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.listFavourite = listFavourite;
    }

    public void setListFavourite(String listFavourite) {
        this.listFavourite = listFavourite;
    }

    public String getListFavourite() {
        return listFavourite;
    }

    public int getIdLogin() {
        return idLogin;
    }

    public void setIdLogin(int idLogin) {
        this.idLogin = idLogin;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
