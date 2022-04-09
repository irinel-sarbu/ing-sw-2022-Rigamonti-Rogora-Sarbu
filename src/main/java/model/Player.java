package model;

import exceptions.AssistantNotInDeckException;
import model.board.Assistant;
import model.board.SchoolBoard;
import util.TowerColor;
import util.Wizard;

import java.util.*;

/**
 * Player Class represents each player inside the model.
 */
public class Player implements Comparator<Player>, Comparable<Player> {
    private final String name;
    private final SchoolBoard schoolBoard;
    private final List<Assistant> assistantDeck;
    private Assistant foldCard;
    private final TowerColor color;
    private boolean disconnected;

    /**
     * Player constructor, initializes all attributes.
     * @param name Unique name passed to the constructor.
     * @param wizard Unique {@link Wizard} passed to the constructor.
     * @param towerColor Unique {@link TowerColor} passed to the constructor.
     */
    public Player(String name, Wizard wizard, TowerColor towerColor) {
        this.name = name;
        this.schoolBoard = new SchoolBoard(this);
        this.assistantDeck = Assistant.getWizardDeck(wizard);
        this.foldCard = null;
        this.color = towerColor;
        this.disconnected = false;
    }

    /**
     * Setter for the attribute {@link Player#disconnected}.
     * @param disconnected Boolean value which will be given to the attribute {@link Player#disconnected}.
     */
    public synchronized void setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
    }

    /**
     * Getter for the attribute {@link Player#disconnected}.
     */
    public synchronized boolean isDisconnected() {
        return this.disconnected;
    }

    /**
     * Changes {@link Player#foldCard} to the last played Assistant Card.
     * @param assistantCard Last played Assistant Card.
     */
    public void pushFoldDeck(Assistant assistantCard) {
        this.foldCard = assistantCard;
    }

    /**
     * Getter for the attribute {@link Player#foldCard}.
     */
    public Assistant peekFoldDeck() {
        return foldCard;
    }

    /**
     * Clear the last played Assistant card from {@link Player#foldCard} and sets it to null.
     */
    public void clearFoldDeck() {
        foldCard = null;
    }

    /**
     * Getter for the attribute {@link Player#assistantDeck}.
     * @return A copy of the list of remaining Assistants.
     */
    public List<Assistant> getAssistants() {
        return new ArrayList<>(assistantDeck);
    }

    /**
     * Removes a selected Assistant from {@link Player#assistantDeck}.
     * @param assistantCard Is the selected Assistant which needs to be removed.
     * @return The selected assistant which needs to be removed.
     * @throws AssistantNotInDeckException If the selected Assistant is not present.
     */
    public Assistant removeCard(Assistant assistantCard) throws AssistantNotInDeckException {
        if (!assistantDeck.contains(assistantCard)) throw new AssistantNotInDeckException();
        assistantDeck.remove(assistantCard);
        //TODO : Check return type
        return assistantCard;
    }

    /**
     * Removes a selected Assistant from {@link Player#assistantDeck}.
     * @param assistantCardID Is the ID correspondent to the selected Assistant which needs to be removed.
     * @throws AssistantNotInDeckException if the selected Assistant is not present.
     */
    public void removeCard(int assistantCardID) throws AssistantNotInDeckException {
        if (assistantCardID >= assistantDeck.size()) throw new AssistantNotInDeckException();
        assistantDeck.remove(assistantCardID);
    }

    /**
     * Getter for the attribute {@link Player#schoolBoard}.
     */
    public SchoolBoard getSchoolBoard() {
        return schoolBoard;
    }

    /**
     *Getter for the Unique {@link TowerColor} stored in the attribute {@link Player#color}.
     */
    public TowerColor getColor() {
        return color;
    }

    /**
     * Getter for the Unique name stored in the attribute {@link Player#name}.
     */
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return name.equals(player.name);
    }

    @Override
    public int compareTo(Player other) {
        return compare(this, other);
    }

    @Override
    public int compare(Player player, Player t1) {
        if (t1.foldCard == null) {
            return player.foldCard != null ? -1 : 0;
        } else {
            return player.foldCard == null ? 1 :
                    player.foldCard.getValue() - t1.foldCard.getValue();
        }
    }
}
