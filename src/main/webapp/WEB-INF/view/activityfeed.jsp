<%--
  Copyright 2017 Google Inc.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
--%>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.format.FormatStyle" %>
<%@ page import="java.time.ZoneId" %>
<%@ page import="codeu.model.data.Activity" %>
<%@ page import="codeu.model.data.ConversationActivity" %>
<%@ page import="codeu.model.data.NewConversationActivity" %>
<%@ page import="codeu.model.data.NewUserActivity" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="codeu.model.store.basic.ActivityStore"%>
<%
List<Message> messages = (List<Message>) request.getAttribute("messages");
%>

<!DOCTYPE html>
<html>
<head>
  <title> Activity Feed </title>
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
    function scrollChat() {
      var chatDiv = document.getElementById('chat');
      chatDiv.scrollTop = chatDiv.scrollHeight;
    };
  </script>
</head>
<body onload="scrollChat()">

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
    <a href="/activityfeed">Activity Feed</a>
  </nav>

  <div id="container">

    <h1>Activity Feed
      <a href="" style="float: right">&#8635;</a></h1>

    <hr/>

    <div id="chat">
      <ul>
    <%
      List<Activity> activities = (List<Activity>) ActivityStore.getInstance().getActivities();
      DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime
          (FormatStyle.MEDIUM, FormatStyle.SHORT).withZone(ZoneId.systemDefault());
      for (Activity activity : activities) {
        String actor = UserStore.getInstance().getUser(activity.getActorId()).getName();
    %>
      <li><strong><%= formatter.format(activity.getCreationTime()) %></strong> 
        <% if(activity instanceof ConversationActivity) { %>
          <%= actor %> sent a message to 
          <a href="/chat/<%= ((ConversationActivity) activity).getConversationTitle() %>"> 
            <%= ((ConversationActivity)activity).getConversationTitle() %></a>: 
            <%= ((ConversationActivity)activity).getMessagePreview() %>
        <% } %>
        <% if(activity instanceof NewConversationActivity) { %>
          <%= actor %> created a new conversation 
            <a href="/chat/<%= ((NewConversationActivity) activity).getConversationTitle() %>">
            <%= ((NewConversationActivity)activity).getConversationTitle() %> </a> !
        <% } %>
        <% if(activity instanceof NewUserActivity) { %>
          <%= actor %> joined!
        <% } %>

      </li>

    <%
      }
    %>
      </ul>
    </div>

    <hr/>

  </div>

</body>
</html>
