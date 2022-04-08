package events.types.clientToServer;

import events.Event;
import events.EventType;
import util.Wizard;

public class EWizardChosen extends Event {
    private final Wizard wizard;

    public EWizardChosen(Wizard wizard) {
        super(EventType.WIZARD_CHOSEN);
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