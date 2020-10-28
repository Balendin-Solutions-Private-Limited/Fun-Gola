package com.thambola.fungola.Store_DB;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class TicketTapedNumbers implements Serializable {


    private String ticketnumber;
    private String tapednumber;
    private String gameid;

    public TicketTapedNumbers(String gameid , String ticketnumber, String tapednumber) {

        this.gameid=gameid;
        this.ticketnumber=ticketnumber;
        this.tapednumber=tapednumber;
    }

    public String getTicketnumber() {
        return ticketnumber;
    }

    public String getTapednumber() {
        return tapednumber;
    }

    public String getGameid() {
        return gameid;
    }

    public void setTicketnumber(String ticketnumber) {
        this.ticketnumber = ticketnumber;
    }

    public void setTapednumber(String tapednumber) {
        this.tapednumber = tapednumber;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    @Override
    public int hashCode() {
      //  return super.hashCode();
        return Integer.parseInt(this.gameid);

    }

    @Override
    public boolean equals(@Nullable Object obj) {
        //return super.equals(obj);
        if (obj instanceof TicketTapedNumbers) {
            return ((TicketTapedNumbers) obj).gameid == gameid;
        }
        return false;
    }
}
