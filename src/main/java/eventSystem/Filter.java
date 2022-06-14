package eventSystem;

/**
 * This class is a Filter class for EventHandlers
 */
public final class Filter {
    private final String filter;

    /**
     * Default constructor
     */
    public Filter(String filter) {
        this.filter = filter;
    }

    /**
     * @return filter
     */
    public String getScope() {
        return filter;
    }
}
