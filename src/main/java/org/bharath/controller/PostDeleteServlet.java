package org.bharath.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bharath.dao.CommentDao;
import org.bharath.dao.CommentDaoImpl;
import org.bharath.dao.LikeDao;
import org.bharath.dao.LikeDaoImpl;
import org.bharath.dao.PostDao;
import org.bharath.dao.PostDaoImpl;
import org.bharath.model.Post;
import org.bharath.model.Users;
import org.bharath.MainCentralizedResource;

/**
 * Servlet implementation class PostDeleteServlet
 */
public class PostDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		if(session.getAttribute("loggedinUser") == null){
			session.setAttribute("isNotLoggedIn", "y");
			response.sendRedirect("index.jsp");
			return;
		}
		
		CommentDao commentDaoImpl = CommentDaoImpl.getInstance(MainCentralizedResource.CONNECTION);
		LikeDao likeDaoImpl = LikeDaoImpl.getInstance(MainCentralizedResource.CONNECTION);
		int postId = -1;
		
		try{
			postId = Integer.parseInt(request.getParameter("postId"));
		}catch(NumberFormatException e){
			MainCentralizedResource.LOGGER.severe(e.toString());
			session.setAttribute("somethingWentWrong", "y");
			response.sendRedirect("post.jsp");
			return;
		}
		
		PostDao postDaoImpl = PostDaoImpl.getInstance(MainCentralizedResource.CONNECTION);
		Post post = postDaoImpl.getPost(postId);
		
		if(post == null){
			session.setAttribute("somethingWentWrong", "y");
			response.sendRedirect("post.jsp");
			return;
		}
		
		Users users = (Users)session.getAttribute("loggedinUser");
		if(post.getUserId() == users.getUserId_() || users.getRoles_().equals("admin")){
			boolean isCommentsDeleted = commentDaoImpl.deleteAllCommentsForThePost(postId);
	        if(!isCommentsDeleted){
	            MainCentralizedResource.LOGGER.warning("Something went wrong in deleting the comments!");
	            session.setAttribute("somethingWentWrong", "y");
				response.sendRedirect("post.jsp");
	            return;
	        }

	        boolean isLikesDeleted = likeDaoImpl.removeAllLikeForSpecificPost(postId);
	        if(!isLikesDeleted){
	            MainCentralizedResource.LOGGER.warning("Something went wrong in deleting all the likes of the post!");
	            session.setAttribute("somethingWentWrong", "y");
				response.sendRedirect("post.jsp");
	            return;
	        }
	        
	        boolean isPostDeleted = postDaoImpl.removePost(postId);
	        
	        if(!isPostDeleted){
	        	MainCentralizedResource.LOGGER.warning("Something went wrong in deleting the post!");
	            session.setAttribute("somethingWentWrong", "y");
				response.sendRedirect("post.jsp");
	            return;
	        }
	        
	        MainCentralizedResource.LOGGER.info("Post was deleted successfully");
	        session.setAttribute("postDeletedSuccessfully", "y");
	        response.sendRedirect("post.jsp");
		}
		
		

        
	}

}
