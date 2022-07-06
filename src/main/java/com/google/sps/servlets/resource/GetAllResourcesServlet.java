package com.google.sps.servlets.resource;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.ListValue;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StringValue;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.gson.Gson;
import data.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/get-all-resources")
public class GetAllResourcesServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    Query<Entity> query =
        Query.newEntityQueryBuilder().setKind("Resource").build();
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
      List<StringValue> category = entity.getList("category");

      Message newMessage = new Message(id, email, message, drink, timestamp);
      messages.add(newMessage);
    }
  }
}
