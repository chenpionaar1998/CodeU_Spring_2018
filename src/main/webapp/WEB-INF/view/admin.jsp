<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="codeu.model.store.basic.MessageStore" %>
<%@ page import="codeu.model.store.basic.ConversationStore" %>

<!DOCTYPE html>

<html>
<head>
  <title>Administration</title>
  <link rel="stylesheet" href="/css/main.css">
    <style>
      label {
        display: inline-block;
        width: 100px;
      }
    </style>
</head>

<body>

  <nav>
    <a id="navTitle" href="/">CodeU Chat App</a>
    <a href="/conversations">Conversation</a>
    <% if (request.getSession().getAttribute("user") != null){ %>
      <a>Hello <%=request.getSession().getAttribute("user")%> !</a>
    <% }else{%>
      <a href="login">Login</a>
      <a href="/register">Register</a>
    <% }%>
  </nav>

  <%
    int userCount = UserStore.getInstance().getUsersCount();
    int messageCount = MessageStore.getInstance().getMessageCount();
    int conversationCount = ConversationStore.getInstance().getConversationCount();
  %>

  <div id="container">
    <h1>Administration</h1>
    <h3>Site Statistics</h3>
    <ul>
      <li>Users: <%= userCount %> </li>
      <li>Conversations: <%= conversationCount %> </li>
      <li>Messages: <%= messageCount %></li>
    </ul>

    <button>Refresh data</button>

  </div>

</body>


</html>
