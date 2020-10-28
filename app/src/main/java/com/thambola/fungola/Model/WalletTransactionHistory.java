package com.thambola.fungola.Model;

public class WalletTransactionHistory
{
    private String transactionType;

    private String transactionSource;

    private String amount;

    private boolean isCoin;

    private String information;

    private String id;

    private User user;

    private String userId;

    private String addedOn;

    public String getTransactionType ()
    {
        return transactionType;
    }

    public void setTransactionType (String transactionType)
    {
        this.transactionType = transactionType;
    }

    public String getTransactionSource ()
    {
        return transactionSource;
    }

    public void setTransactionSource (String transactionSource)
    {
        this.transactionSource = transactionSource;
    }

    public String getAmount ()
    {
        return amount;
    }

    public void setAmount (String amount)
    {
        this.amount = amount;
    }

    public boolean getIsCoin ()
    {
        return isCoin;
    }

    public void setIsCoin (boolean isCoin)
    {
        this.isCoin = isCoin;
    }

    public String getInformation ()
    {
        return information;
    }

    public void setInformation (String information)
    {
        this.information = information;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public User getUser ()
{
    return user;
}

    public void setUser (User user)
    {
        this.user = user;
    }

    public String getUserId ()
    {
        return userId;
    }

    public void setUserId (String userId)
    {
        this.userId = userId;
    }

    public String getAddedOn ()
    {
        return addedOn;
    }

    public void setAddedOn (String addedOn)
    {
        this.addedOn = addedOn;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [transactionType = "+transactionType+", transactionSource = "+transactionSource+", amount = "+amount+", isCoin = "+isCoin+", information = "+information+", id = "+id+", user = "+user+", userId = "+userId+", addedOn = "+addedOn+"]";
    }
}

