package com.alessandro.astages.simple;

import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.restriction.*;
import com.alessandro.astages.core.restriction.item.AItemModRestriction;
import com.alessandro.astages.core.restriction.item.AItemRestriction;
import com.alessandro.astages.core.restriction.recipe.ARecipeRestriction;
import com.alessandro.astages.core.wrapper.OreWrapper;
import com.alessandro.astages.core.wrapper.RecipeWrapper;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.util.develop.UnderDevelopment;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ASimpleElaborator {
    @UnderDevelopment
    public static void elaborateItem(@NotNull ASimpleRestriction simple) {
        ARestrictionManager.ITEM_INSTANCE.addRestriction(new AItemRestriction(simple.id, simple.stage).restrict(ForgeRegistries.ITEMS.getValue(new ResourceLocation(simple.object))));
    }

    @UnderDevelopment
    public static void elaborateMod(@NotNull ASimpleRestriction simple) {
        ARestrictionManager.ITEM_INSTANCE.addRestriction(new AItemModRestriction(simple.id, simple.stage).restrict(simple.object));
    }

    public static void elaborateDimension(@NotNull ASimpleRestriction simple) {
        ARestrictionManager.DIMENSION_INSTANCE.addRestriction(new ADimensionRestriction(simple.id, simple.stage).restrict(new ResourceLocation(simple.object)));
    }

    public static void elaborateGui(@NotNull ASimpleRestriction simple) {
        ARestrictionManager.SCREEN_INSTANCE.addRestriction(new AScreenRestriction(simple.id, simple.stage).restrict(ForgeRegistries.MENU_TYPES.getValue(new ResourceLocation(simple.object))));
    }

    public static void elaborateOre(@NotNull ASimpleRestriction simple) {
        String[] splice = simple.object.split("//");
        BlockState original = Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(splice[0]))).defaultBlockState();
        var replacement = Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(splice[1]))).defaultBlockState();
        ARestrictionManager.ORE_INSTANCE.addRestriction(new AOreRestriction(simple.id, simple.stage).restrict(new OreWrapper(original, replacement)));
    }

    public static void elaborateStructure(@NotNull ASimpleRestriction simple) {
        ARestrictionManager.STRUCTURE_INSTANCE.addRestriction(new AStructureRestriction(simple.id, simple.stage).restrict(new ResourceLocation(simple.object)));
    }

    @SuppressWarnings("unused")
    public static void elaborateBiome(@NotNull ASimpleRestriction simple) {
        throw new UnsupportedOperationException("Biome elaboration not supported! Id of Restriction not allowed: " + simple.id + ".");
    }

    public static void elaborateTame(@NotNull ASimpleRestriction simple) {
        ARestrictionManager.PET_INSTANCE.addRestriction(new APetRestriction(simple.id, simple.stage).restrict(ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(simple.object))).set(Attributes.BREEDABLE, true).set(Attributes.MOUNTABLE, true).set(Attributes.TAMABLE, false));
    }

    public static void elaborateMount(@NotNull ASimpleRestriction simple) {
        ARestrictionManager.PET_INSTANCE.addRestriction(new APetRestriction(simple.id, simple.stage).restrict(ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(simple.object))).set(Attributes.BREEDABLE, true).set(Attributes.MOUNTABLE, false).set(Attributes.TAMABLE, true));
    }

    public static void elaborateRecipe(@NotNull ASimpleRestriction simple) {
        String[] splice = simple.object.split("//");
        var type = ForgeRegistries.RECIPE_TYPES.getValue(new ResourceLocation(splice[0]));
        var id = new ResourceLocation(splice[1]);
        ARestrictionManager.RECIPE_INSTANCE.addRestriction(new ARecipeRestriction(simple.id, simple.stage).restrict(new RecipeWrapper(type, id)));
    }

    @UnderDevelopment
    public static void elaborateArmor(@NotNull ASimpleRestriction simple) {
        var restriction = new AItemRestriction(simple.id, simple.stage);
        restriction.restrict(ForgeRegistries.ITEMS.getValue(new ResourceLocation(simple.object)));
        restriction.set(Attributes.HIDING_TOOLTIP, false)
            .set(Attributes.STORING_IN_INVENTORY, true)
            .set(Attributes.EQUIPPING, true)
            .set(Attributes.ATTACKING, true)
            .set(Attributes.HIDING_JEI, false)
            .set(Attributes.BLOCK_PLACING, true)
            .set(Attributes.LEFT_CLICK_INTERACTIONS, true)
            .set(Attributes.RIGHT_CLICK_INTERACTIONS, true)
            .set(Attributes.BLOCK_BREAKING, true);
    }

    public static int commandItem(CommandContext<CommandSourceStack> c) {
        return addRestrictionForType(ASimpleRestrictionType.ITEM, StringArgumentType.getString(c, "id"), StringArgumentType.getString(c, "stage"), Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(ItemArgument.getItem(c, "item").getItem())).toString());
    }

    public static int commandMod(CommandContext<CommandSourceStack> c) {
        return addRestrictionForType(ASimpleRestrictionType.MOD, StringArgumentType.getString(c, "id"), StringArgumentType.getString(c, "stage"), StringArgumentType.getString(c, "mod"));
    }

    public static int commandDimension(CommandContext<CommandSourceStack> c) throws CommandSyntaxException {
        return addRestrictionForType(ASimpleRestrictionType.DIMENSION, StringArgumentType.getString(c, "id"), StringArgumentType.getString(c, "stage"), DimensionArgument.getDimension(c, "dimension").dimension().location().toString());
    }

    public static int commandGui(CommandContext<CommandSourceStack> c) {
        return addRestrictionForType(ASimpleRestrictionType.GUI, StringArgumentType.getString(c, "id"), StringArgumentType.getString(c, "stage"), StringArgumentType.getString(c, "gui"));
    }

    public static int commandOre(CommandContext<CommandSourceStack> c) {
        return addRestrictionForType(ASimpleRestrictionType.ORE, StringArgumentType.getString(c, "id"), StringArgumentType.getString(c, "stage"),
            Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(BlockStateArgument.getBlock(c, "ore").getState().getBlock())) +
                "//" +
                Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(BlockStateArgument.getBlock(c, "replacement").getState().getBlock()))
        );
    }

    public static int commandStructure(CommandContext<CommandSourceStack> c) throws CommandSyntaxException {
        return addRestrictionForType(ASimpleRestrictionType.STRUCTURE, StringArgumentType.getString(c, "id"), StringArgumentType.getString(c, "stage"), ResourceArgument.getStructure(c, "structure").getType().toString());
    }

    public static int commandBiome(CommandContext<CommandSourceStack> ignoredC) {
        throw new UnsupportedOperationException("Biome elaboration not supported!");
    }

    public static int commandTame(CommandContext<CommandSourceStack> c) throws CommandSyntaxException {
        return addRestrictionForType(ASimpleRestrictionType.TAME, StringArgumentType.getString(c, "id"), StringArgumentType.getString(c, "stage"), Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(ResourceArgument.getSummonableEntityType(c, "tame").value())).toString());
    }

    public static int commandMount(CommandContext<CommandSourceStack> c) throws CommandSyntaxException {
        return addRestrictionForType(ASimpleRestrictionType.MOUNT, StringArgumentType.getString(c, "id"), StringArgumentType.getString(c, "stage"), Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(ResourceArgument.getSummonableEntityType(c, "mount").value())).toString());
    }

    public static int commandRecipe(CommandContext<CommandSourceStack> c) throws CommandSyntaxException {
        return addRestrictionForType(ASimpleRestrictionType.RECIPE, StringArgumentType.getString(c, "id"), StringArgumentType.getString(c, "stage"),
            Objects.requireNonNull(Objects.requireNonNull(ForgeRegistries.RECIPE_TYPES.getKey(ResourceLocationArgument.getRecipe(c, "recipe").getType())).toString()) +
                "//" +
                Objects.requireNonNull(ResourceLocationArgument.getRecipe(c, "recipe").getId().toString())
        );
    }

    public static int commandArmor(CommandContext<CommandSourceStack> c) {
        return addRestrictionForType(ASimpleRestrictionType.ARMOR, StringArgumentType.getString(c, "id"), StringArgumentType.getString(c, "stage"), Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(ItemArgument.getItem(c, "item").getItem())).toString());
    }

    private static int addRestrictionForType(@NotNull ASimpleRestrictionType type, String id, String stage, String object) {
        ASimpleRestrictionManager.addRestriction(type, "simple/" + id, stage, object);

        return 1;
    }
}
