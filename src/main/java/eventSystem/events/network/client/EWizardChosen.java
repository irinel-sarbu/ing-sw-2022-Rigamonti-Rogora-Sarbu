package eventSystem.events.network.client;

import eventSystem.events.network.NetworkEvent;
import util.Wizard;

public final class EWizardChosen extends NetworkEvent {
    private final Wizard wizard;

    public EWizardChosen(Wizard wizard) {
        this.wizard = wizard;
    }

    public Wizard getWizard() {
        return wizard;
    }

    @Override
    public String toString() {
        return "EWizardChosen { wizard: " + wizard + " }";
    }
}