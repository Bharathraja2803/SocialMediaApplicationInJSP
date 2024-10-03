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
<title>People page</title>
<%@ include file="header.jsp" %>
</head>
<body>
<h1>People page</h1>
<%
	if(session.getAttribute("loggedinUser") == null){
		session.setAttribute("isNotLoggedIn", "y");
		response.sendRedirect("index.jsp");
		return;
	}
	
%>

<table border="1" width="90%">  
<tr>
<th>Followers Count</th>
<th>View List of all Followers</th>
<%
	Users users = (Users) session.getAttribute("loggedinUser");
	if(users.getRoles_().equals("user")){
		
	
%>
<th>Following Count</th>
<th>View List of all Following</th>  
<th>Add Following</th>
</tr>  

<%
	FollowerDao followerDaoImpl = FollowerDaoImpl.getInstance(MainCentralizedResource.CONNECTION);
	List<Users> listOfAllFollowers = followerDaoImpl.listAllFollowedUsers(users.getUserId_());
	List<Users> listofAllFollowing = followerDaoImpl.listOfAllUsersFollowingYou(users.getUserId_());
	out.println(
			"<tr>"
			+"<td>"+(listofAllFollowing == null ? 0 : listofAllFollowing.size())+"</td>"
			+"<td><a href=\"viewAllFollowing.jsp\">Follower</a></td>"
			+"<td>"+(listOfAllFollowers == null ? 0 : listOfAllFollowers.size())+"</td>"
			+"<td><a href=\"viewAllFollowers.jsp\">Following</a></td>"
			+"<td><a href=\"addFollowing.jsp\">Add Following</a></td>"
			+"</tr>"
			);
	
	}else{
		

%>
<th>All Users List</th>
</tr>  
<%
		FollowerDao followerDaoImpl = FollowerDaoImpl.getInstance(MainCentralizedResource.CONNECTION);
		List<Users> listOfAllFollowers = followerDaoImpl.listOfAllUsersFollowingYou(users.getUserId_());
		out.println(
				"<tr>"
				+"<td>"+(listOfAllFollowers == null ? 0 : listOfAllFollowers.size())+"</td>"
				+"<td><a href=\"viewAllFollowing.jsp\">Follower</a></td>"
				+"<td><a href=\"allUsersList.jsp\">All user</a></td>"
				+"</tr>"
				);
	
	}
%>
</table>
</body>
</html>