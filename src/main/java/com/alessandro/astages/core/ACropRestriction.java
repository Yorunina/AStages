package com.alessandro.astages.core;

import com.alessandro.astages.util.ACompareCondition;
import com.alessandro.astages.util.ARestriction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ACropRestriction implements ARestriction {
    public final String id;

    public Block crop;
    private boolean isCropClass = false;
    public ACompareCondition compareCondition;
    public int age;

    public ACropRestriction(String id) {
        this.id = id;
    }

    public ACropRestriction restrict(Block crop) {
        this.crop = crop;

        return this;
    }

    public boolean isRestricted(@NotNull BlockState crop, Integer age) {
        if (isCropClass) {
            return elaborateRestriction(age);
        } else {
            return crop.is(this.crop);
        }
    }

    public boolean elaborateRestriction(Integer age) {
        if (age == null) {
            return true;
        }

        return switch (this.compareCondition) {
            case EQUAL -> this.age == age;
            case LESS -> age < this.age;
            case LESS_EQUAL -> age <= this.age;
            case GREAT -> age > this.age;
            case GREAT_EQUAL -> age >= this.age;
        };
    }

    // GETTERS AND SETTERS
    public ACompareCondition getCompareCondition() {
        return compareCondition;
    }

    public ACropRestriction setCompareCondition(ACompareCondition compareCondition) {
        this.isCropClass = true;
        this.compareCondition = compareCondition;

        return this;
    }

    public int getAge() {
        return age;
    }

    public ACropRestriction setAge(int age) {
        this.isCropClass = true;
        this.age = age;

        return this;
    }

    public boolean isCropClass() {
        return isCropClass;
    }
}
