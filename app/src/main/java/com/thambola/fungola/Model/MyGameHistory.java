package com.thambola.fungola.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class MyGameHistory implements Serializable {

    private String gameId;

    private Game game;

    private String startTime;

    private String id;

    private String ticketInfo;

    private User user;

    private ArrayList<UserWins> userWins;

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

    public String getStartTime ()
    {
        return startTime;
    }

    public void setStartTime (String startTime)
    {
        this.startTime = startTime;
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

    public User getUser ()
    {
        return user;
    }

    public void setUser (User user)
    {
        this.user = user;
    }

    public ArrayList<UserWins> getUserWins ()
    {
        return userWins;
    }

    public void setUserWins (ArrayList<UserWins> userWins)
    {
        this.userWins = userWins;
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
        return "ClassPojo [gameId = "+gameId+", game = "+game+", startTime = "+startTime+", id = "+id+", ticketInfo = "+ticketInfo+", user = "+user+", userWins = "+userWins+", userId = "+userId+"]";
    }
}
