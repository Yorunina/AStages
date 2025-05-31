package com.alessandro.astages.core.stage;

import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class AStage {
    private final String stage;
    private final String description;

    private Component addTitle;
    private Component removeTitle;
    private Component addSubTitle;
    private Component removeSubTitle;
    private Component addChatMessage;
    private Component removeChatMessage;
    private int fadeIn = 20;
    private int fadeOut = 20;
    private int stay = 60;

    private boolean serverOnly = false;
    private boolean playerOnly = false;

    public AStage(@NotNull String stage) {
        this.stage = stage;
        this.description = AStagesUtil.capitalizeWords(stage.replace('_', ' '));
    }

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof AStage aStage)) return false;

        return stage.equals(aStage.stage);
    }

    @Override
    public int hashCode() {
        return stage.hashCode();
    }

    public String getStage() {
        return stage;
    }

    public String getDescription() {
        return description;
    }

    public Component getAddTitle() {
        return addTitle;
    }

    public AStage setAddTitle(Component addTitle) {
        this.addTitle = addTitle;
        return this;
    }

    public Component getRemoveTitle() {
        return removeTitle;
    }

    public AStage setRemoveTitle(Component removeTitle) {
        this.removeTitle = removeTitle;
        return this;
    }

    public Component getAddSubTitle() {
        return addSubTitle;
    }

    public AStage setAddSubTitle(Component addSubTitle) {
        this.addSubTitle = addSubTitle;
        return this;
    }

    public Component getRemoveSubTitle() {
        return removeSubTitle;
    }

    public AStage setRemoveSubTitle(Component removeSubTitle) {
        this.removeSubTitle = removeSubTitle;
        return this;
    }

    public Component getAddChatMessage() {
        return addChatMessage;
    }

    public AStage setAddChatMessage(Component addChatMessage) {
        this.addChatMessage = addChatMessage;
        return this;
    }

    public Component getRemoveChatMessage() {
        return removeChatMessage;
    }

    public AStage setRemoveChatMessage(Component removeChatMessage) {
        this.removeChatMessage = removeChatMessage;
        return this;
    }

    public int getFadeIn() {
        return fadeIn;
    }

    public AStage setFadeIn(int fadeIn) {
        this.fadeIn = fadeIn;
        return this;
    }

    public int getFadeOut() {
        return fadeOut;
    }

    public AStage setFadeOut(int fadeOut) {
        this.fadeOut = fadeOut;
        return this;
    }

    public int getStay() {
        return stay;
    }

    public AStage setStay(int stay) {
        this.stay = stay;
        return this;
    }

    public boolean isServerOnly() {
        return serverOnly;
    }

    public AStage setServerOnly(boolean serverOnly) {
        this.serverOnly = serverOnly;
        return this;
    }

    public boolean isPlayerOnly() {
        return playerOnly;
    }

    public AStage setPlayerOnly(boolean playerOnly) {
        this.playerOnly = playerOnly;
        return this;
    }
}
