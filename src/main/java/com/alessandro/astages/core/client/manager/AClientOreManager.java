package com.alessandro.astages.core.client.manager;

import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.client.restriction.AClientOreRestriction;
import com.alessandro.astages.core.wrapper.OreWrapper;
import com.alessandro.astages.store.client.AClientManager;
import com.alessandro.astages.util.ARestrictionType;
import com.alessandro.astages.util.OrderedMultiMap;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AClientOreManager extends AClientManager<AClientOreRestriction, OreWrapper, BlockState> {
    private final OrderedMultiMap<BlockState, AClientOreRestriction> CACHE = OrderedMultiMap.create();
    private final OrderedMultiMap<String, AClientOreRestriction> RESTRICTIONS_BY_STAGE = OrderedMultiMap.create();

    public OrderedMultiMap<String, AClientOreRestriction> getRestrictionsByStage() {
        return RESTRICTIONS_BY_STAGE;
    }

    @Override
    public void reloadBeforeScripts() {
        super.reloadBeforeScripts();
        CACHE.clear();
        RESTRICTIONS_BY_STAGE.clear();
    }

    @Override
    public void addRestriction(AClientOreRestriction restriction) {
        super.addRestriction(restriction);

        AClientRestrictionManager.ORE_STAGES.add(restriction.getStage());
        CACHE.put(restriction.getOriginal(), restriction);
        RESTRICTIONS_BY_STAGE.put(restriction.getStage(), restriction);
    }

    @Override
    public AClientOreRestriction getRestriction(BlockState state) {
        return getRestrictionFromCache(CACHE, state);
    }

    public BlockState getReplacement(BlockState original) {
        var restriction = AClientRestrictionManager.ORE_INSTANCE.getRestriction(original);

        return restriction != null ? restriction.getReplacement() : original;
    }

    @Override
    public void removeRestriction(String id) {
        super.removeRestriction(id);
        CACHE.removeValues(restriction -> restriction.getId().equals(id));
        RESTRICTIONS_BY_STAGE.removeValues(restriction -> restriction.getId().equals(id));
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionType.ORE;
    }
}
