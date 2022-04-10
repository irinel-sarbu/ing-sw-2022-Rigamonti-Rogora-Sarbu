package model.expert;

import exceptions.*;

/**
 * Represents the stash of Coins of each player and the public stash of coins.
 */
public class CoinSupply {
    private final int maxCoins=20;
    private int numOfCoins;

    /**
     * Base constructor of CoinSupply. Initializes {@link CoinSupply#numOfCoins} at 20.
     */
    public CoinSupply(){
        this.numOfCoins=maxCoins;
    }

    /**
     * Advanced constructor of CoinSupply. Initializes {@link CoinSupply#numOfCoins} at the selected Number.
     * @param numOfCoins Is the selected Number.
     */
    public CoinSupply(int numOfCoins) {
        this.numOfCoins = numOfCoins;
    }

    /**
     *Getter for the attribute {@link CoinSupply#numOfCoins}.
     */
    public int getNumOfCoins() {
        return numOfCoins;
    }

    /**
     * Adds 1 coin to the {@link CoinSupply#numOfCoins}.
     */
    public void addCoin() {
        this.numOfCoins += 1;
    }

    /**
     * Adds a selected number of coins to the {@link CoinSupply#numOfCoins}.
     * @param numOfCoins Is the selected number of coins.
     */
    public void addCoins(int numOfCoins) {
        this.numOfCoins += numOfCoins;
    }

    /**
     * Removes a selected number of coins from {@link CoinSupply#numOfCoins}.
     * @param numOfCoins Is the selected number of coins.
     * @return How many coins has successfully removed.
     * @throws supplyEmptyException If the supply is already empty or there aren't enough coins.
     */
    public int removeCoins(int numOfCoins) throws supplyEmptyException {
        if (this.numOfCoins < numOfCoins) throw new supplyEmptyException();
        this.numOfCoins -= numOfCoins;
        return numOfCoins;
    }
}
