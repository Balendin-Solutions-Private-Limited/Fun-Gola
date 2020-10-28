package com.thambola.fungola.Model;

public class NotoficationMessage {

    private String Message;

    private String Winners;

    public String getMessage ()
    {
        return Message;
    }

    public void setMessage (String Message)
    {
        this.Message = Message;
    }

    public String getWinners ()
    {
        return Winners;
    }

    public void setWinners (String Winners)
    {
        this.Winners = Winners;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Message = "+Message+", Winners = "+Winners+"]";
    }
}

