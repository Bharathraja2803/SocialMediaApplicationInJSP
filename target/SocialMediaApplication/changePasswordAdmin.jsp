<%@page import="org.bharath.MainCentralizedResource"%>
<%@page import="org.bharath.model.Users"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Admin Change password</title>
<%@ include file="header.jsp" %>
</head>
<body>
<div>
<h1>Change Password</h1>
<%
	if(session.getAttribute("loggedinUser") == null){
		session.setAttribute("isNotLoggedIn", "y");
		response.sendRedirect("index.jsp");
		return;
	}
	
	Users users = (Users) session.getAttribute("loggedinUser");
	if(!users.getRoles_().equals("admin")){
		out.println("<h3>No access to this page</h3>");
		return;
	}
	
	
	int targetUserId = -1;
	
	try{
		targetUserId = Integer.parseInt(request.getParameter("targetUserId"));
	}catch(NumberFormatException e){
		MainCentralizedResource.LOGGER.severe(e.toString());
		session.setAttribute("somethingWentWrong", "y");
		response.sendRedirect("allUsersList.jsp");
		return;
	}
	
	
	
%>
</div>
<div>
<%
out.println("<form action=\"ChangePasswordAdminServlet\" method=\"get\">");
%>
<label for="new_password">New Password: </label>
<input type="password" name="newPassword" id="new_password">
<label for="confirm_password">Confirm Password: </label>
<input type="password" name="confirmPassword" id="confirm_password">
<input type="hidden" name="targetUserIdFromForm" value=<%=targetUserId%>>
<input type="submit" value="Change password">
</form>
</div>

<div>
<h3>Password Criteria:</h3>
<p>
<ol>
<li>Minimum length of 8 characters</li>
<li>At least one lowercase letter</li>
<li>At least one uppercase letter</li>
<li>At least one special character(Allowed special Characters: @$!%*?&)</li>
<li>At least one number</li>
</ol>
</p>
</div>
</body>
</html>