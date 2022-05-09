package model.board;


import exceptions.supplyEmptyException;
import model.expert.CoinSupply;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CoinSupplyTest {
    private CoinSupply coinSupply1, coinSupply2;

    @BeforeEach
    public void setup() {
        coinSupply1 = new CoinSupply();
        coinSupply2 = new CoinSupply(30);
    }

    @Test
    public void GetCoins() {
        assertEquals(20, coinSupply1.getNumOfCoins());
        assertEquals(30, coinSupply2.getNumOfCoins());
        assertTrue(coinSupply1.toString().length() != 0);
        assertTrue(coinSupply2.toString().length() != 0);
    }

    @Test
    public void setCoins() {
        coinSupply1.addCoin();
        assertEquals(21, coinSupply1.getNumOfCoins());
        coinSupply2.addCoins(2);
        assertEquals(32, coinSupply2.getNumOfCoins());
    }

    @Test
    public void removeCoins() {
        try {
            coinSupply1.removeCoins(2);
        } catch (supplyEmptyException e) {
            fail();
        }
        assertEquals(18, coinSupply1.getNumOfCoins());
        assertThrows(supplyEmptyException.class, () -> coinSupply1.removeCoins(200));
    }
}
