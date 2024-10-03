package org.bharath;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.bharath.utils.DBConnection;


public class MainCentralizedResource {
	public static final Logger LOGGER = Logger.getLogger(MainCentralizedResource.class.toString());
	public static final Connection CONNECTION = DBConnection.getConnection();
	static{
        try{
        	/*  this works absolutely fine. but we are giving absolute path.  relative path should be given 
        	FileInputStream fileInputStream = new FileInputStream("C:\\Users\\bharath-22329\\workspace\\SocialMediaApplication\\src\\main\\java\\org\\bharath\\config\\log.properties");
            LogManager.getLogManager().readConfiguration(fileInputStream);
            FileHandler fileHandler = new FileHandler("C:\\Users\\bharath-22329\\workspace\\SocialMediaApplication\\src\\main\\java\\org\\bharath\\logs\\application.log");
            */
        	/* Not working
        	InputStream fileInputStream = MainCentralizedResource.class.getResourceAsStream("log.properties");
        	while(true){
        		byte byteContent = (byte) fileInputStream.read();
        		if(byteContent <=0){
        			break;
        		}
        		System.out.print((char)byteContent);
        	}
        	*/
        	/* Not working
        	FileInputStream fileInputStream = new FileInputStream("./config/log.properties");
            LogManager.getLogManager().readConfiguration(fileInputStream);
            FileHandler fileHandler = new FileHandler("./logs/application.log");
            */
        	/* Not working
        	FileInputStream fileInputStream = new FileInputStream("/org/bharath/config/log.properties");
            LogManager.getLogManager().readConfiguration(fileInputStream);
            FileHandler fileHandler = new FileHandler("/org/bharath/logs/application.log");
            */
        	
        	/* Not working
        	FileInputStream fileInputStream = new FileInputStream("/src/main/java/org/bharath/config/log.properties");
            LogManager.getLogManager().readConfiguration(fileInputStream);
            FileHandler fileHandler = new FileHandler("/src/main/java/org/bharath/logs/application.log");
            */
        	FileInputStream fileInputStream = new FileInputStream("C:\\Users\\bharath-22329\\workspace\\SocialMediaApplication\\src\\main\\java\\org\\bharath\\config\\log.properties");
            LogManager.getLogManager().readConfiguration(fileInputStream);
            FileHandler fileHandler = new FileHandler("C:\\Users\\bharath-22329\\workspace\\SocialMediaApplication\\src\\main\\java\\org\\bharath\\logs\\application.log");
            LOGGER.addHandler(fileHandler);
        } catch (IOException e) {
            LOGGER.severe(e.toString());
        }
	}
	
	 public static String generateHashedPassword(String password){
	        MessageDigest md = null;
	        try {
	            md = MessageDigest.getInstance("SHA-256");
	        } catch (NoSuchAlgorithmException e) {
	            MainCentralizedResource.LOGGER.severe(e.toString());
	            return null;
	        }
	        byte[] shaBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

	        BigInteger number = new BigInteger(1, shaBytes);

	        StringBuilder hexString = new StringBuilder(number.toString(16));

	        while (hexString.length() < 32)
	        {
	            hexString.insert(0, '0');
	        }

	        return hexString.toString();
	    }
}
