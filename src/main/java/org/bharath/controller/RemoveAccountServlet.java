package org.bharath.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bharath.dao.CommentDao;
import org.bharath.dao.CommentDaoImpl;
import org.bharath.dao.FollowerDao;
import org.bharath.dao.FollowerDaoImpl;
import org.bharath.dao.LikeDao;
import org.bharath.dao.LikeDaoImpl;
import org.bharath.dao.PostDao;
import org.bharath.dao.PostDaoImpl;
import org.bharath.dao.UsersDao;
import org.bharath.dao.UsersDaoImpl;
import org.bharath.model.Post;
import org.bharath.model.Users;
import org.bharath.MainCentralizedResource;


/**
 * Servlet implementation class RemoveAccountServlet
 */
public class RemoveAccountServlet extends HttpServlet {
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
		
		PostDao postDaoImpl = PostDaoImpl.getInstance(MainCentralizedResource.CONNECTION);
		List<Post> allPostOfUser = postDaoImpl.getAllMyPost(targetUserId);
		CommentDao commentDaoImpl = CommentDaoImpl.getInstance(MainCentralizedResource.CONNECTION);
		LikeDao likeDaoImpl = LikeDaoImpl.getInstance(MainCentralizedResource.CONNECTION);
        if(allPostOfUser != null){
            boolean isCommentInterrupted = false;
            boolean isLikeInterrupted = false;
            boolean isPostInterrupted = false;

            for(Post currentPost : allPostOfUser){
                boolean isCommentsDeleted = commentDaoImpl.deleteAllCommentsForThePost(currentPost.getPostId());
                if(!isCommentsDeleted){
                    MainCentralizedResource.LOGGER.warning("Something went wrong in deleting the comments!");
                    isCommentInterrupted = true;
                    break;
                }

                boolean isLikesDeleted = likeDaoImpl.removeAllLikeForSpecificPost(currentPost.getPostId());
                if(!isLikesDeleted){
                    MainCentralizedResource.LOGGER.warning("Something went wrong in deleting all the likes of the user!");
                    isLikeInterrupted = true;
                    break;
                }

                boolean isPostDeleted = postDaoImpl.removePost(currentPost.getPostId());

                if(!isPostDeleted){
                    MainCentralizedResource.LOGGER.warning("Something went wrong");
                    isPostInterrupted = true;
                    break;
                }
            }
            if(isCommentInterrupted || isLikeInterrupted || isPostInterrupted){
                MainCentralizedResource.LOGGER.warning("Something went wrong");
                session.setAttribute("somethingWentWrong", "y");
    			response.sendRedirect("addUsersList.jsp");
    			return;
            }
        }

        boolean isCommentsForTheUsersDeleted = commentDaoImpl.deleteAllCommentsForTheUser(targetUserId);

        if(!isCommentsForTheUsersDeleted){
            MainCentralizedResource.LOGGER.warning("Something went wrong");
            session.setAttribute("somethingWentWrong", "y");
			response.sendRedirect("addUsersList.jsp");
			return;
        }

        boolean isLikesDeletedUsingUserId = likeDaoImpl.removeAllLikesByUserId(targetUserId);

        if(!isLikesDeletedUsingUserId){
            MainCentralizedResource.LOGGER.warning("Something went wrong");
            session.setAttribute("somethingWentWrong", "y");
			response.sendRedirect("addUsersList.jsp");
			return;
        }

        FollowerDao followerDaoImpl = FollowerDaoImpl.getInstance(MainCentralizedResource.CONNECTION);
        List<Users> listOfAllFollowers = followerDaoImpl.listOfAllUsersFollowingYou(targetUserId);

        if(listOfAllFollowers != null){
            boolean isFollowingUserUnFollowInterrupted = false;
            for(Users user : listOfAllFollowers){
                if(!followerDaoImpl.unFollowUser(user.getUserId_(), targetUserId)){
                    isFollowingUserUnFollowInterrupted = true;
                    break;
                }
            }
            if(isFollowingUserUnFollowInterrupted){
                MainCentralizedResource.LOGGER.warning("Something went wrong");
                session.setAttribute("somethingWentWrong", "y");
    			response.sendRedirect("addUsersList.jsp");
    			return;
            }
        }

        List<Users> listOfAllUsersFollowing = followerDaoImpl.listAllFollowedUsers(targetUserId);

        if(listOfAllUsersFollowing != null){
            boolean isFollowingUserUnFollowInterrupted = false;
            for(Users user : listOfAllUsersFollowing){
                if(!followerDaoImpl.unFollowUser(targetUserId, user.getUserId_())){
                    isFollowingUserUnFollowInterrupted = true;
                    break;
                }
            }
            if(isFollowingUserUnFollowInterrupted){
                MainCentralizedResource.LOGGER.warning("Something went wrong");
                session.setAttribute("somethingWentWrong", "y");
    			response.sendRedirect("addUsersList.jsp");
    			return;
            }
        }
		
		UsersDao usersDaoImpl = UsersDaoImpl.getInstance(MainCentralizedResource.CONNECTION);
		boolean isAccountRemoved = usersDaoImpl.removeUser(users.getUserId_(), targetUserId);
		
		if(!isAccountRemoved){
			MainCentralizedResource.LOGGER.warning("Something went wrong in removing the user account");
			session.setAttribute("isRemovalFailed", "y");
			response.sendRedirect("allUsersList.jsp");
			return;
		}
		
		MainCentralizedResource.LOGGER.info("Successfully removed the account");
		session.setAttribute("removalAccountSuccessful", "y");
		response.sendRedirect("allUsersList.jsp");
	}

}
