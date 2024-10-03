/**
 * This is the bean class for comment table
 */
package org.bharath.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Comment implements Serializable{

	private int commentId;
    private LocalDate commentDate;
    private LocalTime commentTime;
    private int commentUserId;
    private int postId;
    private String commentText;

    public Comment() {
    }
    
    

    public Comment(int commentId, LocalDate commentDate, LocalTime commentTime, int commentUserId, int postId,
			String commentText) {
		this.commentId = commentId;
		this.commentDate = commentDate;
		this.commentTime = commentTime;
		this.commentUserId = commentUserId;
		this.postId = postId;
		this.commentText = commentText;
	}



	public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public LocalDate getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(LocalDate commentDate) {
        this.commentDate = commentDate;
    }

    public LocalTime getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(LocalTime commentTime) {
        this.commentTime = commentTime;
    }

    public int getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(int commentUserId) {
        this.commentUserId = commentUserId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", commentDate=" + commentDate +
                ", commentTime=" + commentTime +
                ", commentUserId=" + commentUserId +
                ", postId=" + postId +
                ", commentText='" + commentText + '\'' +
                '}';
    }
}
