package com.example.pluginsystem.plugins;

import android.os.Handler;
import android.os.HandlerThread;

import androidx.annotation.NonNull;

public class PluginManagerHandlerThread extends HandlerThread {

    private PluginManagerHandler handler;
    private final Handler.Callback callback;
    public PluginManagerHandlerThread(@NonNull String name, @NonNull Handler.Callback callback) {

        super(name);
        this.callback = callback;
    }

    public PluginManagerHandler getHandler(){

        if(this.handler == null) {

            synchronized (this) {

                if(this.handler == null) {

                    this.handler = new PluginManagerHandler(getLooper(), this.callback);
                }
            }

        }
        return this.handler;
    }
}
