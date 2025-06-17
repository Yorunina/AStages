package com.alessandro.astages.mixin.loot;

import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.store.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@Mixin(RandomizableContainerBlockEntity.class)
public abstract class ARandomizableContainerBlockEntity {
    @Shadow public abstract ItemStack getItem(int pIndex);

    @Shadow public abstract ItemStack removeItem(int pIndex, int pCount);

    @Shadow public abstract void setItem(int pIndex, ItemStack pStack);

    @Unique
    private RandomizableContainerBlockEntity randomizableContainerBlockEntity$self() {
        return (RandomizableContainerBlockEntity) (Object) this;
    }

    @Inject(method = "unpackLootTable", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/loot/LootTable;fill(Lnet/minecraft/world/Container;Lnet/minecraft/world/level/storage/loot/LootParams;J)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    public void astages$unpackLootTable(Player player, CallbackInfo ci, LootTable lootTable, LootParams.Builder builder) {
        var size = randomizableContainerBlockEntity$self().getContainerSize();

        for (int slot = 0; slot < size; slot++) {
            var stack = getItem(slot);
            var copiedStack = stack.copy();
            var restriction = ARestrictionManager.LOOT_INSTANCE.getRestriction(player, stack, null, lootTable.getLootTableId());

            if (restriction != null) {
                removeItem(slot, stack.getCount());

                if (restriction.isEnabled(Attributes.HAS_REPLACER)) {
                    var replacer = restriction.getReplacer().apply(copiedStack);

                    if (!replacer.isEmpty()) {
                        setItem(slot, replacer);
                    }
                }
            }
        }
    }
}
