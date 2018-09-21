package user.com.cus.DataModel.Item;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import user.com.cus.Utils.MyDatabase;

/**
 * Created by User on 28/01/2018.
 */

@Table( database = MyDatabase.class)
public class ItemSaved extends BaseModel{
    @PrimaryKey
    @Column
    private int id;
    @Column
    private int idToko;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private int price;
    @Column
    private String imgUrl;
    @Column
    private boolean isWishlist;
    @Column
    private int count;
    @Column
    private String imgUrlToko;
    @Column
    private String nameToko;
    @Column
    private String addressToko;
    @Column
    private String phoneToko;

    public ItemSaved(){}

    public ItemSaved(int id, int idToko, String name, String description, int price, String imgUrl, boolean isWishlist, int count, String imgUrlToko, String nameToko, String addressToko, String phoneToko) {
        this.id = id;
        this.idToko = idToko;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
        this.isWishlist = isWishlist;
        this.count = count;
        this.imgUrlToko = imgUrlToko;
        this.nameToko = nameToko;
        this.addressToko = addressToko;
        this.phoneToko = phoneToko;
    }

    public String getImgUrlToko() {
        return imgUrlToko;
    }

    public void setImgUrlToko(String imgUrlToko) {
        this.imgUrlToko = imgUrlToko;
    }

    public String getNameToko() {
        return nameToko;
    }

    public void setNameToko(String nameToko) {
        this.nameToko = nameToko;
    }

    public String getAddressToko() {
        return addressToko;
    }

    public void setAddressToko(String addressToko) {
        this.addressToko = addressToko;
    }

    public String getPhoneToko() {
        return phoneToko;
    }

    public void setPhoneToko(String phoneToko) {
        this.phoneToko = phoneToko;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdToko() {
        return idToko;
    }

    public void setIdToko(int idToko) {
        this.idToko = idToko;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean isWishlist() {
        return isWishlist;
    }

    public void setWishlist(boolean wishlist) {
        isWishlist = wishlist;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
