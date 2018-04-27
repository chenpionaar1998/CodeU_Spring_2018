package codeu.controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class AdminServletTest {

  private AdminServlet adminServlet;
  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;
  private HttpSession mockSession;


  @Before
  public void setup() throws IOException {
    adminServlet = new AdminServlet();
    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockSession = Mockito.mock(HttpSession.class);
    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);
  }

  @Test
  public void testDoGetAdmin() throws IOException, ServletException {
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/admin.jsp")).thenReturn(mockRequestDispatcher);
    Mockito.when(mockSession.getAttribute("user")).thenReturn("adminTest");
    Mockito.when(mockSession.getAttribute("admin")).thenReturn(true);
    adminServlet.doGet(mockRequest,mockResponse);
    Mockito.verify(mockRequestDispatcher).forward(mockRequest,mockResponse);
  }

  @Test
  public void testDoGetNotLoggedIn() throws IOException, ServletException {
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/login.jsp")).thenReturn(mockRequestDispatcher);
    Mockito.when(mockSession.getAttribute("user")).thenReturn(null);
    adminServlet.doGet(mockRequest,mockResponse);
    Mockito.verify(mockRequestDispatcher).forward(mockRequest,mockResponse);
    Mockito.verify(mockRequest).setAttribute("error", "Please login first!");
  }

  @Test
  public void testDoGetNoPermission() throws IOException, ServletException {
    Mockito.when(mockRequest.getRequestDispatcher("/index.jsp")).thenReturn(mockRequestDispatcher);
    Mockito.when(mockSession.getAttribute("user")).thenReturn("randomUser");
    Mockito.when(mockSession.getAttribute("admin")).thenReturn(false);
    adminServlet.doGet(mockRequest,mockResponse);
    Mockito.verify(mockRequestDispatcher).forward(mockRequest,mockResponse);
    Mockito.verify(mockRequest).setAttribute("error", "This user does not have permission.");
  }
}
