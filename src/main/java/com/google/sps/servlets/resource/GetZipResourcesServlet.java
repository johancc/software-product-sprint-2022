package com.google.sps.servlets.resource;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StringValue;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.gson.Gson;
import data.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/get-zip-resources")
public class GetZipResourcesServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    String userZip = request.getParameter("zipCode");
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    Query<Entity> query = Query.newEntityQueryBuilder()
                              .setKind("Resource")
                              .setFilter(PropertyFilter.eq("zip", userZip))
                              .build();
    QueryResults<Entity> results = datastore.run(query);
    List<Resource> resources = new ArrayList<>();
    while (results.hasNext()) {
      Entity entity = results.next();

      String name = entity.getString("name");
      String phone = entity.getString("phone");
      String email = entity.getString("email");
      String url = entity.getString("url");
      String zip = entity.getString("zip");
      String description = entity.getString("description");
      String[] category = handleListValue(entity.getList("category"));

      Resource newResource =
          new Resource(name, phone, email, url, zip, description, category);
      resources.add(newResource);
    }
    Gson gson = new Gson();
    response.setContentType("application/json");
    response.getWriter().println(gson.toJson(resources));
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
