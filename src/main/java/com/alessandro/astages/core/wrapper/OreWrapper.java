package com.alessandro.astages.core.wrapper;

import net.minecraft.world.level.block.state.BlockState;

public record OreWrapper(BlockState original, BlockState replacement) { }
