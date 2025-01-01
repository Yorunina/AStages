package com.alessandro.astages.core;

import com.alessandro.astages.util.ARestriction;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class APetRestriction implements ARestriction {
    public final String id;

    public boolean isTamable = false;
    public boolean isBreedable = false;
    public boolean isMountable = false;

    public Function<Entity, Component> tameMessage = entity -> Component.translatable("message.astages.pet.tame", entity.getName()).withStyle(ChatFormatting.RED);
    public Function<Entity, Component> breedMessage = entity -> Component.translatable("message.astages.pet.breed", entity.getName()).withStyle(ChatFormatting.RED);
    public Function<Entity, Component> mountMessage = entity -> Component.translatable("message.astages.pet.mount", entity.getName()).withStyle(ChatFormatting.RED);

    public List<EntityType<?>> pets = new ArrayList<>();

    public APetRestriction(String id) {
        this.id = id;
    }

    public APetRestriction restrict(EntityType<?> pet) {
        pets.add(pet);

        return this;
    }

    public boolean isRestricted(EntityType<?> pet) {
        return pets.contains(pet);
    }

    @Override
    public String toString() {
        return "APetRestriction{" +
            "id='" + id + '\'' +
            ", isTamable=" + isTamable +
            ", isBreedable=" + isBreedable +
            ", isMountable=" + isMountable +
            ", tameMessage=" + tameMessage +
            ", breedMessage=" + breedMessage +
            ", mountMessage=" + mountMessage +
            ", pets=" + pets +
            '}';
    }

    public List<EntityType<?>> getPets() {
        return pets;
    }

    public APetRestriction setPets(List<EntityType<?>> pets) {
        this.pets = pets;

        return this;
    }

    public boolean isTamable() {
        return isTamable;
    }

    public APetRestriction setTamable(boolean tamable) {
        isTamable = tamable;

        return this;
    }

    public boolean isBreedable() {
        return isBreedable;
    }

    public APetRestriction setBreedable(boolean breedable) {
        isBreedable = breedable;

        return this;
    }

    public boolean isMountable() {
        return isMountable;
    }

    public APetRestriction setMountable(boolean mountable) {
        isMountable = mountable;

        return this;
    }

    public Component getTameMessage(Entity pet) {
        return tameMessage.apply(pet);
    }

    public APetRestriction setTameMessage(Function<Entity, Component> tameMessage) {
        this.tameMessage = tameMessage;

        return this;
    }

    public Component getBreedMessage(Entity pet) {
        return breedMessage.apply(pet);
    }

    public APetRestriction setBreedMessage(Function<Entity, Component> breedMessage) {
        this.breedMessage = breedMessage;

        return this;
    }

    public Component getMountMessage(Entity pet) {
        return mountMessage.apply(pet);
    }

    public APetRestriction setMountMessage(Function<Entity, Component> mountMessage) {
        this.mountMessage = mountMessage;

        return this;
    }
}
