package com.alessandro.astages.datageneration;

import com.alessandro.astages.AStages;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ALanguageProvider extends LanguageProvider {
    public ALanguageProvider(PackOutput output, String locale) {
        super(output, AStages.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        // GENERAL
        add("title.astages.add", "%s unlocked!");
        add("chat.astages.add", "Stage %s added successfully!");
        add("chat.astages.remove", "Stage %s removed successfully!");
        add("chat.astages.not_present", "Stages %s is not present in your stages");
        add("chat.astages.remove_all", "All stages removed successfully!");
        add("chat.astages.info.no_stages", "No stages unlocked for player %s!");
        add("chat.astages.info.has_stages", "Stages unlocked by %s:");
        add("chat.astages.info.list_item", " - %s");

        // ITEM
        add("tooltip.astages.item.hidden_name", "Unfamiliar Item");
        add("tooltip.astages.item.item_description", "This item has been staged to [%s]");
        add("message.astages.item.drop", "You dropped the %s.");
        add("message.astages.item.mine", "You do not know how to mine with the %s.");
        add("message.astages.item.attach", "You don't know how to attack with the %s.");
        add("message.astages.item.use", "You don't know how to use the %s.");
        add("message.astages.item.pickup", "You couldn't pick up the %s.");
        add("message.astages.item.place", "You couldn't place the %s.");
        add("tooltip.astages.item.jade_integration.item", "Unfamiliar Item");
        add("tooltip.astages.item.jade_integration.block", "Unfamiliar Block");

        // DIMENSION
        add("message.astages.dimension", "You can't visit this dimension!");

        // SCREEN
        add("message.astages.screen", "You can't open this menu!");

        // PET
        add("message.astages.pet.tame", "You can't tame %s!");
        add("message.astages.pet.breed", "You can't breed %s!");
        add("message.astages.pet.mount", "You can't mount %s!");

        // STRUCTURE
        add("message.astages.structure.attack", "You can't attack in %s!");
        add("message.astages.structure.interact", "You can't interact with %s!");
        add("message.astages.structure.enter", "You can't enter in %s!");
        add("message.astages.structure.place", "You can't place in %s!");
        add("message.astages.structure.break", "You can't break in %s!");

        // JADE INTEGRATION
        add("config.jade.plugin_astages.block_component_provider", "AStages");
    }
}
