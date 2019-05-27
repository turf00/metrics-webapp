package com.justin.contrast.metric.processing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * This class is thread safe for concurrent reading but not writing.  It is therefore designed for a single writer and multiple readers.
 * The class maintains a ring buffer of entries up to its maximum capacity with FIFO semantics.
 * It supports fast lookup of values by the key using a map internally and therefore should be O(1) lookup.
 * The class handles up to {@value Long#MAX_VALUE} requests in total which should be more than enough for any application.
 * @param <K> the key type used for looking up values quickly.
 * @param <V> the value type, i.e. the actual data stored.
 */
class RingBufferWithLookup<K, V> {

    private final int capacity;
    private final AtomicReferenceArray<K> buffer;
    private final Map<K, V> map;
    private long position;

    RingBufferWithLookup(final int capacity) {
        this.capacity = capacity;
        buffer = new AtomicReferenceArray<>(capacity);
        map = new ConcurrentHashMap<>(capacity);
    }

    void put(K key, V value) {
        final int currPos = (int) position++ % capacity;
        final K oldKey = buffer.getAndSet(currPos, key);

        removeFromMap(oldKey);
        addToMap(key, value);
    }

    void clear() {
        map.clear();
        for (int i = 0; i < capacity; i++) {
            final K key = buffer.getAndSet(i, null);
            removeFromMap(key);
        }
    }

    V getByPos(final int pos) {
        final K key = buffer.get(pos);
        return map.get(key);
    }

    V getByKey(final K key) {
        return map.get(key);
    }

    List<V> getAll() {
        return new ArrayList<>(map.values());
    }

    private void removeFromMap(final K toRemove) {
        if (toRemove != null) {
            map.remove(toRemove);
        }
    }

    private void addToMap(final K key, final V value) {
        if (value != null) {
            map.put(key, value);
        }
    }
}
