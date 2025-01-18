package com.alessandro.astages.networking.packet.syncer;

import com.alessandro.astages.core.client.AClientMobRestriction;
import com.alessandro.astages.core.client.AClientRestrictionManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class MobSyncerS2CPacket {
    private final String id;
    private final String stage;
    private final List<EntityType<?>> types;
    // private final EntityType<?> type;
    private final Component jadeMobMessage;

    public MobSyncerS2CPacket(String id, String stage, List<EntityType<?>> types, Component jadeMobMessage) {
    // public MobSyncerS2CPacket(String id, String stage, EntityType<?> type, Component jadeMobMessage) {
        this.id = id;
        this.stage = stage;
        this.types = types;
        this.jadeMobMessage = jadeMobMessage;
    }

    public MobSyncerS2CPacket(@NotNull FriendlyByteBuf buf) {
        id = buf.readUtf();
        stage = buf.readUtf();
        types = buf.readList(r -> r.readRegistryIdUnsafe(ForgeRegistries.ENTITY_TYPES));
        // type = buf.readRegistryIdUnsafe(ForgeRegistries.ENTITY_TYPES);
        jadeMobMessage = buf.readComponent();
    }

    public void toBytes(@NotNull FriendlyByteBuf buf) {
        buf.writeUtf(id);
        buf.writeUtf(stage);
         buf.writeCollection(types, (b, type) -> b.writeRegistryIdUnsafe(ForgeRegistries.ENTITY_TYPES, type));
        // buf.writeRegistryIdUnsafe(ForgeRegistries.ENTITY_TYPES, type);
        buf.writeComponent(jadeMobMessage);
    }

    public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var restriction = new AClientMobRestriction(id, stage, types, jadeMobMessage);
            AClientRestrictionManager.MOB_INSTANCE.addRestriction(id, restriction);
        });

        ctx.get().setPacketHandled(true);
    }
}
