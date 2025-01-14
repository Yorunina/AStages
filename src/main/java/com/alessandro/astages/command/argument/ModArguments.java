package com.alessandro.astages.command.argument;

import com.alessandro.astages.AStages;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModArguments {
    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARGUMENT_TYPES = DeferredRegister.create(BuiltInRegistries.COMMAND_ARGUMENT_TYPE, AStages.MODID);

    public static final DeferredHolder<ArgumentTypeInfo<?, ?>, SingletonArgumentInfo<AStagesAddArgument>> ADD_STAGES_ARGUMENT = ARGUMENT_TYPES.register("add_stages", () -> ArgumentTypeInfos.registerByClass(AStagesAddArgument.class, SingletonArgumentInfo.contextFree(AStagesAddArgument::stages)));
    public static final DeferredHolder<ArgumentTypeInfo<?, ?>, SingletonArgumentInfo<AStagesRemoveArgument>> REMOVE_STAGES_ARGUMENT = ARGUMENT_TYPES.register("remove_stages", () -> ArgumentTypeInfos.registerByClass(AStagesRemoveArgument.class, SingletonArgumentInfo.contextFree(AStagesRemoveArgument::stages)));
}
