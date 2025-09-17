package com.alessandro.astages.core.stage.permanent;

import com.alessandro.astages.api.stage.event.GrantedEvent;
import com.alessandro.astages.api.stage.implementation.AGrantable;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@Deprecated(forRemoval = true)
@SuppressWarnings("unused")
public abstract class AStageDisplay implements AGrantable {
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

    private boolean hasCustomGrantEvent = false;
    private Consumer<GrantedEvent> grantedEvent;

    public AStageDisplay(@NotNull String stage) {
        this.stage = stage;
        this.description = AStagesUtil.capitalizeWords(stage.replace('_', ' '));
    }

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof AStageDisplay aStage)) return false;

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

    public AStageDisplay setAddTitle(Component addTitle) {
        this.addTitle = addTitle;
        return this;
    }

    public Component getRemoveTitle() {
        return removeTitle;
    }

    public AStageDisplay setRemoveTitle(Component removeTitle) {
        this.removeTitle = removeTitle;
        return this;
    }

    public Component getAddSubTitle() {
        return addSubTitle;
    }

    public AStageDisplay setAddSubTitle(Component addSubTitle) {
        this.addSubTitle = addSubTitle;
        return this;
    }

    public Component getRemoveSubTitle() {
        return removeSubTitle;
    }

    public AStageDisplay setRemoveSubTitle(Component removeSubTitle) {
        this.removeSubTitle = removeSubTitle;
        return this;
    }

    public Component getAddChatMessage() {
        return addChatMessage;
    }

    public AStageDisplay setAddChatMessage(Component addChatMessage) {
        this.addChatMessage = addChatMessage;
        return this;
    }

    public Component getRemoveChatMessage() {
        return removeChatMessage;
    }

    public AStageDisplay setRemoveChatMessage(Component removeChatMessage) {
        this.removeChatMessage = removeChatMessage;
        return this;
    }

    public int getFadeIn() {
        return fadeIn;
    }

    public AStageDisplay setFadeIn(int fadeIn) {
        this.fadeIn = fadeIn;
        return this;
    }

    public int getFadeOut() {
        return fadeOut;
    }

    public AStageDisplay setFadeOut(int fadeOut) {
        this.fadeOut = fadeOut;
        return this;
    }

    public int getStay() {
        return stay;
    }

    public AStageDisplay setStay(int stay) {
        this.stay = stay;
        return this;
    }

    public boolean isServerOnly() {
        return serverOnly;
    }

    public AStageDisplay setServerOnly(boolean serverOnly) {
        this.serverOnly = serverOnly;
        return this;
    }

    public boolean isPlayerOnly() {
        return playerOnly;
    }

    public AStageDisplay setPlayerOnly(boolean playerOnly) {
        this.playerOnly = playerOnly;
        return this;
    }

    public AStageDisplay whenGranted(Consumer<GrantedEvent> consumer) {
        grantedEvent = consumer;
        hasCustomGrantEvent = true;
        return this;
    }

    @Override
    public void postGrantedEvent(GrantedEvent event) {
        grantedEvent.accept(event);
    }

    public boolean hasCustomGrantEvent() {
        return hasCustomGrantEvent;
    }
}
