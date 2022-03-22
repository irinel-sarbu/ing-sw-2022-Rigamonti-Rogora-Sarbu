package model.player;

import exceptions.AssistantNotInDeckException;
import exceptions.NotEnoughCoinsException;
import model.board.Assistant;
import model.board.SchoolBoard;
import util.Wizard;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

public class Player {
    private final String name;
    private final SchoolBoard schoolBoard;
    private final List<Assistant> assistantDeck;
    private final Stack<Assistant> foldDeck;
    private int coins;

    public Player(String name, Wizard wizard) {
        this.name = name;
        this.schoolBoard = new SchoolBoard();
        this.assistantDeck = Assistant.getWizardDeck(wizard);
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

    public void removeCoins(int coins) throws NotEnoughCoinsException {
        if (coins <= 0) return;
        if (coins > this.coins) throw new NotEnoughCoinsException();
        this.coins -= coins;
    }

    public String getName() {
        return name;
    }
}
