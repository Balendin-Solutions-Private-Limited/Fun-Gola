package com.thambola.fungola.Store_DB;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class TicketTapedClaim implements Serializable {


    private String tapednumberslist;
    private String ticketnumber;
    private String tapedclaim;
    private String gameid;

    public TicketTapedClaim(String gameid, String ticketnumber, String tapedclaim, String tapednumberslist) {

        this.gameid=gameid;
        this.tapedclaim=tapedclaim;
        this.ticketnumber=ticketnumber;
        this.tapednumberslist=tapednumberslist;
    }


    public String getGameid() {
        return gameid;
    }


    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    public String getTicketnumber() {
        return ticketnumber;
    }



    public String getTapedclaim() {
        return tapedclaim;
    }

    public void setTapedclaim(String tapedclaim) {
        this.tapedclaim = tapedclaim;
    }

    public String getTapednumberslist() {
        return tapednumberslist;
    }

    public void setTapednumberslist(String tapednumberslist) {
        this.tapednumberslist = tapednumberslist;
    }

    @Override
    public int hashCode() {
      //  return super.hashCode();
        return Integer.parseInt(this.gameid);

    }

    @Override
    public boolean equals(@Nullable Object obj) {
        //return super.equals(obj);
        if (obj instanceof TicketTapedClaim) {
            return ((TicketTapedClaim) obj).gameid == gameid;
        }
        return false;
    }
}
