package com.alessandro.astages.core.restriction.ud.models;

import com.alessandro.astages.util.develop.Info;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class AItemModel {
    // extends ABaseModel<AItemModel> {
    private Predicate<ItemStack> predicate = null;

//    @Override
//    public @NotNull AttributeStore allowedAttributes() {
//        return AttributeStore.builder()
//            .addAttribute(ModelAttributes.IGNORE_ITEMS)
//            .addAttribute(ModelAttributes.IGNORE_TAGS);
//    }

    @Info("If the function written here returns true, object is restricted!")
    public void setCheckRestrictionFunction(Predicate<ItemStack> predicate) {
        this.predicate = predicate;
    }

    public boolean isRestricted(ItemStack stack) {
        return predicate.test(stack);
    }
}
