package user.com.cus.DataModel.User;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 26/01/2018.
 */

public class FavouriteModel {
    @SerializedName("item_id")
    @Expose
    private int itemId;
    @SerializedName("count")
    @Expose
    private int count;

    public FavouriteModel(int itemId, int count) {
        this.itemId = itemId;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}
