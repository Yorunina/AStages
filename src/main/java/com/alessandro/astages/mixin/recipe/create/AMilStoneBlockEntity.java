package com.alessandro.astages.mixin.recipe.create;

import com.simibubi.create.content.kinetics.millstone.MillstoneBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MillstoneBlockEntity.class)
public class AMilStoneBlockEntity {
    @Unique
    public MillstoneBlockEntity astages$self() {
        return (MillstoneBlockEntity) (Object) this;
    }

//    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/AllRecipeTypes;find(Lnet/minecraft/world/Container;Lnet/minecraft/world/level/Level;)Ljava/util/Optional;"))
//    public <C extends Container, T extends Recipe<C>> Optional<T> astages$tick(AllRecipeTypes instance, C inv, Level level) {
//        var recipe = level.getRecipeManager().getRecipeFor(instance.getType(), inv, level);
//
//        return null;
//    }
}
