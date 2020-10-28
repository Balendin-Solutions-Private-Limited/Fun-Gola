package com.thambola.fungola.Model;

public class GetGameWinners
{
    private String timeStamp;

    private String gameType;

    private String totalPrice;

    private String gameTitle;

    private String winType;

    private String username;

    public String getTimeStamp ()
    {
        return timeStamp;
    }

    public void setTimeStamp (String timeStamp)
    {
        this.timeStamp = timeStamp;
    }

    public String getGameType ()
    {
        return gameType;
    }

    public void setGameType (String gameType)
    {
        this.gameType = gameType;
    }

    public String getTotalPrice ()
    {
        return totalPrice;
    }

    public void setTotalPrice (String totalPrice)
    {
        this.totalPrice = totalPrice;
    }

    public String getGameTitle ()
{
    return gameTitle;
}

    public void setGameTitle (String gameTitle)
    {
        this.gameTitle = gameTitle;
    }

    public String getWinType ()
    {
        return winType;
    }

    public void setWinType (String winType)
    {
        this.winType = winType;
    }

    public String getUsername ()
    {
        return username;
    }

    public void setUsername (String username)
    {
        this.username = username;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [timeStamp = "+timeStamp+", gameType = "+gameType+", totalPrice = "+totalPrice+", gameTitle = "+gameTitle+", winType = "+winType+", username = "+username+"]";
    }
}
