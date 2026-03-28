package com.alessandro.astages.internal.experimental.loot;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.LootModifier;

@NotNullParamsAndMethodsReturn
public class ALootModifier extends LootModifier {
    public static final Codec<ALootModifier> CODEC = RecordCodecBuilder.create(
        instance -> codecStart(instance).apply(instance, ALootModifier::new)
    );

    public ALootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
//        for (var condition : conditions) {
//            AStages.LOGGER.debug(condition.toString());
//        }
//
//        var entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
//
//        if (entity != null) {
//            AStages.LOGGER.debug(entity.getType().toString());
//        }
//
//        generatedLoot.add(new ItemStack(Items.EMERALD, 10));
        return generatedLoot;
    }

    @Override
    public Codec<ALootModifier> codec() {
        return CODEC;
    }
}
