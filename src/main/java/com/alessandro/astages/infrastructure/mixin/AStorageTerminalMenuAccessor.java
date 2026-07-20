package com.alessandro.astages.infrastructure.mixin;

import com.tom.storagemod.gui.StorageTerminalMenu;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StorageTerminalMenu.class)
public interface AStorageTerminalMenuAccessor {
    @Accessor("pinv")
    Inventory astages$getPinv();
}
