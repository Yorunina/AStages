package com.alessandro.astages.infrastructure.networking.packet.recipe;

import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.wrapper.RecipeModWrapper;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.recipe.AClientRecipeModRestriction;
import com.alessandro.astages.engine.server.restriction.recipe.ARecipeModRestriction;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.infrastructure.networking.packet.BaseRestrictionSyncer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

@NotNullParams
@Info("For now, required only by JEI.")
public class SyncRecipeModS2C extends BaseRestrictionSyncer {
    private final int priority;
    private final boolean reverse;
    private final String modId;
    private final List<ResourceLocation> ignoredRecipeIds;

    public SyncRecipeModS2C(ARecipeModRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getPriority(), restriction.get(Attributes.REVERSE), restriction.getModId(), restriction.getIgnoredRecipeIds());
    }

    public SyncRecipeModS2C(String id, String stage, int priority, boolean reverse, String modId, List<ResourceLocation> ignoredRecipeIds) {
        super(id, stage);
        this.priority = priority;
        this.reverse = reverse;
        this.modId = modId;
        this.ignoredRecipeIds = ignoredRecipeIds;
    }

    public SyncRecipeModS2C(FriendlyByteBuf buf) {
        super(buf);
        this.priority = buf.readInt();
        this.reverse = buf.readBoolean();
        this.modId = buf.readUtf();
        this.ignoredRecipeIds = buf.readList(FriendlyByteBuf::readResourceLocation);
    }

    public void toBytes(FriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(priority);
        buf.writeBoolean(reverse);
        buf.writeUtf(modId);
        buf.writeCollection(ignoredRecipeIds, FriendlyByteBuf::writeResourceLocation);
    }

    @Override
    public void handle() {
        var restriction = new AClientRecipeModRestriction(getId(), getStage())
                .set(Attributes.REVERSE, reverse)
                .restrict(new RecipeModWrapper(modId))
                .ignoreItems(ignoredRecipeIds);

        AClientRestrictionManager.RECIPE_INSTANCE.addRestriction(restriction);
    }
}
