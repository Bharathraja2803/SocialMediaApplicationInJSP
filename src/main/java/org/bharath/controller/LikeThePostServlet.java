package org.bharath.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bharath.dao.LikeDao;
import org.bharath.dao.LikeDaoImpl;
import org.bharath.model.Users;
import org.bharath.MainCentralizedResource;

/**
 * Servlet implementation class LikeThePostServlet
 */
public class LikeThePostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LikeDao likeDaoImpl = LikeDaoImpl.getInstance(MainCentralizedResource.CONNECTION);
		HttpSession session = request.getSession();
		
		if(session.getAttribute("loggedinUser") == null){
			session.setAttribute("isNotLoggedIn", "y");
			response.sendRedirect("index.jsp");
			return;
		}
		
		int postId = -1;
		try{
			postId = Integer.parseInt(request.getParameter("postId"));
		}catch(NumberFormatException e){
			MainCentralizedResource.LOGGER.severe(e.toString());
			session.setAttribute("somethingWentWrong", "y");
			response.sendRedirect("post.jsp");
			return;
		}
		
		Users users = (Users)session.getAttribute("loggedinUser");
		
        boolean isLikeActionDone = likeDaoImpl.likeThePost(users.getUserId_(), postId);
        if(isLikeActionDone){
            MainCentralizedResource.LOGGER.info("Like action completed successfully");
            session.setAttribute("likeActionSuccessful", "y");
            response.sendRedirect("post.jsp");
        }else{
            MainCentralizedResource.LOGGER.warning("Something went wrong in liking the post!");
            session.setAttribute("somethingWentWrong", "y");
            response.sendRedirect("post.jsp");
			return;
        }
	}

}
