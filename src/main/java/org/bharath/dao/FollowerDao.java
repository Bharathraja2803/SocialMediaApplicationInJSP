package org.bharath.dao;

import org.bharath.model.Users;

import java.util.List;

public interface FollowerDao {
    boolean followUser(int userId, int followerId);
    List<Users> listAllFollowedUsers(int userId);
    boolean isUserIdExits(int userId);
    List<Users> listAllNotFollowedUsers(int userId);
    List<Users> listOfAllUsersFollowingYou(int userId);
    boolean isFollowing(int userId, int followerId);
    boolean unFollowUser(int userId, int unFollowId);
}
