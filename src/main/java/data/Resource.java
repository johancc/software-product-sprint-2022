package data;

import com.google.cloud.datastore.ListValue;

public final class Resource {
  private final String name;
  private final String phone;
  private final String email;
  private final String url;
  private final String zip;
  private final String description;
  private final ListValue category;

  public Resource(String name, String phone, String email, String url,
                  String zip, String description, ListValue category) {
    this.name = name;
    this.phone = phone;
    this.email = email;
    this.url = url;
    this.zip = zip;
    this.description = description;
    this.category = category;
  }
}
