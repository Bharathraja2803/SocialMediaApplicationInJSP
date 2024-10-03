/**
 * This is the bean class for Follower table
 */
package org.bharath.model;

import java.io.Serializable;

public class Follower implements Serializable{

	private int user_id;
    private int following_user_id;

    public Follower() {
    }
    
    

    public Follower(int user_id, int following_user_id) {
		super();
		this.user_id = user_id;
		this.following_user_id = following_user_id;
	}



	public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getFollowing_user_id() {
        return following_user_id;
    }

    public void setFollowing_user_id(int following_user_id) {
        this.following_user_id = following_user_id;
    }

    @Override
    public String toString() {
        return "Follower{" +
                "user_id=" + user_id +
                ", following_user_id=" + following_user_id +
                '}';
    }
}
