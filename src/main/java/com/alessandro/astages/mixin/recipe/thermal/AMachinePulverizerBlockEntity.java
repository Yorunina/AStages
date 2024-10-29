package com.alessandro.astages.mixin.recipe.thermal;

import cofh.thermal.core.util.managers.machine.PulverizerRecipeManager;
import cofh.thermal.expansion.common.block.entity.machine.MachinePulverizerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = MachinePulverizerBlockEntity.class, remap = false)
public class AMachinePulverizerBlockEntity {
    @Unique
    public MachinePulverizerBlockEntity astages$self() {
        return (MachinePulverizerBlockEntity) (Object) this;
    }
//
//    /**
//     * @author Alessandro
//     * @reason AStages integration
//     */
//    @Overwrite
//    protected boolean cacheRecipe() {
//        var recipe = PulverizerRecipeManager.instance().getRecipe(astages$self());
//
//        return false;
//    }
}
