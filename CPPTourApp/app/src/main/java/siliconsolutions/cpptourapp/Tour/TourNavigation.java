package siliconsolutions.cpptourapp.Tour;

import android.graphics.Color;
import android.util.Log;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import retrofit2.Response;
import siliconsolutions.cpptourapp.Directions.GeoCodeResponse;
import siliconsolutions.cpptourapp.Directions.Leg;
import siliconsolutions.cpptourapp.Directions.StartLocation;
import siliconsolutions.cpptourapp.Directions.Step;


public class TourNavigation {
    private static TourNavigation instance;
    private List<Response<GeoCodeResponse>> responseList;
    private double totalTime;
    private List<LatLng> latLngsList;
    private com.google.android.gms.maps.model.Polyline line;
    private GoogleMap mMap;
    private String polyLine;

    public static TourNavigation getInstance(List<Response<GeoCodeResponse>> responseList, GoogleMap mMap){
        if(instance == null){
            instance =  new TourNavigation(responseList,mMap);

        }
        return instance;
    }

    private TourNavigation(List<Response<GeoCodeResponse>> responseList, GoogleMap mMap){
        this.responseList = responseList;
        this.mMap = mMap;
    }

    private void generateTotalTime(){
        for(Response<GeoCodeResponse> response : responseList){
            String timeString = response.body().routes.get(0).getLegs().get(0).getDuration().getText();
            String str = timeString.replaceAll("\\D+","");
            totalTime += Double.parseDouble(str);
        }
    }

    public void generatePolyLines(){
        for(int i = 0; i < responseList.size();i++){
            polyLine = responseList.get(i).body().routes.get(0).getOverviewPolyline().getPoints();
            latLngsList = decodePoly(polyLine);
            int color = getRandomColor();
            for(int j = 0; j < latLngsList.size() - 1;j++){
                LatLng s = latLngsList.get(j);
                LatLng d = latLngsList.get(j + 1);
                line = mMap.addPolyline(new PolylineOptions().add(new LatLng(s.latitude,s.longitude),new LatLng(d.latitude,d.longitude))
                        .width(5).color(color).geodesic(true));
            }
        }
    }

    private void displayRouteMarkers(int i,int j){
        List<Step> legs = responseList.get(i).body().routes.get(0).getLegs().get(0).getSteps();
        for(int index = 0; index < legs.size();index++){
            StartLocation s = legs.get(index).getStartLocation();
            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker()).position(new LatLng(s.getLat(),s.getLng())));
        }
    }

    private ArrayList<LatLng> decodePoly(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),(((double) lng / 1E5)));
            poly.add(p);
        }

        for(int i=0;i < poly.size(); i++){
            Log.i("Location", "Point sent: Latitude: " + poly.get(i).latitude + " Longitude: " + poly.get(i).longitude);
        }
        return poly;
    }

    private String getTotalDuration(){
        return null;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }

    public String getTotalTime(){
        generateTotalTime();
        return (totalTime + "mins");
    }

    public int getRandomColor(){
        Random random = new Random();
        int i = random.nextInt(7) + 1;
        switch (i){
            case 1:
                return Color.BLACK;
            case 2:
                return Color.BLUE;
            case 3:
                return Color.GREEN;
            case 4:
                return Color.RED;
            case 5:
                return Color.YELLOW;
            case 6:
                return Color.WHITE;
            default:
                return Color.DKGRAY;
        }
    }
}
