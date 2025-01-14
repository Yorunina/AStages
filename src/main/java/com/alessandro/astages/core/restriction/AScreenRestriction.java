package com.alessandro.astages.core.restriction;

import com.alessandro.astages.store.ARestriction;
import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.store.Attributes;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AScreenRestriction extends ARestriction<AScreenRestriction, MenuType<?>, MenuType<?>> {
    private final List<MenuType<?>> menus = new ArrayList<>();

    public AScreenRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
        return AttributeStore.builder()
            .addAttribute(Attributes.Screen.OPEN_MESSAGE);
    }

    @Override
    public AScreenRestriction restrict(MenuType<?> menu) {
        menus.add(menu);

        return this;
    }

    @Override
    public boolean isRestricted(MenuType<?> menu) {
        for (MenuType<?> men : menus) {
            if (men.equals(menu)) {
                return true;
            }
        }

        return false;
    }
}
