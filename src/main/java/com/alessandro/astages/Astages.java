package com.alessandro.astages;

import com.alessandro.astages.capability.ModData;
import com.alessandro.astages.capability.PlayerStageProvider;
import com.alessandro.astages.command.argument.ModArguments;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(Astages.MODID)
public class Astages {
    public static final String MODID = "astages";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Astages(IEventBus modEventBus, ModContainer modContainer) {
        ModArguments.ARGUMENT_TYPES.register(modEventBus);
        ModData.ATTACHMENT_TYPES.register(modEventBus);
        PlayerStageProvider.ATTACHMENT_TYPES.register(modEventBus);
    }
}
