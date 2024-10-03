package org.bharath.controller;

import java.io.IOException;


import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bharath.dao.UsersDao;
import org.bharath.dao.UsersDaoImpl;
import org.bharath.model.Users;
import org.bharath.MainCentralizedResource;


public class AuthenticationFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest) request).getSession();
		if(session.getAttribute("loggedinUser") != null){
			chain.doFilter(request, response);
			return;
		}
		String userid = request.getParameter("userid");
		String password = request.getParameter("password");
		
        int userIdInteger = -1;
        try{
            userIdInteger = Integer.parseInt(userid);
        }catch(NumberFormatException e){
            MainCentralizedResource.LOGGER.warning("User id incorrect\nuserid: " + userid);
            MainCentralizedResource.LOGGER.severe(e.toString());
            session.setAttribute("inValidUserId", "y");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
            return;
        }

        UsersDao usersDaoImpl = UsersDaoImpl.getInstance(MainCentralizedResource.CONNECTION);
        boolean isExits = usersDaoImpl.isUserIdExits(userIdInteger);
        if(!isExits){
            MainCentralizedResource.LOGGER.warning("User id incorrect, userid: " + userid);
            session.setAttribute("inValidUserId", "y");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
            return;
        }

        Users users = usersDaoImpl.getUser(userIdInteger);
        String hashedPassword = MainCentralizedResource.generateHashedPassword(password);
        boolean isPasswordCorrect = users.getPassword_().equals(hashedPassword);

        if(!isPasswordCorrect){
            MainCentralizedResource.LOGGER.warning("Password incorrect!..");
            session.setAttribute("inCorrectPassword", "y");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
            return;
        }

        
        
        if(users.isBlocked() == 'y'){
            MainCentralizedResource.LOGGER.warning("Your account is blocked cannot login");
            session.setAttribute("isAccountBlocked", "y");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
            return;
        }

        MainCentralizedResource.LOGGER.info("Welcome " + users.getUserName_());
        session.setAttribute("loggedinUser", users);
		chain.doFilter(request, response);
		
	}

}
