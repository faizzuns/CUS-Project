package user.com.cus.DataModel.History;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 21/02/2018.
 */

public class HistoryItem {
    @SerializedName("item_id")
    @Expose
    private int id;
    @SerializedName("item_name")
    @Expose
    private String name;
    @SerializedName("item_quantity")
    @Expose
    private int quantity;
    @SerializedName("price_total")
    @Expose
    private int price;

    public HistoryItem(int id, String name, int quantity, int price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public String toString(){
        String data = "";

        data += getId()+"@"+getName()+"@"+getQuantity()+"@"+getPrice();

        return data;
    }

    public static HistoryItem toObject(String data){
        String[] temp = data.split("@");
        return new HistoryItem(Integer.parseInt(temp[0]),temp[1],Integer.parseInt(temp[2]),Integer.parseInt(temp[3]));
    }
}
