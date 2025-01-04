package com.alessandro.astages.core.restriction;

import com.alessandro.astages.store.ARestriction;
import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.store.Attributes;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AStructureRestriction extends ARestriction<AStructureRestriction, ResourceLocation, ResourceLocation> {
    private final List<ResourceLocation> structures = new ArrayList<>();

    public AStructureRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
        return AttributeStore.builder()
            .addAttribute(Attributes.ATTACKING)
            .addAttribute(Attributes.GENERIC_INTERACTIONS)
            .addAttribute(Attributes.ENTERING)
            .addAttribute(Attributes.BLOCK_PLACING)
            .addAttribute(Attributes.BLOCK_BREAKING)
            .addAttribute(Attributes.EXPLOSIONS_AFFECT_BLOCKS)
            .addAttribute(Attributes.EXPLOSIONS_AFFECT_ENTITIES)

            .addAttribute(Attributes.Structure.ATTACK_MESSAGE)
            .addAttribute(Attributes.Structure.INTERACT_MESSAGE)
            .addAttribute(Attributes.Structure.ENTER_MESSAGE)
            .addAttribute(Attributes.Structure.PLACING_MESSAGE)
            .addAttribute(Attributes.Structure.MINING_MESSAGE);
    }

    @Override
    public AStructureRestriction restrict(ResourceLocation structure) {
        structures.add(structure);

        return this;
    }

    @Override
    public boolean isRestricted(ResourceLocation structure) {
        for (ResourceLocation str : structures) {
            if (str.equals(structure)) {
                return true;
            }
        }

        return false;
    }

    public List<ResourceLocation> getStructures() {
        return structures;
    }
}
