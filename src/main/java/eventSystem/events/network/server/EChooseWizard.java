package eventSystem.events.network.server;

import eventSystem.events.Event;
import util.Wizard;

import java.util.List;

public class EChooseWizard extends Event {
    private final List<Wizard> wizards;

    public EChooseWizard(List<Wizard> wizards) {
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
