package com.alessandro.astages.core.server.restriction.item;

import com.alessandro.astages.core.AModelManager;
import com.alessandro.astages.store.AModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
public class AItemPredicateRestriction extends ABaseItemRestriction<AItemPredicateRestriction, ResourceLocation> {
    private ResourceLocation modelId;

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
        if (stack.isEmpty()) { return false; }

        return ((AModel<Predicate<ItemStack>>) AModelManager.MODELS.getModel(modelId)).modelObject().test(stack);
    }

    public ResourceLocation getModelId() {
        return modelId;
    }
}
