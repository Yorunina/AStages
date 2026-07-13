package com.alessandro.astages.api.tag;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import dev.latvian.mods.rhino.Context;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

@NotNullParamsAndMethodsReturn
public class AItemTag {
    private final TagKey<Item> tag;

    private AItemTag(TagKey<Item> tag) {
        this.tag = tag;
    }

    public static AItemTag of(String id) {
        String cleaned = id.startsWith("#") ? id.substring(1) : id;
        return new AItemTag(TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), AResourceLocation.parse(cleaned)));
    }

    public static AItemTag of(TagKey<Item> tag) {
        return new AItemTag(tag);
    }

    public TagKey<Item> getTag() {
        return tag;
    }

    public static @Nullable AItemTag wrap(Context cx, @Nullable Object object) {
        if (object == null) { return null; }

        if (object instanceof AItemTag itemTag) {
            return itemTag;
        } else if (object instanceof TagKey<?> itemTag) {
            return AItemTag.of(TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), itemTag.location()));
        }

        return AItemTag.of(String.valueOf(object));
    }
}
