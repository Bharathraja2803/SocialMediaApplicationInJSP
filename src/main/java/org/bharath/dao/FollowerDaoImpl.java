package org.bharath.dao;


import org.bharath.model.Users;
import org.bharath.utils.MainCentralizedResource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FollowerDaoImpl implements FollowerDao{
    private static FollowerDaoImpl followerDao_ = null;
    private static Connection connection_ = null;

    private FollowerDaoImpl(Connection connection){
        connection_ = connection;
    }

    /**
     * This method is used to insert the record in the DB
     * @param userId
     * @param followerId
     * @return
     */
    @Override
    public boolean followUser(int userId, int followerId){
        boolean isFollowing = isFollowing(userId, followerId);
        if(isFollowing){
            MainCentralizedResource.LOGGER.warn(String.format("You are already following %d user_id", followerId));
            return false;
        }

        boolean isUserExits = isUserIdExits(userId);
        boolean isFollowerExits = isUserIdExits(followerId);

        if(!isUserExits){
            MainCentralizedResource.LOGGER.warn("User id is invalid");
            return false;
        }

        if(!isFollowerExits){
            MainCentralizedResource.LOGGER.warn("Follower id is invalid");
            return false;
        }

        try {
            PreparedStatement insertQueryToAddFollowers = connection_.prepareStatement("INSERT INTO follower (user_id, following_user_id) VALUES (?, ?)");
            insertQueryToAddFollowers.setInt(1, userId);
            insertQueryToAddFollowers.setInt(2, followerId);
            insertQueryToAddFollowers.execute();
            MainCentralizedResource.LOGGER.info("Added the record to the follower table");
            return true;
        } catch (SQLException e) {
            MainCentralizedResource.LOGGER.fatal(e.toString());
            return false;
        }
    }

    /**
     * This method is used to list all the followed users by the userId
     * @param userId
     * @return
     */
    @Override
    public List<Users> listAllFollowedUsers(int userId){
        List<Users> usersList = new ArrayList<>();
        boolean isUserIdExits = isUserIdExits(userId);
        if(!isUserIdExits){
            MainCentralizedResource.LOGGER.warn("You're user id is not valid!");
            return null;
        }

        try {
            PreparedStatement selectQueryForAllFollowerIdByUserId = connection_.prepareStatement("select * from users where user_id in (select following_user_id from follower where user_id = ?)");
            selectQueryForAllFollowerIdByUserId.setInt(1, userId);
            ResultSet resultSet = selectQueryForAllFollowerIdByUserId.executeQuery();
            while(resultSet.next()){
                Users users = new Users();
                users.setUserId_(resultSet.getInt("user_id"));
                users.setUserName_(resultSet.getString("user_name"));
                users.setPassword_(resultSet.getString("password"));
                users.setBirthday_(resultSet.getDate("birthdate").toLocalDate());
                users.setEmailId_(resultSet.getString("email_id"));
                users.setSignupDate_(resultSet.getDate("signup_date").toLocalDate());
                users.setSignupTime_(resultSet.getTime("signup_time").toLocalTime());
                users.setBlocked(resultSet.getString("is_blocked").charAt(0));
                users.setRoles_(resultSet.getString("roles"));
                usersList.add(users);
            }
        } catch (SQLException e) {
            MainCentralizedResource.LOGGER.fatal(e.toString());
            return null;
        }

        if(usersList.isEmpty()){
            MainCentralizedResource.LOGGER.warn("There were no users you are following");
            return null;
        }

        MainCentralizedResource.LOGGER.info("Successfully retrieved the following users list");
        return usersList;
    }

    /**
     * This method is used to check id the user id is a valid user in DB
     * @param userId
     * @return
     */
    @Override
    public boolean isUserIdExits(int userId){
        UsersDaoImpl usersDaoImp = UsersDaoImpl.getInstance(connection_);
        List<Users> allUsersInSocialMedia = usersDaoImp.listAllUsers();
        return allUsersInSocialMedia.stream().anyMatch(e -> e.getUserId_() == userId);

    }

    /**
     * This method is used to list all the users who you are not following
     * @param userId
     * @return
     */
    @Override
    public List<Users> listAllNotFollowedUsers(int userId){
        boolean isUserIdExits = isUserIdExits(userId);
        if(!isUserIdExits){
            MainCentralizedResource.LOGGER.warn("You're user id is not valid!");
            return null;
        }

        List<Users> followedUserList = listAllFollowedUsers(userId);
        UsersDaoImpl usersDaoImp = UsersDaoImpl.getInstance(connection_);
        List<Users> allUsersInSocialMedia = usersDaoImp.listAllUsers();
        allUsersInSocialMedia.removeIf(t -> t.getUserId_() == userId);
        if(followedUserList == null){
            MainCentralizedResource.LOGGER.warn("No followers were added for the user so far");
            return allUsersInSocialMedia;
        }

        for(Users users : followedUserList){
            allUsersInSocialMedia.removeIf(users1 -> users1.getUserId_() == users.getUserId_());
        }
        MainCentralizedResource.LOGGER.info("Successfully retrieved all the non followed users list");
        return allUsersInSocialMedia;
    }

    /**
     * This method is used to list all the user following you
     * @param userId
     * @return
     */
    @Override
    public List<Users> listOfAllUsersFollowingYou(int userId){
        List<Users> usersList = new ArrayList<>();
        boolean isUserIdExits = isUserIdExits(userId);
        if(!isUserIdExits){
            MainCentralizedResource.LOGGER.warn("You're user id is not valid!");
            return null;
        }

        try {
            PreparedStatement selectQueryForAllFollowerIdByUserId = connection_.prepareStatement("select * from users where user_id in (select user_id from follower where following_user_id = ?)");
            selectQueryForAllFollowerIdByUserId.setInt(1, userId);
            ResultSet resultSet = selectQueryForAllFollowerIdByUserId.executeQuery();
            while(resultSet.next()){
                Users users = new Users();
                users.setUserId_(resultSet.getInt("user_id"));
                users.setUserName_(resultSet.getString("user_name"));
                users.setPassword_(resultSet.getString("password"));
                users.setBirthday_(resultSet.getDate("birthdate").toLocalDate());
                users.setEmailId_(resultSet.getString("email_id"));
                users.setSignupDate_(resultSet.getDate("signup_date").toLocalDate());
                users.setSignupTime_(resultSet.getTime("signup_time").toLocalTime());
                users.setBlocked(resultSet.getString("is_blocked").charAt(0));
                users.setRoles_(resultSet.getString("roles"));
                usersList.add(users);
            }
        } catch (SQLException e) {
            MainCentralizedResource.LOGGER.fatal(e.toString());
            return null;
        }

        if(usersList.isEmpty()){
            MainCentralizedResource.LOGGER.warn("There were no followers following you");
            return null;
        }

        MainCentralizedResource.LOGGER.info("Successfully retrieved the followers list");
        return usersList;
    }

    /**
     * This method is used to check the user is following to the target user id
     * @param userId
     * @param followerId
     * @return
     */
    @Override
    public boolean isFollowing(int userId, int followerId){
        try {
            PreparedStatement selectQueryForUser = connection_.prepareStatement("select * from follower where user_id = ? and following_user_id = ?");
            selectQueryForUser.setInt(1, userId);
            selectQueryForUser.setInt(2, followerId);
            ResultSet resultSet = selectQueryForUser.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            MainCentralizedResource.LOGGER.fatal(e.toString());
        }
        return false;
    }

    /**
     * This method is used to delete the record in the follower table
     * @param userId
     * @param unFollowId
     * @return
     */
    @Override
    public boolean unFollowUser(int userId, int unFollowId){
        boolean isUserExits = isUserIdExits(userId);
        boolean isFollowerExits = isUserIdExits(unFollowId);

        if(!isUserExits){
            MainCentralizedResource.LOGGER.warn("User id is invalid");
            return false;
        }

        if(!isFollowerExits){
            MainCentralizedResource.LOGGER.warn("Follower id is invalid");
            return false;
        }

        boolean isFollowingUserId = isFollowing(userId, unFollowId);

        if(!isFollowingUserId){
            MainCentralizedResource.LOGGER.warn(String.format("The User Id %d is not following %d user_id", userId, unFollowId));
            return false;
        }

        try{

            PreparedStatement deleteQueryToUnFollowUser = connection_.prepareStatement("delete from follower where user_id = ? and following_user_id = ?");

            deleteQueryToUnFollowUser.setInt(1, userId);
            deleteQueryToUnFollowUser.setInt(2, unFollowId);
            deleteQueryToUnFollowUser.execute();
            MainCentralizedResource.LOGGER.info("Successfully removed the record from the follower table");
            return true;
        }catch(SQLException e){
            MainCentralizedResource.LOGGER.fatal(e.toString());
            return false;
        }


    }

    /**
     * This method will return the instance of the FollowerDeo
     * @param connection
     * @return
     */
    public static FollowerDaoImpl getInstance(Connection connection){
        if(followerDao_ == null){
            followerDao_ = new FollowerDaoImpl(connection);
        }
        return followerDao_;
    }
}
