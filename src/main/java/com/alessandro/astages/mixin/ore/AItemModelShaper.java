package com.alessandro.astages.mixin.ore;

import com.alessandro.astages.core.AClientRestrictionManager;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemModelShaper.class)
public class AItemModelShaper {
    @ModifyArg(method = "getItemModel(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/client/resources/model/BakedModel;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemModelShaper;getItemModel(Lnet/minecraft/world/item/Item;)Lnet/minecraft/client/resources/model/BakedModel;"))
    public Item astages$getItemModel(Item item) {
        if (item instanceof BlockItem blockItem) {
            var associatedBlockState = blockItem.getBlock().defaultBlockState();
            var restriction = AClientRestrictionManager.ORE_INSTANCE.getRestriction(associatedBlockState);

            if (restriction != null) {
                return restriction.getReplacement().getBlock().asItem();
            }
        }

        return item;
    }
}