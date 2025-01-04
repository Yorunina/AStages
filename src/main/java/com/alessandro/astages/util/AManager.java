package com.alessandro.astages.util;

import com.alessandro.astages.util.develop.Info;
import net.minecraft.world.entity.player.Player;

public interface AManager<T extends ARestriction, U> {
    void addRestriction(String stage, T restriction);

    T getRestriction(String id);

    @Info("For server!")
    T getRestriction(Player player, U object);

    void reloadBeforeScripts();
}
