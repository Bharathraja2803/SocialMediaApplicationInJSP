package org.bharath.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bharath.dao.UsersDao;
import org.bharath.dao.UsersDaoImpl;
import org.bharath.model.Users;
import org.bharath.utils.MainCentralizedResource;

/**
 * Servlet Filter implementation class ForgetPasswordEmailDobValidationFilter
 */
public class ForgetPasswordEmailDobValidationFilter implements Filter {
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String emailid = request.getParameter("mailid");
		String dob = request.getParameter("dob");
		
		
		UsersDao usersDaoImpl = UsersDaoImpl.getInstance(MainCentralizedResource.CONNECTION);
        int userId = usersDaoImpl.getUserIdByEmailId(emailid);
        HttpSession session = ((HttpServletRequest) request).getSession();
        
        if(userId == -1){
            MainCentralizedResource.LOGGER.warn("Entered email is not valid!");
            session.setAttribute("isEmailInvaid", "y");
            RequestDispatcher rd = request.getRequestDispatcher("forget_password.jsp");
            rd.forward(request, response);
			return;
        }

        Users users = usersDaoImpl.getUser(userId);

        if(users == null){
            MainCentralizedResource.LOGGER.warn("Something went wrong");
            session.setAttribute("isForgetPassordUnsuccessfull", "y");
            RequestDispatcher rd = request.getRequestDispatcher("forget_password.jsp");
            rd.forward(request, response);
			return;
        }

        if( dob.isEmpty() || (!users.getBirthday_().equals(LocalDate.parse(dob, DateTimeFormatter.ofPattern("yyyy-MM-dd"))))){
            MainCentralizedResource.LOGGER.warn("Entered date of birth is invalid");
            session.setAttribute("isDobInvalid", "y");
            RequestDispatcher rd = request.getRequestDispatcher("forget_password.jsp");
            rd.forward(request, response);
			return;
        }

        MainCentralizedResource.LOGGER.info("Successfully retrieved the user");
        session.setAttribute("userDetailsWhoFogetPasword", users);
		chain.doFilter(request, response);
	}
	
}
