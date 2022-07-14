package data;

public final class Resource {
  private final String name;
  private String phone;
  private String email;
  private String url;
  private String zip;
  private String description;
  private String[] category;
  private Double lat;
  private Double lon;

  // Constructor
  public Resource(String name, String phone, String email, String url,
                  String zip, String description, String[] category, Double lat,
                  Double lon) {
    this.name = name;
    this.phone = phone;
    this.email = email;
    this.url = url;
    this.zip = zip;
    this.description = description;
    this.category = category;
    this.lat = lat;
    this.lon = lon;
  }

  // Constructor without lat lon
  public Resource(String name, String phone, String email, String url,
                  String zip, String description, String[] category) {
    this.name = name;
    this.phone = phone;
    this.email = email;
    this.url = url;
    this.zip = zip;
    this.description = description;
    this.category = category;
    this.lat = 0.0;
    this.lon = 0.0;
  }

  // Aggregated Resource Constructore
  public Resource(String name, Double lat, Double lon) {
    this.name = name;
    this.lat = lat;
    this.lon = lon;
  }
}
