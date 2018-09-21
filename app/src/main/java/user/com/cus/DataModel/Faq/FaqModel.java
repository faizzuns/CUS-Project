package user.com.cus.DataModel.Faq;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import user.com.cus.DataModel.ErrorModel;

import java.util.List;

/**
 * Created by User on 01/02/2018.
 */

public class FaqModel {
    @SerializedName("error")
    @Expose
    private ErrorModel error;
    @SerializedName("result")
    @Expose
    private List<FaqResult> listFaq;

    public FaqModel(ErrorModel error, List<FaqResult> listFaq) {
        this.error = error;
        this.listFaq = listFaq;
    }

    public ErrorModel getError() {
        return error;
    }

    public void setError(ErrorModel error) {
        this.error = error;
    }

    public List<FaqResult> getListFaq() {
        return listFaq;
    }

    public void setListFaq(List<FaqResult> listFaq) {
        this.listFaq = listFaq;
    }
}
