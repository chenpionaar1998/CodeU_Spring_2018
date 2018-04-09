<%
String aboutUser = (String) request.getAttribute("aboutUser");
String profilePage = (String) request.getAttribute("profilePage");
%>
<!DOCTYPE html>
<html>
<head>
  <title><%= profilePage %>'s Profile Page</title>	// correct format of user's profile
  <link rel="stylesheet" href="/css/main.css" type="text/css">
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
      
	<% if(request.getSession().getAttribute("currentUser") == profilePage){ %>
	  <h1>Edit Your About Me (only you can see this)</h1>
	  // TODO: This edit box might not be big enough
	  <form action="/profile" method="POST">
	      <input type="text" name="conversationTitle">
	      <button type="submit">Submit</button>
	  </form>
	      
	  <hr/>
	<% } %>
	
	<h1><%= profilePage %>'s Sent Messages</h1>

    <%
    Queue<Message> profileMessages = 
      (Queue<Message>) request.getAttribute("profileMessages");
    if(profileMessages == null || profileMessages.isEmpty()){
    %>
      <p><%= profilePage %> has not sent any messages</p>
    <%
    }
    else{
    %>
      // TODO: this might not be correct
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

	// TODO: add scroll botton to messages
  </div>
</body>
</html>
