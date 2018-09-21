package user.com.cus.DataModel.History;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import user.com.cus.DataModel.ErrorModel;

import java.util.List;

/**
 * Created by User on 21/02/2018.
 */

public class HistoryModel {
    @SerializedName("error")
    @Expose
    private ErrorModel error;
    @SerializedName("result")
    private List<HistoryResult> list;

    public HistoryModel(ErrorModel error, List<HistoryResult> list) {
        this.error = error;
        this.list = list;
    }

    public ErrorModel getError() {
        return error;
    }

    public void setError(ErrorModel error) {
        this.error = error;
    }

    public List<HistoryResult> getList() {
        return list;
    }

    public void setList(List<HistoryResult> list) {
        this.list = list;
    }
}
