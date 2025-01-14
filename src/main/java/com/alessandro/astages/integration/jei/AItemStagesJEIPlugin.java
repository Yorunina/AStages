package com.alessandro.astages.integration.jei;

import com.alessandro.astages.AStages;
import com.alessandro.astages.event.custom.ClientSynchronizeStagesEvent;
import com.alessandro.astages.event.custom.actions.ClientJeiReloadEvent;
import com.alessandro.astages.event.custom.actions.ClientJeiUpdateEvent;
import com.alessandro.astages.integration.Mods;
import com.alessandro.astages.networking.packet.syncer.IsJeiRestrictedC2SPacket;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.neoforged.neoforge.client.event.RecipesUpdatedEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JeiPlugin
public class AItemStagesJEIPlugin implements IModPlugin {
    private IJeiRuntime runtime;
    private static final ResourceLocation PLUGIN_ID = ResourceLocation.fromNamespaceAndPath(AStages.MODID, "item_jei");
    public static final List<ItemStack> itemsToHide = Collections.synchronizedList(new ArrayList<>());

    public AItemStagesJEIPlugin() {
        if (!Mods.JEI.isLoaded()) return;

        if (EffectiveSide.get().isClient() && !EffectiveSide.get().isServer()) {
            NeoForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ClientSynchronizeStagesEvent.class, e -> requestItemsToServer());
            NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, false, RecipesUpdatedEvent.class, e -> requestItemsToServer());
            NeoForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ClientJeiReloadEvent.class, e -> requestItemsToServer());

            NeoForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ClientJeiUpdateEvent.class, e -> updateGui());
        }
    }

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
    }

    public void requestItemsToServer() {
        if (runtime != null) {
            IIngredientManager ingredientManager = runtime.getIngredientManager();

            // Reset JEI
            if (!itemsToHide.isEmpty()) {
                ingredientManager.addIngredientsAtRuntime(VanillaTypes.ITEM_STACK, itemsToHide);
                itemsToHide.clear();
            }

            ItemStack lastItem = (ItemStack) ingredientManager.getAllItemStacks().toArray()[ ingredientManager.getAllItemStacks().size() - 1];
            // Get Restrictions
            for (ItemStack stack : ingredientManager.getAllItemStacks()) {
                PacketDistributor.sendToServer(new IsJeiRestrictedC2SPacket(stack, ItemStack.matches(stack, lastItem)));
            }
        }
    }

    public void updateGui() {
        if (runtime != null) {
            IIngredientManager ingredientManager = runtime.getIngredientManager();

            // Hide in JEI
            if (!itemsToHide.isEmpty()) {
                ingredientManager.removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, itemsToHide);
            }
        }
    }
}

