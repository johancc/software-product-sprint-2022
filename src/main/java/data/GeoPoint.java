package data;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * For handling latitude and longitude related operations.
 *
 * @implNote bounding box calculation based on
 *           https://stackoverflow.com/questions/238260/how-to-calculate-the-bounding-box-for-a-given-lat-lng-location
 */
public class GeoPoint {
  // Fixed consts
  private Double WGS84_a = 6378137.0; // Major semiaxis [m]
  private Double WGS84_b = 6356752.3; // Minor semiaxis [m]

  // Center lat and lon (user input)
  public Double centerLat;
  public Double centerLon;

  // Bounding box variables, to be calculated
  public Double minLon;
  public Double minLat;
  public Double maxLon;
  public Double maxLat;

  /**
   * Class Constructor based on known latitude and longitude
   *
   * @param centerLat the latitude of the bounding box center point
   * @param centerLon the longitude of the bounding box center point
   */
  public GeoPoint(Double centerLat, Double centerLon) {
    this.centerLat = centerLat;
    this.centerLon = centerLon;
  }

  /**
   * Class Constructor that generates center latitude and longitude based on zip
   * code with Google Maps API
   *
   * @param userZip user-input zip code
   */
  public GeoPoint(String userZip) {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest req =
        HttpRequest.newBuilder()
            .uri(URI.create(
                "https://maps.googleapis.com/maps/api/geocode/json?key=" +
                System.getenv("GOOGLE_MAPS_API_KEY") +
                "&components=postal_code:" + userZip +
                "&result_type=street_address"))
            .build();
    try {
      // Call Google Maps API
      HttpResponse<String> res =
          client.send(req, HttpResponse.BodyHandlers.ofString());
      // Extract latitude and longitude info from response
      String resStr = res.body();
      JSONParser parser = new JSONParser();
      JSONArray resArr =
          (JSONArray)((JSONObject)parser.parse(resStr)).get("results");
      JSONObject resLatLon =
          (JSONObject)((JSONObject)((JSONObject)resArr.get(0)).get("geometry"))
              .get("location");
      // Set latitude and longitude
      centerLat = Double.valueOf(resLatLon.get("lat").toString());
      centerLon = Double.valueOf(resLatLon.get("lng").toString());
    } catch (InterruptedException | ParseException e) {
      e.printStackTrace();
      System.out.println("Failed to parse JSON");
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Failed to get Google Maps API response");
    }
  }

  /**
   * Calculate bounding box for a given range in km
   *
   * @param km desired range of nearby box in kilometers
   */
  public void getBoundingBox(Double km) {
    Double lat = deg2rad(centerLat);
    Double lon = deg2rad(centerLon);
    Double halfSide = 1000 * km;
    Double radius = WGS84EarthRadius(lat);
    Double pradius = radius * Math.cos(lat);
    minLat = rad2deg(lat - halfSide / radius);
    maxLat = rad2deg(lat + halfSide / radius);
    minLon = rad2deg(lon - halfSide / pradius);
    maxLon = rad2deg(lon + halfSide / pradius);
  }

  // Degree Radian Converter
  private Double deg2rad(Double degrees) { return Math.PI * degrees / 180.0; }

  // Radian Degree Converter
  private Double rad2deg(Double radians) { return 180.0 * radians / Math.PI; }

  // Earth radius at a given latitude, according to the WGS-84 ellipsoid [m]
  private Double WGS84EarthRadius(Double lat) {
    Double An = WGS84_a * WGS84_a * Math.cos(lat);
    Double Bn = WGS84_b * WGS84_b * Math.sin(lat);
    Double Ad = WGS84_a * Math.cos(lat);
    Double Bd = WGS84_b * Math.sin(lat);
    return Math.sqrt((An * An + Bn * Bn) / (Ad * Ad + Bd * Bd));
  }
}
