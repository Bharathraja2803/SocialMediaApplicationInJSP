package org.bharath.dao;


import org.bharath.model.Comment;
import org.bharath.MainCentralizedResource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentDaoImpl implements CommentDao{
    private static CommentDaoImpl commentDao_ = null;
    private static Connection connection_ = null;

    private CommentDaoImpl(Connection connection){
        connection_ = connection;
    }

    /**
     * This method is used to delete all the comments for the specific post
     * @param postId
     * @return
     */
    @Override
    public boolean deleteAllCommentsForThePost(int postId){

        List<Comment> commentList = getAllCommentsForThePost(postId);

        if(commentList == null){
            MainCentralizedResource.LOGGER.info("No comments to delete for deleting the post");
            return true;
        }

        try {
            PreparedStatement deleteQueryForSpecificPost = connection_.prepareStatement("delete from comment where post_id = ?");
            deleteQueryForSpecificPost.setInt(1, postId);
            deleteQueryForSpecificPost.execute();
            MainCentralizedResource.LOGGER.info("Comments deleted Successfully!.");
            return true;
        } catch (SQLException e) {
            MainCentralizedResource.LOGGER.severe(e.toString());
            return false;
        }

    }

    /**
     * This method is used to delete all comments for the specific user
     * @param userId
     * @return
     */
    @Override
    public boolean deleteAllCommentsForTheUser(int userId){
        UsersDaoImpl usersDaoImp = UsersDaoImpl.getInstance(connection_);
        if(!usersDaoImp.isUserIdExits(userId)){
            MainCentralizedResource.LOGGER.warning("Entered user id is invalid");
            return false;
        }
        try {
            PreparedStatement deleteQueryToRemoveComment = connection_.prepareStatement("delete from comment where comment_user_id = ?");
            deleteQueryToRemoveComment.setInt(1, userId);
            deleteQueryToRemoveComment.execute();
            MainCentralizedResource.LOGGER.info("Successfully deleted the comments for the user");
            return true;
        } catch (SQLException e) {
            MainCentralizedResource.LOGGER.severe(e.toString());
            return false;
        }
    }

    /**
     * This method is used to insert record to the DB
     * @param userId
     * @param postId
     * @param commentText
     * @return
     */
    @Override
    public boolean commentThePost(int userId, int postId, String commentText){
        try {
            PreparedStatement insertQueryForCommentingThePost = connection_.prepareStatement("INSERT INTO comment (comment_id, comment_date, comment_time, comment_user_id, post_id, comment_text) VALUES (nextval('comment_id_sequence'), CURRENT_DATE, CURRENT_TIME, ?, ?, ?)");
            insertQueryForCommentingThePost.setInt(1, userId);
            insertQueryForCommentingThePost.setInt(2, postId);
            insertQueryForCommentingThePost.setString(3, commentText);
            insertQueryForCommentingThePost.execute();
            MainCentralizedResource.LOGGER.info("Successfully added the comment to the post id: " + postId);
            return true;
        } catch (SQLException e) {
            MainCentralizedResource.LOGGER.severe(e.toString());
            return false;
        }
    }

    /**
     * This method is used to get all the comments for the specific post
     * @param postId
     * @return
     */
    @Override
    public List<Comment> getAllCommentsForThePost(int postId){
        List<Comment> commentList = new ArrayList<>();
        try {
            PreparedStatement selectQueryForFetchingAllCommentsForThePost = connection_.prepareStatement("select * from comment where post_id = ?");
            selectQueryForFetchingAllCommentsForThePost.setInt(1, postId);
            ResultSet resultSet = selectQueryForFetchingAllCommentsForThePost.executeQuery();
            while(resultSet.next()){
                Comment comment = new Comment();
                comment.setCommentId(resultSet.getInt("comment_id"));
                comment.setCommentDate(resultSet.getDate("comment_date").toLocalDate());
                comment.setCommentTime(resultSet.getTime("comment_time").toLocalTime());
                comment.setCommentUserId(resultSet.getInt("comment_user_id"));
                comment.setPostId(resultSet.getInt("post_id"));
                comment.setCommentText(resultSet.getString("comment_text"));
                commentList.add(comment);
            }

            if(commentList.isEmpty()){
                MainCentralizedResource.LOGGER.warning("There was no comments for the post with post id: " + postId);
                return null;
            }

            MainCentralizedResource.LOGGER.info("Successfully fetched all the comments for the post with post id: " + postId);
            return commentList;
        } catch (SQLException e) {
            MainCentralizedResource.LOGGER.severe(e.toString());
            return null;
        }
    }

    /**
     * Thie method returns the instance of the CommentDeo class
     * @param connection
     * @return
     */
    public static CommentDaoImpl getInstance(Connection connection){
        if(commentDao_ == null){
            commentDao_ = new CommentDaoImpl(connection);
        }
        return commentDao_;
    }
}
