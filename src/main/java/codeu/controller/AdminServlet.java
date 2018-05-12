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

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
        if (request.getSession().getAttribute("user") == null){
          request.setAttribute("error", "Please login first!");
          request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request,response);
        } else {
          if ((boolean) request.getSession().getAttribute("admin")) {
            request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request,response);
          } else {
            request.setAttribute("error", "This user does not have permission.");
            request.getRequestDispatcher("/index.jsp").forward(request,response);
          }
        }
  }

}
