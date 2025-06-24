package com.alessandro.astages.util;

import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AChatUtils {
    public static int INDENT_FACTOR = 2; // Equivalent in spaces for indentation
    private static int LIST_NUMBER = 0;

    public static Component copy(String value, String hover, ChatFormatting formatting) {
        return copy(Component.literal(value).withStyle(formatting), Component.literal(hover));
    }

    public static Component copy(String value, String hover) {
        return copy(Component.literal(value), Component.literal(hover));
    }

    public static Component copy(Component value, Component hover) {
        return Component.literal("") // " - "
                .withStyle(ChatFormatting.GRAY)
                .withStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, value.getString())))
                .withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover.copy().append(" - click to copy!"))))
                .append(value);
    }

    public static Component indent(int times, Component value) {
        return Component.literal(" ".repeat(INDENT_FACTOR  * times)).append(value);
    }

    public static void setFirstListNumber(int value) {
        LIST_NUMBER = value;
    }

    public static void resetListNumber() {
        LIST_NUMBER = 1;
    }

    public static void resetListNumber(int value) {
        LIST_NUMBER = value;
    }

    public static Component listItem(Component value) {
        var toReturn = Component.literal(LIST_NUMBER + ". ").append(value);
        LIST_NUMBER++;
        return toReturn;
    }

    public static Component dashItem(Component value) {
        return Component.literal("- ").append(value);
    }
}