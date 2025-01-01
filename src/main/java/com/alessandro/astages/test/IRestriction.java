//package com.alessandro.astages.test;
//
//import java.util.List;
//
//public interface IRestriction<T extends IRestriction<T>> {
//    boolean isDisabled(AStageRestrictions restriction) throws SetValueNotSupported;
//
//    T setValue(AStageRestrictions restriction, boolean value) throws SetValueNotSupported;
//
//    List<AStageRestrictions> allowedTypes();
//
//    default void checkProperty(AStageRestrictions restriction) throws SetValueNotSupported {
//        if (!allowedTypes().contains(restriction)) {
//            throw new SetValueNotSupported(restriction);
//        }
//    }
//}
