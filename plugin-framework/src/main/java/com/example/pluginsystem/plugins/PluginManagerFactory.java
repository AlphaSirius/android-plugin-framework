package com.example.pluginsystem.plugins;

import androidx.annotation.NonNull;

import com.example.pluginsystem.PluginObjectFactory;

public class PluginManagerFactory {

    private final PluginObjectFactory pluginObjectFactory;
    public PluginManagerFactory(@NonNull PluginObjectFactory pluginObjectFactory) {

        this.pluginObjectFactory = pluginObjectFactory;
    }
    public PluginManager createPluginManager() {

        return new PluginManager(this.pluginObjectFactory);
    }
}
