package org.bharath.dao;

import org.bharath.model.Comment;

import java.util.List;

public interface CommentDao {
    boolean deleteAllCommentsForThePost(int postId);
    boolean deleteAllCommentsForTheUser(int userId);
    boolean commentThePost(int userId, int postId, String commentText);
    List<Comment> getAllCommentsForThePost(int postId);
}
