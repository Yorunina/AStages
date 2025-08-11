package com.alessandro.astages;

import com.alessandro.astages.block.ModBlocks;
import com.alessandro.astages.capability.AProvider;
import com.alessandro.astages.command.argument.ModArguments;
import com.alessandro.astages.config.AStagesClient;
import com.alessandro.astages.config.AStagesCommon;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.item.ModItems;
import com.alessandro.astages.plugin.*;
import com.alessandro.astages.store.AttributeStore;
import com.google.common.base.Stopwatch;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Mod(AStages.MODID)
public class AStages {
    public static final String MODID = "astages";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Stopwatch TIMER = Stopwatch.createUnstarted();

    public AStages(IEventBus modEventBus, @NotNull ModContainer modContainer) {
        AProvider.ATTACHMENT_TYPES.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModArguments.ARGUMENT_TYPES.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, AStagesCommon.SPEC, "astages-common.toml");
        modContainer.registerConfig(ModConfig.Type.CLIENT, AStagesClient.SPEC, "astages-client.toml");

        APluginFinder.getAllPlugins();

        var managerContainer = ManagerContainer.initialize();
        APluginManager.callMethod(managerContainer, AStagesPlugin::registerManagers);
        ARestrictionManager.EXTERNAL_MANAGERS.putAll(managerContainer.get());

        var attributeContainer = AttributeContainer.initialize();
        APluginManager.callMethod(attributeContainer, AStagesPlugin::attachAttributes);
        var result = attributeContainer.get();
        for (var clazz : result.keySet()) {
            ARestrictionManager.ATTACHED_ATTRIBUTES.computeIfAbsent(clazz, key -> AttributeStore.builder()).combineWith(result.get(clazz));
        }
    }
}
