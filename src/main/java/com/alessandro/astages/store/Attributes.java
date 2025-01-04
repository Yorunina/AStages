package com.alessandro.astages.store;

import com.alessandro.astages.util.ACompareCondition;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

// Read attributes as: can be used in/can be showed...
public class Attributes {
    public static final Attribute<Boolean> RENDERING_NAME = Attribute.create("rendering_name", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> HIDING_TOOLTIP = Attribute.create("hiding_tooltip", AttributeTypes.BOOLEAN, true);
    public static final Attribute<Boolean> PICKING_UP = Attribute.create("picking_up", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> EQUIPPING = Attribute.create("equipping", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> STORING_IN_INVENTORY = Attribute.create("storing_in_inventory", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> ATTACKING = Attribute.create("attacking", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> HIDING_JEI = Attribute.create("hiding_jei", AttributeTypes.BOOLEAN, true);
    public static final Attribute<Boolean> BLOCK_PLACING = Attribute.create("block_placing", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> LEFT_CLICK_INTERACTIONS = Attribute.create("left_click_interactions", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> RIGHT_CLICK_INTERACTIONS = Attribute.create("right_click_interactions", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> BLOCK_BREAKING = Attribute.create("block_breaking", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> TAMABLE = Attribute.create("tamable", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> BREEDABLE = Attribute.create("breedable", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> MOUNTABLE = Attribute.create("mountable", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> ENTERING = Attribute.create("entering", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> GENERIC_INTERACTIONS = Attribute.create("generic_interactions", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> EXPLOSIONS_AFFECT_BLOCKS = Attribute.create("explosion_affect_blocks", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> EXPLOSIONS_AFFECT_ENTITIES = Attribute.create("explosion_affect_entities", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> SPAWNER = Attribute.create("explosion_affect_entities", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> BIDIRECTIONAL = Attribute.create("bidirectional", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> ANVIL = Attribute.create("anvil", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> ENCHANTING_TABLE = Attribute.create("enchanting_table", AttributeTypes.BOOLEAN, false);

    public static final Attribute<Integer> PICK_UP_DELAY = Attribute.create("pick_up_delay", AttributeTypes.INTEGER, 60);
    public static final Attribute<Integer> AGE = Attribute.create("pick_up_delay", AttributeTypes.INTEGER, null);
    public static final Attribute<Integer> LEVEL = Attribute.create("level", AttributeTypes.INTEGER, null);

    public static final Attribute<ResourceLocation> DIMENSION = Attribute.create("dimension", AttributeTypes.RESOURCE_LOCATION, null);

    public static final Attribute<EntityType<?>> REPLACE = Attribute.create("replace", AttributeTypes.ENTITY, null);

    public static final Attribute<ACompareCondition> COMPARE_CONDITION = Attribute.create("compare_condition", AttributeTypes.COMPARE_CONDITION, null);

    // If the creation of subclasses is HORRIBLE, change the default value to null and request, when you add an attribute to the attribute store, to check if a new default value is set
    // ITEM
    public static class Item {
        public static final Attribute<Function<ItemStack, Component>> HIDDEN_NAME = Attribute.create("hidden_name", AttributeTypes.STACK_TO_COMPONENT, stack -> Component.translatable("tooltip.astages.item.hidden_name", stack.getHoverName()).withStyle(ChatFormatting.RED));
        public static final Attribute<Function<ItemStack, Component>> DROP_MESSAGE = Attribute.create("drop_message", AttributeTypes.STACK_TO_COMPONENT, stack -> Component.translatable("message.astages.item.drop", stack.getHoverName()).withStyle(ChatFormatting.RED));
        public static final Attribute<Function<ItemStack, Component>> ATTACK_MESSAGE = Attribute.create("attack_message", AttributeTypes.STACK_TO_COMPONENT, stack -> Component.translatable("message.astages.item.attach", stack.getHoverName()).withStyle(ChatFormatting.RED));
        public static final Attribute<Function<ItemStack, Component>> PICKING_UP_MESSAGE = Attribute.create("picking_up_message", AttributeTypes.STACK_TO_COMPONENT, stack -> Component.translatable("message.astages.item.pickup", stack.getHoverName()).withStyle(ChatFormatting.RED));
        public static final Attribute<Function<ItemStack, Component>> USING_MESSAGE = Attribute.create("using_message", AttributeTypes.STACK_TO_COMPONENT, stack -> Component.translatable("message.astages.item.use", stack.getHoverName()).withStyle(ChatFormatting.RED));
        public static final Attribute<Function<ItemStack, Component>> MINING_MESSAGE = Attribute.create("mining_message", AttributeTypes.STACK_TO_COMPONENT, stack -> Component.translatable("message.astages.item.mine", stack.getHoverName()).withStyle(ChatFormatting.RED));
        public static final Attribute<Function<ItemStack, Component>> PLACING_MESSAGE = Attribute.create("placing_message", AttributeTypes.STACK_TO_COMPONENT, stack -> Component.translatable("message.astages.item.place", stack.getHoverName()).withStyle(ChatFormatting.RED));
        public static final Attribute<Function<ItemStack, Component>> JADE_ITEM_MESSAGE = Attribute.create("jade_item_message", AttributeTypes.STACK_TO_COMPONENT, stack -> Component.translatable("tooltip.astages.item.jade_integration.item", stack.getHoverName()).withStyle(ChatFormatting.RED));
        public static final Attribute<Function<ItemStack, Component>> JADE_BLOCK_MESSAGE = Attribute.create("jade_block_message", AttributeTypes.STACK_TO_COMPONENT, stack -> Component.translatable("tooltip.astages.item.jade_integration.block", stack.getHoverName()).withStyle(ChatFormatting.RED));
    }

    // PET
    public static class Pet {
        public static final Attribute<Function<Entity, Component>> TAME_MESSAGE = Attribute.create("tame_message", AttributeTypes.ENTITY_TO_COMPONENT, entity -> Component.translatable("message.astages.pet.tame", entity.getName()).withStyle(ChatFormatting.RED));
        public static final Attribute<Function<Entity, Component>> BREED_MESSAGE = Attribute.create("breed_message", AttributeTypes.ENTITY_TO_COMPONENT, entity -> Component.translatable("message.astages.pet.breed", entity.getName()).withStyle(ChatFormatting.RED));
        public static final Attribute<Function<Entity, Component>> MOUNT_MESSAGE = Attribute.create("mount_message", AttributeTypes.ENTITY_TO_COMPONENT, entity -> Component.translatable("message.astages.pet.mount", entity.getName()).withStyle(ChatFormatting.RED));
    }

    // STRUCTURE
    public static class Structure {
        public static final Attribute<Function<ResourceLocation, Component>> ATTACK_MESSAGE = Attribute.create("attack_message", AttributeTypes.RESOURCE_LOCATION_TO_COMPONENT, resourceLocation -> Component.translatable("message.astages.structure.attack", AStagesUtil.structureToDescription(resourceLocation)).withStyle(ChatFormatting.RED));
        public static final Attribute<Function<ResourceLocation, Component>> INTERACT_MESSAGE = Attribute.create("interact_message", AttributeTypes.RESOURCE_LOCATION_TO_COMPONENT, resourceLocation -> Component.translatable("message.astages.structure.interact", AStagesUtil.structureToDescription(resourceLocation)).withStyle(ChatFormatting.RED));
        public static final Attribute<Function<ResourceLocation, Component>> ENTER_MESSAGE = Attribute.create("enter_message", AttributeTypes.RESOURCE_LOCATION_TO_COMPONENT, resourceLocation -> Component.translatable("message.astages.structure.enter", AStagesUtil.structureToDescription(resourceLocation)).withStyle(ChatFormatting.RED));
        public static final Attribute<Function<ResourceLocation, Component>> PLACING_MESSAGE = Attribute.create("placing_message", AttributeTypes.RESOURCE_LOCATION_TO_COMPONENT, resourceLocation -> Component.translatable("message.astages.structure.place", AStagesUtil.structureToDescription(resourceLocation)).withStyle(ChatFormatting.RED));
        public static final Attribute<Function<ResourceLocation, Component>> MINING_MESSAGE = Attribute.create("mining_message", AttributeTypes.RESOURCE_LOCATION_TO_COMPONENT, resourceLocation -> Component.translatable("message.astages.structure.break", AStagesUtil.structureToDescription(resourceLocation)).withStyle(ChatFormatting.RED));
    }

    // SCREEN
    public static class Screen {
        public static final Attribute<Function<MenuType<?>, Component>> OPEN_MESSAGE = Attribute.create("open_message", AttributeTypes.MENU_TO_COMPONENT, menu -> Component.translatable("message.astages.screen", menu.toString()).withStyle(ChatFormatting.RED));
    }

    // DIMENSION
    public static class Dimension {
        public static final Attribute<Function<ResourceLocation, Component>> ENTER_MESSAGE = Attribute.create("enter_message", AttributeTypes.RESOURCE_LOCATION_TO_COMPONENT, resourceLocation -> Component.translatable("message.astages.dimension", resourceLocation).withStyle(ChatFormatting.RED));
    }
}
