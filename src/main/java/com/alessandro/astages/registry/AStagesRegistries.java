package com.alessandro.astages.registry;

import com.alessandro.astages.AStages;
import com.alessandro.astages.store.Attribute;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@Mod.EventBusSubscriber(modid = AStages.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AStagesRegistries {
    public static Collection<Attribute<?>> getAllEntries() {
        return AStages.ATTRIBUTES_REGISTRY.get().getValues();
    }

    public static class Keys {
        public static final ResourceKey<Registry<Attribute<?>>> ATTRIBUTES = ResourceKey.createRegistryKey(AStagesUtil.fromNamespaceAndPath("attributes"));
    }
}
