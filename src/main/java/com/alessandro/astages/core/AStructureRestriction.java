package com.alessandro.astages.core;

import com.alessandro.astages.util.ARestriction;
import com.alessandro.astages.util.AStagesUtil;
import com.alessandro.astages.util.ToBeTested;
import com.alessandro.astages.util.UnderDevelopment;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings("unused")
public class AStructureRestriction implements ARestriction {
    public final String id;
    public final String stage;

    public List<ResourceLocation> structures = new ArrayList<>();

    public boolean canAttackEntities = false;
    public boolean canInteract = false;
    @UnderDevelopment public boolean canEnter = false;
    @ToBeTested public boolean canBlockBePlaced = false;
    public boolean canBlockBeBroken = false;
    public boolean makeExplosionsAffectBlocks = false;
    public boolean makeExplosionsAffectEntities = false;

    public Function<ResourceLocation, Component> attackMessage = resourceLocation -> Component.translatable("message.astages.structure.attack", AStagesUtil.structureToDescription(resourceLocation)).withStyle(ChatFormatting.RED);
    public Function<ResourceLocation, Component> interactMessage = resourceLocation -> Component.translatable("message.astages.structure.interact", AStagesUtil.structureToDescription(resourceLocation)).withStyle(ChatFormatting.RED);
    @UnderDevelopment public Function<ResourceLocation, Component> enterMessage = resourceLocation -> Component.translatable("message.astages.structure.enter", AStagesUtil.structureToDescription(resourceLocation)).withStyle(ChatFormatting.RED);
    @ToBeTested public Function<ResourceLocation, Component> placeMessage = resourceLocation -> Component.translatable("message.astages.structure.place", AStagesUtil.structureToDescription(resourceLocation)).withStyle(ChatFormatting.RED);
    @ToBeTested public Function<ResourceLocation, Component> breakMessage = resourceLocation -> Component.translatable("message.astages.structure.break", AStagesUtil.structureToDescription(resourceLocation)).withStyle(ChatFormatting.RED);


    public AStructureRestriction(String id, String stage) {
        this.id = id;
        this.stage = stage;
    }

    public AStructureRestriction restrict(ResourceLocation structure) {
        structures.add(structure);

        return this;
    }

    public boolean isRestricted(ResourceLocation structure) {
        for (ResourceLocation str : structures) {
            if (str.equals(structure)) {
                return true;
            }
        }

        return false;
    }

    // GETTER AND SETTERS
    public boolean isCanAttackEntities() {
        return canAttackEntities;
    }

    public AStructureRestriction setCanAttackEntities(boolean canAttackEntities) {
        this.canAttackEntities = canAttackEntities;

        return this;
    }

    public boolean isCanEnter() {
        return canEnter;
    }

    public AStructureRestriction setCanEnter(boolean canEnter) {
        this.canEnter = canEnter;

        return this;
    }

    public boolean isCanInteract() {
        return canInteract;
    }

    public AStructureRestriction setCanInteract(boolean canInteract) {
        this.canInteract = canInteract;

        return this;
    }

    @ToBeTested
    public boolean isCanBlockBePlaced() {
        return canBlockBePlaced;
    }

    @ToBeTested
    public AStructureRestriction setCanBlockBePlaced(boolean canBlockBePlaced) {
        this.canBlockBePlaced = canBlockBePlaced;

        return this;
    }

    public boolean isCanBlockBeBroken() {
        return canBlockBeBroken;
    }

    public AStructureRestriction setCanBlockBeBroken(boolean canBlockBeBroken) {
        this.canBlockBeBroken = canBlockBeBroken;

        return this;
    }

    public boolean isMakeExplosionsAffectBlocks() {
        return makeExplosionsAffectBlocks;
    }

    public AStructureRestriction setMakeExplosionsAffectBlocks(boolean makeExplosionsAffectBlocks) {
        this.makeExplosionsAffectBlocks = makeExplosionsAffectBlocks;

        return this;
    }

    public boolean isMakeExplosionsAffectEntities() {
        return makeExplosionsAffectEntities;
    }

    public AStructureRestriction setMakeExplosionsAffectEntities(boolean makeExplosionsAffectEntities) {
        this.makeExplosionsAffectEntities = makeExplosionsAffectEntities;

        return this;
    }

    public Component getAttackMessage(ResourceLocation resourceLocation) {
        return attackMessage.apply(resourceLocation);
    }

    public AStructureRestriction setAttackMessage(Function<ResourceLocation, Component> attackMessage) {
        this.attackMessage = attackMessage;

        return this;
    }

    public Component getInteractMessage(ResourceLocation resourceLocation) {
        return interactMessage.apply(resourceLocation);
    }

    public AStructureRestriction setInteractMessage(Function<ResourceLocation, Component> interactMessage) {
        this.interactMessage = interactMessage;

        return this;
    }

    public Component getEnterMessage(ResourceLocation resourceLocation) {
        return enterMessage.apply(resourceLocation);
    }

    public AStructureRestriction setEnterMessage(Function<ResourceLocation, Component> enterMessage) {
        this.enterMessage = enterMessage;

        return this;
    }

    public Component getPlaceMessage(ResourceLocation resourceLocation) {
        return placeMessage.apply(resourceLocation);
    }

    public AStructureRestriction setPlaceMessage(Function<ResourceLocation, Component> placeMessage) {
        this.placeMessage = placeMessage;

        return this;
    }

    public Component getBreakMessage(ResourceLocation resourceLocation) {
        return breakMessage.apply(resourceLocation);
    }

    public AStructureRestriction setBreakMessage(Function<ResourceLocation, Component> breakMessage) {
        this.breakMessage = breakMessage;

        return this;
    }
}
