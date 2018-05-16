package codeu.controller;

import codeu.model.store.basic.ActivityStore;
import codeu.model.store.basic.ActivityStore;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet class responsible for activity feed page
 */
public class ActivityServlet extends HttpServlet {

  /* store class that gives access to activities */
  private ActivityStore activityStore;

  /* set up state for handling activity feed requests */
  public void init() {
    setActivityStore(ActivityStore.getInstance());
  }

  /* Function fires when user navigates to the page. The function sets the 
   * request's activities attribute to the list of acitivities
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    request.setAttribute("activities", activityStore.getActivitiesCopy());
    request.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp").forward(request, response);
  }

  public void setActivityStore(ActivityStore activityStore) {
    this.activityStore = activityStore;
  }
}
