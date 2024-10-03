<%@page import="org.bharath.model.Comment"%>
<%@page import="org.bharath.dao.CommentDaoImpl"%>
<%@page import="org.bharath.dao.CommentDao"%>
<%@page import="org.bharath.model.Like"%>
<%@page import="org.bharath.MainCentralizedResource"%>
<%@page import="org.bharath.dao.LikeDaoImpl"%>
<%@page import="org.bharath.dao.LikeDao"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.time.LocalDateTime"%>
<%@page import="org.bharath.model.Post"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="org.bharath.model.Users" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Post page</title>
<%@ include file="header.jsp" %>
</head>
<body>
<%
	if(session.getAttribute("loggedinUser") == null){
		session.setAttribute("isNotLoggedIn", "y");
		response.sendRedirect("index.jsp");
		return;
	}

	if(session.getAttribute("isPostCreationSuccessful") != null){
		out.println("<h3>Post created successfully!</h3>");
		session.removeAttribute("isPostCreationSuccessful");
	}
	
	if(session.getAttribute("isThereProblemInFetchingAllUsers") != null){
		out.println("<h3>Something went wrong! try logout and login again</h3>");
		session.removeAttribute("isThereProblemInFetchingAllUsers");
	}
	
	if(session.getAttribute("noPostByAllUsers") != null){
		out.println("<h3>No one posted anything</h3>");
		session.removeAttribute("noPostByAllUsers");
	}
	
	if(session.getAttribute("isThereProblemInFetchingFollower") != null){
		out.println("<h3>You have followed no one yet</h3>");
		session.removeAttribute("isThereProblemInFetchingFollower");
	}
	
	if(session.getAttribute("somethingWentWrong") != null){
		out.println("<h3>Something went wrong! Try Logout and Login again</h3>");
		session.removeAttribute("somethingWentWrong");
	}
	
	if(session.getAttribute("likeActionSuccessful") != null){
		out.println("<h3>You have liked the post</h3>");
		session.removeAttribute("likeActionSuccessful");
	}
	
	if(session.getAttribute("unlikeCompleted") != null){
		out.println("<h3>You have unliked the post</h3>");
		session.removeAttribute("unlikeCompleted");
	}
	
	if(session.getAttribute("isCommentingThePostSuccessful") != null){
		out.println("<h3>You have Commented the post</h3>");
		session.removeAttribute("isCommentingThePostSuccessful");
	}
	
	if(session.getAttribute("noLikesForThePost") != null){
		out.println("<h3>No Likes for the post</h3>");
		session.removeAttribute("noLikesForThePost");
	}
	
	if(session.getAttribute("noCommentsForThePost") != null){
		out.println("<h3>No Comments for the post</h3>");
		session.removeAttribute("noCommentsForThePost");
	}
	
	if(session.getAttribute("postDeletedSuccessfully") != null){
		out.println("<h3>Post Was deleted Successfully</h3>");
		session.removeAttribute("postDeletedSuccessfully");
	}
	
%>

<div>
<h1>Welcome <%=((Users)session.getAttribute("loggedinUser")).getUserName_()%></h1>
</div>
<div>
<h2><a href="addpost.jsp">Create post</a></h2>
</div>
<div>
<%
	List<Post> allPostToView = (List<Post>)session.getAttribute("postsToView");
	if(allPostToView == null || allPostToView.isEmpty()){
		out.println("<h3>No post in your queue. Follow people to get post</h3>");
		
	}else{

%>
<table border="1" width="90%">  
<tr>
<th>Post Creation Date Time</th>
<th>User</th>
<th>Post</th>
<th>Like</th>  
<th>Comment</th>
<th>View All Likes</th>
<th>View All Comments</th>
<th>Delete the post</th>
</tr>  

<%
	LikeDao likeDaoImpl = LikeDaoImpl.getInstance(MainCentralizedResource.CONNECTION);
	CommentDao commentDaoImpl = CommentDaoImpl.getInstance(MainCentralizedResource.CONNECTION);
	for(Post postOneByOne : allPostToView){
		Like like = likeDaoImpl.getLike( ((Users) session.getAttribute("loggedinUser")).getUserId_(), postOneByOne.getPostId());
		List<Like> likes = likeDaoImpl.getAllLikesForThePost(postOneByOne.getPostId());
		List<Comment> comments = commentDaoImpl.getAllCommentsForThePost(postOneByOne.getPostId());
		
		out.println("<tr>"
	+"<td>"+LocalDateTime.of(postOneByOne.getPostedDate(), postOneByOne.getPostedTime()).format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))+"</td>"
	+"<td>"+postOneByOne.getUserId()+"</td>"
	+"<td>"+postOneByOne.getPostContent()+"</td>"
	+"<td>"+(likes == null ? "" : likes.size()) + (like == null ? "<a href=\"LikeThePostServlet?postId="+postOneByOne.getPostId()+"\">Like</a>": "<a href=\"UnlikeThePostServlet?likeId="+like.getLikeId()+"\">Unlike</a>") +"</td>"
	+"<td>"+(comments == null ? "" : comments.size())+"<a href=\"comment.jsp?postId="+postOneByOne.getPostId()+"\">Comment</a></td>"
	+"<td><a href=\"viewLikes.jsp?postId="+postOneByOne.getPostId()+"\">View Likes</a></td>"
	+"<td><a href=\"viewCommnets.jsp?postId="+postOneByOne.getPostId()+"\">View Comments</a></td>"
	+"<td>"+(postOneByOne.getUserId() == ((Users)session.getAttribute("loggedinUser")).getUserId_() ? "<a href=\"PostDeleteServlet?postId="+postOneByOne.getPostId()+"\">Delete</a>" : ((Users)session.getAttribute("loggedinUser")).getRoles_().equals("admin") ? "<a href=\"PostDeleteServlet?postId="+postOneByOne.getPostId()+"\">Delete</a>" : "" )+"</td>"
	+"</tr>");
	}
	}
%>
</table>
</div>

</body>
</html>