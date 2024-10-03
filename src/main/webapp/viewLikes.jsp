<%@page import="org.bharath.model.Like"%>
<%@page import="java.util.List"%>
<%@page import="org.bharath.dao.LikeDaoImpl"%>
<%@page import="org.bharath.dao.LikeDao"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.time.LocalDateTime"%>
<%@page import="org.bharath.model.Post"%>
<%@page import="org.bharath.dao.PostDaoImpl"%>
<%@page import="org.bharath.dao.PostDao"%>
<%@page import="org.bharath.MainCentralizedResource"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>View Likes</title>
<%@ include file="header.jsp" %>
</head>
<body>
<div>
<h1>View Likes</h1>
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
		MainCentralizedResource.LOGGER.warning("Something went wrong in feting the post to view likes!");
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
<h2>Likes</h2>
<table border="1" width="90%">  
<tr>
<th>Like Date Time</th>
<th>User</th>
</tr>
<%
	LikeDao likeDaoImpl = LikeDaoImpl.getInstance(MainCentralizedResource.CONNECTION);
	List<Like> likesList = likeDaoImpl.getAllLikesForThePost(postId);
	if(likesList == null){
		MainCentralizedResource.LOGGER.warning("No likes for this post!");
	    session.setAttribute("noLikesForThePost", "y");
	    response.sendRedirect("post.jsp");
		return;
	}
	
	for(Like likeOneByOne: likesList){
		out.println(
				"<tr>"
				+"<td>"+LocalDateTime.of(likeOneByOne.getLikeDate(), likeOneByOne.getLikeTime()).format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))+"</td>"
				+"<td>"+likeOneByOne.getUserId()+"</td>"
				+"</tr>"
				);
	}
	
%>
</table>
</div>
</body>
</html>