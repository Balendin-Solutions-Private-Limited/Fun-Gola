package com.thambola.fungola.Store_DB;

import com.thambola.fungola.Model.StartGame;

import java.io.Serializable;

public class Store_GameResponse implements Serializable {


    private String encrypttickets;
    private StartGame store_gameResponse;

    public Store_GameResponse(String encrypted, StartGame startGame1) {

        this.encrypttickets=encrypted;
        this.store_gameResponse=startGame1;
    }



    public String getEncrypttickets() {
        return encrypttickets;
    }

    public void setEncrypttickets(String encrypttickets) {
        this.encrypttickets = encrypttickets;
    }

    public StartGame getStore_gameResponse() {
        return store_gameResponse;
    }

    public void setStore_gameResponse(StartGame store_gameResponse) {
        this.store_gameResponse = store_gameResponse;
    }
}
