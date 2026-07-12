package com.alessandro.astages.infrastructure.integration.kubejs;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.constant.AFilter;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.stage.BaseStage;
import com.alessandro.astages.api.stage.Stage;
import com.alessandro.astages.api.stage.TemporaryStage;
import com.alessandro.astages.api.time.ATime;
import com.alessandro.astages.engine.server.restriction.*;
import com.alessandro.astages.engine.server.restriction.item.*;
import com.alessandro.astages.engine.server.restriction.recipe.ABaseRecipeRestriction;
import com.alessandro.astages.engine.server.restriction.recipe.ARecipeModRestriction;
import com.alessandro.astages.engine.server.restriction.recipe.ARecipeRestriction;
import com.alessandro.astages.engine.store.*;
import com.alessandro.astages.infrastructure.integration.Mods;
import com.alessandro.astages.infrastructure.integration.kubejs.bridge.KubeJSEventBridge;
import com.alessandro.astages.infrastructure.integration.kubejs.bridge.KubeJSStageEvents;
import com.alessandro.astages.infrastructure.integration.kubejs.util.KubeJSClientModelUtils;
import com.alessandro.astages.infrastructure.integration.kubejs.util.KubeJSClientUtils;
import com.alessandro.astages.infrastructure.integration.kubejs.util.KubeJSModelUtils;
import com.alessandro.astages.infrastructure.integration.kubejs.util.KubeJSServerUtils;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;

@NotNullParams
public class AKubeJSPlugin extends KubeJSPlugin {
    static {
        if (Mods.KUBEJS.isLoaded()) {
            KubeJSEventBridge.init();
        }
    }

    @Override
    public void registerTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        typeWrappers.registerSimple(ATime.class, ATime::of);
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        if (event.getType().isServer() || event.getType().isStartup()) {
            event.add("AStages", KubeJSServerUtils.class);
            event.add("AModels", KubeJSModelUtils.class);
        }

        if (event.getType().isClient()) {
            event.add("AStagesClient", KubeJSClientUtils.class);
            event.add("AModels", KubeJSClientModelUtils.class);
        }

        event.add("ATime", ATime.class);
        event.add("AFilter", AFilter.class);
        event.add("ARestrictionTypes", ARestrictionTypes.class);
        event.add("ASimpleRestrictionTypes", ASimpleRestrictionTypes.class);
        event.add("AttributeTypes", AttributeTypes.class);
        event.add("StageAttributeTypes", StageAttributeTypes.class);

        event.add("Attributes", Attributes.class);
        event.add("ItemAttributes", Attributes.Item.class);
        // RecipeAttributes
        // CropAttributes
        event.add("DimensionAttributes", Attributes.Dimension.class);
        // EffectAttributes
        // EnchantAttributes
        // LootAttributes
        event.add("MobAttributes", Attributes.Mob.class);
        // OreAttributes
        event.add("PetAttributes", Attributes.Pet.class);
        event.add("RegionAttributes", Attributes.Region.class);
        event.add("ScreenAttributes", Attributes.Screen.class);
        event.add("StructureAttributes", Attributes.Structure.class);
        event.add("StageAttributes", StageAttributes.class);

        event.add("ABaseItemRestriction", ABaseItemRestriction.class);
        event.add("AItemRestriction", AItemRestriction.class);
        event.add("AItemModRestriction", AItemModRestriction.class);
        event.add("AItemTagRestriction", AItemTagRestriction.class);
        event.add("AItemPredicateRestriction", AItemPredicateRestriction.class);

        event.add("ABaseRecipeRestriction", ABaseRecipeRestriction.class);
        event.add("ARecipeRestriction", ARecipeRestriction.class);
        event.add("ARecipeModRestriction", ARecipeModRestriction.class);

        event.add("ACropRestriction", ACropRestriction.class);
        event.add("ADimensionRestriction", ADimensionRestriction.class);
        event.add("AEffectRestriction", AEffectRestriction.class);
        event.add("AEnchantRestriction", AEnchantRestriction.class);
        event.add("ALootRestriction", ALootRestriction.class);
        event.add("AMobRestriction", AMobRestriction.class);
        event.add("AOreRestriction", AOreRestriction.class);
        event.add("APetRestriction", APetRestriction.class);
        event.add("ARegionRestriction", ARegionRestriction.class);
        event.add("AScreenRestriction", AScreenRestriction.class);
        event.add("AStructureRestriction", AStructureRestriction.class);

        event.add("BaseStage", BaseStage.class);
        event.add("Stage", Stage.class);
        event.add("TemporaryStage", TemporaryStage.class);
    }

    @Override
    public void registerEvents() {
        KubeJSStageEvents.GROUP.register();
    }

    @Override
    public void init() {
        AStages.LOGGER.debug("ASTAGES-KUBEJS: INITIALIZED PLUGIN!");
    }
}
