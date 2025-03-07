package com.alessandro.astages.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class OrderedMultiMap<K, V extends Comparable<V>> {
    private final Map<K, SortedSet<V>> map;

    private OrderedMultiMap() {
        map = new HashMap<>();
    }

    @Contract(value = " -> new", pure = true)
    public static <K, V extends Comparable<V>> @NotNull OrderedMultiMap<K, V> create() {
        return new OrderedMultiMap<>();
    }

    public Collection<SortedSet<V>> values() {
        return map.values();
    }

    public Set<K> keySet() {
        return map.keySet();
    }

    public void put(K key, V value) {
        map.computeIfAbsent(key, k -> new TreeSet<>()).add(value);
    }

    public SortedSet<V> get(K key) {
        return map.getOrDefault(key, new TreeSet<>());
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public void clear() {
        map.clear();
    }

    @Override
    public String toString() {
        return "OrderedMultiMap{" +
            "map=" + map +
            '}';
    }
}
