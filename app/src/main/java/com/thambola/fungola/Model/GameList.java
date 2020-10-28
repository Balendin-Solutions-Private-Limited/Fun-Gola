package com.thambola.fungola.Model;

import androidx.annotation.Nullable;

public class GameList {

    private int gameId;

    private String gameType;

    private int numberOfTicketsPurchasedByUser;

    private String numberOfUsersPurchased;

    private String numberOfTicketsBuyed;

    private String totalPrice;

    private String gameStartTime;

    private String price;

    private String minUser;

    private int maxTicketsPerGamer;

    private String title;

    private String maxUser;

    private String gameStatus;

    private String lineOne;

    private String lineThree;

    private String lineTwo;

    private String fullHousee;

    private String charge;

    private String firstFive;



    public int getGameId ()
    {
        return gameId;
    }

    public void setGameId (int gameId)
    {
        this.gameId = gameId;
    }

    public String getGameType ()
    {
        return gameType;
    }

    public void setGameType (String gameType)
    {
        this.gameType = gameType;
    }

    public int getNumberOfTicketsPurchasedByUser ()
    {
        return numberOfTicketsPurchasedByUser;
    }

    public void setNumberOfTicketsPurchasedByUser (int numberOfTicketsPurchasedByUser)
    {
        this.numberOfTicketsPurchasedByUser = numberOfTicketsPurchasedByUser;
    }

    public String getNumberOfUsersPurchased ()
    {
        return numberOfUsersPurchased;
    }

    public void setNumberOfUsersPurchased (String numberOfUsersPurchased)
    {
        this.numberOfUsersPurchased = numberOfUsersPurchased;
    }

    public String getNumberOfTicketsBuyed ()
    {
        return numberOfTicketsBuyed;
    }

    public void setNumberOfTicketsBuyed (String numberOfTicketsBuyed)
    {
        this.numberOfTicketsBuyed = numberOfTicketsBuyed;
    }

    public String getTotalPrice ()
    {
        return totalPrice;
    }

    public void setTotalPrice (String totalPrice)
    {
        this.totalPrice = totalPrice;
    }

    public String getGameStartTime ()
    {
        return gameStartTime;
    }

    public void setGameStartTime (String gameStartTime)
    {
        this.gameStartTime = gameStartTime;
    }

    public String getPrice ()
    {
        return price;
    }

    public void setPrice (String price)
    {
        this.price = price;
    }

    public String getMinUser ()
    {
        return minUser;
    }

    public void setMinUser (String minUser)
    {
        this.minUser = minUser;
    }

    public int getMaxTicketsPerGamer ()
    {
        return maxTicketsPerGamer;
    }

    public void setMaxTicketsPerGamer (int maxTicketsPerGamer)
    {
        this.maxTicketsPerGamer = maxTicketsPerGamer;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getMaxUser ()
    {
        return maxUser;
    }

    public void setMaxUser (String maxUser)
    {
        this.maxUser = maxUser;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    public String getLineOne() {
        return lineOne;
    }

    public void setLineOne(String lineOne) {
        this.lineOne = lineOne;
    }

    public String getLineThree() {
        return lineThree;
    }

    public void setLineThree(String lineThree) {
        this.lineThree = lineThree;
    }

    public String getLineTwo() {
        return lineTwo;
    }

    public void setLineTwo(String lineTwo) {
        this.lineTwo = lineTwo;
    }

    public String getFullHousee() {
        return fullHousee;
    }

    public void setFullHousee(String fullHousee) {
        this.fullHousee = fullHousee;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getFirstFive() {
        return firstFive;
    }

    public void setFirstFive(String firstFive) {
        this.firstFive = firstFive;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [gameId = "+gameId+", gameType = "+gameType+", numberOfTicketsPurchasedByUser = "+numberOfTicketsPurchasedByUser+", numberOfUsersPurchased = "+numberOfUsersPurchased+", numberOfTicketsBuyed = "+numberOfTicketsBuyed+", totalPrice = "+totalPrice+", gameStartTime = "+gameStartTime+", price = "+price+", minUser = "+minUser+", maxTicketsPerGamer = "+maxTicketsPerGamer+", title = "+title+", maxUser = "+maxUser+"]";
    }

    @Override
    public boolean equals(@Nullable Object o) {
      //  return super.equals(obj);
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameList gameList = (GameList) o;
        return gameType.equals(gameList.gameType);

        /*return gameList == simpson.age &&
                weight == simpson.weight &&
                name.equals(simpson.name);*/
    }

    public boolean gamerunning;

    public String convertedTime,convertedDate;

    public boolean isGamerunning() {
        return gamerunning;
    }

    public void setGamerunning(boolean gamerunning) {
        this.gamerunning = gamerunning;
    }

    public String getConvertedTime() {
        return convertedTime;
    }

    public void setConvertedTime(String convertedTime) {
        this.convertedTime = convertedTime;
    }

    public String getConvertedDate() {
        return convertedDate;
    }

    public void setConvertedDate(String convertedDate) {
        this.convertedDate = convertedDate;
    }
}
