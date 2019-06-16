package com.example.pluginsystem.plugins;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.core.util.Consumer;

public class PluginHostRunnable implements Runnable {

    private Consumer<IPluginHost> consumer;

    public void setUp(@NonNull Consumer<IPluginHost> consumer) {

        this.consumer = consumer;
    }

    public void run(@NonNull IPluginHost pluginHost){

        if(pluginHost != null) {

            this.consumer.accept(pluginHost);
        }
    }

    @Override
    public final void run() {
    }
}
