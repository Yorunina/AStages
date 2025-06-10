package com.alessandro.astages.command.argument;

import com.alessandro.astages.util.ARestrictionType;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AStagesRestrictionTypeArgument implements ArgumentType<ARestrictionType> {
    private static final Collection<String> EXAMPLES = Arrays.asList("item", "recipe");
    private static final DynamicCommandExceptionType ERROR_INVALID_TYPE = new DynamicCommandExceptionType(s -> Component.literal("Unknown type: " + s));

    public static AStagesRestrictionTypeArgument types() {
        return new AStagesRestrictionTypeArgument();
    }

    public static ARestrictionType getType(CommandContext<CommandSourceStack> context, String name) {
        return context.getArgument(name, ARestrictionType.class);
    }

    @Override
    public ARestrictionType parse(StringReader stringReader) throws CommandSyntaxException {
        var typeString = stringReader.readUnquotedString();

        try {
            return ARestrictionType.getType(typeString);
        } catch (IllegalArgumentException exception) {
            throw ERROR_INVALID_TYPE.create(typeString);
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(ARestrictionType.types(), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
