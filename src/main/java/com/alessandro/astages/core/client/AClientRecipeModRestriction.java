package com.alessandro.astages.core.client;

import com.alessandro.astages.util.AClientRestriction;

public record AClientRecipeModRestriction(String id, String stage, String modId) implements AClientRestriction { }
