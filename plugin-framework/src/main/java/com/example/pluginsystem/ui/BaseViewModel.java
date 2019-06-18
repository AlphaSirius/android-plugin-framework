package com.example.pluginsystem.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.pluginsystem.PluginObjectFactory;
import com.example.pluginsystem.plugins.PluginManager;

public class BaseViewModel extends ViewModel {

    private PluginManager pluginManager;
    public PluginManager getPluginManager(@NonNull PluginObjectFactory pluginObjectFactory) {

        if(this.pluginManager == null) {

            synchronized (this) {

                if(this.pluginManager == null) {

                    this.pluginManager = pluginObjectFactory.createPluginManager();
                }
            }
        }

        return pluginManager;
    }
}
