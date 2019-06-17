package com.example.pluginsystem.plugins;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;

public class PluginManagerHandler extends Handler {

    public static final int EXECUTE_RUNNABLE = 1;
    private final Handler.Callback callback;


    public PluginManagerHandler(@NonNull Looper looper, @NonNull Handler.Callback callback) {

        super(looper);
        this.callback = callback;
    }

    public Message obtainMessage(@NonNull Consumer<IPluginHost> consumer) {

        return obtainMessage(EXECUTE_RUNNABLE, consumer);
    }

    @Override
    public void handleMessage(Message msg) {

        callback.handleMessage(msg);
    }
}
