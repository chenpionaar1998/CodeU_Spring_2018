<!DOCTYPE html>
<html>
<head>
  <title><%= conversation.getTitle() %></title> // change to username's profile
  <link rel="stylesheet" href="/css/main.css" type="text/css">

  <style>
    #chat {
      background-color: white;
      height: 500px;
      overflow-y: scroll
    }
  </style>

  <script>
    // scroll the chat div to the bottom
    function scrollMessages() {
      var chatDiv = document.getElementById('messages');
      chatDiv.scrollTop = chatDiv.scrollHeight;
    };
  </script>
</head>
<body onload="scrollMessages()">

  <nav>
    <a id="navTitle" href="/">CodeU Chat App</a>
    <a href="/conversations">Conversations</a>	// username's profile
      <% if (request.getSession().getAttribute("user") != null) { %>
    <a>Hello <%= request.getSession().getAttribute("user") %>!</a>
    <% } else { %>
      <a href="/login">Login</a>
      <a href="/register">Register</a>
    <% } %>
    <a href="/about.jsp">About</a>
  </nav>

</body>
</html>
