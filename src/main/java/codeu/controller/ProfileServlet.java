package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class ProfileServlet extends HttpServlet{

  /** Store class that gives access to Messages. */
  private MessageStore messageStore;

  /** Store class that gives access to Users. */
  private UserStore userStore;

  /** Set up state for handling chat requests. */
  @Override
  public void init() throws ServletException {
    super.init();
    setMessageStore(MessageStore.getInstance());
    setUserStore(UserStore.getInstance());
  }

  /**
   * Sets the MessageStore used by this servlet. This function provides a common setup method for
   * use by the test framework or the servlet's init() function.
   */
  void setMessageStore(MessageStore messageStore) {
    this.messageStore = messageStore;
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  /**
   * This function fires when a user navigates to the profile URL. It gets the user’s name from the 
   * URL, finds the corresponding data about the user (About Me, sent messages) It then forwards to 
   * profile.jsp for rendering.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String requestUrl = request.getRequestURI();
    String profilePage = requestUrl.substring("/profile/".length());
    User profileUser = userStore.getUser(profilePage);
    
    if (profileUser == null) {
      // couldn't find user, redirect to empty profile page
      request.setAttribute("error", "Profile user not found");
      response.sendRedirect("/profile");
      return;
    }
    
    String aboutUser = profileUser.getAbout();
    request.setAttribute("profileUser", profileUser);
    request.setAttribute("aboutUser", aboutUser);
    request.getRequestDispatcher("/WEB-INF/view/profile.jsp").forward(request, response);
  }
  
  /**
   * This function fires when a user submits the form on the profile page. It gets the logged-in 
   * username from the session, the About Me information from the clicked URL, allows the user to 
   * edit About me if the profile corresponds to the current user, and the sent message from the 
   * submitted form data.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String currentUser = (String) request.getSession().getAttribute("user");
    if (currentUser == null) {
      // user is not logged in, let them log in first to view any profile page
      request.setAttribute("error", "Please login first.");
      request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
      return;
    }

    if (userStore.getUser(currentUser) == null) {
      // user was not found, don't let them view the profile page
      request.setAttribute("error", "User not found");
      request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
      return;
    }
    
    String requestUrl = request.getRequestURI();
    String profilePage = requestUrl.substring("/profile/".length());
    User profileUser = userStore.getUser(profilePage);
    String aboutUser = request.getParameter("aboutUser");
    
    if (currentUser.equals(profilePage)) {
       // the user viewing the page is the owner of the profile, allows edition
      String cleanedAbout = Jsoup.clean(aboutUser, Whitelist.none());
      profileUser.setAbout(cleanedAbout);
    }

    response.sendRedirect(requestUrl);
  }
}
