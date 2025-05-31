package com.alessandro.astages.integration.jei;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Contract;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CustomItemStackKey {
    private final ItemStack stack;

    private CustomItemStackKey(ItemStack stack) {
        this.stack = stack;
    }

    @Contract(value = "_ -> new", pure = true)
    public static CustomItemStackKey build(ItemStack stack) {
        return new CustomItemStackKey(stack);
    }

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof CustomItemStackKey that)) return false;

        return this.stack.equals(that.stack, false);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(stack.getDescriptionId());
        // result = 31 * result + Objects.hashCode(stack.getCount()); // IGNORE COUNT!
        if (stack.hasTag()) { result = 31 * result + Objects.hashCode(stack.getTag()); }
        return result;
    }
}
