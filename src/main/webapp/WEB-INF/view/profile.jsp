<%
String aboutUser = (String) request.getAttribute("aboutUser");
String profileUser = (String) request.getAttribute("profileUser");
%>
<!DOCTYPE html>
<html>
<head>
  <title><%= profileUser %>'s Profile Page</title>
  <link rel="stylesheet" href="/css/main.css" type="text/css">
  <hr/>
</head>
<body>
  <nav>
    <a id="navTitle" href="/">CodeU Chat App</a>
    <a href="/conversations">Conversations</a>
      <% if (request.getSession().getAttribute("user") != null) { %>
    <a>Hello <%= request.getSession().getAttribute("user") %>!</a>
    <% } else { %>
      <a href="/login">Login</a>
      <a href="/register">Register</a>
    <% } %>
    <a href="/about.jsp">About</a>
  </nav>

  <div id="container">
    style="width:75%; margin-left:auto; margin-right:auto; margin-top: 50px;">

    <h1>About <%= profilePage %></h1>
    <p><%= aboutUser %></p>
      
	<% if(request.getSession().getAttribute("currentUser") == profileUser){ %>
	  <h1>Edit Your About Me (only you can see this)
	  	<a href="" style="float: right">&#8635;</a></h1>
	  	
	  <form action="/profile" method="POST">
	      <input type="text" name="conversationTitle">
	      <button type="submit">Submit</button>
	  </form>
	      
	  <hr/>
	<% } %>
	
	<h1><%= profileUser %>'s Sent Messages</h1>

    <%
    Queue<Message> profileMessages = 
      (Queue<Message>) profileUser.getMessages();
    if(profileMessages == null || profileMessages.isEmpty()){
    %>
      <p><%= profileUser %> has not sent any messages</p>
    <%
    }
    else{
    %>
      <ul class="mdl-list">
    <%
      for(Message message: profileMessages){
    %>
      <li><strong><%= message.getCreationTime() %>:</strong> <%= message.getContent() %></li>
    <%
      }
    %>
      </ul>
    <%
    }
    %>
	<hr/>
	
  </div>
</body>
</html>
