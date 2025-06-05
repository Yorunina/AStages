package com.alessandro.astages.core.client.restriction;

import com.alessandro.astages.core.wrapper.OreWrapper;
import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.store.client.AClientRestriction;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AClientOreRestriction extends AClientRestriction<AClientOreRestriction, OreWrapper, BlockState> {
    private BlockState original;
    private BlockState replacement;

    public AClientOreRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
        return AttributeStore.builder();
    }

    @Override
    public AClientOreRestriction restrict(OreWrapper wrapper) {
        this.original = wrapper.original();
        this.replacement = wrapper.replacement();

        return this;
    }

    public boolean isRestricted(BlockState original) {
        return this.original.equals(original);
    }

    public BlockState getOriginal() {
        return original;
    }

    public BlockState getReplacement() {
        return replacement;
    }
}
