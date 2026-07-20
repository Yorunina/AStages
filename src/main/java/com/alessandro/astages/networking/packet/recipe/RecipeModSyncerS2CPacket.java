package com.alessandro.astages.networking.packet.recipe;

import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.client.restriction.recipe.AClientRecipeModRestriction;
import com.alessandro.astages.core.server.restriction.recipe.ARecipeModRestriction;
import com.alessandro.astages.core.wrapper.RecipeModWrapper;
import com.alessandro.astages.networking.packet.RestrictionSyncerPacket;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.store.Attributes;
import net.minecraft.network.FriendlyByteBuf;

@NotNullParams
@Info("For now, required only by JEI.")
public class RecipeModSyncerS2CPacket extends RestrictionSyncerPacket {
    private final int priority;
    private final boolean reverse;
    private final String modId;

    public RecipeModSyncerS2CPacket(ARecipeModRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getPriority(), restriction.get(Attributes.REVERSE), restriction.getModId());
    }

    public RecipeModSyncerS2CPacket(String id, String stage, int priority, boolean reverse, String modId) {
        super(id, stage);
        this.priority = priority;
        this.reverse = reverse;
        this.modId = modId;
    }

    public RecipeModSyncerS2CPacket(FriendlyByteBuf buf) {
        super(buf);
        this.priority = buf.readInt();
        this.reverse = buf.readBoolean();
        this.modId = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(priority);
        buf.writeBoolean(reverse);
        buf.writeUtf(modId);
    }

    @Override
    public void handle() {
        var restriction = new AClientRecipeModRestriction(getId(), getStage())
                .set(Attributes.REVERSE, reverse)
                .restrict(new RecipeModWrapper(modId));

        AClientRestrictionManager.RECIPE_INSTANCE.addRestriction(restriction);
    }
}
