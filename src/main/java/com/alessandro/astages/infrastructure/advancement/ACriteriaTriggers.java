package com.alessandro.astages.infrastructure.advancement;

import net.minecraft.advancements.CriteriaTriggers;

public class ACriteriaTriggers {
    public static final StageEarnTrigger STAGE_EARN = CriteriaTriggers.register(new StageEarnTrigger());

    public static void init() { }
}