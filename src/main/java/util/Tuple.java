package util;

import java.util.Map.Entry;

public class Tuple<K, V> implements Entry<K, V> {
    private final K key;
    private V value;

    /**
     * Constructor of the Tuple class
     */
    public Tuple(final K key, final V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Getter for the Key Attribute
     */
    public K getKey() {
        return key;
    }

    /**
     * Getter for the Value Attribute
     */
    public V getValue() {
        return value;
    }

    /**
     * Setter for the Value Attribute
     */
    public V setValue(final V value) {
        final V oldValue = this.value;
        this.value = value;
        return oldValue;
    }

    /**
     * Overrides toString()
     */
    @Override
    public String toString() {
        return "{ " + key + ", " + value + " }";
    }
}