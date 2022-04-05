package model.expert;

import exceptions.DiningRoomFullException;
import exceptions.*;
import util.Color;

public class CoinSupply {
    private final int maxCoins=20;
    private int numOfCoins;

    public CoinSupply(){
        this.numOfCoins=maxCoins;
    }

    public CoinSupply(int numOfCoins) {
        this.numOfCoins = numOfCoins;
    }

    public int getNumOfCoins() {
        return numOfCoins;
    }

    public void addCoin() {
        this.numOfCoins += 1;
    }

    public void addCoins(int numOfCoins) {
        this.numOfCoins += numOfCoins;
    }

    public int removeCoins(int numOfCoins) throws supplyEmptyException {
        if (this.numOfCoins == 0) throw new supplyEmptyException();
        this.numOfCoins -= numOfCoins;
        return numOfCoins;
    }
}
