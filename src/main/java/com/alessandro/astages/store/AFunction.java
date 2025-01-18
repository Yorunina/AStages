package com.alessandro.astages.store;

@FunctionalInterface
public interface AFunction<T, R> {
    R apply(T t);
}
