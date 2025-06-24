package com.alessandro.astages.command;

import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.store.server.ARestriction;
import com.alessandro.astages.util.AChatUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AStagesInfoCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("astages").requires(c -> c.hasPermission(2))
                .then(Commands.literal("hand").executes(AStagesInfoCommands::getItemInHandInfo))
        );
    }

    @SuppressWarnings("NullPointerException")
    private static int getItemInHandInfo(CommandContext<CommandSourceStack> context) {
        var player = context.getSource().getPlayer();
        if (player == null) { return 0; }
        var stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (stack.is(Items.AIR)) { return 1; }
        var item = stack.getItem();

        player.sendSystemMessage(Component.literal("Item Info:"));
        player.sendSystemMessage(AChatUtils.dashItem(AChatUtils.copy(BuiltInRegistries.ITEM.getKey(item).toString(), "Item ID", ChatFormatting.GREEN)));

        player.sendSystemMessage(AChatUtils.emptyRow());
        AChatUtils.sendRestrictionsInfo(player, "Item restrictions:", "Item restriction", () -> ARestrictionManager.ITEM_INSTANCE.getAllRestrictions(stack), ARestriction::getId);

        return 1;
    }
}
