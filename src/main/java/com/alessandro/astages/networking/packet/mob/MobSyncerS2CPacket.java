package com.alessandro.astages.networking.packet.mob;

import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.client.restriction.AClientMobRestriction;
import com.alessandro.astages.core.server.restriction.AMobRestriction;
import com.alessandro.astages.networking.packet.RestrictionSyncerPacket;
import com.alessandro.astages.store.Attributes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class MobSyncerS2CPacket extends RestrictionSyncerPacket {
    private final List<EntityType<?>> types;
    private final Component jadeMobMessage;

    public MobSyncerS2CPacket(AMobRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getMobs(), restriction.get(Attributes.Mob.JADE_MOB_MESSAGE).get());
    }

    public MobSyncerS2CPacket(String id, String stage, List<EntityType<?>> types, Component jadeMobMessage) {
        super(id, stage);
        this.types = types;
        this.jadeMobMessage = jadeMobMessage;
    }

    public MobSyncerS2CPacket(FriendlyByteBuf buf) {
        super(buf);
        types = buf.readList(r -> r.readRegistryIdUnsafe(ForgeRegistries.ENTITY_TYPES));
        jadeMobMessage = buf.readComponent();
    }

    public void toBytes(FriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeCollection(types, (b, type) -> b.writeRegistryIdUnsafe(ForgeRegistries.ENTITY_TYPES, type));
        buf.writeComponent(jadeMobMessage);
    }

    @Override
    public void handle() {
        var restriction = new AClientMobRestriction(getId(), getStage())
                .set(Attributes.Mob.JADE_MOB_MESSAGE, () -> jadeMobMessage);

        for (var type : types) {
            restriction.restrict(type);
        }

        AClientRestrictionManager.MOB_INSTANCE.addRestriction(restriction);
    }
}
