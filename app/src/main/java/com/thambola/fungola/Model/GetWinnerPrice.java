package com.thambola.fungola.Model;

import java.util.ArrayList;

public class GetWinnerPrice {


    private String gameType;

    private String lineTwo;

    private ArrayList<WinningDetails> winningDetails;

    private String firstFive;

    private float gameTotalPrice;

    private float yourTotalPrice;

    private String lineOne;

    private String fullName;

    private String gameTitle;

    private String lineThree;

    private String userName;

    private String fullHousee;

    public String getGameType ()
    {
        return gameType;
    }

    public void setGameType (String gameType)
    {
        this.gameType = gameType;
    }

    public String getLineTwo ()
    {
        return lineTwo;
    }

    public void setLineTwo (String lineTwo)
    {
        this.lineTwo = lineTwo;
    }

    public ArrayList<WinningDetails> getWinningDetails ()
    {
        return winningDetails;
    }

    public void setWinningDetails (ArrayList<WinningDetails> winningDetails)
    {
        this.winningDetails = winningDetails;
    }

    public String getFirstFive ()
    {
        return firstFive;
    }

    public void setFirstFive (String firstFive)
    {
        this.firstFive = firstFive;
    }

    public float getGameTotalPrice ()
    {
        return gameTotalPrice;
    }

    public void setGameTotalPrice (float gameTotalPrice)
    {
        this.gameTotalPrice = gameTotalPrice;
    }

    public float getYourTotalPrice ()
    {
        return yourTotalPrice;
    }

    public void setYourTotalPrice (float yourTotalPrice)
    {
        this.yourTotalPrice = yourTotalPrice;
    }

    public String getLineOne ()
    {
        return lineOne;
    }

    public void setLineOne (String lineOne)
    {
        this.lineOne = lineOne;
    }

    public String getFullName ()
    {
        return fullName;
    }

    public void setFullName (String fullName)
    {
        this.fullName = fullName;
    }

    public String getGameTitle ()
    {
        return gameTitle;
    }

    public void setGameTitle (String gameTitle)
    {
        this.gameTitle = gameTitle;
    }

    public String getLineThree ()
    {
        return lineThree;
    }

    public void setLineThree (String lineThree)
    {
        this.lineThree = lineThree;
    }

    public String getUserName ()
    {
        return userName;
    }

    public void setUserName (String userName)
    {
        this.userName = userName;
    }

    public String getFullHousee ()
    {
        return fullHousee;
    }

    public void setFullHousee (String fullHousee)
    {
        this.fullHousee = fullHousee;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [gameType = "+gameType+", lineTwo = "+lineTwo+", winningDetails = "+winningDetails+", firstFive = "+firstFive+", gameTotalPrice = "+gameTotalPrice+", yourTotalPrice = "+yourTotalPrice+", lineOne = "+lineOne+", fullName = "+fullName+", gameTitle = "+gameTitle+", lineThree = "+lineThree+", userName = "+userName+", fullHousee = "+fullHousee+"]";
    }
}
