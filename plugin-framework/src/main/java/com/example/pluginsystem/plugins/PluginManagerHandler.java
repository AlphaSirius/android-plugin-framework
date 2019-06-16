package com.example.pluginsystem.plugins;

import android.os.Handler;
import android.os.HandlerThread;

import androidx.annotation.NonNull;

public class PluginManagerHandler extends HandlerThread {

    private Handler handler;
    public PluginManagerHandler(@NonNull String name, @NonNull Handler.Callback callback) {
        super(name);
    }

    public Handler getHandler(){

        if(this.handler == null) {

            synchronized (this) {

                if(this.handler == null) {

                    this.handler = new Handler(getLooper());
                }
            }

        }
        return this.handler;
    }
}
