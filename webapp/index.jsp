<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
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
  
  <form name="courses" action="courses.jsp" method="post" style="width:30%">
	<div class="form-group">
	  	<label for="coursesInput">Courses Taken:</label>
      <input type="text" class="form-control" name="coursesInput" placeholder="Enter courses taken so far">
    </div>
	<button class="btn btn-success" type="submit">Submit</button>
  </form>
  
  </center>
  </body>
</html>
