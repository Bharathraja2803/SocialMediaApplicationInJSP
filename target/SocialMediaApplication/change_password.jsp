<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Change password</title>
</head>
<body>
<div>
<h1>Change Password</h1>
<a href="index.jsp">LoginPage</a>
</div>
<div>
<%
	if(session.getAttribute("bothPasswordsAreNotMatching") != null){
		out.println("<h3>New Password and Confirm password are mismatching!..</h3>");
		session.removeAttribute("bothPasswordsAreNotMatching");
	}
	
	if(session.getAttribute("isPasswordInValid") != null){
		out.println("<h3>Password entered is not matcing the criteria!..</h3>");
		session.removeAttribute("isPasswordInValid");
	}
	
	if(session.getAttribute("isOldPasswordAndNewPasswordAreSame") != null){
		out.println("<h3>Old password and new password are same!..</h3>");
		session.removeAttribute("isOldPasswordAndNewPasswordAreSame");
	}
	
	if(session.getAttribute("isChangingPasswordWithNewPasswordSuccessfull") != null){
		out.println("<h3>Something went wrong in changing the password!..</h3>");
		session.removeAttribute("isChangingPasswordWithNewPasswordSuccessfull");
	}
	
%>
</div>
<div>
<form action="ChangePasswordServlet" method="post">
<label for="new_password">New Password: </label>
<input type="password" name="newPassword" id="new_password">
<label for="confirm_password">Confirm Password: </label>
<input type="password" name="confirmPassword" id="confirm_password">
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