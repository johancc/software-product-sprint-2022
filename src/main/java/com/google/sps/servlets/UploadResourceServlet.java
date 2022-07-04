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
    // String resourceName = Jsoup.clean(request.getParameter("resource-name"),
    // Whitelist.none()); String resourcePhone =
    // Jsoup.clean(request.getParameter("resource-phone"), Whitelist.none());
    // String resourceEmail =
    // Jsoup.clean(request.getParameter("resource-email"), Whitelist.none());
    // String resourceURL = Jsoup.clean(request.getParameter("resource-url"),
    // Whitelist.none()); String resourceZIP =
    // Jsoup.clean(request.getParameter("resource-zip"), Whitelist.none());
    // String categoryFinances =
    // Jsoup.clean(request.getParameter("category-finances"), Whitelist.none());
    // String categoryFood = Jsoup.clean(request.getParameter("category-food"),
    // Whitelist.none()); String categorySponsorship =
    // Jsoup.clean(request.getParameter("category-sponsorship"),
    // Whitelist.none()); String categoryMentorship =
    // Jsoup.clean(request.getParameter("category-mentorship"),
    // Whitelist.none()); String categoryEducation =
    // Jsoup.clean(request.getParameter("category-education"),
    // Whitelist.none()); String description =
    // Jsoup.clean(request.getParameter("description-input"), Whitelist.none());
    String resourceName = request.getParameter("resource-name");
    String resourcePhone = request.getParameter("resource-phone");
    String resourceEmail = request.getParameter("resource-email");
    String resourceURL = request.getParameter("resource-url");
    String resourceZIP = request.getParameter("resource-zip");
    String categoryFinances = request.getParameter("category-finances");
    String categoryFood = request.getParameter("category-food");
    String categorySponsorship = request.getParameter("category-sponsorship");
    String categoryMentorship = request.getParameter("category-mentorship");
    String categoryEducation = request.getParameter("category-education");
    String description = request.getParameter("description-input");

    // Print all the values:
    // response.getWriter().println("Resource Phone: " + resourcePhone);
    // response.getWriter().println("Resource Email: " + resourceEmail);
    // response.getWriter().println("Resource URL: " + resourceURL);
    // response.getWriter().println("Resource ZIP code: " + resourceZIP);
    // response.getWriter().println("Finances: " + categoryFinances);
    // response.getWriter().println("Food: " + categoryFood);
    // response.getWriter().println("Sponsorship: " + categorySponsorship);
    // response.getWriter().println("Mentorship: " + categoryMentorship);
    // response.getWriter().println("Education: " + categoryEducation);
    response.getWriter().println("Resource Name: " + resourceName);
    response.getWriter().println("Resource Phone: " + resourcePhone);
    response.getWriter().println("Resource Email: " + resourceEmail);
    response.getWriter().println("Resource URL: " + resourceURL);
    response.getWriter().println("Resource ZIP code: " + resourceZIP);
    response.getWriter().println("Finances: " + categoryFinances);
    response.getWriter().println("Food: " + categoryFood);
    response.getWriter().println("Sponsorship: " + categorySponsorship);
    response.getWriter().println("Mentorship: " + categoryMentorship);
    response.getWriter().println("Education: " + categoryEducation);
    response.getWriter().println("Description: " + description);

    // Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    // KeyFactory keyFactory = datastore.newKeyFactory().setKind("Task");
    // FullEntity taskEntity = Entity.newBuilder(keyFactory.newKey())
    //                             .set("title", title)
    //                             .set("timestamp", timestamp)
    //                             .build();
    // datastore.put(taskEntity);

    // response.sendRedirect("/index.html");
  }
}