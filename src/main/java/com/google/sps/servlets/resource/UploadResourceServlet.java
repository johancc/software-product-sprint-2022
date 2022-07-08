package com.google.sps.servlets.resource;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.ListValue;
import com.google.cloud.datastore.StringValue;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

/** Servlet responsible for creating new tasks. */
@WebServlet("/new-resource")
public class UploadResourceServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    // Sanitize user input to remove HTML tags and JavaScript.
    String name =
        Jsoup.clean(request.getParameter("resource-name"), Safelist.none());
    String phone =
        Jsoup.clean(request.getParameter("resource-phone"), Safelist.none());
    String email =
        Jsoup.clean(request.getParameter("resource-email"), Safelist.none());
    String url =
        Jsoup.clean(request.getParameter("resource-url"), Safelist.none());
    String zip =
        Jsoup.clean(request.getParameter("resource-zip"), Safelist.none());
    String description =
        Jsoup.clean(request.getParameter("description-input"), Safelist.none());
    String[] category = request.getParameterValues("category");
    Double lat = 0.0, lon = 0.0;

    // Generate Latitude and Longitude based on Zip Code from user
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest req =
        HttpRequest.newBuilder()
            .uri(URI.create(
                "https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyC7Xskjjq_cfnffdTfXYmyUBkcekfu3xbg&components=postal_code:" +
                zip + "&result_type=street_address"))
            .build();
    try {
      HttpResponse<String> res =
          client.send(req, HttpResponse.BodyHandlers.ofString());
      String resStr = res.body();
      JSONParser parser = new JSONParser();
      JSONArray resArr =
          (JSONArray)((JSONObject)parser.parse(resStr)).get("results");
      JSONObject resLatLon =
          (JSONObject)((JSONObject)((JSONObject)resArr.get(0)).get("geometry"))
              .get("location");
      lat = new Double(resLatLon.get("lat").toString());
      lon = new Double(resLatLon.get("lng").toString());
    } catch (InterruptedException | ParseException e) {
      response.getWriter().println("Failed to convert Zip Code");
      e.printStackTrace();
    }

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
            .set("lat", lat)
            .set("lon", lon)
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