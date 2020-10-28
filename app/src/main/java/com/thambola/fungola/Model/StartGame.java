package com.thambola.fungola.Model;

import java.io.Serializable;

public class StartGame implements Serializable
{
    private String gameUserId;

    private String ticketInfo;

    private TicketPurchased ticketPurchased;

    public String getGameUserId ()
    {
        return gameUserId;
    }

    public void setGameUserId (String gameUserId)
    {
        this.gameUserId = gameUserId;
    }

    public String getTicketInfo ()
    {
        return ticketInfo;
    }

    public void setTicketInfo (String ticketInfo)
    {
        this.ticketInfo = ticketInfo;
    }

    public TicketPurchased getTicketPurchased ()
    {
        return ticketPurchased;
    }

    public void setTicketPurchased (TicketPurchased ticketPurchased)
    {
        this.ticketPurchased = ticketPurchased;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [gameUserId = "+gameUserId+", ticketInfo = "+ticketInfo+", ticketPurchased = "+ticketPurchased+"]";
    }
}
			
