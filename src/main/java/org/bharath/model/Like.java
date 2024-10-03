/**
 * This is the bean class for Like table
 */
package org.bharath.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Like implements Serializable{
    private int likeId;
    private int userId;
    private int postId;
    private LocalDate likeDate;
    private LocalTime likeTime;

    public Like() {
    }

    
    public Like(int likeId, int userId, int postId, LocalDate likeDate, LocalTime likeTime) {
		super();
		this.likeId = likeId;
		this.userId = userId;
		this.postId = postId;
		this.likeDate = likeDate;
		this.likeTime = likeTime;
	}


	public int getLikeId() {
        return likeId;
    }

    public void setLikeId(int likeId) {
        this.likeId = likeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public LocalDate getLikeDate() {
        return likeDate;
    }

    public void setLikeDate(LocalDate likeDate) {
        this.likeDate = likeDate;
    }

    public LocalTime getLikeTime() {
        return likeTime;
    }

    public void setLikeTime(LocalTime likeTime) {
        this.likeTime = likeTime;
    }

    @Override
    public String toString() {
        return "Like{" +
                "likeId=" + likeId +
                ", userId=" + userId +
                ", postId=" + postId +
                ", likeDate=" + likeDate +
                ", likeTime=" + likeTime +
                '}';
    }
}
