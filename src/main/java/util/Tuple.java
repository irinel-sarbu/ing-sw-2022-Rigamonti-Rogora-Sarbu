package util;

import java.util.Map.Entry;

public class Tuple<K, V> implements Entry<K, V> {
    private final K key;
    private V value;

    public Tuple(final K key, final V value) {
        this.key = key;
        this.value = value;
    }

    public synchronized K getKey() {
        return key;
    }

    public synchronized V getValue() {
        return value;
    }

    public synchronized V setValue(final V value) {
        final V oldValue = this.value;
        this.value = value;
        return oldValue;
    }
}