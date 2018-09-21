package user.com.cus.DataModel.Item;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 01/02/2018.
 */

public class ItemData {
    @SerializedName("toko_id")
    @Expose
    private int tokoId;

    public ItemData(int tokoId) {
        this.tokoId = tokoId;
    }

    public int getTokoId() {
        return tokoId;
    }

    public void setTokoId(int tokoId) {
        this.tokoId = tokoId;
    }
}
