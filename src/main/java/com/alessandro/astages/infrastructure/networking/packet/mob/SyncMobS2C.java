package com.alessandro.astages.infrastructure.networking.packet.mob;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.AClientMobRestriction;
import com.alessandro.astages.engine.server.restriction.AMobRestriction;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.infrastructure.networking.packet.BaseRestrictionSyncer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;

@NotNullParams
public class SyncMobS2C extends BaseRestrictionSyncer {
    private final Set<EntityType<?>> types;
    private final Component jadeMobMessage;

    public SyncMobS2C(AMobRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getMobs(), restriction.get(Attributes.Mob.JADE_MOB_MESSAGE).get());
    }

    public SyncMobS2C(String id, String stage, Set<EntityType<?>> types, Component jadeMobMessage) {
        super(id, stage);
        this.types = types;
        this.jadeMobMessage = jadeMobMessage;
    }

    public SyncMobS2C(FriendlyByteBuf buf) {
        super(buf);
        types = buf.readCollection(HashSet::new, r -> r.readRegistryIdUnsafe(ForgeRegistries.ENTITY_TYPES));
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
