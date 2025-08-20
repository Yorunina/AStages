package com.alessandro.astages.core.server.manager;

import com.alessandro.astages.capability.ServerStageData;
import com.alessandro.astages.core.server.restriction.ALootRestriction;
import com.alessandro.astages.store.server.AManager;
import com.alessandro.astages.util.ARestrictionType;
import com.alessandro.astages.util.AStagesUtil;
import com.alessandro.astages.util.annotations.NotNullParams;
import com.alessandro.astages.util.annotations.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@NotNullParams
public class ALootManager extends AManager<ALootRestriction, Void, ItemStack> {
    public ALootRestriction getRestriction(Player player, ItemStack stack, @Nullable EntityType<?> entityType, @Nullable ResourceLocation lootTable) {
        if (stack.isEmpty()) { return null; }

        return getRestrictions().stream().filter(r -> r.isRestricted(stack, entityType, lootTable) && !AStagesUtil.hasStage(player, r.getStage())).findFirst().orElse(null);
    }

    public ALootRestriction getRestriction(MinecraftServer server, ItemStack stack, @Nullable EntityType<?> entityType, @Nullable ResourceLocation lootTable) {
        if (stack.isEmpty()) { return null; }
        var data = ServerStageData.getData(server);

        return getRestrictions().stream().filter(r -> r.isRestricted(stack, entityType, lootTable) && !data.has(r.getStage())).findFirst().orElse(null);
    }

    public ALootRestriction getRestriction(ItemStack stack, @Nullable EntityType<?> entityType, @Nullable ResourceLocation lootTable, @Nullable Player player, @Nullable MinecraftServer server) {
        ALootRestriction serverRestriction = null;
        ALootRestriction playerRestriction = null;

        if (server != null) { serverRestriction = getRestriction(server, stack, entityType, lootTable); }
        if (serverRestriction == null) { // If the stage is unlocked in the server, pass!
            return null;
        }

        if (player != null) { playerRestriction = getRestriction(player, stack, entityType, lootTable); }
        return playerRestriction;
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionType.LOOT;
    }
}
