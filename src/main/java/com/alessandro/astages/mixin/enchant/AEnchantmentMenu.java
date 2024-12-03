package com.alessandro.astages.mixin.enchant;

import com.alessandro.astages.AStages;
import com.alessandro.astages.capability.BlockStageProvider;
import com.alessandro.astages.core.AEnchantManager;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Mixin(EnchantmentMenu.class)
public abstract class AEnchantmentMenu {
    @Shadow @Final public ContainerLevelAccess access;

    @Shadow @Final public int[] costs;

    @Shadow public abstract List<EnchantmentInstance> getEnchantmentList(ItemStack pStack, int pEnchantSlot, int pLevel);

    @Unique
    private EnchantmentMenu enchantmentMenu$self() {
        return (EnchantmentMenu) (Object) this;
    }

//    @ModifyVariable(method = "lambda$slotsChanged$0", at = @At(value = "STORE"), ordinal = 0)
    private @NotNull List<EnchantmentInstance> injected(@NotNull List<EnchantmentInstance> value) {
        AStages.LOGGER.debug("BEFORE: {}", value);

        AtomicReference<Player> owner = new AtomicReference<>();

        access.execute((level, pos) -> {
            var blockEntity = level.getBlockEntity(pos);

            if (blockEntity != null && level.getServer() != null) {
                blockEntity.getCapability(BlockStageProvider.BLOCK_STAGE).ifPresent(blockStage -> owner.set(AStagesUtil.getPlayerFromUUID(level.getServer(), blockStage.getOwner())));
            }
        });

        if (owner.get() instanceof ServerPlayer player) {
            List<Integer> toRemove = new ArrayList<>();

            for (int i = 0; i < value.size(); i++) {
//                var restriction = ARestrictionManager.ENCHANT_INSTANCE.getRestriction()
            }
        }

        AStages.LOGGER.debug("AFTER: {}", value);
        return value;
    }
}
