package com.example.pluginsystem.plugins;


import androidx.core.util.Consumer;

@FunctionalInterface
public interface IPluginOwner {

    void getPluginManager(Consumer<PluginManager> consumer);


}
