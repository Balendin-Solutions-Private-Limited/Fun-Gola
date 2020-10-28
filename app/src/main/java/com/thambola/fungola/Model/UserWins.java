package com.thambola.fungola.Model;

import java.io.Serializable;

public class UserWins implements Serializable {


    private String claimType;

    private String gameUserId;

    private String id;

    private String numberInfo;

    public String getClaimType ()
    {
        return claimType;
    }

    public void setClaimType (String claimType)
    {
        this.claimType = claimType;
    }

    public String getGameUserId ()
    {
        return gameUserId;
    }

    public void setGameUserId (String gameUserId)
    {
        this.gameUserId = gameUserId;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getNumberInfo ()
    {
        return numberInfo;
    }

    public void setNumberInfo (String numberInfo)
    {
        this.numberInfo = numberInfo;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [claimType = "+claimType+", gameUserId = "+gameUserId+", id = "+id+", numberInfo = "+numberInfo+"]";
    }
}
