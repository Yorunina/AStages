package com.alessandro.astages.core.client;

import com.alessandro.astages.AStages;
import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.core.AItemRestriction;
import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.networking.packet.ud.IsItemRestrictedC2SPacket;
import com.alessandro.astages.util.AClientManager;
import com.alessandro.astages.util.AClientQuestionType;
import com.alessandro.astages.util.AStagesUtil;
import com.alessandro.astages.util.Triple;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

// SOLVED ISSUE WITH RECIPE

@OnlyIn(Dist.CLIENT)
public class AClientItemManager implements AClientManager {
    public Map<String, List<AClientItemRestriction>> restrictions = new HashMap<>();
    public Map<ItemStack, AClientItemRestriction> cache = new HashMap<>();




    // You need to reset only on reloading
    public List<Triple<String, ItemStack, Component>> restrictedStacksForTooltip = new ArrayList<>();
    public List<Triple<String, ItemStack, Component>> restrictedStacksForName = new ArrayList<>();

    public List<ItemStack> notRestrictedStacksForTooltip = new ArrayList<>();
    public List<ItemStack> notRestrictedStacksForName = new ArrayList<>();

    public AClientItemRestriction getRestriction(@NotNull ItemStack stack) {
        if (stack.isEmpty()) { return null; }

        for (String stage : restrictions.keySet()) {
            for (AClientItemRestriction restriction : restrictions.get(stage)) {
                if (restriction.stack().equals(stack, false)) {
                    if (ClientPlayerStage.hasStage(stage)) {
                        return null;
                    } else {
                        return restriction;
                    }
                }
            }
        }

        ModNetworking.sendToServer(new IsItemRestrictedC2SPacket(stack));
        return null;
    }

//    public Component isTooltipRestricted(ItemStack stack) {
//        for (var item : notRestrictedStacksForTooltip) {
//            if (item.equals(stack, false)) {
//                return null;
//            }
//        }
//
//        for (var triple : restrictedStacksForTooltip) {
//            if (!ClientPlayerStage.hasStage(triple.a())) {
//                if (stack.equals(triple.b(), false)) {
//                    return triple.c();
//                }
//            }
////            else {
////                return null;
////            }
//        }
//
//        AStages.LOGGER.debug("Requested to the SERVER for {}.", stack.toString());
//        ModNetworking.sendToServer(new IsItemRestrictedC2SPacket(stack, AClientQuestionType.TOOLTIP, false));
//        return null;
//    }

//    public Component isRenderItemNameRestricted(ItemStack stack) {
//        if (notRestrictedStacksForName.contains(stack)) { return null; }
//
//        for (var triple : restrictedStacksForName) {
//            if (!ClientPlayerStage.hasStage(triple.a())) {
//                if (stack.equals(triple.b(), false)) {
//                    return triple.c();
//                }
//            }
////            else {
////                return null;
////            }
//        }
//
//        ModNetworking.sendToServer(new IsItemRestrictedC2SPacket(stack, AClientQuestionType.NAME, false));
//        return null;
//    }

//    public Map<String, List<AClientItemRestriction>> restrictions = new HashMap<>();
//
//    public static List<Item> itemsRestricted = new ArrayList<>();
//    public Map<String, Set<Item>> itemsRestrictedWithStage = new HashMap<>();

//    public void addRestriction(String stage, AClientItemRestriction item) {
//        var newList = restrictions.getOrDefault(stage, new ArrayList<>());
//        newList.add(item);
//        restrictions.put(stage, newList);
//    }
//
//    public @Nullable AClientItemRestriction getRestriction(Item item) {
//        for (String stage : restrictions.keySet()) {
//            for (AClientItemRestriction restriction : restrictions.get(stage)) {
//                if (restriction.stack.is(item) && !ClientPlayerStage.getPlayerStages().contains(stage)) {
//                    return restriction;
//                }
//            }
//        }
//
//        return null;
//    }
}
