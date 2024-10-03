
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
<title>List All Followers</title>
<%@ include file="header.jsp" %>
</head>
<body>
<div>
<h1>All Followers</h1>
<%
	if(session.getAttribute("somethingWentWrong") != null){
		out.println("<h3>Something went wrong try log out and login again!</h3>");
		session.removeAttribute("somethingWentWrong");
	}

	if(session.getAttribute("isUnfollowSuccessful") != null){
		out.println("<h3>Successfully unfollowed the user!</h3>");
		session.removeAttribute("isUnfollowSuccessful");
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
<h2>Followers</h2>
<%
	FollowerDao followerDaoImpl = FollowerDaoImpl.getInstance(MainCentralizedResource.CONNECTION);
	List<Users> listOfAllFollowers = followerDaoImpl.listAllFollowedUsers(users.getUserId_());
	
	if(listOfAllFollowers == null){
		out.println("<h3>You have not followed any people</h3>");
		return;
	}else{
		
	
%>
<table border="1" width="90%">  
<tr>
<th>User id</th>
<th>User name</th>
<th>Action</th>
</tr>

<%
		for(Users user : listOfAllFollowers){
			out.println(
					"<tr>"
					+"<td>"+user.getUserId_()+"</td>"
					+"<td>"+user.getUserName_()+"</td>"
					+"<td><a href=\"UnfollowServlet?userId="+user.getUserId_()+"&pageId=1\">Unfollow</a></td>"
					+"</tr>"
					);
		}	
	}
%>

</table>
</div>
</body>
</html>