package org.bharath.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bharath.dao.FollowerDao;
import org.bharath.dao.FollowerDaoImpl;
import org.bharath.model.Users;
import org.bharath.MainCentralizedResource;

/**
 * Servlet implementation class UnfollowServlet
 */
public class UnfollowServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		if(session.getAttribute("loggedinUser") == null){
			session.setAttribute("isNotLoggedIn", "y");
			response.sendRedirect("index.jsp");
			return;
		}
		
		int userIdToUnfollow = -1;
		String pageId = request.getParameter("pageId");
		try{
			userIdToUnfollow = Integer.parseInt(request.getParameter("userId"));
			
		}catch(NumberFormatException e){
			MainCentralizedResource.LOGGER.severe(e.toString());
			session.setAttribute("somethingWentWrong", "y");
			if(pageId.equals("1")){
				response.sendRedirect("viewAllFollowers.jsp");
			}else{
				response.sendRedirect("viewAllFollowing.jsp");
			}
			return;
		}
		
		Users users = (Users) session.getAttribute("loggedinUser");
		FollowerDao followerDaoImpl = FollowerDaoImpl.getInstance(MainCentralizedResource.CONNECTION);
		boolean isUnfollowSuccessfull = followerDaoImpl.unFollowUser(users.getUserId_(), userIdToUnfollow);
		
		if(!isUnfollowSuccessfull){
			MainCentralizedResource.LOGGER.severe("Something went wrong in unfollowing the user");
			session.setAttribute("somethingWentWrong", "y");
			if(pageId.equals("1")){
				response.sendRedirect("viewAllFollowers.jsp");
			}else{
				response.sendRedirect("viewAllFollowing.jsp");
			}
			return;
		}
		
		MainCentralizedResource.LOGGER.info("Unfollowing the user completed");
		session.setAttribute("isUnfollowSuccessful", "y");
		if(pageId.equals("1")){
			response.sendRedirect("viewAllFollowers.jsp");
		}else{
			response.sendRedirect("viewAllFollowing.jsp");
		}
		
	}
}
