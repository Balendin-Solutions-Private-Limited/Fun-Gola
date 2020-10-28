package com.thambola.fungola.Model;

public class GetUsersPerGame
{
    private String gameId;

    private String userUserName;

    private String numberOfTickets;

    private String gameTitle;

    private String id;

    private String ticketInfo;

    private String userId;

    public String getGameId ()
    {
        return gameId;
    }

    public void setGameId (String gameId)
    {
        this.gameId = gameId;
    }

    public String getUserUserName ()
{
    return userUserName;
}

    public void setUserUserName (String userUserName)
    {
        this.userUserName = userUserName;
    }

    public String getNumberOfTickets ()
    {
        return numberOfTickets;
    }

    public void setNumberOfTickets (String numberOfTickets)
    {
        this.numberOfTickets = numberOfTickets;
    }

    public String getGameTitle ()
{
    return gameTitle;
}

    public void setGameTitle (String gameTitle)
    {
        this.gameTitle = gameTitle;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getTicketInfo ()
{
    return ticketInfo;
}

    public void setTicketInfo (String ticketInfo)
    {
        this.ticketInfo = ticketInfo;
    }

    public String getUserId ()
    {
        return userId;
    }

    public void setUserId (String userId)
    {
        this.userId = userId;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [gameId = "+gameId+", userUserName = "+userUserName+", numberOfTickets = "+numberOfTickets+", gameTitle = "+gameTitle+", id = "+id+", ticketInfo = "+ticketInfo+", userId = "+userId+"]";
    }
}
