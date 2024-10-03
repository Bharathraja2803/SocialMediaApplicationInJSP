package org.bharath.controller;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bharath.dao.UsersDao;
import org.bharath.dao.UsersDaoImpl;
import org.bharath.model.Users;
import org.bharath.MainCentralizedResource;

/**
 * Servlet implementation class ChangePasswordAdminServlet
 */
public class ChangePasswordAdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String newPassword = request.getParameter("newPassword");
		String confirmPassword = request.getParameter("confirmPassword");
		HttpSession session = request.getSession();
		if(!newPassword.equals(confirmPassword)){
			session.setAttribute("bothPasswordsAreNotMatching", "y");
			response.sendRedirect("allUsersList.jsp");
			return;
		}
		
		if(!Pattern.matches( "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", newPassword)){
			session.setAttribute("isPasswordInValid", "y");
			response.sendRedirect("allUsersList.jsp");
			return;
		}
		
		UsersDao usersDaoImpl = UsersDaoImpl.getInstance(MainCentralizedResource.CONNECTION);
		int targetUserId = -1;
		try{
			targetUserId = Integer.parseInt(request.getParameter("targetUserIdFromForm"));
		}catch(NumberFormatException e){
			MainCentralizedResource.LOGGER.severe(e.toString());
			session.setAttribute("somethingWentWrong", "y");
			response.sendRedirect("allUsersList.jsp");
			return;
		}
		Users targetUser = usersDaoImpl.getUser(targetUserId);
		if(targetUser == null){
            MainCentralizedResource.LOGGER.warning("Something went wrong in geting the user details!");
            session.setAttribute("somethingWentWrong", "y");
			response.sendRedirect("allUsersList.jsp");
			return;
        }
		
		String oldHashedPassword = targetUser.getPassword_();
		if(oldHashedPassword.equals(MainCentralizedResource.generateHashedPassword(newPassword))){
			session.setAttribute("isOldPasswordAndNewPasswordAreSame", "y");
			response.sendRedirect("allUsersList.jsp");
			return;
		}
		
		
		
		

		
		
		
		String hashedPassword = MainCentralizedResource.generateHashedPassword(newPassword);
        
		boolean isPasswordChangeSuccessful = usersDaoImpl.resetOwnPassword(targetUser.getUserId_(), hashedPassword);
		
        if(!isPasswordChangeSuccessful){
            MainCentralizedResource.LOGGER.warning("Something went wrong in changing the password in DB");
            session.setAttribute("somethingWentWrong", "y");
			response.sendRedirect("allUsersList.jsp");
			return;
        }
        
        MainCentralizedResource.LOGGER.info("Changing the new password is successfull");
        session.setAttribute("isChangingThePasswordSuccessfull", "y");
        response.sendRedirect("allUsersList.jsp");
	}
}
