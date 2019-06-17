package com.example.pluginsystem.plugins.core;

import androidx.annotation.NonNull;

import com.example.pluginsystem.PluginObjectFactory;
import com.example.pluginsystem.plugins.PluginInterests;
import com.example.pluginsystem.plugins.PluginManagerHandler;
import com.example.pluginsystem.plugins.PluginManagerHandlerThread;

public class AndroidPermissionPlugin extends BasePlugin {

    private static final int startRequestId = 1;
    private static final int lastRequestId = 100;

    public AndroidPermissionPlugin(@NonNull PluginManagerHandler pluginManagerHandler, @NonNull PluginObjectFactory pluginObjectFactory) {

        super(startRequestId, lastRequestId, new PluginInterests.Builder().setInterestedInPermissionResult(true).build(), pluginManagerHandler, pluginObjectFactory);
    }

}
