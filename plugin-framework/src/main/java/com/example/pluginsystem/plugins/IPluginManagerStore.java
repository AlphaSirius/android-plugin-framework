package com.example.pluginsystem.plugins;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;


public interface IPluginManagerStore {

    void getLastSavedInstanceState(@NonNull Consumer<Bundle> consumer);

    @NonNull String getSignature();

}
