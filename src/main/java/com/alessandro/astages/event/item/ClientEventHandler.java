package com.alessandro.astages.event.item;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.event.custom.PlayerInventoryChangedEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = AStages.MODID, value = Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void onItemTooltip(@NotNull ItemTooltipEvent event) {
        if (event.getEntity() != null) {
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(event.getItemStack());
            if (restriction == null) { return; }

            if (restriction.hideTooltip) {
                event.getToolTip().clear();
                // event.getToolTip().add(Component.literal("Unfamiliar Item").withStyle(ChatFormatting.RED));
                event.getToolTip().add(restriction.getHiddenName(event.getItemStack()));
            }
        }
    }
}
