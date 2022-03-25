package model.player;

import exceptions.AssistantNotInDeckException;
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

    public Player(String name, Wizard wizard) {
        this.name = name;
        this.schoolBoard = new SchoolBoard();
        this.assistantDeck = Assistant.getWizardDeck(wizard);
        this.foldDeck = new Stack<>();
    }

    public void pushFoldDeck(Assistant assistantCard) {
        foldDeck.push(assistantCard);
    }

    public Assistant peekFoldDeck() {
        return foldDeck.peek();
    }

    public List<Assistant> getAssistants() {
        return new ArrayList<>(assistantDeck);
    }

    public void removeCard(Assistant assistantCard) throws AssistantNotInDeckException {
        if (!assistantDeck.contains(assistantCard)) throw new AssistantNotInDeckException();
        assistantDeck.remove(assistantCard);
    }

    public void removeCard(int assistantCardID) throws AssistantNotInDeckException {
        if(assistantCardID >= assistantDeck.size()) throw new AssistantNotInDeckException();
        assistantDeck.remove(assistantCardID);
    }

    public SchoolBoard getSchoolBoard() {
        return schoolBoard;
    }

    public String getName() {
        return name;
    }
}
