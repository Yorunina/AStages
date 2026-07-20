package com.alessandro.astages.infrastructure.integration.probejs;

import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.probejs.ProbeConfig;
import com.probejs.ProbeJS;
import com.probejs.docs.formatter.formatter.IFormatter;
import net.minecraft.tags.TagKey;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.stream.Collectors;

@NotNullMethodsReturn
public class AItemTagFormatter implements IFormatter {
    @Override
    public @Unmodifiable List<String> format(Integer indent, Integer stepIndent) {
        var suggestions = "string";

        var tagManager = ForgeRegistries.ITEMS.tags();
        if (tagManager != null) {
            var collectedTags = tagManager.getTagNames()
                .map(TagKey::location)
                .map(resourceLocation ->
                    ProbeJS.GSON.toJson(resourceLocation.toString()))
                .collect(Collectors.joining(" | "));

            if (!collectedTags.isEmpty() && ProbeConfig.INSTANCE.allowRegistryLiteralDumps) {
                suggestions = collectedTags;
            }
        }

        return List.of("%stype AItemTag = %s;".formatted(" ".repeat(indent), suggestions));
    }
}
