package siliconsolutions.cpptourapp.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Phuoc on 5/26/2017.
 */

public class BusRouteA {
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longtitude")
    private String longtitude;
    @SerializedName("name")
    private String busRouteName;
    @SerializedName("number")
    private String busRouteNumber;

    public BusRouteA(String latitude, String longtitude, String busRouteName, String busRouteNumber) {
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.busRouteName = busRouteName;
        this.busRouteNumber = busRouteNumber;
    }

    public String getLatitude()
    {
        return latitude;
    }

    public void setLatitudee(String latitude)
    {
        this.latitude = latitude;
    }

    public String getLongtitude()
    {
        return longtitude;
    }

    public void setLongtitude(String longtitude)
    {
        this.longtitude = longtitude;
    }

    public String getBusRouteName()
    {
        return busRouteName;
    }

    public void setBusRouteName(String busRouteName)
    {
        this.busRouteName = busRouteName;
    }

    public String getBusRouteNumber() {
        return busRouteNumber;
    }

    public void setBusRouteNumber(String busRouteNumber)
    {
        this.busRouteNumber = busRouteNumber;
    }
}
