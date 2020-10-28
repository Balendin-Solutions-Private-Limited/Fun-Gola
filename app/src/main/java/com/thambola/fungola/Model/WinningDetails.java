package com.thambola.fungola.Model;

public class WinningDetails {


    private float money;

    private String winType;

    public float getMoney ()
    {
        return money;
    }

    public void setMoney (float money)
    {
        this.money = money;
    }

    public String getWinType ()
    {
        return winType;
    }

    public void setWinType (String winType)
    {
        this.winType = winType;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [money = "+money+", winType = "+winType+"]";
    }
}

