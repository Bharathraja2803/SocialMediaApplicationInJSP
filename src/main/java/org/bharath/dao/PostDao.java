package org.bharath.dao;

import org.bharath.model.Post;

import java.util.List;

public interface PostDao {

    boolean createPost(String postContent, int userId);
    boolean removePost(int postId);
    boolean isPostExists(int postId);
    List<Post> getAllMyPost(int userId);
}
