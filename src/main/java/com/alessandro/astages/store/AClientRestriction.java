package com.alessandro.astages.store;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class AClientRestriction<R extends AClientRestriction<R, U, V>, U, V> implements Comparable<R> {
    private final String id;
    private final String stage;
    private int priority = 0;

    protected AClientRestriction(String id, String stage) {
        this.id = id;
        this.stage = stage;
    }

    public abstract R restrict(U object);

    public abstract boolean isRestricted(V object);

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AClientRestriction<?, ?, ?>)) { return false; }

        return Objects.equals(((AClientRestriction<?, ?, ?>) obj).id, this.id) &&
            Objects.equals(((AClientRestriction<?, ?, ?>) obj).stage, this.stage);
    }

    @Override
    public int compareTo(@NotNull R that) {
        if (this.priority == that.getPriority()) {
            return this.id.compareTo(that.getId());
        }

        return -Integer.compare(this.priority, that.getPriority()); // Ascending order!
    }

    public String getId() {
        return id;
    }

    public String getStage() {
        return stage;
    }

    public int getPriority() {
        return priority;
    }

    @SuppressWarnings({ "unchecked", "unused" })
    public R setPriority(int priority) {
        this.priority = priority;

        return (R) this;
    }
}
