package com.alessandro.astages.internal.experimental;

import net.minecraftforge.eventbus.api.Event;

public class ClientReloadResourcePacksEvent extends Event {
    public static class Pre extends ClientReloadResourcePacksEvent { }
    public static class Post extends ClientReloadResourcePacksEvent { }
}
