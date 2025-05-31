package com.alessandro.astages.networking.packet.recipe;

import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.client.recipe.AClientRecipeRestriction;
import com.alessandro.astages.core.restriction.recipe.ARecipeRestriction;
import com.alessandro.astages.networking.packet.RestrictionSyncerPacket;
import com.alessandro.astages.util.develop.Info;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@Info("For now, required only by JEI.")
public class RecipeSyncerS2CPacket extends RestrictionSyncerPacket {
    private final int priority;
    private final RecipeType<?> type;
    private final List<ResourceLocation> recipes;

    public RecipeSyncerS2CPacket(ARecipeRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getPriority(), restriction.getType(), restriction.getRecipes());
    }

    public RecipeSyncerS2CPacket(String id, String stage, int priority, RecipeType<?> type, List<ResourceLocation> recipes) {
        super(id, stage);
        this.priority = priority;
        this.type = type;
        this.recipes = recipes;
    }

    public RecipeSyncerS2CPacket(FriendlyByteBuf buf) {
        super(buf);
        priority = buf.readInt();
        type = buf.readRegistryIdUnsafe(ForgeRegistries.RECIPE_TYPES);
        recipes = buf.readList(FriendlyByteBuf::readResourceLocation);
    }

    public void toBytes(FriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(priority);
        buf.writeRegistryIdUnsafe(ForgeRegistries.RECIPE_TYPES, type);
        buf.writeCollection(recipes, FriendlyByteBuf::writeResourceLocation);
    }

    @Override
    public void handle() {
        var restriction = new AClientRecipeRestriction(getId(), getStage(), priority, type, recipes);
        AClientRestrictionManager.RECIPE_INSTANCE.addRestriction(getStage(), restriction);
    }
}
