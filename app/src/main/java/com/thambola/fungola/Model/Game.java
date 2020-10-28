package com.thambola.fungola.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable
{
    private String gameType;

    private String gameTotalPrice;

    private String randomGameNumbers;

    private String title;

    private String modifiedById;

    private String minUser;

    private ArrayList<TicketPurchased> ticketPurchaseds;

    private String lineOne;

    private String modifiedBy;

    private String id;

    private String lineThree;

    private String createdById;

    private String maxUser;

    private String charge;

    private String ticketPrice;

    private String firstFive;

    private String gameStatus;

    private String maxTicketsPerGamer;

    private String fullHousee;

    private String startDateTime;

    private String lineTwo;

    private String createdDate;

    private String createdBy;

    private String modifiedDate;

    private String status;

    public String getGameType ()
    {
        return gameType;
    }

    public void setGameType (String gameType)
    {
        this.gameType = gameType;
    }

    public String getGameTotalPrice ()
    {
        return gameTotalPrice;
    }

    public void setGameTotalPrice (String gameTotalPrice)
    {
        this.gameTotalPrice = gameTotalPrice;
    }

    public String getRandomGameNumbers ()
    {
        return randomGameNumbers;
    }

    public void setRandomGameNumbers (String randomGameNumbers)
    {
        this.randomGameNumbers = randomGameNumbers;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getModifiedById ()
{
    return modifiedById;
}

    public void setModifiedById (String modifiedById)
    {
        this.modifiedById = modifiedById;
    }

    public String getMinUser ()
    {
        return minUser;
    }

    public void setMinUser (String minUser)
    {
        this.minUser = minUser;
    }

    public ArrayList<TicketPurchased> getTicketPurchaseds ()
    {
        return ticketPurchaseds;
    }

    public void setTicketPurchaseds (ArrayList<TicketPurchased> ticketPurchaseds)
    {
        this.ticketPurchaseds = ticketPurchaseds;
    }

    public String getLineOne ()
    {
        return lineOne;
    }

    public void setLineOne (String lineOne)
    {
        this.lineOne = lineOne;
    }

    public String getModifiedBy ()
{
    return modifiedBy;
}

    public void setModifiedBy (String modifiedBy)
    {
        this.modifiedBy = modifiedBy;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getLineThree ()
    {
        return lineThree;
    }

    public void setLineThree (String lineThree)
    {
        this.lineThree = lineThree;
    }

    public String getCreatedById ()
    {
        return createdById;
    }

    public void setCreatedById (String createdById)
    {
        this.createdById = createdById;
    }

    public String getMaxUser ()
    {
        return maxUser;
    }

    public void setMaxUser (String maxUser)
    {
        this.maxUser = maxUser;
    }

    public String getCharge ()
    {
        return charge;
    }

    public void setCharge (String charge)
    {
        this.charge = charge;
    }

    public String getTicketPrice ()
    {
        return ticketPrice;
    }

    public void setTicketPrice (String ticketPrice)
    {
        this.ticketPrice = ticketPrice;
    }

    public String getFirstFive ()
    {
        return firstFive;
    }

    public void setFirstFive (String firstFive)
    {
        this.firstFive = firstFive;
    }

    public String getGameStatus ()
    {
        return gameStatus;
    }

    public void setGameStatus (String gameStatus)
    {
        this.gameStatus = gameStatus;
    }

    public String getMaxTicketsPerGamer ()
    {
        return maxTicketsPerGamer;
    }

    public void setMaxTicketsPerGamer (String maxTicketsPerGamer)
    {
        this.maxTicketsPerGamer = maxTicketsPerGamer;
    }

    public String getFullHousee ()
    {
        return fullHousee;
    }

    public void setFullHousee (String fullHousee)
    {
        this.fullHousee = fullHousee;
    }

    public String getStartDateTime ()
    {
        return startDateTime;
    }

    public void setStartDateTime (String startDateTime)
    {
        this.startDateTime = startDateTime;
    }

    public String getLineTwo ()
    {
        return lineTwo;
    }

    public void setLineTwo (String lineTwo)
    {
        this.lineTwo = lineTwo;
    }

    public String getCreatedDate ()
    {
        return createdDate;
    }

    public void setCreatedDate (String createdDate)
    {
        this.createdDate = createdDate;
    }

    public String getCreatedBy ()
{
    return createdBy;
}

    public void setCreatedBy (String createdBy)
    {
        this.createdBy = createdBy;
    }

    public String getModifiedDate ()
{
    return modifiedDate;
}

    public void setModifiedDate (String modifiedDate)
    {
        this.modifiedDate = modifiedDate;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [gameType = "+gameType+", gameTotalPrice = "+gameTotalPrice+", randomGameNumbers = "+randomGameNumbers+", title = "+title+", modifiedById = "+modifiedById+", minUser = "+minUser+", ticketPurchaseds = "+ticketPurchaseds+", lineOne = "+lineOne+", modifiedBy = "+modifiedBy+", id = "+id+", lineThree = "+lineThree+", createdById = "+createdById+", maxUser = "+maxUser+", charge = "+charge+", ticketPrice = "+ticketPrice+", firstFive = "+firstFive+", gameStatus = "+gameStatus+", maxTicketsPerGamer = "+maxTicketsPerGamer+", fullHousee = "+fullHousee+", startDateTime = "+startDateTime+", lineTwo = "+lineTwo+", createdDate = "+createdDate+", createdBy = "+createdBy+", modifiedDate = "+modifiedDate+", status = "+status+"]";
    }
}