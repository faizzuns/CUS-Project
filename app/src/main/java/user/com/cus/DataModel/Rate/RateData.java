package user.com.cus.DataModel.Rate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 22/02/2018.
 */

public class RateData {
    @SerializedName("rating")
    @Expose
    private int rating;
    @SerializedName("comment")
    @Expose
    private String comment;

    public RateData(int rating, String s) {
        this.rating = rating;
        comment = s;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
