package com.alessandro.astages.networking.packet.mob;

import com.alessandro.astages.core.client.AClientMobRestriction;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.restriction.AMobRestriction;
import com.alessandro.astages.store.Attributes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class MobSyncerS2CPacket {
    private final String id;
    private final String stage;
    private final List<EntityType<?>> types;
    private final Component jadeMobMessage;

    public MobSyncerS2CPacket(AMobRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getMobs(), restriction.get(Attributes.Mob.JADE_MOB_MESSAGE).get());
    }

    public MobSyncerS2CPacket(String id, String stage, List<EntityType<?>> types, Component jadeMobMessage) {
        this.id = id;
        this.stage = stage;
        this.types = types;
        this.jadeMobMessage = jadeMobMessage;
    }

    public MobSyncerS2CPacket(FriendlyByteBuf buf) {
        id = buf.readUtf();
        stage = buf.readUtf();
        types = buf.readList(r -> r.readRegistryIdUnsafe(ForgeRegistries.ENTITY_TYPES));
        jadeMobMessage = buf.readComponent();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(id);
        buf.writeUtf(stage);
         buf.writeCollection(types, (b, type) -> b.writeRegistryIdUnsafe(ForgeRegistries.ENTITY_TYPES, type));
        buf.writeComponent(jadeMobMessage);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var restriction = new AClientMobRestriction(id, stage, types, jadeMobMessage);
            AClientRestrictionManager.MOB_INSTANCE.addRestriction(stage, restriction);
        });

        ctx.get().setPacketHandled(true);
    }
}
