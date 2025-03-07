package com.alessandro.astages.core.client;

import com.alessandro.astages.util.AClientRestriction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;

import java.util.List;

public record AClientMobRestriction(String id, String stage,
                                    List<EntityType<?>> types, Component jadeMobMessage) implements AClientRestriction {

    public boolean isRestricted(EntityType<?> type) {
        return this.types.contains(type);
    }
}
