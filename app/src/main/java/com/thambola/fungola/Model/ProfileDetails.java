package com.thambola.fungola.Model;

import java.io.Serializable;

public class ProfileDetails implements Serializable
{
    private String lastName;

    private String walletTransactions;

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

    private String isMobileVerified;

    private String referralCodeToBeShared;

    private String id;

    private String referralCodeApplied;

    private String email;

    public String getLastName ()
    {
        return lastName;
    }

    public void setLastName (String lastName)
    {
        this.lastName = lastName;
    }

    public String getWalletTransactions ()
{
    return walletTransactions;
}

    public void setWalletTransactions (String walletTransactions)
    {
        this.walletTransactions = walletTransactions;
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

    public String getIsMobileVerified ()
    {
        return isMobileVerified;
    }

    public void setIsMobileVerified (String isMobileVerified)
    {
        this.isMobileVerified = isMobileVerified;
    }

    public String getReferralCodeToBeShared ()
    {
        return referralCodeToBeShared;
    }

    public void setReferralCodeToBeShared (String referralCodeToBeShared)
    {
        this.referralCodeToBeShared = referralCodeToBeShared;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getReferralCodeApplied ()
    {
        return referralCodeApplied;
    }

    public void setReferralCodeApplied (String referralCodeApplied)
    {
        this.referralCodeApplied = referralCodeApplied;
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
        return "ClassPojo [lastName = "+lastName+", walletTransactions = "+walletTransactions+", gender = "+gender+", profilePic = "+profilePic+", mobile = "+mobile+", dateOfBirth = "+dateOfBirth+", type = "+type+", userName = "+userName+", deviceId = "+deviceId+", isEmailVerified = "+isEmailVerified+", firstName = "+firstName+", password = "+password+", isMobileVerified = "+isMobileVerified+", referralCodeToBeShared = "+referralCodeToBeShared+", id = "+id+", referralCodeApplied = "+referralCodeApplied+", email = "+email+"]";
    }
}
	
		