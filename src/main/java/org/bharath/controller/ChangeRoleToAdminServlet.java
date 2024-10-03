package org.bharath.controller;

import java.io.IOException;
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
 * Servlet implementation class ChangeRoleToAdminServlet
 */
public class ChangeRoleToAdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		if(session.getAttribute("loggedinUser") == null){
			session.setAttribute("isNotLoggedIn", "y");
			response.sendRedirect("index.jsp");
			return;
		}
		
		Users users = (Users) session.getAttribute("loggedinUser");
		
		if(!users.getRoles_().equals("admin")){
			session.setAttribute("notAdminUser", "y");
			response.sendRedirect("allUsersList.jsp");
			return;
		}
		
		int targetUserId = -1;
		
		try{
			targetUserId = Integer.parseInt(request.getParameter("userId"));
		}catch(NumberFormatException e){
			MainCentralizedResource.LOGGER.severe(e.toString());
			session.setAttribute("somethingWentWrong", "y");
			response.sendRedirect("addUsersList.jsp");
			return;
		}
		UsersDao usersDaoImpl = UsersDaoImpl.getInstance(MainCentralizedResource.CONNECTION);
		boolean isRoleUpdated = usersDaoImpl.updateTheRoleOfTheUser(users.getUserId_(), targetUserId, "admin");
		
		if(!isRoleUpdated){
			MainCentralizedResource.LOGGER.warning("Something went wrong in updating the user role to admin");
			session.setAttribute("isRoleUpdateFailure", "y");
			response.sendRedirect("allUsersList.jsp");
			return;
		}
		
		MainCentralizedResource.LOGGER.info("Role of the user changes to admin");
		session.setAttribute("roleChangeSuccessful", "y");
		response.sendRedirect("allUsersList.jsp");
	}


}
