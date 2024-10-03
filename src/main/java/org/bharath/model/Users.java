/**
 * This is the bean class for users table
 */
package org.bharath.model;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Users implements Serializable{
    private int userId_;
    private String userName_;
    private String password_;
    private LocalDate birthday_;
    private String emailId_;
    private LocalDate signupDate_;
    private LocalTime signupTime_;
    private String roles_;
    private char isBlocked_;

    public Users() {

    }


    public Users(int userId_, String userName_, String password_, LocalDate birthday_, String emailId_,
			LocalDate signupDate_, LocalTime signupTime_, String roles_, char isBlocked_) {
		super();
		this.userId_ = userId_;
		this.userName_ = userName_;
		this.password_ = password_;
		this.birthday_ = birthday_;
		this.emailId_ = emailId_;
		this.signupDate_ = signupDate_;
		this.signupTime_ = signupTime_;
		this.roles_ = roles_;
		this.isBlocked_ = isBlocked_;
	}


	public String getRoles_() {
        return roles_;
    }

    public void setRoles_(String roles_) {
        this.roles_ = roles_;
    }

    public int getUserId_() {
        return userId_;
    }

    public void setUserId_(int userId_) {
        this.userId_ = userId_;
    }

    public String getUserName_() {
        return userName_;
    }

    public void setUserName_(String userName_) {
        this.userName_ = userName_;
    }

    public String getPassword_() {
        return password_;
    }

    public void setPassword_(String password_) {
        this.password_ = password_;
    }

    public LocalDate getBirthday_() {
        return birthday_;
    }

    public void setBirthday_(LocalDate birthday_) {
        this.birthday_ = birthday_;
    }

    public String getEmailId_() {
        return emailId_;
    }

    public void setEmailId_(String emailId_) {
        this.emailId_ = emailId_;
    }


    public LocalDate getSignupDate_() {
        return signupDate_;
    }

    public void setSignupDate_(LocalDate signupDate_) {
        this.signupDate_ = signupDate_;
    }

    public LocalTime getSignupTime_() {
        return signupTime_;
    }

    public void setSignupTime_(LocalTime signupTime_) {
        this.signupTime_ = signupTime_;
    }

    public char isBlocked() {
        return isBlocked_;
    }

    public void setBlocked(char blocked) {
        isBlocked_ = blocked;
    }

    @Override
    public String toString() {
        return "Users{" +
                "userId_=" + userId_ +
                ", userName_='" + userName_ + '\'' +
                ", password_='" + password_ + '\'' +
                ", birthday_=" + birthday_ +
                ", emailId_='" + emailId_ + '\'' +
                ", signupDate_=" + signupDate_ +
                ", signupTime_=" + signupTime_ +
                ", roles_='" + roles_ + '\'' +
                ", isBlocked=" + isBlocked_ +
                '}';
    }
}
