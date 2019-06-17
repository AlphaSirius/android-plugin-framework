package com.example.pluginsystem.plugins.core;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;

import com.example.pluginsystem.PluginObjectFactory;
import com.example.pluginsystem.plugins.PluginInterests;
import com.example.pluginsystem.plugins.PluginManagerHandler;
import com.example.pluginsystem.plugins.PluginManagerHandlerThread;


public class Navigator extends BasePlugin {

    private static final int startRequestId = 101;
    private static final int lastRequestId = 200;

    public Navigator(@NonNull PluginManagerHandler pluginManagerHandler, @NonNull PluginObjectFactory pluginObjectFactory) {
        super(startRequestId, lastRequestId, new PluginInterests.Builder().setInterestedInActivityResult(true).build(), pluginManagerHandler, pluginObjectFactory);
    }

    public void startActivity(@RequiresPermission Intent intent, @Nullable Bundle options) {

        executeOnPluginHost(pluginHost -> {

            pluginHost.startActivity(intent, options);
        });
    }
}
