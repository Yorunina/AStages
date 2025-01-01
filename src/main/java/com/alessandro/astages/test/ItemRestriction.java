//package com.alessandro.astages.test;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.List;
//
//public class ItemRestriction implements IRestriction<ItemRestriction> {
//    public final String id;
//    public final String stage;
//
//    private boolean breaking = AStageRestrictions.BLOCK_BREAKING.defaultValue;
//    private boolean placeable = AStageRestrictions.BLOCK_INTERACTION.defaultValue;
//
//    public ItemRestriction(String id, String stage) {
//        this.id = id;
//        this.stage = stage;
//    }
//
//    @Override
//    public boolean isDisabled(AStageRestrictions restriction) {
//        checkProperty(restriction);
//
//        return switch (restriction) {
//            case BLOCK_BREAKING -> !breaking;
//            case BLOCK_INTERACTION -> !placeable;
//            default -> false;
//        };
//    }
//
//    @Override
//    public ItemRestriction setValue(@NotNull AStageRestrictions restriction, boolean value) {
//        checkProperty(restriction);
//
//        switch (restriction) {
//            case BLOCK_BREAKING -> breaking = value;
//            case BLOCK_INTERACTION -> placeable = value;
//        }
//
//        return this;
//    }
//
//    @Override
//    public List<AStageRestrictions> allowedTypes() {
//        return List.of(AStageRestrictions.BLOCK_BREAKING, AStageRestrictions.BLOCK_INTERACTION);
//    }
//}
