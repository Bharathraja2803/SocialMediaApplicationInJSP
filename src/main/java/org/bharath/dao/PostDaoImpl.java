package org.bharath.dao;


import org.bharath.model.Post;
import org.bharath.utils.MainCentralizedResource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostDaoImpl implements PostDao{
    private static PostDaoImpl postDao_ = null;
    private static Connection connection_ = null;

    private PostDaoImpl(Connection connection){
        connection_ = connection;
    }

    /**
     * This method is used to insert the post into the database
     * @param postContent
     * @param userId
     * @return
     */
    @Override
    public boolean createPost(String postContent, int userId){
        try {
            PreparedStatement createNewPostStatement = connection_.prepareStatement("INSERT INTO post (post_id, user_id, posted_date, posted_time, post_content) VALUES (nextval('post_id_sequence'), ?, CURRENT_DATE, CURRENT_TIME, ?)");
            createNewPostStatement.setInt(1, userId);
            createNewPostStatement.setString(2, postContent);
            createNewPostStatement.execute();
            MainCentralizedResource.LOGGER.info("Successfully posted the message.");
            return true;
        } catch (SQLException e) {
            MainCentralizedResource.LOGGER.fatal(e.toString());
            return false;
        }
    }

    /**
     * This method is used to delete the post from the database
     * @param postId
     * @return
     */
    @Override
    public boolean removePost(int postId){
        boolean isPostExits = isPostExists(postId);

        if(!isPostExits){
            MainCentralizedResource.LOGGER.warn("Post not exists");
            return false;
        }
        try {
            PreparedStatement removePostByPostId = connection_.prepareStatement("delete from post where post_id = ?");
            removePostByPostId.setInt(1, postId);
            removePostByPostId.execute();
            MainCentralizedResource.LOGGER.info("Successfully deleted the post!");
            return true;
        } catch (SQLException e) {
            MainCentralizedResource.LOGGER.fatal(e.toString());
            return false;
        }

    }

    /**
     * This method is used to check whether the post exists in the database
     * @param postId
     * @return
     */
    @Override
    public boolean isPostExists(int postId){
        try {
            PreparedStatement selectQueryToCheckPostExits = connection_.prepareStatement("select post_id from post where post_id = ?");
            selectQueryToCheckPostExits.setInt(1, postId);
            ResultSet resultSet = selectQueryToCheckPostExits.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            MainCentralizedResource.LOGGER.fatal(e.toString());
            return false;
        }
    }

    /**
     * This method is used to get the instance of the PostDeo class
     * @param connection
     * @return
     */
    public static PostDaoImpl getInstance(Connection connection){
        if(postDao_ == null){
            postDao_ = new PostDaoImpl(connection);
        }
        return postDao_;
    }

    /**
     * This method is used to get all the post by the specific user
     * @param userId
     * @return
     */
    @Override
    public List<Post> getAllMyPost(int userId){
        List<Post> myAllPosts = new ArrayList<>();
        try {
            PreparedStatement selectQueryToFetchAllMyPost = connection_.prepareStatement("select * from post where user_id = ?");
            selectQueryToFetchAllMyPost.setInt(1, userId);
            ResultSet resultSet = selectQueryToFetchAllMyPost.executeQuery();
            while(resultSet.next()){
                Post post = new Post();
                post.setPostId(resultSet.getInt("post_id"));
                post.setUserId(resultSet.getInt("user_id"));
                post.setPostedDate(resultSet.getDate("posted_date").toLocalDate());
                post.setPostedTime(resultSet.getTime("posted_time").toLocalTime());
                post.setPostContent(resultSet.getString("post_content"));
                myAllPosts.add(post);
            }

            if(myAllPosts.isEmpty()){
                MainCentralizedResource.LOGGER.warn(String.format("User %s have not posted any post", userId));
                return null;
            }
            return myAllPosts;
        } catch (SQLException e) {
            MainCentralizedResource.LOGGER.fatal(e.toString());
            return null;
        }
    }
}
