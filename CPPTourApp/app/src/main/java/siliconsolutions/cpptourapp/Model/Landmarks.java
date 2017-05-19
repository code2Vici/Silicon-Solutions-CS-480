package siliconsolutions.cpptourapp.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Phuoc on 5/8/2017.
 */

public class Landmarks {

    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longtitude")
    private String longtitude;
    @SerializedName("name")
    private String landmarksName;
    @SerializedName("number")
    private String landmarksNumber;
    private boolean isFavorite;

    public Landmarks(String latitude, String longtitude, String landmarksName, String landmarksNumber) {
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.landmarksName = landmarksName;
        this.landmarksNumber = landmarksNumber;
        isFavorite = false;
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

    public String getLandmarkName()
    {
        return landmarksName;
    }

    public void setLandmarkName(String landmarksName)
    {
        this.landmarksName = landmarksName;
    }

    public String getLandmarkNumber() {
        return landmarksNumber;
    }

    public void setLandmarkNumber(String landmarksNumber)
    {
        this.landmarksNumber = landmarksNumber;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
