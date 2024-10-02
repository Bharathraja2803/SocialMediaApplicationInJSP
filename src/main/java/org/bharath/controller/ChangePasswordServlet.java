package org.bharath.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bharath.dao.UsersDao;
import org.bharath.dao.UsersDaoImpl;
import org.bharath.model.Users;
import org.bharath.utils.MainCentralizedResource;

/**
 * Servlet implementation class ChangePasswordServlet
 */
public class ChangePasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String newPassword = request.getParameter("newPassword");
		UsersDao usersDaoImpl = UsersDaoImpl.getInstance(MainCentralizedResource.CONNECTION);
		HttpSession session = request.getSession();
		Users targetUser = (Users) session.getAttribute("userDetailsWhoFogetPasword");
		if(targetUser == null){
            MainCentralizedResource.LOGGER.warn("Something went wrong in geting the passsword forgotten user details!");
            session.setAttribute("isForgetPassordUnsuccessfull", "y");
            RequestDispatcher rd = request.getRequestDispatcher("forget_password.jsp");
            rd.forward(request, response);
			return;
        }
		
		String hashedPassword = MainCentralizedResource.generateHashedPassword(newPassword);
        
		boolean isPasswordChangeSuccessful = usersDaoImpl.resetOwnPassword(targetUser.getUserId_(), hashedPassword);
		
        if(!isPasswordChangeSuccessful){
            MainCentralizedResource.LOGGER.warn("Something went wrong in changing the password in DB");
            RequestDispatcher rd = request.getRequestDispatcher("change_password.jsp");
			session.setAttribute("isChangingPasswordWithNewPasswordSuccessfull", "y");
			rd.forward(request, response);
            return;
        }
        
        MainCentralizedResource.LOGGER.info("Changing the new password is successfull");
        session.setAttribute("isChangingThePasswordSuccessfull", "y");
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
	}

}
