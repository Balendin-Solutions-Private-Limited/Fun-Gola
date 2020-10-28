package com.thambola.fungola.Model;

import java.io.Serializable;

public class User implements Serializable {
    private String lastName;

    private String gender;

    private String profilePic;

    private String mobile;

    private String dateOfBirth;

    private String type;

    private String userName;

    private String deviceId;

    private String isEmailVerified;

    private String firstName;

    private String password;

    private String referalCode;

    private String isMobileVerified;

    private String id;

    private String email;

    public String getLastName ()
    {
        return lastName;
    }

    public void setLastName (String lastName)
    {
        this.lastName = lastName;
    }

    public String getGender ()
    {
        return gender;
    }

    public void setGender (String gender)
    {
        this.gender = gender;
    }

    public String getProfilePic ()
    {
        return profilePic;
    }

    public void setProfilePic (String profilePic)
    {
        this.profilePic = profilePic;
    }

    public String getMobile ()
    {
        return mobile;
    }

    public void setMobile (String mobile)
    {
        this.mobile = mobile;
    }

    public String getDateOfBirth ()
    {
        return dateOfBirth;
    }

    public void setDateOfBirth (String dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public String getUserName ()
    {
        return userName;
    }

    public void setUserName (String userName)
    {
        this.userName = userName;
    }

    public String getDeviceId ()
    {
        return deviceId;
    }

    public void setDeviceId (String deviceId)
    {
        this.deviceId = deviceId;
    }

    public String getIsEmailVerified ()
    {
        return isEmailVerified;
    }

    public void setIsEmailVerified (String isEmailVerified)
    {
        this.isEmailVerified = isEmailVerified;
    }

    public String getFirstName ()
    {
        return firstName;
    }

    public void setFirstName (String firstName)
    {
        this.firstName = firstName;
    }

    public String getPassword ()
    {
        return password;
    }

    public void setPassword (String password)
    {
        this.password = password;
    }

    public String getReferalCode ()
    {
        return referalCode;
    }

    public void setReferalCode (String referalCode)
    {
        this.referalCode = referalCode;
    }

    public String getIsMobileVerified ()
    {
        return isMobileVerified;
    }

    public void setIsMobileVerified (String isMobileVerified)
    {
        this.isMobileVerified = isMobileVerified;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [lastName = "+lastName+", gender = "+gender+", profilePic = "+profilePic+", mobile = "+mobile+", dateOfBirth = "+dateOfBirth+", type = "+type+", userName = "+userName+", deviceId = "+deviceId+", isEmailVerified = "+isEmailVerified+", firstName = "+firstName+", password = "+password+", referalCode = "+referalCode+", isMobileVerified = "+isMobileVerified+", id = "+id+", email = "+email+"]";
    }
}
			
