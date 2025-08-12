package com.alessandro.astages.registry;

import com.alessandro.astages.AStages;
import com.alessandro.astages.store.Attribute;
import com.alessandro.astages.util.AStagesUtil;
import com.mojang.blaze3d.MethodsReturnNonnullByDefault;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.jetbrains.annotations.Unmodifiable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@EventBusSubscriber(modid = AStages.MODID, bus = EventBusSubscriber.Bus.MOD)
public class AStagesRegistries {
    public static Registry<Attribute<?>> ATTRIBUTES = new RegistryBuilder<>(Keys.ATTRIBUTES) // Different name and path to be consistent with Neoforge Syntax
        .sync(true)
        .create();

    @SubscribeEvent
    public static void newRegistries(NewRegistryEvent event) {
        event.register(ATTRIBUTES);
    }

    public static @Unmodifiable Collection<Attribute<?>> getAllEntries() {
        return AStagesRegistries.ATTRIBUTES.stream().toList();
    }

    public static class Keys {
        public static final ResourceKey<Registry<Attribute<?>>> ATTRIBUTES = ResourceKey.createRegistryKey(AStagesUtil.fromNamespaceAndPath("attributes"));
    }
}
