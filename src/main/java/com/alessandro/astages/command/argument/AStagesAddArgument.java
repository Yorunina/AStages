package com.alessandro.astages.command.argument;

import com.alessandro.astages.capability.PlayerStageProvider;
import com.alessandro.astages.core.ARestrictionManager;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntitySelector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class AStagesAddArgument implements ArgumentType<String> {
    private static final Collection<String> EXAMPLES = Arrays.asList("test_stage_1", "test_stage_2");
    private static final DynamicCommandExceptionType ERROR_INVALID_STAGE = new DynamicCommandExceptionType(s -> Component.literal("Invalid stage argument: " + s));

    @Contract(value = " -> new", pure = true)
    public static @NotNull AStagesAddArgument stages() {
        return new AStagesAddArgument();
    }

    public static String getStage(@NotNull CommandContext<CommandSourceStack> context, String name) {
        return context.getArgument(name, String.class);
    }

    @Override
    public String parse(@NotNull StringReader stringReader) throws CommandSyntaxException {
        var stageString = stringReader.readUnquotedString();

        if (stageString == null) { throw ERROR_INVALID_STAGE.create(null); }

        return stageString;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, SuggestionsBuilder builder) {
        // Try remove client stages
        // context.
//        var player = context.getArgument("player", EntitySelector.class);
//        AtomicReference<List<String>> stagesAlreadyAdded = new AtomicReference<>(new ArrayList<>());
//
//        player.getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> stagesAlreadyAdded.set(playerStage.getStages()));
//
//        var toReturn = ARestrictionManager.ALL_STAGES.stream().dropWhile(string -> stagesAlreadyAdded.get().contains(string)).sorted();
        return SharedSuggestionProvider.suggest(ARestrictionManager.ALL_STAGES.stream().sorted(), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
