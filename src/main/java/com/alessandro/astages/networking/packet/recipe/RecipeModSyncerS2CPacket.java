package com.alessandro.astages.networking.packet.recipe;

import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.client.restriction.recipe.AClientRecipeModRestriction;
import com.alessandro.astages.core.server.restriction.recipe.ARecipeModRestriction;
import com.alessandro.astages.core.wrapper.RecipeModWrapper;
import com.alessandro.astages.networking.packet.RestrictionSyncerPacket;
import com.alessandro.astages.api.annotation.nullability.NotNullParams;
import com.alessandro.astages.api.annotation.develop.Info;
import net.minecraft.network.FriendlyByteBuf;

@NotNullParams
@Info("For now, required only by JEI.")
public class RecipeModSyncerS2CPacket extends RestrictionSyncerPacket {
    private final int priority;
    private final String modId;

    public RecipeModSyncerS2CPacket(ARecipeModRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getPriority(), restriction.getModId());
    }

    public RecipeModSyncerS2CPacket(String id, String stage, int priority, String modId) {
        super(id, stage);
        this.priority = priority;
        this.modId = modId;
    }

    public RecipeModSyncerS2CPacket(FriendlyByteBuf buf) {
        super(buf);
        this.priority = buf.readInt();
        this.modId = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(priority);
        buf.writeUtf(modId);
    }

    @Override
    public void handle() {
        var restriction = new AClientRecipeModRestriction(getId(), getStage())
                .restrict(new RecipeModWrapper(modId));

        AClientRestrictionManager.RECIPE_INSTANCE.addRestriction(restriction);
    }
}
