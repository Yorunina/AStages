package com.alessandro.astages.integration.jei;

// import mezz.jei.api.forge.ForgeTypes

import com.alessandro.astages.AStages;
import com.alessandro.astages.event.custom.ClientSynchronizeStagesEvent;
import com.alessandro.astages.event.custom.actions.ClientJeiReloadEvent;
import com.alessandro.astages.event.custom.actions.ClientJeiUpdateEvent;
import com.alessandro.astages.integration.Mods;
import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.networking.packet.syncer.IsJeiRestrictedC2SPacket;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JeiPlugin
public class AItemStagesJEIPlugin implements IModPlugin {
    private IJeiRuntime runtime;
    private static final ResourceLocation PLUGIN_ID = new ResourceLocation(AStages.MODID, "item_jei");
    public static final List<ItemStack> itemsToHide = Collections.synchronizedList(new ArrayList<>());

    public AItemStagesJEIPlugin() {
        if (!Mods.JEI.isLoaded()) return;

        if (EffectiveSide.get().isClient() && !EffectiveSide.get().isServer()) {
            MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ClientSynchronizeStagesEvent.class, e -> requestItemsToServer());
            MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, false, RecipesUpdatedEvent.class, e -> requestItemsToServer());
            MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ClientJeiReloadEvent.class, e -> requestItemsToServer());

            MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ClientJeiUpdateEvent.class, e -> updateGui());
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
                ModNetworking.sendToServer(new IsJeiRestrictedC2SPacket(stack, stack.equals(lastItem, false)));
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
