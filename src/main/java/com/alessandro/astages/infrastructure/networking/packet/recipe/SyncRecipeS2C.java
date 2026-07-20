package com.alessandro.astages.infrastructure.networking.packet.recipe;

import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.wrapper.RecipeWrapper;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.recipe.AClientRecipeRestriction;
import com.alessandro.astages.engine.server.restriction.recipe.ARecipeRestriction;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.infrastructure.networking.packet.BaseRestrictionSyncer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@NotNullParams
@Info("For now, required only by JEI.")
public class SyncRecipeS2C extends BaseRestrictionSyncer {
    private final int priority;
    private final boolean reverse;
    private final RecipeType<?> type;
    private final List<ResourceLocation> recipes;

    public SyncRecipeS2C(ARecipeRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getPriority(), restriction.get(Attributes.REVERSE), restriction.getType(), restriction.getRecipes());
    }

    public SyncRecipeS2C(String id, String stage, int priority, boolean reverse, RecipeType<?> type, List<ResourceLocation> recipes) {
        super(id, stage);
        this.priority = priority;
        this.reverse = reverse;
        this.type = type;
        this.recipes = recipes;
    }

    public SyncRecipeS2C(FriendlyByteBuf buf) {
        super(buf);
        priority = buf.readInt();
        reverse = buf.readBoolean();
        type = buf.readRegistryIdUnsafe(ForgeRegistries.RECIPE_TYPES);
        recipes = buf.readList(FriendlyByteBuf::readResourceLocation);
    }

    public void toBytes(FriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(priority);
        buf.writeBoolean(reverse);
        buf.writeRegistryIdUnsafe(ForgeRegistries.RECIPE_TYPES, type);
        buf.writeCollection(recipes, FriendlyByteBuf::writeResourceLocation);
    }

    @Override
    public void handle() {
        var restriction = new AClientRecipeRestriction(getId(), getStage())
                .setPriority(priority)
                .set(Attributes.REVERSE, reverse);

        for (ResourceLocation recipe : recipes) {
            restriction.restrict(new RecipeWrapper(type, recipe));
        }

        AClientRestrictionManager.RECIPE_INSTANCE.addRestriction(restriction);
    }
}
