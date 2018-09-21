package user.com.cus.DataModel.History;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 21/02/2018.
 */

public class HistoryData {
    @SerializedName("user_id")
    @Expose
    private int id;

    public HistoryData(int id) {
        this.id = id;
    }
}
