package data;

public final class Resource {
  private final String name;
  private final String phone;
  private final String email;
  private final String url;
  private final String zip;
  private final String description;
  private final String[] category;
  private final Double lat;
  private final Double lon;

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
}
