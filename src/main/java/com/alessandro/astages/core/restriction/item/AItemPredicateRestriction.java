package com.alessandro.astages.core.restriction.item;

import com.alessandro.astages.core.ARestrictionManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class AItemPredicateRestriction extends ABaseItemRestriction<AItemPredicateRestriction, ResourceLocation> {
    private ResourceLocation modelId;
    // private static final TypeToken<Predicate<ItemStack>> STACK = new TypeToken<>() {};

    public AItemPredicateRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public AItemPredicateRestriction restrict(ResourceLocation modelId) {
        this.modelId = modelId;
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean isRestricted(ItemStack stack) {
        return ((AModel<Predicate<ItemStack>>) ARestrictionManager.MODELS.getModel(modelId)).getModelObject().test(stack);
        // return ((AModel<Predicate<ItemStack>>) ARestrictionManager.MODELS.getModel(modelId, STACK.getRawType())).getModelObject().test(stack);
        // return ARestrictionManager.MODELS.getModel(modelId, Predicate.class).getModelObject().test(stack);
    }
}
