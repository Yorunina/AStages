package com.alessandro.astages.core.server.manager;

import com.alessandro.astages.core.server.restriction.ALootRestriction;
import com.alessandro.astages.store.server.AManager;
import com.alessandro.astages.util.ARestrictionType;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ALootManager extends AManager<ALootRestriction, Void, ItemStack> {
    public ALootRestriction getRestriction(Player player, ItemStack stack, @Nullable EntityType<?> entityType, @Nullable ResourceLocation lootTable) {
        if (stack.isEmpty()) { return null; }

        return getRestrictions().stream().filter(r -> r.isRestricted(stack, entityType, lootTable) && !AStagesUtil.hasStage(player, r.getStage())).findFirst().orElse(null);
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionType.LOOT;
    }
}
