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
<!DOCTYPE html>
<html>
<head>
  <title>CodeU Chat App</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <nav>
    <a id="navTitle" href="/">CodeU Chat App</a>
    <a href="/conversations">Conversations</a>
    <% if(request.getSession().getAttribute("user") != null){ %>
      <a>Hello <%= request.getSession().getAttribute("user") %>!</a>
    <% } else{ %>
      <a href="/login">Login</a>
      <a href="/register">Register</a>
    <% } %>
    <a href="/about.jsp">About</a>
    <a href="/activityfeed">Activity Feed</a>
  </nav>

  <div id="container">
    <div
      style="width:75%; margin-left:auto; margin-right:auto; margin-top: 50px;">

      <h1>About Team 21</h1>
      <p>
        Team 21 is a team of 3 university students from North America with the
        same passion of learning new skills and creating interesting apps. Here
        is a list of the team members:
      </p>
      <ul>
        <li><strong>Heng Sun (PA)</strong> Our supereme and righteous leader(PA).</li>
        <li><strong>Dean Chiu</strong></li>
        <li><strong>Sherry Yang</strong> Sherry is a sophomore studying at 
          University of Washington,Seattle (Yes, she craves for the sunlight). 
          So far, she has been gaining backend skills and is hoping for getting 
          involved in the "frontend community." One interesting fact about her 
          is that she has been (too) into the Kpop band BTS since college, it is 
          sometimes unhealthy to watch countless Youtube vidoes about them ^o^.</li>
        <li><strong>Naomi McCracken</strong> Naomi is a third year Mathematics 
          and Computer Science (undergraduate) student at University of 
          California San Diego. Naomi is especially interested in working in 
          backend development, and she especially enjoys learning about about 
          algorithms and data structures. Her favorite sorting algorithm is 
          <a href="https://en.wikipedia.org/wiki/Bogosort">Bogosort</a>, 
          particularly the version that produces permutations of input until 
          the sorted version is produced. Naomi is excited to be a part of 
          the CodeU program and learn a lot alongside her teammates.</li>
      </ul>
      
      <h1>About the CodeU Chat App</h1>
      <p>
        This is an example chat application designed to be a starting point
        for your CodeU project team work. Here's some stuff to think about:
      </p>

      <ul>
        <li><strong>Algorithms and data structures:</strong> We've made the app
          and the code as simple as possible. You will have to extend the
          existing data structures to support your enhancements to the app,
          and also make changes for performance and scalability as your app
          increases in complexity.</li>
        <li><strong>Look and feel:</strong> The focus of CodeU is on the Java
          side of things, but if you're particularly interested you might use
          HTML, CSS, and JavaScript to make the chat app prettier.</li>
        <li><strong>Customization:</strong> Think about a group you care about.
          What needs do they have? How could you help? Think about technical
          requirements, privacy concerns, and accessibility and
          internationalization.</li>
      </ul>

      <p>
        This is your code now. Get familiar with it and get comfortable
        working with your team to plan and make changes. Start by updating the
        homepage and this about page to tell your users more about your team.
        This page should also be used to describe the features and improvements
        you've added.
      </p>
    </div>
  </div>
</body>
</html>
