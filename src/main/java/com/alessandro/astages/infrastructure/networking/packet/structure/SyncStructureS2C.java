package com.alessandro.astages.infrastructure.networking.packet.structure;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.AClientStructureRestriction;
import com.alessandro.astages.engine.server.restriction.AStructureRestriction;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.infrastructure.networking.packet.BaseRestrictionSyncer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

@NotNullParams
public class SyncStructureS2C extends BaseRestrictionSyncer {
    private final Set<ResourceLocation> structures;
    private final boolean enter;

    public SyncStructureS2C(AStructureRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getStructures(), restriction.get(Attributes.ENTERING));
    }

    public SyncStructureS2C(String id, String stage, Set<ResourceLocation> structures, boolean enter) {
        super(id, stage);
        this.structures = structures;
        this.enter = enter;
    }

    public SyncStructureS2C(FriendlyByteBuf buf) {
        super(buf);
        structures = buf.readCollection(HashSet::new, FriendlyByteBuf::readResourceLocation);
        enter = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeCollection(structures, FriendlyByteBuf::writeResourceLocation);
        buf.writeBoolean(enter);
    }

    @Override
    public void handle() {
        var restriction = new AClientStructureRestriction(getId(), getStage())
            .set(Attributes.ENTERING, enter);

        for (var structure : structures) {
            restriction.restrict(structure);
        }

        AClientRestrictionManager.STRUCTURE_INSTANCE.addRestriction(restriction);
    }
}
