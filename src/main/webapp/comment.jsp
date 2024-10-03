<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="org.bharath.dao.PostDaoImpl"%>
<%@page import="org.bharath.dao.PostDao"%>
<%@page import="org.bharath.model.Post"%>
<%@page import="java.time.LocalDateTime"%>
<%@page import="org.bharath.MainCentralizedResource"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add Comments</title>
<%@ include file="header.jsp" %>
</head>
<body>
<div>
<h1>Add Comment</h1>
<%
	if(session.getAttribute("loggedinUser") == null){
		session.setAttribute("isNotLoggedIn", "y");
		response.sendRedirect("index.jsp");
		return;
	}
	
	int postId	= -1;
	try{
		postId = Integer.parseInt(request.getParameter("postId"));
	}catch(NumberFormatException e){
		MainCentralizedResource.LOGGER.severe(e.toString());
		session.setAttribute("somethingWentWrong", "y");
		response.sendRedirect("post.jsp");
		return;
	}
	PostDao postDaoImpl = PostDaoImpl.getInstance(MainCentralizedResource.CONNECTION);
	
	Post post = postDaoImpl.getPost(postId);
	if(post == null){
		MainCentralizedResource.LOGGER.warning("Something went wrong in commenting the post!");
        session.setAttribute("somethingWentWrong", "y");
        response.sendRedirect("post.jsp");
		return;
	}
	
%>
</div>
<div>
<h2>Post: </h2>
<table border="1" width="90%">  
<tr>
<th>Post Creation Date Time</th>
<th>User</th>
<th>Post</th>
</tr>
<tr>
<td><%=LocalDateTime.of(post.getPostedDate(), post.getPostedTime()).format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) %></td>
<td><%=post.getUserId() %></td>
<td><%=post.getPostContent() %></td>
</tr>
</table>
</div>
<div>
<form action="AddCommentServlet" method="post">
<label for="comment">Enter the comment:</label>
<input type="text" name="comment" id="comment">
<input type="hidden" name="postid" value="<%=postId %>">
<input type="submit" value="Post Comment">
</form>
</div>
</body>
</html>