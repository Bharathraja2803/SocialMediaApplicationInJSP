package org.bharath.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bharath.dao.UsersDaoImpl;
import org.bharath.utils.MainCentralizedResource;


/**
 * Servlet Filter implementation class ValidateUserDetailsForSignUpFilter
 */
public class ValidateUserDetailsForSignUpFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest) request).getSession();
		
		String username = request.getParameter("username");
		if(username.isEmpty()){
			RequestDispatcher rd = request.getRequestDispatcher("signupPage.jsp");
			session.setAttribute("isUserNameEmpty", "y");
			rd.forward(request, response);
			return;
		}
		
		String password = request.getParameter("password");
		
		if(!Pattern.matches( "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", password)){
			RequestDispatcher rd = request.getRequestDispatcher("signupPage.jsp");
			session.setAttribute("isPasswordInValid", "y");
			rd.forward(request, response);
			return;
		}
		
		
		String dob = request.getParameter("dob");
		if(dob.isEmpty()){
			RequestDispatcher rd = request.getRequestDispatcher("signupPage.jsp");
			session.setAttribute("isDobInValid", "y");
			rd.forward(request, response);
			return;
		}
		
		
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate birthDayDate = LocalDate.parse(dob, dateTimeFormatter);
		if(birthDayDate.isBefore(LocalDate.now().minusYears(110)) || birthDayDate.isAfter(LocalDate.now().minusYears(18))){
			RequestDispatcher rd = request.getRequestDispatcher("signupPage.jsp");
			session.setAttribute("isDobInValid", "y");
			rd.forward(request, response);
			return;
		}
		
		String emailid = request.getParameter("mailid");
		
		if(!Pattern.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", emailid)){
			RequestDispatcher rd = request.getRequestDispatcher("signupPage.jsp");
			session.setAttribute("isEmailIdInValid", "y");
			rd.forward(request, response);
			return;
		}
		
		UsersDaoImpl usersDaoImpl = UsersDaoImpl.getInstance(MainCentralizedResource.CONNECTION);
		if(usersDaoImpl.isEmailAlreadyExits(emailid)){
			RequestDispatcher rd = request.getRequestDispatcher("signupPage.jsp");
			session.setAttribute("isMailIdAlreadyExists", "y");
			rd.forward(request, response);
			return;
		}
		
		chain.doFilter(request, response);
	}

}
