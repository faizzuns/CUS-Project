package user.com.cus.DataModel.History;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import user.com.cus.DataModel.Item.ItemResult;
import user.com.cus.DataModel.Payment.ItemList;
import user.com.cus.DataModel.Place.PlaceResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 20/02/2018.
 */

public class HistoryResult {

    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("estimation")
    @Expose
    private String estimation;
    @SerializedName("toko")
    @Expose
    private List<PlaceResult> toko;
    @SerializedName("list_items")
    @Expose
    private List<HistoryItem> list;
    @SerializedName("rating")
    @Expose
    private int rating;

    public HistoryResult(String transactionId, String createdAt, int status, String estimation, List<PlaceResult> toko, List<HistoryItem> list, int rating) {
        this.transactionId = transactionId;
        this.createdAt = createdAt;
        this.status = status;
        this.estimation = estimation;
        this.toko = toko;
        this.list = list;
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getStatus() {
        return status;
    }

    public String getEstimation() {
        return estimation;
    }

    public List<PlaceResult> getToko() {
        return toko;
    }

    public List<HistoryItem> getList() {
        return list;
    }

    public String toString(){
        String data = "";

        data += getTransactionId()+"#"+getCreatedAt()+"#"+getStatus()+"#"+getEstimation()+"#";

        if (getToko().size() == 0){
            data += "kosong#";
        }else{
            data += getToko().get(0).toStringHistory() + "#";
        }

        for (int i =0; i < getList().size(); i++){
            data += getList().get(i).toString();
            if (i != getList().size()-1){
                data += "&";
            }
        }
        if (getList().size() == 0){
            data += "kosong";
        }

        data += "#"+getRating();

        return data;
    }

    public static HistoryResult toObject(String data){
        String[] temp = data.split("#");

        List<PlaceResult> list = new ArrayList<>();
        list.add(PlaceResult.toObjectHistory(temp[4]));

        List<HistoryItem> listHistory = new ArrayList<>();
        String[] tempData = temp[5].split("&");

        for (String s : tempData){
            listHistory.add(HistoryItem.toObject(s));
        }

        return new HistoryResult(temp[0],temp[1],Integer.parseInt(temp[2]),temp[3],list,listHistory,Integer.parseInt(temp[6]));
    }
}
