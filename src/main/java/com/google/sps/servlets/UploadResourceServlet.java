package main.java.com.google.sps.servlets;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/** Servlet responsible for creating new tasks. */
@WebServlet("/new-resource")
public class UploadResourceServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    // Sanitize user input to remove HTML tags and JavaScript.
    String resourceName =
        Jsoup.clean(request.getParameter("resource-name"), Whitelist.none());
    String resourcePhone =
        Jsoup.clean(request.getParameter("resource-phone"), Whitelist.none());
    String resourceEmail =
        Jsoup.clean(request.getParameter("resource-email"), Whitelist.none());
    String resourceURL =
        Jsoup.clean(request.getParameter("resource-url"), Whitelist.none());
    String resourceZIP =
        Jsoup.clean(request.getParameter("resource-zip"), Whitelist.none());
    String description = Jsoup.clean(request.getParameter("description-input"),
                                     Whitelist.none());

    // Deal with checkboxes to avoid null pointer exception
    Boolean categoryFinances, categoryFood, categorySponsorship,
        categoryMentorship, categoryEducation;
    categoryFinances = handleCheckboxes(request, "category-finances");
    categoryFood = handleCheckboxes(request, "category-food");
    categorySponsorship = handleCheckboxes(request, "category-sponsorship");
    categoryMentorship = handleCheckboxes(request, "category-mentorship");
    categoryEducation = handleCheckboxes(request, "category-education");

    Datastore resourceDatastore =
        DatastoreOptions.getDefaultInstance().getService();
    KeyFactory keyFactory =
        resourceDatastore.newKeyFactory().setKind("Resource");
    FullEntity resourceEntity =
        Entity.newBuilder(keyFactory.newKey())
            .set("name", resourceName)
            .set("phone", resourcePhone)
            .set("email", resourceEmail)
            .set("url", resourceURL)
            .set("zip", resourceZIP)
            .set("category-finances", categoryFinances)
            .set("category-food", categoryFood)
            .set("category-sponsorship", categorySponsorship)
            .set("category-mentorship", categoryMentorship)
            .set("category-education", categoryEducation)
            .set("description", description)
            .build();
    resourceDatastore.put(resourceEntity);

    response.sendRedirect("/index.html");
  }

  static Boolean handleCheckboxes(HttpServletRequest request,
                                  String input_parameter) {
    String input = request.getParameter(input_parameter);
    if (input == null)
      return false;
    return true;
  }
}