package siliconsolutions.cpptourapp.Model;
import com.google.gson.annotations.SerializedName;

public class Building {

    @SerializedName("description")
    private String descritption;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longtitude")
    private String longtitude;
    @SerializedName("name")
    private String buildingName;
    @SerializedName("number")
    private String buildingNumber;
    @SerializedName("image")
    private String imageUrl;
    private boolean isFavorite;

    public Building(String latitude, String longtitude, String buildingName, String buildingNumber, String description, String imageUrl) {
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.buildingName = buildingName;
        this.buildingNumber = buildingNumber;
        this.descritption = description;
        this.imageUrl = imageUrl;
        isFavorite = false;
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

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getDescritption() {
        return descritption;
    }

    public void setDescritption(String descritption) {
        this.descritption = descritption;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
