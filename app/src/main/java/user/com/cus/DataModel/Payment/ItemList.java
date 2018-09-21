package user.com.cus.DataModel.Payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 01/02/2018.
 */

public class ItemList {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("toko_id")
    @Expose
    private int tokoId;
    @SerializedName("item_id")
    @Expose
    private int itemId;
    @SerializedName("item_quantity")
    @Expose
    private int itemQuantity;
    @SerializedName("total_price")
    @Expose
    private int totalPrice;

    public ItemList(String name, int tokoId, int itemId, int itemQuantity, int totalPrice) {
        this.name = name;
        this.tokoId = tokoId;
        this.itemId = itemId;
        this.itemQuantity = itemQuantity;
        this.totalPrice = totalPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTokoId() {
        return tokoId;
    }

    public void setTokoId(int tokoId) {
        this.tokoId = tokoId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
