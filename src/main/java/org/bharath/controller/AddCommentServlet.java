package org.bharath.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bharath.dao.CommentDao;
import org.bharath.dao.CommentDaoImpl;
import org.bharath.model.Users;
import org.bharath.MainCentralizedResource;

/**
 * Servlet implementation class AddCommentServlet
 */
public class AddCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if(session.getAttribute("loggedinUser") == null){
			session.setAttribute("isNotLoggedIn", "y");
			response.sendRedirect("index.jsp");
			return;
		}
		
		String comment = request.getParameter("comment");
		CommentDao commentDaoImpl = CommentDaoImpl.getInstance(MainCentralizedResource.CONNECTION);
		int postId = -1;
		try{
			postId = Integer.parseInt(request.getParameter("postid")) ;	
		}catch(NumberFormatException e){
			MainCentralizedResource.LOGGER.warning("Something went wrong in commenting the post!");
	        session.setAttribute("somethingWentWrong", "y");
	        response.sendRedirect("post.jsp");
			return;
		}
		
		Users users = ((Users) session.getAttribute("loggedinUser"));
		boolean isCommentSuccessFul = commentDaoImpl.commentThePost(users.getUserId_(), postId, comment);
		if(!isCommentSuccessFul){
			MainCentralizedResource.LOGGER.warning("Something went wrong in commenting the post!");
	        session.setAttribute("somethingWentWrong", "y");
	        response.sendRedirect("post.jsp");
			return;
		}
		
		MainCentralizedResource.LOGGER.info("Successfully commented the post");
		session.setAttribute("isCommentingThePostSuccessful", "y");
		response.sendRedirect("post.jsp");
	}

}
