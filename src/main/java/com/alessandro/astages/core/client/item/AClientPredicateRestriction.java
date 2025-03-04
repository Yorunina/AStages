package com.alessandro.astages.core.client.item;

import com.alessandro.astages.core.AModelManager;
import com.alessandro.astages.store.AModel;
import com.alessandro.astages.store.AClientRestriction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class AClientPredicateRestriction extends AClientRestriction<AClientPredicateRestriction, ResourceLocation, ItemStack> {
    private ResourceLocation modelId;

    public AClientPredicateRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public AClientPredicateRestriction restrict(ResourceLocation model) {
        this.modelId = model;
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean isRestricted(ItemStack stack) {
        return ((AModel<Predicate<ItemStack>>) AModelManager.MODELS.getModel(modelId)).modelObject().test(stack);
    }
}
