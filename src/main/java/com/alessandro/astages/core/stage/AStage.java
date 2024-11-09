package com.alessandro.astages.core.stage;

import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class AStage {
    public final String stage;
    public final String description;

    public Component addTitle;
    public Component removeTitle;
    public Component addSubTitle;
    public Component removeSubTitle;
    public int fadeIn = 20;
    public int fadeOut = 20;
    public int stay = 60;

    public AStage(@NotNull String stage) {
        this.stage = stage;
        this.description = AStagesUtil.capitalizeWords(stage.replace('_', ' '));
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
}
