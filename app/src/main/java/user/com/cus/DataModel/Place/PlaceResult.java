package user.com.cus.DataModel.Place;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 26/01/2018.
 */

public class PlaceResult {
    @SerializedName("toko_id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("img_url")
    @Expose
    private String imgUrl;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("open_at")
    @Expose
    private String openAt;
    @SerializedName("close_at")
    @Expose
    private String closedAt;
    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("longitude")
    @Expose
    private double longitude;
    @SerializedName("is_close")
    @Expose
    private boolean isClose;

    public PlaceResult(int id, String name, String imgUrl, String address, String description, String phone, String openAt, String closedAt, double latitude, double longitude, boolean isClose) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
        this.address = address;
        this.description = description;
        this.phone = phone;
        this.openAt = openAt;
        this.closedAt = closedAt;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isClose = isClose;
    }

    public PlaceResult(String name, String address, String phone, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public boolean isClose() {
        return isClose;
    }

    public void setClose(boolean close) {
        isClose = close;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOpenAt() {
        return openAt;
    }

    public void setOpenAt(String openAt) {
        this.openAt = openAt;
    }

    public String getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(String closedAt) {
        this.closedAt = closedAt;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLangitude(double longitude) {
        this.longitude = longitude;
    }

    public String toStringHistory(){
        String data = "";

        data += getName()+"@"+getAddress()+"@"+getPhone()+"@"+getLatitude()+"@"+getLongitude();

        return data;
    }

    public static PlaceResult toObjectHistory(String data){
        String[] temp = data.split("@");

        return new PlaceResult(temp[0], temp[1], temp[2], Double.parseDouble(temp[3]), Double.parseDouble(temp[4]));
    }
}
