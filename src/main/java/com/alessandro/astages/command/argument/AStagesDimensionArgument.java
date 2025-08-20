package com.alessandro.astages.command.argument;

import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.util.annotations.NotNullParamsAndMethodsReturn;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@NotNullParamsAndMethodsReturn
public class AStagesDimensionArgument implements ArgumentType<String> {
    private static final Collection<String> EXAMPLES = Arrays.asList("test_dimension_id_1", "test_dimension_id_2");
    private static final DynamicCommandExceptionType ERROR_INVALID_ID = new DynamicCommandExceptionType(s -> Component.literal("Invalid id argument: " + s));

    @Contract(value = " -> new", pure = true)
    public static AStagesDimensionArgument dimensionIds() {
        return new AStagesDimensionArgument();
    }

    public static String getDimensionId(CommandContext<CommandSourceStack> context, String name) {
        return context.getArgument(name, String.class);
    }

    @Override
    public String parse(StringReader stringReader) throws CommandSyntaxException {
        var dimensionIdString = stringReader.readUnquotedString();

        if (dimensionIdString == null) { throw ERROR_INVALID_ID.create(null); }

        return dimensionIdString;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(AClientRestrictionManager.DIMENSION_IDS, builder);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
