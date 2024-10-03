package org.bharath.controller;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bharath.model.Users;
import org.bharath.MainCentralizedResource;

/**
 * Servlet Filter implementation class PasswordValidateFilter
 */
public class ChangePasswordValidateFilter implements Filter {
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String newPassword = request.getParameter("newPassword");
		String confirmPassword = request.getParameter("confirmPassword");
		HttpSession session = ((HttpServletRequest) request).getSession();
		if(!newPassword.equals(confirmPassword)){
			RequestDispatcher rd = request.getRequestDispatcher("change_password.jsp");
			session.setAttribute("bothPasswordsAreNotMatching", "y");
			rd.forward(request, response);
			MainCentralizedResource.LOGGER.warning("New Password and Confirm password are mismatching");
			return;
		}
		
		if(!Pattern.matches( "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", newPassword)){
			RequestDispatcher rd = request.getRequestDispatcher("change_password.jsp");
			session.setAttribute("isPasswordInValid", "y");
			rd.forward(request, response);
			MainCentralizedResource.LOGGER.warning("Password entered is not matcing the criteria");
			return;
		}
		
		Users users = (Users) session.getAttribute("userDetailsWhoFogetPasword");
		
		if(users == null){
			MainCentralizedResource.LOGGER.warning("Something went wrong in geting the passsword forgotten user details!");
            session.setAttribute("isForgetPassordUnsuccessfull", "y");
            RequestDispatcher rd = request.getRequestDispatcher("forget_password.jsp");
            rd.forward(request, response);
			return;
		}
		
		String oldHashedPassword = users.getPassword_();
		if(oldHashedPassword.equals(MainCentralizedResource.generateHashedPassword(newPassword))){
			RequestDispatcher rd = request.getRequestDispatcher("change_password.jsp");
			session.setAttribute("isOldPasswordAndNewPasswordAreSame", "y");
			rd.forward(request, response);
			MainCentralizedResource.LOGGER.warning("Password entered is same as old password");
			return;
		}
		
		MainCentralizedResource.LOGGER.info("New Password and Confirm password meets the criteria");
		chain.doFilter(request, response);
	}

}
