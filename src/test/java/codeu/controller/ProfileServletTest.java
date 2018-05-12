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
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
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

public class ProfileServletTest {

  private ProfileServlet profileServlet;
  private HttpServletRequest mockRequest;
  private HttpSession mockSession;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;
  private MessageStore mockMessageStore;
  private UserStore mockUserStore;

  @Before
  public void setup() {
    profileServlet = new ProfileServlet();

    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockSession = Mockito.mock(HttpSession.class);
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/profile.jsp"))
        .thenReturn(mockRequestDispatcher);

    mockMessageStore = Mockito.mock(MessageStore.class);
    profileServlet.setMessageStore(mockMessageStore);

    mockUserStore = Mockito.mock(UserStore.class);
    profileServlet.setUserStore(mockUserStore);
  }

  @Test
  public void testDoGet() throws IOException, ServletException {
    Mockito.when(mockRequest.getRequestURI()).thenReturn("/profile/test_profileUser");
    
    Queue<Message> testMessages = new LinkedList<Message>();
    addSomeMessages(testMessages);
    String testAbout = "This is just a sample About Me";
    
    UserStore mockUserStore = Mockito.mock(UserStore.class);
    Mockito.when(mockUserStore.isUserRegistered("test_profileUser")).thenReturn(true);
    
    User mockUser = Mockito.mock(User.class);
    Mockito.when(mockUserStore.getUser("test_profileUser")).thenReturn(mockUser);
    Mockito.when(mockUser.getName()).thenReturn("test_profileUser");
    Mockito.when(mockUser.getAbout()).thenReturn(testAbout);
    Mockito.when(mockUser.getMessages()).thenReturn(testMessages);
    
    profileServlet.doGet(mockRequest, mockResponse);
    
    // Mockito.verify(mockRequest).setAttribute("profileUser", "test_profileUser");
    // Mockito.verify(mockRequest).setAttribute("aboutUser", testAbout);
    // Mockito.verify(mockRequest).setAttribute("profileMessages", testMessages);
    // Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }
  
  private void addSomeMessages(Queue<Message> testMessages) {
	  for (int i = 0; i < 16; i++) {
	        testMessages.add(new Message(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
	        					"test message " + i, Instant.now()));
	    }
  }
  
  @Test
  public void testDoPost_SameUser() throws IOException, ServletException {
    Mockito.when(mockRequest.getRequestURI()).thenReturn("/profile/test_profileUser");
    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_profileUser");

    User testUser = new User(UUID.randomUUID(), "test_profileUser", "password", Instant.now());
    Mockito.when(mockUserStore.getUser("test_profileUser")).thenReturn(testUser);
    
    String aboutUser = "This is just a sample About Me";
    Mockito.when(mockRequest.getParameter("aboutUser")).thenReturn(aboutUser);
    
    for (int i = 0; i < 16; i++) {
        testUser.addMessage(new Message(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
        					"test message " + i, Instant.now()));
    }
    profileServlet.doPost(mockRequest, mockResponse);
    
//    ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
//    Assert.assertEquals(userArgumentCaptor.getValue().getName(), "test_profileUser");
//    Assert.assertEquals(userArgumentCaptor.getValue().getAbout(), "This is just a sample About Me");    
    Mockito.verify(mockResponse).sendRedirect("/profile/test_profileUser");
  }
  
  @Test
  public void testDoPost_DifferentUSser() throws IOException, ServletException {
	Mockito.when(mockRequest.getRequestURI()).thenReturn("/profile/test_profileUser");
    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_otherUser");

    User testUser = new User(UUID.randomUUID(), "test_otherUser", "password", Instant.now());
    Mockito.when(mockUserStore.getUser("test_otherUser")).thenReturn(testUser);
    
    String aboutUser = "This is just a sample About Me";
    Mockito.when(mockRequest.getParameter("aboutUser")).thenReturn(aboutUser);
    
    for (int i = 0; i < 16; i++) {
        testUser.addMessage(new Message(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
        					"test message " + i, Instant.now()));
    }

    profileServlet.doPost(mockRequest, mockResponse);
    
//    ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
//    Assert.assertEquals(userArgumentCaptor.getValue().getName(), "test_profileUser");
//    Assert.assertEquals(userArgumentCaptor.getValue().getAbout(), "This is just a sample About Me");
//    // TODO: verify the no empty edit box if present for About Me

    Mockito.verify(mockResponse).sendRedirect("/profile/test_profileUser");
  }
}

