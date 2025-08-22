package com.alessandro.astages.store;

import com.alessandro.astages.api.annotation.develop.Info;

@Info("Mark a restriction as dirty to tell the server to update all clients! NOT during SERVER LOADING!")
public interface AMarkable {
    void markAsDirty();
}
