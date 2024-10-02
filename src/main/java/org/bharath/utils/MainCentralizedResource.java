package org.bharath.utils;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainCentralizedResource {
	public static final Logger LOGGER = LogManager.getLogger(MainCentralizedResource.class);
	public static final Connection CONNECTION = DBConnection.getConnection();
	
	 public static String generateHashedPassword(String password){
	        MessageDigest md = null;
	        try {
	            md = MessageDigest.getInstance("SHA-256");
	        } catch (NoSuchAlgorithmException e) {
	            MainCentralizedResource.LOGGER.fatal(e.toString());
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
