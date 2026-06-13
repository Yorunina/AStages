package com.alessandro.astages;

import com.alessandro.astages.api.plugin.AStagesPlugin;
import com.alessandro.astages.api.plugin.container.AttributeContainer;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.api.store.ASimpleRestrictionType;
import com.alessandro.astages.api.store.Attribute;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.*;
import com.alessandro.astages.engine.store.ARestrictionTypes;
import com.alessandro.astages.engine.store.ASimpleRestrictionTypes;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.engine.store.StageAttributes;
import com.alessandro.astages.infrastructure.advancement.ACriteriaTriggers;
import com.alessandro.astages.infrastructure.command.argument.ACommandArguments;
import com.alessandro.astages.infrastructure.config.AStagesClient;
import com.alessandro.astages.infrastructure.config.AStagesCommon;
import com.alessandro.astages.infrastructure.manager.ClientManagerScanner;
import com.alessandro.astages.infrastructure.manager.ManagerScanner;
import com.alessandro.astages.infrastructure.networking.Networking;
import com.alessandro.astages.infrastructure.plugin.PluginScanner;
import com.alessandro.astages.internal.experimental.block.ModBlocks;
import com.alessandro.astages.internal.experimental.item.ModItems;
import com.alessandro.astages.internal.experimental.loot.AModifiers;
import com.google.common.base.Stopwatch;
import com.mojang.logging.LogUtils;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import org.slf4j.Logger;

import java.util.function.Supplier;

@SuppressWarnings("removal")
@Mod(AStages.MODID)
public class AStages {
    public static final String MODID = "astages";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Stopwatch TIMER = Stopwatch.createUnstarted();

    public static Supplier<IForgeRegistry<Attribute<?>>> ATTRIBUTES_REGISTRY = Attributes.ATTRIBUTES.makeRegistry(RegistryBuilder::new);
    public static Supplier<IForgeRegistry<ARestrictionType>> RESTRICTION_TYPES_REGISTRY = ARestrictionTypes.RESTRICTION_TYPES.makeRegistry(RegistryBuilder::new);
    public static Supplier<IForgeRegistry<ASimpleRestrictionType>> SIMPLE_RESTRICTION_TYPES_REGISTRY = ASimpleRestrictionTypes.SIMPLE_RESTRICTION_TYPES.makeRegistry(RegistryBuilder::new);

    public AStages() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ACommandArguments.ARGUMENT_TYPES.register(modEventBus);
        AModifiers.MODIFIERS.register(modEventBus);

        ACriteriaTriggers.init();

        Attributes.ATTRIBUTES.register(modEventBus);
        Attributes.Item.ATTRIBUTES.register(modEventBus);
        Attributes.Pet.ATTRIBUTES.register(modEventBus);
        Attributes.Structure.ATTRIBUTES.register(modEventBus);
        Attributes.Screen.ATTRIBUTES.register(modEventBus);
        Attributes.Dimension.ATTRIBUTES.register(modEventBus);
        Attributes.Mob.ATTRIBUTES.register(modEventBus);
        Attributes.Region.ATTRIBUTES.register(modEventBus);

        StageAttributes.ATTRIBUTES.register(modEventBus);

        ARestrictionTypes.RESTRICTION_TYPES.register(modEventBus);
        ASimpleRestrictionTypes.SIMPLE_RESTRICTION_TYPES.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AStagesCommon.SPEC, "astages/astages-common.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, AStagesClient.SPEC, "astages/astages-client.toml");
        Networking.register();

        ManagerScanner.getAllManagers();
        ClientManagerScanner.getAllClientManagers();
        PluginScanner.getAllPlugins();

        var attributeContainer = AttributeContainer.initialize();
        PluginManager.callMethod(attributeContainer, AStagesPlugin::attachAttributes);
        var result = attributeContainer.get();
        for (var clazz : result.keySet()) {
            ARestrictionManager.ATTACHED_ATTRIBUTES.computeIfAbsent(clazz, key -> AttributeStore.builder()).combineWith(result.get(clazz));
        }

        var clientAttributeContainer = AttributeContainer.initialize();
        PluginManager.callMethod(attributeContainer, AStagesPlugin::attachClientAttributes);
        var clientResult = clientAttributeContainer.get();
        for (var clazz : clientResult.keySet()) {
            AClientRestrictionManager.ATTACHED_ATTRIBUTES.computeIfAbsent(clazz, key -> AttributeStore.builder()).combineWith(clientResult.get(clazz));
        }

        var stageAttributeContainer = AttributeContainer.initialize();
        PluginManager.callMethod(attributeContainer, AStagesPlugin::attachStageAttributes);
        var stageResult = stageAttributeContainer.get();
        for (var clazz : stageResult.keySet()) {
            AStageManager.ATTACHED_ATTRIBUTES.computeIfAbsent(clazz, key -> AttributeStore.builder()).combineWith(stageResult.get(clazz));
        }

        var clientStageAttributeContainer = AttributeContainer.initialize();
        PluginManager.callMethod(attributeContainer, AStagesPlugin::attachClientStageAttributes);
        var clientStageResult = clientStageAttributeContainer.get();
        for (var clazz : clientStageResult.keySet()) {
            AClientStageManager.ATTACHED_ATTRIBUTES.computeIfAbsent(clazz, key -> AttributeStore.builder()).combineWith(clientStageResult.get(clazz));
        }
    }

    static {
        ARestrictionManager.ITEM_INSTANCE.whiteListContainer(ChestBlockEntity.class, null);
        ARestrictionManager.ITEM_INSTANCE.whiteListContainer(CompoundContainer.class, null);
        ARestrictionManager.ITEM_INSTANCE.whiteListContainer(BarrelBlockEntity.class, null);
    }
}
