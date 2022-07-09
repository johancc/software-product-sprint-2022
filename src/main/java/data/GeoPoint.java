package data;

public class GeoPoint {
  // Fixed consts
  private Double centerLat;
  private Double centerLon;
  private Double WGS84_a = 6378137.0; // Major semiaxis [m]
  private Double WGS84_b = 6356752.3; // Minor semiaxis [m]

  // Bounding box variables, to be calculated
  public Double minLon;
  public Double minLat;
  public Double maxLon;
  public Double maxLat;

  // Constructor
  public GeoPoint(Double centerLat, Double centerLon) {
    this.centerLat = centerLat;
    this.centerLon = centerLon;
  }

  // Calculate bounding box for a given range in km
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

  /**
   * Note:
   * Implementation based on
   * https://stackoverflow.com/questions/238260/how-to-calculate-the-bounding-box-for-a-given-lat-lng-location
   */
}
