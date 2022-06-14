package eventSystem.events.network.server;

import eventSystem.events.Event;
import util.Wizard;

import java.util.List;

/**
 * Class that represents a server to client message.
 */
public class EChooseWizard extends Event {
    private final List<Wizard> wizards;

    /**
     * Default constructor
     *
     * @param wizards list of available wizards
     */
    public EChooseWizard(List<Wizard> wizards) {
        this.wizards = wizards;
    }

    /**
     * Getter
     *
     * @return list of available wizards
     */
    public List<Wizard> getAvailableWizards() {
        return wizards;
    }

    @Override
    public String toString() {
        return "EChooseWizard { wizards: " + wizards + " }";
    }
}
