package com.thambola.fungola.Model;

public class BuyTicket
{
    private String gameId;

    private Game game;

    private String numberOfTickets;

    private String id;

    private String userId;

    private User user;

    public String getGameId ()
    {
        return gameId;
    }

    public void setGameId (String gameId)
    {
        this.gameId = gameId;
    }

    public Game getGame ()
    {
        return game;
    }

    public void setGame (Game game)
    {
        this.game = game;
    }

    public String getNumberOfTickets ()
    {
        return numberOfTickets;
    }

    public void setNumberOfTickets (String numberOfTickets)
    {
        this.numberOfTickets = numberOfTickets;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getUserId ()
    {
        return userId;
    }

    public void setUserId (String userId)
    {
        this.userId = userId;
    }

    public User getUser ()
{
    return user;
}

    public void setUser (User user)
    {
        this.user = user;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [gameId = "+gameId+", game = "+game+", numberOfTickets = "+numberOfTickets+", id = "+id+", userId = "+userId+", user = "+user+"]";
    }
}