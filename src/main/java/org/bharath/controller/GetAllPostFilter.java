package org.bharath.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.bharath.dao.FollowerDao;
import org.bharath.dao.FollowerDaoImpl;
import org.bharath.dao.PostDao;
import org.bharath.dao.PostDaoImpl;
import org.bharath.dao.UsersDao;
import org.bharath.dao.UsersDaoImpl;
import org.bharath.model.Post;
import org.bharath.model.Users;
import org.bharath.MainCentralizedResource;

/**
 * Servlet Filter implementation class GetAllPostFilter
 */
public class GetAllPostFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpSession session = ((HttpServletRequest)request).getSession();
		
		if(session.getAttribute("loggedinUser") == null){
			session.setAttribute("isNotLoggedIn", "y");
			((HttpServletResponse)response).sendRedirect("index.jsp");
			return;
		}
		
		Users users = (Users) session.getAttribute("loggedinUser");
		PostDao postDaoImpl = PostDaoImpl.getInstance(MainCentralizedResource.CONNECTION);
		String role = users.getRoles_();
		if(role.equals("admin")){
			UsersDao usersDaoImp = UsersDaoImpl.getInstance(MainCentralizedResource.CONNECTION);
	        List<Users> listOfAllUsers = usersDaoImp.listAllUsers();
	        if(listOfAllUsers == null){
	            MainCentralizedResource.LOGGER.warning("Something went wrong!");
	            session.setAttribute("isThereProblemInFetchingAllUsers", "y");
	            ((HttpServletResponse)response).sendRedirect("post.jsp");
	            return;
	        }
	        
	        List<Post> allFollowersPost = new ArrayList<>();
	        for(Users userinList : listOfAllUsers){
	            List<Post> allMyPostPerUser = postDaoImpl.getAllMyPost(userinList.getUserId_());
	            if(allMyPostPerUser == null){
	                MainCentralizedResource.LOGGER.warning("Something went wrong in fetching all users post");
	                continue;
	            }
	            allFollowersPost.addAll(allMyPostPerUser);
	        }
	        
	        if(allFollowersPost.isEmpty()){
	            MainCentralizedResource.LOGGER.warning("No post posted by any user");
	            session.setAttribute("noPostByAllUsers", "y");
	            ((HttpServletResponse)response).sendRedirect("post.jsp");
	            return;
	        }
	        session.setAttribute("postsToView", allFollowersPost);
	        chain.doFilter(request, response);
	        
		}else{
			FollowerDao followerDaoImpl = FollowerDaoImpl.getInstance(MainCentralizedResource.CONNECTION);
	        List<Users> listOfAllFollowers = followerDaoImpl.listAllFollowedUsers(users.getUserId_());
	        List<Post> allFollowersPost = new ArrayList<>();
	        if(listOfAllFollowers == null){
	            MainCentralizedResource.LOGGER.warning("Something went wrong in fetching the followers!");
	        }else{
	        	for(Users userinList : listOfAllFollowers){
		            List<Post> allMyPostPerUser = postDaoImpl.getAllMyPost(userinList.getUserId_());
		            if(allMyPostPerUser == null){
		                MainCentralizedResource.LOGGER.warning("Something went wrong in fetching all followers post");
		                continue;
		            }
		            allFollowersPost.addAll(allMyPostPerUser);
		        }	
	        }
	        
	        
	        List<Post> myPosts = postDaoImpl.getAllMyPost(users.getUserId_());
	        if(myPosts == null){
	        	MainCentralizedResource.LOGGER.warning("Something went wrong in fetching my post");
	        }else{
	        	allFollowersPost.addAll(myPosts);
	        }
	        
	        if(allFollowersPost.isEmpty()){
	            MainCentralizedResource.LOGGER.warning("No post posted by any follower");
	            session.setAttribute("noPostByAllUsers", "y");
	            if(session.getAttribute("postsToView") != null){
	            	session.removeAttribute("postsToView");
	            }
	            chain.doFilter(request, response);
	            return;
	        }
	        
	        session.setAttribute("postsToView", allFollowersPost);
	        chain.doFilter(request, response);    
		}
	
	}

}
