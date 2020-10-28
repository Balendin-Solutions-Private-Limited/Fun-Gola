package com.thambola.fungola.Model;

import java.io.Serializable;

public class TicketPurchased implements Serializable
{
    private String gameId;

    private Game game;

    private String numberOfTickets;

    private String id;

    private User user;

    private String userId;

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

    @Override
    public String toString()
    {
        return "ClassPojo [gameId = "+gameId+", game = "+game+", numberOfTickets = "+numberOfTickets+", id = "+id+", user = "+user+", userId = "+userId+"]";
    }
}