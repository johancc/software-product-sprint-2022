package com.google.sps.servlets.resource;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.ListValue;
import com.google.cloud.datastore.StringValue;
import data.GeoPoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

/** Servlet responsible for creating new tasks. */
@WebServlet("/new-resource")
public class UploadResourceServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    // Sanitize user input to remove HTML tags and JavaScript.
    String name = Jsoup.clean(request.getParameter("name"), Safelist.none());
    String phone = Jsoup.clean(request.getParameter("phone"), Safelist.none());
    String email = Jsoup.clean(request.getParameter("email"), Safelist.none());
    String url = Jsoup.clean(request.getParameter("url"), Safelist.none());
    String zip = Jsoup.clean(request.getParameter("zip"), Safelist.none());
    String description =
        Jsoup.clean(request.getParameter("description"), Safelist.none());
    String[] category = request.getParameterValues("category");

    // Generate Latitude and Longitude based on Zip Code from user
    GeoPoint point = new GeoPoint(zip);

    // Store resource in Datastore
    Datastore resourceDatastore =
        DatastoreOptions.getDefaultInstance().getService();
    KeyFactory keyFactory =
        resourceDatastore.newKeyFactory().setKind("Resource");
    FullEntity resourceEntity =
        Entity.newBuilder(keyFactory.newKey())
            .set("name", name)
            .set("phone", phone)
            .set("email", email)
            .set("url", url)
            .set("zip", zip)
            .set("category", ListValue.of(handleArray(category)))
            .set("description", description)
            .set("lat", point.centerLat)
            .set("lon", point.centerLon)
            .build();
    resourceDatastore.put(resourceEntity);

    response.sendRedirect("/index.html");
  }

  // Helper function that converts an array of strings into a list of
  // StringValue
  static List<StringValue> handleArray(String[] arr) {
    List<StringValue> list = new ArrayList<>();
    for (String str : arr) {
      list.add(StringValue.of(str));
    }
    return list;
  }
}