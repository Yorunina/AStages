package com.alessandro.astages.mixin;

import com.alessandro.astages.capability.BlockStageProvider;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.util.AStagesUtil;
import com.simibubi.create.content.schematics.cannon.SchematicannonBlockEntity;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;

@Mixin(value = SchematicannonBlockEntity.class, remap = false)
public class ASchematicannonBlockEntity {
    @Unique
    private SchematicannonBlockEntity schematicannonBlockEntity$self() {
        return (SchematicannonBlockEntity) (Object) this;
    }

    @Inject(method = "tickPrinter", at = @At(value = "INVOKE", ordinal = 1, target = "Lcom/simibubi/create/content/schematics/cannon/SchematicannonBlockEntity;grabItemsFromAttachedInventories(Lcom/simibubi/create/content/schematics/requirement/ItemRequirement$StackRequirement;Z)Z"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void astages$tickPrinter(CallbackInfo ci, ItemStack blueprint, ItemRequirement requirement, List<ItemRequirement.StackRequirement> requiredItems, Iterator var4, ItemRequirement.StackRequirement stackToCheck) {
        if (schematicannonBlockEntity$self().getLevel() == null && schematicannonBlockEntity$self().getLevel().getServer() == null) { return; }

        schematicannonBlockEntity$self().getCapability(BlockStageProvider.BLOCK_STAGE).ifPresent(blockStage -> {
            var ownerUUID = blockStage.getOwner();
            var owner = AStagesUtil.getPlayerFromUUID(schematicannonBlockEntity$self().getLevel().getServer(), ownerUUID);

            if (owner != null) {
                var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(owner, stackToCheck.stack);

                if (restriction != null) {
                    ci.cancel();
                }
            }
        });
    }
}
