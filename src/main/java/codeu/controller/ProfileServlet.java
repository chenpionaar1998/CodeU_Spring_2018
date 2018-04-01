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
import java.util.Set;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class ProfileServlet extends HttpServlet{

  /** Store class that gives access to Conversations. */
  private ConversationStore conversationStore;

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
   * This function fires when a user navigates to the profile page through the link from the 
   * conversation page. It gets the user’s name from the URL, finds the corresponding data about 
   * the user (About Me, sent messages) It then forwards to profile.jsp for rendering.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String requestUrl = request.getRequestURI();
    String profileName = requestUrl.substring(requestUrl.lastIndexOf("/") + 1);
    
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
    String username = (String) request.getSession().getAttribute("user");
    if (username == null) {
      // user is not logged in, don't let them add a message
      response.sendRedirect("/login");
      return;
    }

    User user = userStore.getUser(username);
    if (user == null) {
      // user was not found, don't let them add a message
      response.sendRedirect("/login");
      return;
    }
    String requestUrl = request.getRequestURI();
    String profileName = requestUrl.substring(requestUrl.lastIndexOf("/") + 1);
    
    response.sendRedirect(profileName);
    
  }
}
