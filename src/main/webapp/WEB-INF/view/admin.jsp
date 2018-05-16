<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.io.File" %>
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
  <link rel="stylesheet" href="/css/wordCloud.css">
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
    <a href="/about.jsp">About</a>
    <a href="/testdata">Load Test Data</a>
    <a href="/searchResult">Search</a>
    <a>Hello <%=request.getSession().getAttribute("user")%> !</a>
  </nav>

  <%
    int userCount = UserStore.getInstance().getUserCount();
    int messageCount = MessageStore.getInstance().getMessageCount();
    int conversationCount = ConversationStore.getInstance().getConversationCount();
    String topUser = UserStore.getInstance().getTopUser();
    List<String> userList = new ArrayList<>();
    userList = UserStore.getInstance().makeJSONString();
  %>
  <div style="display:none;" id="JSONList"><%= userList %></div>

  <div id="container">
    <h1>Administration</h1>
    <h3>Site Statistics</h3>
    <ul>
      <li id="userCount">Users: <%= userCount %> </li>
      <li id="conversationCount">Conversations: <%= conversationCount %> </li>
      <li id="messageCount">Messages: <%= messageCount %></li>
      <li id="topUser">Top User: <%= topUser %> </li>
    </ul>

    <form action="/admin" method="GET">
      <button type="submit">Refresh data</button>
    </form>
  </div>
  <div id="wordCloud"></div>

  <%-- use d3 library for the word cloud, treat d3.layout.cloud.js as a blackbox, implementations for the actual word cloud is in wordCLoud.js--%>
  <script type="text/javascript" src="/js/d3.v3.min.js"></script>
  <script type="text/javascript" src="/js/d3.layout.cloud.js" ></script>
  <script type="text/javascript" src="/js/wordCloud.js" ></script>
</body>


</html>
