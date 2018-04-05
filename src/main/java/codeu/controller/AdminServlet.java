package codeu.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/** Servlet class responsible for the admin page. */
public class AdminServlet extends HttpServlet {

  private static final String[] permittedUser = {"adminTest"};

  private boolean contains(String[] arr, String target){
    for( String element: arr){
      if(element.equals(target))return true;
    }
    return false;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
        if (contains(permittedUser ,(String) request.getSession().getAttribute("user"))) {
          request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request,response);
        } else if (request.getSession().getAttribute("user") == null) {
          request.setAttribute("error", "Please login first!");
          request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request,response);
        } else {
          request.setAttribute("error", "This user does not have permission.");
          request.getRequestDispatcher("/index.jsp").forward(request,response);
        }
  }

}
