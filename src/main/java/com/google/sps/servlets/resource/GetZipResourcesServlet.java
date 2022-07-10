package com.google.sps.servlets.resource;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StringValue;
import com.google.cloud.datastore.StructuredQuery.CompositeFilter;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.gson.Gson;
import data.GeoPoint;
import data.Resource;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@WebServlet("/get-zip-resources")
public class GetZipResourcesServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    String userZip = request.getParameter("zipCode");
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

    // Generate Latitude and Longitude based on Zip Code from user
    Double userLat = 0.0, userLon = 0.0;
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest req =
        HttpRequest.newBuilder()
            .uri(URI.create(
                "https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyC7Xskjjq_cfnffdTfXYmyUBkcekfu3xbg&components=postal_code:" +
                userZip + "&result_type=street_address"))
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
      userLat = new Double(resLatLon.get("lat").toString());
      userLon = new Double(resLatLon.get("lng").toString());
    } catch (InterruptedException | ParseException e) {
      response.getWriter().println("Failed to convert Zip Code");
      e.printStackTrace();
    }

    // Calculate bounding box using user's zip code as center
    // Currently uses 5km as a bounding range
    GeoPoint point = new GeoPoint(userLat, userLon);
    point.getBoundingBox(5.0);

    // Latitude filter query
    Query<Entity> latQuery = Query.newEntityQueryBuilder()
                                 .setKind("Resource")
                                 .setFilter(CompositeFilter.and(
                                     PropertyFilter.lt("lat", point.maxLat),
                                     PropertyFilter.gt("lat", point.minLat)))
                                 .build();
    QueryResults<Entity> latResults = datastore.run(latQuery);
    List<Resource> latFiltered = new ArrayList<>();
    while (latResults.hasNext()) {
      Entity entity = latResults.next();

      String name = entity.getString("name");
      String phone = entity.getString("phone");
      String email = entity.getString("email");
      String url = entity.getString("url");
      String zip = entity.getString("zip");
      String description = entity.getString("description");
      String[] category = handleListValue(entity.getList("category"));
      Double lat = entity.getDouble("lat");
      Double lon = entity.getDouble("lon");

      Resource newResource = new Resource(name, phone, email, url, zip,
                                          description, category, lat, lon);
      latFiltered.add(newResource);
    }

    // Longitude filter query
    Query<Entity> lonQuery = Query.newEntityQueryBuilder()
                                 .setKind("Resource")
                                 .setFilter(CompositeFilter.and(
                                     PropertyFilter.lt("lon", point.maxLon),
                                     PropertyFilter.gt("lon", point.minLon)))
                                 .build();
    QueryResults<Entity> results = datastore.run(lonQuery);
    List<Resource> lonFiltered = new ArrayList<>();
    while (results.hasNext()) {
      Entity entity = results.next();

      String name = entity.getString("name");
      String phone = entity.getString("phone");
      String email = entity.getString("email");
      String url = entity.getString("url");
      String zip = entity.getString("zip");
      String description = entity.getString("description");
      String[] category = handleListValue(entity.getList("category"));
      Double lat = entity.getDouble("lat");
      Double lon = entity.getDouble("lon");

      Resource newResource = new Resource(name, phone, email, url, zip,
                                          description, category, lat, lon);
      lonFiltered.add(newResource);
    }

    // Convert to Json and send to client
    List<Resource> finalList = latFiltered.stream()
                                   .filter(item -> !lonFiltered.contains(item))
                                   .collect(Collectors.toList());
    Gson gson = new Gson();
    response.setContentType("application/json");
    response.getWriter().println(gson.toJson(finalList));
  }

  // Helper function that converts a List of StringValue to an array of String
  static String[] handleListValue(List<StringValue> listVal) {
    String[] strArr = new String[listVal.size()];
    for (int i = 0; i < listVal.size(); i++) {
      strArr[i] = listVal.get(i).get();
    }
    return strArr;
  }
}
