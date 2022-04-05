package events.types.serverToClient;

import events.Event;
import events.EventType;
import util.Wizard;

import java.util.List;

public class EChooseWizard extends Event {
    private final List<Wizard> wizards;

    public EChooseWizard(List<Wizard> wizards) {
        super(EventType.CHOOSE_WIZARD);
        this.wizards = wizards;
    }

    public List<Wizard> getAvailableWizards() {
        return wizards;
    }

    @Override
    public String toString() {
        return "EChooseWizard { wizards: " + wizards + " }";
    }
}
