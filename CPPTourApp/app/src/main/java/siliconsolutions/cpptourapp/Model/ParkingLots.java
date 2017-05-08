package siliconsolutions.cpptourapp.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Phuoc on 5/8/2017.
 */

public class ParkingLots {

    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longtitude")
    private String longtitude;
    @SerializedName("name")
    private String parkingLotsName;
    @SerializedName("number")
    private String parkingLotsNumber;

    public ParkingLots(String latitude, String longtitude, String parkingLotsName, String parkingLotsNumber) {
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.parkingLotsName = parkingLotsName;
        this.parkingLotsNumber = parkingLotsNumber;
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

    public String getParkingLotsName()
    {
        return parkingLotsName;
    }

    public void setParkingLotsName(String parkingLotsName)
    {
        this.parkingLotsName = parkingLotsName;
    }

    public String getParkingLotsNumber() {
        return parkingLotsNumber;
    }

    public void setParkingLotsNumber(String parkingLotsNumber)
    {
        this.parkingLotsNumber = parkingLotsNumber;
    }
}
