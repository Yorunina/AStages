package com.alessandro.astages.core;

import com.alessandro.astages.util.ARestriction;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.MenuType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class AScreenRestriction implements ARestriction {
    public final String id;

    public List<MenuType<?>> menus = new ArrayList<>();

    public Function<MenuType<?>, Component> openMessage = menu -> Component.translatable("message.astages.screen", menu.toString()).withStyle(ChatFormatting.RED);

    public AScreenRestriction(String id) {
        this.id = id;
    }

    public AScreenRestriction restrict(MenuType<?> menu) {
        menus.add(menu);

        return this;
    }

    public boolean isRestricted(MenuType<?> menu) {
        for (MenuType<?> men : menus) {
            if (men.equals(menu)) {
                return true;
            }
        }

        return false;
    }

    public Component getOpenMessage(MenuType<?> menu) {
        return openMessage.apply(menu);
    }

    public AScreenRestriction setOpenMessage(Function<MenuType<?>, Component> hiddenName) {
        this.openMessage = hiddenName;

        return this;
    }
}
