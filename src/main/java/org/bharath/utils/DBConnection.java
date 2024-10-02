/**
 * This is the centralized class to provide DB connection throughout the application
 */
package org.bharath.utils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection dbConnection = null;
    private static Connection connection_ = null;

    private DBConnection(){
        String username = "postgres";
        String password = "postgres";
        try {
            String url = "jdbc:postgresql://127.0.0.1:5432/SocialMediaDB";

            Class.forName("org.postgresql.Driver");

            connection_ = DriverManager.getConnection(
                    url, username, password);


        } catch (ClassNotFoundException | SQLException e) {
            MainCentralizedResource.LOGGER.fatal(e.toString());
        }
    }

    /**
     * This method is used to get the connection for the DB
     * @return
     */
    public static Connection getConnection(){
        if(dbConnection == null){
            dbConnection = new DBConnection();
        }
        return connection_;
    }

    /**
     * This method closes the connection before the garbage collection happens
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        connection_.close();
        super.finalize();
    }
}
