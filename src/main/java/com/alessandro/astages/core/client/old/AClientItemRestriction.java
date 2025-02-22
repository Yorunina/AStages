package com.alessandro.astages.core.client.old;

import com.alessandro.astages.util.AClientRestriction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

//public record AClientItemRestriction(String id, String stage, ItemStack stack, boolean renderItemName, boolean hideTooltip,
//                                     boolean hideInJEI, boolean hideInJade, Component tooltipMessage, Component jeiMessage,
//                                     Component jadeMessage) implements AClientRestriction {
public record AClientItemRestriction(String id, String stage, ItemStack stack, boolean renderItemName,
                                     boolean hideTooltip, Component tooltipMessage, Component jadeItemMessage,
                                     Component jadeBlockMessage) implements AClientRestriction {
}
