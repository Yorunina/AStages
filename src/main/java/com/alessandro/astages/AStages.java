package com.alessandro.astages;

import com.alessandro.astages.command.argument.ModArguments;
import com.alessandro.astages.config.AStagesCommon;
import com.alessandro.astages.networking.ModNetworking;
import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(AStages.MODID)
@SuppressWarnings("removal")
public class AStages {
    public static final String MODID = "astages";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AStages() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModArguments.ARGUMENT_TYPES.register(modEventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AStagesCommon.SPEC, "astages-common.toml");

        ModNetworking.register();
    }
}
