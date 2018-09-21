package user.com.cus.DataModel.Faq;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 01/02/2018.
 */

public class FaqResult {
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("answer")
    @Expose
    private String answer;
    @SerializedName("is_show")
    @Expose
    private boolean isShow;

    public FaqResult(String question, String answer, boolean isShow) {
        this.question = question;
        this.answer = answer;
        this.isShow = isShow;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
