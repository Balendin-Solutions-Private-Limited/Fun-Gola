package com.thambola.fungola.Model;

public class GenerateCheckSum
{
    private String orderId;

    private String checkSum;

    private PaytmConfig paytmConfig;

    public String getOrderId ()
    {
        return orderId;
    }

    public void setOrderId (String orderId)
    {
        this.orderId = orderId;
    }

    public String getCheckSum ()
    {
        return checkSum;
    }

    public void setCheckSum (String checkSum)
    {
        this.checkSum = checkSum;
    }

    public PaytmConfig getPaytmConfig ()
    {
        return paytmConfig;
    }

    public void setPaytmConfig (PaytmConfig paytmConfig)
    {
        this.paytmConfig = paytmConfig;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [orderId = "+orderId+", checkSum = "+checkSum+", paytmConfig = "+paytmConfig+"]";
    }
}