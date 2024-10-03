package org.bharath.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bharath.dao.PostDao;
import org.bharath.dao.PostDaoImpl;
import org.bharath.model.Users;
import org.bharath.MainCentralizedResource;


/**
 * Servlet implementation class PostCreationServlet
 */
public class PostCreationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		if(session.getAttribute("loggedinUser") == null){
			session.setAttribute("isNotLoggedIn", "y");
			response.sendRedirect("index.jsp");
			return;
		}
		
		PostDao postDaoImpl = PostDaoImpl.getInstance(MainCentralizedResource.CONNECTION);
		Users users = (Users)session.getAttribute("loggedinUser");
		String postContent = request.getParameter("postContent");
		boolean isPostCreationSuccessful = postDaoImpl.createPost(postContent, users.getUserId_());
		
		if(!isPostCreationSuccessful){
			MainCentralizedResource.LOGGER.warning("Something went wrong in post creation");
			session.setAttribute("isProblemInPostAddition", "y");
			response.sendRedirect("addpost.jsp");
			return;
		}
		
		
		MainCentralizedResource.LOGGER.info("Successfully created the post");
		session.setAttribute("isPostCreationSuccessful", "y");
		response.sendRedirect("post.jsp");
	}

}
