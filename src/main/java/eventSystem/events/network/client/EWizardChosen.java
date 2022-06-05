package eventSystem.events.network.client;

import eventSystem.events.network.NetworkEvent;
import util.Wizard;

/**
 * Class that represents a client to server message.
 */
public final class EWizardChosen extends NetworkEvent {
    private final Wizard wizard;

    /**
     * Default constructor
     *
     * @param wizard chosen wizard
     */
    public EWizardChosen(Wizard wizard) {
        this.wizard = wizard;
    }

    /**
     * Getter
     *
     * @return wizard chosen
     */
    public Wizard getWizard() {
        return wizard;
    }

    @Override
    public String toString() {
        return "EWizardChosen { wizard: " + wizard + " }";
    }
}