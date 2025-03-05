package com.alessandro.astages.networking.packet.recipe;

import com.alessandro.astages.core.client.AClientRecipeModRestriction;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.restriction.ARecipeRestriction;
import com.alessandro.astages.util.develop.Info;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@Info("For now, required only by JEI.")
public class RecipeModSyncerS2CPacket {
    private final String id;
    private final String stage;
    private final String modId;

    public RecipeModSyncerS2CPacket(@NotNull ARecipeRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getModId());
    }

    public RecipeModSyncerS2CPacket(String id, String stage, String modId) {
        this.id = id;
        this.stage = stage;
        this.modId = modId;
    }

    public RecipeModSyncerS2CPacket(@NotNull FriendlyByteBuf buf) {
        this.id = buf.readUtf();
        this.stage = buf.readUtf();
        this.modId = buf.readUtf();
    }

    public void toBytes(@NotNull FriendlyByteBuf buf) {
        buf.writeUtf(id);
        buf.writeUtf(stage);
        buf.writeUtf(modId);
    }

    public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var restriction = new AClientRecipeModRestriction(id, stage, modId);
            AClientRestrictionManager.RECIPE_INSTANCE.addRestriction(restriction);
        });

        ctx.get().setPacketHandled(true);
    }
}
