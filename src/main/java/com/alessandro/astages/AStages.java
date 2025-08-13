package com.alessandro.astages;

import com.alessandro.astages.block.ModBlocks;
import com.alessandro.astages.command.argument.ModArguments;
import com.alessandro.astages.config.AStagesClient;
import com.alessandro.astages.config.AStagesCommon;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.server.manager.AItemManager;
import com.alessandro.astages.item.ModItems;
import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.plugin.APluginFinder;
import com.alessandro.astages.plugin.APluginManager;
import com.alessandro.astages.plugin.AStagesPlugin;
import com.alessandro.astages.plugin.container.AttributeContainer;
import com.alessandro.astages.plugin.container.ManagerContainer;
import com.alessandro.astages.store.Attribute;
import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.store.Attributes;
import com.google.common.base.Stopwatch;
import com.mojang.logging.LogUtils;
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

@Mod(AStages.MODID)
@SuppressWarnings("removal")
public class AStages {
    public static final String MODID = "astages";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Stopwatch TIMER = Stopwatch.createUnstarted();

    public static Supplier<IForgeRegistry<Attribute<?>>> ATTRIBUTES_REGISTRY = Attributes.ATTRIBUTES.makeRegistry(RegistryBuilder::new);

    public AStages() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModArguments.ARGUMENT_TYPES.register(modEventBus);

        Attributes.ATTRIBUTES.register(modEventBus);
        Attributes.Item.ATTRIBUTES.register(modEventBus);
        Attributes.Pet.ATTRIBUTES.register(modEventBus);
        Attributes.Structure.ATTRIBUTES.register(modEventBus);
        Attributes.Screen.ATTRIBUTES.register(modEventBus);
        Attributes.Dimension.ATTRIBUTES.register(modEventBus);
        Attributes.Mob.ATTRIBUTES.register(modEventBus);
        Attributes.Region.ATTRIBUTES.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AStagesCommon.SPEC, "astages-common.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, AStagesClient.SPEC, "astages-client.toml");
        ModNetworking.register();

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

    static {
        AItemManager.whiteListContainer(ChestBlockEntity.class, null);
        AItemManager.whiteListContainer(BarrelBlockEntity.class, null);
    }
}
