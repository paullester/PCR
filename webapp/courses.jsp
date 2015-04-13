<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="PennCourseRecommender.Wrapper" %>
<jsp:useBean id="link" scope="application" class="PennCourseRecommender.Wrapper" />
<jsp:setProperty name="link" property="*" />

<%
String courses = request.getParameter( "coursesInput" );
String blargh = PennCourseRecommender.Wrapper.apiCall(courses);
%>

<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Penn Course Recommender</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap-theme.min.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.0/jquery.min.js" type="text/javascript"></script>
    </script>
  </head>
  
  <%@ include file="overlay.jsp" %>

  <body>
  <center>
  <div class="page-header">
    <h1 class="title">Penn Course Recommender</h1>
  </div>
  <p>This makes super shitty course recommendations</p>
  <p>for Penn students only</p>
  <p>courtesy of BJPS</p>
  
<%
out.println("<br><b>Welcome, " + blargh + "!");
%>

  </center>
  </body>
</html>
