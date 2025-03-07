package com.alessandro.astages.core.client.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public record AClientItemPropertyRestriction(String id, String stage, ItemStack stack,
                                             boolean renderItemName, boolean hideTooltip,
                                             Component tooltipMessage, Component jadeItemMessage,
                                             Component jadeBlockMessage) {
}
