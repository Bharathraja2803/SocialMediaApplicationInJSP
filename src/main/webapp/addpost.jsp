<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Post Addition</title>
<%@ include file="header.jsp" %>
</head>
<body>

<%
	if(session.getAttribute("loggedinUser") == null){
		session.setAttribute("isNotLoggedIn", "y");
		response.sendRedirect("index.jsp");
		return;
	}

	if(session.getAttribute("isProblemInPostAddition") != null){
		out.println("<h3>Something went wrong in post creation, try again!</h3>");
		session.removeAttribute("isProblemInPostAddition");
	}
%>
<h1>Add post</h1>
<div>
<form action="PostCreationServlet" method="post">
<label for="postContent">Post Content: </label>
<input type="text" name="postContent" id="postContent">
<input type="submit" value="post">
</form>
</div>
</body>
</html>