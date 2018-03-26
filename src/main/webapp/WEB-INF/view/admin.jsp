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


</body>


</html>
