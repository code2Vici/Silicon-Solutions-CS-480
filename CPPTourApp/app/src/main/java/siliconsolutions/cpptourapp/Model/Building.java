package siliconsolutions.cpptourapp.Model;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Phuoc on 5/7/2017.
 */

public class Building {

    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longtitude")
    private String longtitude;
    @SerializedName("name")
    private String buildingName;
    @SerializedName("number")
    private String buildingNumber;

    public Building(String latitude, String longtitude, String buildingName, String buildingNumber) {
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.buildingName = buildingName;
        this.buildingNumber = buildingNumber;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitudee(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }
}
