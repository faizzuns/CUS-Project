package user.com.cus.DataModel.Payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by User on 02/02/2018.
 */

public class PaymentData {
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("item_list")
    @Expose
    private List<ItemList> list;
    @SerializedName("estimation_hour")
    @Expose
    private int hour;
    @SerializedName("estimation_minute")
    @Expose
    private int minute;
    @SerializedName("notes")
    @Expose
    private String notes;

    public PaymentData(int userId, List<ItemList> list, int hour, int minute, String notes) {
        this.userId = userId;
        this.list = list;
        this.hour = hour;
        this.minute = minute;
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<ItemList> getList() {
        return list;
    }

    public void setList(List<ItemList> list) {
        this.list = list;
    }
}
