package com.alessandro.astages.core.restriction;

import com.alessandro.astages.store.ARestriction;
import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.store.Attributes;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class APetRestriction extends ARestriction<APetRestriction, EntityType<?>, EntityType<?>> {
    private final List<EntityType<?>> pets = new ArrayList<>();

    public APetRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
        return AttributeStore.builder()
            .addAttribute(Attributes.TAMABLE)
            .addAttribute(Attributes.BREEDABLE)
            .addAttribute(Attributes.MOUNTABLE)

            .addAttribute(Attributes.Pet.TAME_MESSAGE)
            .addAttribute(Attributes.Pet.BREED_MESSAGE)
            .addAttribute(Attributes.Pet.MOUNT_MESSAGE);
    }

    @Override
    public APetRestriction restrict(EntityType<?> pet) {
        pets.add(pet);

        return this;
    }

    @Override
    public boolean isRestricted(EntityType<?> pet) {
        return pets.contains(pet);
    }
}
