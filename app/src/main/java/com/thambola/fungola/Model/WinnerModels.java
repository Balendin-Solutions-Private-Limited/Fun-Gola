package com.thambola.fungola.Model;

public class WinnerModels {

    private String Username;

    private String WinType;

    private String TotalPrice;

    public String getUsername ()
    {
        return Username;
    }

    public void setUsername (String Username)
    {
        this.Username = Username;
    }

    public String getWinType ()
    {
        return WinType;
    }

    public void setWinType (String WinType)
    {
        this.WinType = WinType;
    }

    public String getTotalPrice ()
    {
        return TotalPrice;
    }

    public void setTotalPrice (String TotalPrice)
    {
        this.TotalPrice = TotalPrice;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Username = "+Username+", WinType = "+WinType+", TotalPrice = "+TotalPrice+"]";
    }
}
