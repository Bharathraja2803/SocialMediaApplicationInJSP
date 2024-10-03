<%@page import="java.util.List"%>
<%@page import="org.bharath.MainCentralizedResource"%>
<%@page import="org.bharath.dao.FollowerDaoImpl"%>
<%@page import="org.bharath.dao.FollowerDao"%>
<%@page import="org.bharath.model.Users"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>List of all following</title>
<%@ include file="header.jsp" %>
</head>
<body>
<div>
<h1>All Following</h1>
<%
	if(session.getAttribute("somethingWentWrong") != null){
		out.println("<h3>Something went wrong try log out and login again!</h3>");
		session.removeAttribute("somethingWentWrong");
	}

	if(session.getAttribute("isUnfollowSuccessful") != null){
		out.println("<h3>Successfully unfollowed the user!</h3>");
		session.removeAttribute("isUnfollowSuccessful");
	}
	
	if(session.getAttribute("isFollowingTheUserCompleted") != null){
		out.println("<h3>Successfully followed the user!</h3>");
		session.removeAttribute("isFollowingTheUserCompleted");
	}
%>
</div>
<div>
<h2>My account</h2>
<%
	if(session.getAttribute("loggedinUser") == null){
		session.setAttribute("isNotLoggedIn", "y");
		response.sendRedirect("index.jsp");
		return;
	}
	
	Users users = (Users) session.getAttribute("loggedinUser");
%>

<table border="1" width="90%">  
<tr>
<th>User id</th>
<th>User name</th>
</tr>
<tr>
<td><%=users.getUserId_() %></td>
<td><%=users.getUserName_() %></td>
</tr>
</table>
</div>
<div>
<h2>Following</h2>
<%
	FollowerDao followerDaoImpl = FollowerDaoImpl.getInstance(MainCentralizedResource.CONNECTION);
	List<Users> listOfAllFollowing= followerDaoImpl.listOfAllUsersFollowingYou(users.getUserId_());
	
	if(listOfAllFollowing == null){
		out.println("<h3>No users following you</h3>");
		return;
	}else{
		
	
%>
<table border="1" width="90%">  
<tr>
<th>User id</th>
<th>User name</th>
<% if(!users.getRoles_().equals("admin")){ %>
<th>Action</th>
<%} %>
</tr>

<%
		for(Users user : listOfAllFollowing){
			out.println(
					"<tr>"
					+"<td>"+user.getUserId_()+"</td>"
					+"<td>"+user.getUserName_()+"</td>");
					if(!users.getRoles_().equals("admin")){
						out.println("<td>"+(followerDaoImpl.isFollowing(users.getUserId_(), user.getUserId_()) ? "<a href=\"UnfollowServlet?userId="+user.getUserId_()+"&pageId=2\">Unfollow</a>":"<a href=\"FollowServlet?userId="+user.getUserId_()+"&pageId=1\">Follow</a>")+"</td>");	
					}
			out.println("</tr>");
					
		}	
	}
%>

</table>
</div>
</body>
</html>