package eventSystem;

public final class Filter {
    private final String filter;

    public Filter(String filter) {
        this.filter = filter;
    }

    public String getScope() {
        return filter;
    }
}
