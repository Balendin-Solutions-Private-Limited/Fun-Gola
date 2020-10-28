package com.thambola.fungola.Model;

public class PaytmConfig
{
    private String websiteName;

    private String callBackUrl;

    private String industryType;

    private String masterKey;

    private String txnUrl;

    private String mid;

    private String responseDetails;

    private String txnStatusUrl;

    public String getWebsiteName ()
    {
        return websiteName;
    }

    public void setWebsiteName (String websiteName)
    {
        this.websiteName = websiteName;
    }

    public String getCallBackUrl ()
    {
        return callBackUrl;
    }

    public void setCallBackUrl (String callBackUrl)
    {
        this.callBackUrl = callBackUrl;
    }

    public String getIndustryType ()
    {
        return industryType;
    }

    public void setIndustryType (String industryType)
    {
        this.industryType = industryType;
    }

    public String getMasterKey ()
    {
        return masterKey;
    }

    public void setMasterKey (String masterKey)
    {
        this.masterKey = masterKey;
    }

    public String getTxnUrl ()
    {
        return txnUrl;
    }

    public void setTxnUrl (String txnUrl)
    {
        this.txnUrl = txnUrl;
    }

    public String getMid ()
    {
        return mid;
    }

    public void setMid (String mid)
    {
        this.mid = mid;
    }

    public String getResponseDetails ()
{
    return responseDetails;
}

    public void setResponseDetails (String responseDetails)
    {
        this.responseDetails = responseDetails;
    }

    public String getTxnStatusUrl ()
    {
        return txnStatusUrl;
    }

    public void setTxnStatusUrl (String txnStatusUrl)
    {
        this.txnStatusUrl = txnStatusUrl;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [websiteName = "+websiteName+", callBackUrl = "+callBackUrl+", industryType = "+industryType+", masterKey = "+masterKey+", txnUrl = "+txnUrl+", mid = "+mid+", responseDetails = "+responseDetails+", txnStatusUrl = "+txnStatusUrl+"]";
    }
}
		
