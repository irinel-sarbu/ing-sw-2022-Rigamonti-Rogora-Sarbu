package model;

import exceptions.AssistantNotInDeckException;
import model.board.Assistant;
import model.board.SchoolBoard;
import util.TowerColor;
import util.Wizard;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

public class Player {
    private final String name;
    private final SchoolBoard schoolBoard;
    private final List<Assistant> assistantDeck;
    private Assistant foldCard;
    private final TowerColor color;

    public Player(String name, Wizard wizard, TowerColor towerColor) {
        this.name = name;
        this.schoolBoard = new SchoolBoard(this);
        this.assistantDeck = Assistant.getWizardDeck(wizard);
        this.foldCard = null;
        this.color = towerColor;
    }

    public void pushFoldDeck(Assistant assistantCard) {
        this.foldCard = assistantCard;
    }

    public Assistant peekFoldDeck() {
        return foldCard;
    }

    public void clearFoldDeck() {
        foldCard = null;
    }

    public List<Assistant> getAssistants() {
        return new ArrayList<>(assistantDeck);
    }

    public Assistant removeCard(Assistant assistantCard) throws AssistantNotInDeckException {
        if (!assistantDeck.contains(assistantCard)) throw new AssistantNotInDeckException();
        assistantDeck.remove(assistantCard);
        return assistantCard;
    }

    public void removeCard(int assistantCardID) throws AssistantNotInDeckException {
        if (assistantCardID >= assistantDeck.size()) throw new AssistantNotInDeckException();
        assistantDeck.remove(assistantCardID);
    }

    public SchoolBoard getSchoolBoard() {
        return schoolBoard;
    }

    public TowerColor getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
}
