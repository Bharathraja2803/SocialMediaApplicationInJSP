package org.bharath.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bharath.dao.LikeDao;
import org.bharath.dao.LikeDaoImpl;
import org.bharath.MainCentralizedResource;

/**
 * Servlet implementation class UnlikeThePostServlet
 */
public class UnlikeThePostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LikeDao likeDaoImpl = LikeDaoImpl.getInstance(MainCentralizedResource.CONNECTION);
		HttpSession session = request.getSession();
		
		if(session.getAttribute("loggedinUser") == null){
			session.setAttribute("isNotLoggedIn", "y");
			response.sendRedirect("index.jsp");
			return;
		}
		
		int likeId = -1;
		try{
			likeId = Integer.parseInt(request.getParameter("likeId"));
		}catch(NumberFormatException e){
			MainCentralizedResource.LOGGER.severe(e.toString());
			session.setAttribute("somethingWentWrong", "y");
			response.sendRedirect("post.jsp");
			return;
		}
		
		boolean isUnlikeSuccessful = likeDaoImpl.removeLike(likeId);
		
		if(!isUnlikeSuccessful){
			MainCentralizedResource.LOGGER.warning("Something went wrong in unliking the post");
			session.setAttribute("somethingWentWrong", "y");
			response.sendRedirect("post.jsp");
			return;
		}
		
		MainCentralizedResource.LOGGER.info("Unlike action completed successfully");
		session.setAttribute("unlikeCompleted", "y");
		response.sendRedirect("post.jsp");
	}

}
