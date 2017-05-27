package siliconsolutions.cpptourapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by user on 5/25/17.
 */

public class Tour {
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("distance")
    private String distance;
    @SerializedName("markers")
    private List<TourMarkers> tourMarkersList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public List<TourMarkers> getTourMarkersList() {
        return tourMarkersList;
    }

    public void setTourMarkersList(List<TourMarkers> tourMarkersList) {
        this.tourMarkersList = tourMarkersList;
    }
}
