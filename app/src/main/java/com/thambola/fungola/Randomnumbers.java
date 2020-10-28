package com.thambola.fungola;

import java.util.ArrayList;

public class Randomnumbers {




    private final int intValue;
    private ArrayList randomNumbers;
    private int tiketnumber;

    public Randomnumbers(int i, ArrayList randomNumbers, int intValue) {
        this.tiketnumber=i;
        this.randomNumbers=randomNumbers;
        this.intValue=intValue;
    }
    public ArrayList getRandomNumbers() {
        return randomNumbers;
    }

    public void setRandomNumbers(ArrayList randomNumbers) {
        this.randomNumbers = randomNumbers;
    }

    public int getTiketnumber() {
        return tiketnumber;
    }

    public void setTiketnumber(int tiketnumber) {
        this.tiketnumber = tiketnumber;
    }

    public int getIntValue() {
        return intValue;
    }
}
