package com.example.pluginsystem.plugins;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.core.util.Consumer;
import androidx.lifecycle.LifecycleObserver;

import com.example.pluginsystem.ui.BaseActivity;


public interface IPluginHost extends Handler.Callback, LifecycleObserver, INonConfigurationInstances {

    void registerPlugin(IPlugin plugin);

    void requestFinish();

    void finishImmediately();

    void runInactivityContext(@NonNull Consumer<BaseActivity> consumer);

    <T extends IPlugin> void ifPresent(@NonNull Class<T> tClass, Consumer<T> consumer);

    void requestPermissions(@NonNull String[] permissions, @IntRange(from = 0) int requestCode);

    void startActivity(@RequiresPermission Intent intent, @Nullable Bundle options);

    void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options);

    void pluginManagerHandler(@NonNull Consumer<PluginManagerHandler> consumer);
}
