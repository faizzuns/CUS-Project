package user.com.cus.DataModel.Item;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by User on 21/02/2018.
 */

public class ItemModelResult {
    @SerializedName("list_items")
    @Expose
    private List<ItemResult> listResult;
    @SerializedName("is_close")
    @Expose
    private boolean isClose;

    public ItemModelResult(List<ItemResult> listResult, boolean isClose) {
        this.listResult = listResult;
        this.isClose = isClose;
    }

    public List<ItemResult> getListResult() {
        return listResult;
    }

    public boolean isClose() {
        return isClose;
    }
}
