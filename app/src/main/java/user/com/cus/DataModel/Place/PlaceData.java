package user.com.cus.DataModel.Place;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 27/01/2018.
 */

public class PlaceData {
    @SerializedName("category")
    @Expose
    private int category;
    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("longitude")
    @Expose
    private double longitude;
    @SerializedName("low_rad")
    @Expose
    private double lowRad;
    @SerializedName("high_rad")
    @Expose
    private double highRad;

    public PlaceData(int category, double latitude, double longitude, double lowRad, double highRad) {
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lowRad = lowRad;
        this.highRad = highRad;
    }
}
