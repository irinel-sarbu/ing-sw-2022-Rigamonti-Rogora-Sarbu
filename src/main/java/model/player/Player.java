package model.player;

import exceptions.AssistantNotInDeckException;
import exceptions.InvalidTransactionException;
import exceptions.NotEnoughCoinsException;
import model.board.SchoolBoard;
import util.Wizard;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

public class Player {
    private final String name;
    private SchoolBoard schoolBoard;
    private Wizard assistantCardBack;
    private List<Assistant> assistantDeck;
    private Stack<Assistant> foldDeck;
    private int coins;

    public Player(String name, Wizard assistantCardBack) {
        this.name = name;
        this.schoolBoard = new SchoolBoard();
        this.assistantCardBack = assistantCardBack;
        this.assistantDeck = new ArrayList<>();
        this.foldDeck = new Stack<>();
        this.coins = 0;
    }

    public void pushFoldDeck(Assistant assistantCard) {
        foldDeck.push(assistantCard);
    }

    public Assistant getTopFoldDeck() {
        return foldDeck.peek();
    }

    public List<Assistant> getAssistants() {
        return new ArrayList<>(assistantDeck);
    }

    public void removeCard(Assistant assistantCard) throws AssistantNotInDeckException {
        if (!assistantDeck.contains(assistantCard)) throw new AssistantNotInDeckException();
        assistantDeck.remove(assistantCard);
    }

    public SchoolBoard getSchoolBoard() {
        return this.schoolBoard;
    }

    public int getCoins() {
        return coins;
    }

    public void addCoins(int coins) {
        if (coins <= 0) return;
        this.coins += coins;
    }

    public void removeCoin(int coins) throws NotEnoughCoinsException {
        if (coins <= 0) return;
        if (coins > this.coins) throw new NotEnoughCoinsException();
        this.coins -= coins;
    }

    public String getName() {
        return name;
    }
}
