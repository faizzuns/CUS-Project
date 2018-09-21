package user.com.cus.DataModel.Place;

/**
 * Created by User on 17/02/2018.
 */

public class SearchLocation {
    private String name;
    private String alamat;
    private double lat;
    private double lng;

    public SearchLocation(String name, String alamat, double lat, double lng) {
        this.name = name;
        this.alamat = alamat;
        this.lat = lat;
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
