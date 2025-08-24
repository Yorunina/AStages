package com.alessandro.astages.api.constant;

public enum AOperation {
    ADD(true, true),
    ADD_ALL(true, false),
    REMOVE(false, true),
    REMOVE_ALL(false, false),
    GET(false, false),
    LOGIN(false, false);

    private final boolean needToBeChecked;
    private final boolean supportOnlyOneStage;

    AOperation(boolean needToBeChecked, boolean supportOnlyOneStage) {
        this.needToBeChecked = needToBeChecked;
        this.supportOnlyOneStage = supportOnlyOneStage;
    }

    public boolean needToBeChecked() {
        return needToBeChecked;
    }

    public boolean supportOnlyOneStage() {
        return supportOnlyOneStage;
    }
}
