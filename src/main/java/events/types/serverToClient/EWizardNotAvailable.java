package events.types.serverToClient;

import events.Event;
import events.EventType;
import util.Wizard;

import java.util.List;

public class EWizardNotAvailable extends Event {
    private final List<Wizard> wizards;

    public EWizardNotAvailable(List<Wizard> wizards) {
        super(EventType.WIZARD_NOT_AVAILABLE);
        this.wizards = wizards;
    }

    public List<Wizard> getAvailableWizards() {
        return wizards;
    }
}
