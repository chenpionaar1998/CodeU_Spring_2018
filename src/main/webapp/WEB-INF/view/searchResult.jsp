<%@ page import="codeu.model.store.basic.IndexStore" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.UUID" %>
<%@ page import="java.time.Instant" %>


<!DOCTYPE html>

<html>
<head>
  <title>Result</title>
  <link rel="stylesheet" href="/css/wordCloud.css">
    <style>
      label {
        display: inline-block;
        width: 100px;
      }

      .search-container {
        position: relative;
        left: 10vw;
        top: 5vh;
      }

      .search-container input[type=text] {
        padding: 6px;
        margin-top: 8px;
        font-size: 17px;
        border: none;
      }

      .search-container button {
        padding: 6px 10px;
        margin-top: 8px;
        margin-right: 16px;
        background: #ddd;
        font-size: 17px;
        border: none;
        cursor: pointer;
      }

      .search-container button:hover {
        background: #ccc;
      }
    </style>
</head>

<body>

  <nav>
    <a id="navTitle" href="/">CodeU Chat App</a>
    <a href="/conversations">Conversation</a>
    <a href="/about.jsp">About</a>
    <% if(request.getSession().getAttribute("user") != null){ %>
      <a href="/admin">Admin Page</a>
      <a>Hello <%= request.getSession().getAttribute("user") %>!</a>
    <% } else{ %>
      <a href="/login">Login</a>
      <a href="/register">Register</a>
    <% } %>
    <a href="/about.jsp">About</a>
    <a href="/activity_feed">Activity Feed</a>
  </nav>

  <div class="search-container">
    <%-- TODO: fix this to the actual page found after backend search --%>
    <form action="/searchResult" method="GET">
      <input type="text" placeholder="Search.." name="search">
      <button type="submit"><i class="fa fa-search">search</i></button>
    </form>
  </div>

  <div id="result">
    <% if (request.getParameter("search") != null){ %>
      <h1 class="search-container"> Result </h1>
    <%
      String targetString = request.getParameter("search");
      Set<Message> resultSet = IndexStore.getInstance().searchWord(targetString);
      if (resultSet != null){
        for (Message message: resultSet){
          String time = message.getCreationTime().toString();
          time = time.replace("T"," ");
          time = time.replace("Z"," ");
          time = time.substring(0,19);
          UUID authorId = message.getAuthorId();
          User user = UserStore.getInstance().getUser(authorId);
          String username = user.getName();
    %>
          <h3 class="search-container" > <%=time%> <%=username%> :<%=message.getContent()%></h3>
    <%
        }
      } else {
    %>
        <h3 class="search-container">Word not found</h3>
    <%
      }
    } %>
  </div>
</body>

</html>
