package com.example.pluginsystem.plugins.core;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.core.util.Consumer;

import com.example.pluginsystem.PluginObjectFactory;
import com.example.pluginsystem.plugins.IPlugin;
import com.example.pluginsystem.plugins.IPluginHost;
import com.example.pluginsystem.plugins.PluginHostRunnable;
import com.example.pluginsystem.plugins.PluginInterests;
import com.example.pluginsystem.plugins.PluginManagerHandler;
import com.example.pluginsystem.utils.AssertUtility;
import com.example.pluginsystem.utils.Predicate;


public class BasePlugin implements IPlugin {

    private final int startRequestId;
    private final int lastRequestId;
    private final AssertUtility assertUtility;
    private final PluginManagerHandler pluginManagerHandler;
    private final PluginObjectFactory pluginObjectFactory;
    private volatile PluginInterests pluginInterests;

    protected BasePlugin(int startRequestId, int lastRequestId, @NonNull PluginInterests pluginInterests, @NonNull PluginManagerHandler pluginManagerHandler, @NonNull PluginObjectFactory pluginObjectFactory) {

        this.startRequestId = startRequestId;
        this.lastRequestId = lastRequestId;
        this.assertUtility = pluginObjectFactory.get(AssertUtility.class);
        this.pluginInterests = pluginInterests;
        this.pluginManagerHandler = pluginManagerHandler;
        this.pluginObjectFactory = pluginObjectFactory;
    }

    protected AssertUtility getAssertUtility(){

        return this.assertUtility;
    }

    protected void updatePluginInterests(@NonNull PluginInterests pluginInterests){

        this.assertUtility.throwIfNull(pluginInterests);
        this.pluginInterests = pluginInterests;
    }

    @Override
    public int getStartRequestId() {

        return this.startRequestId;
    }

    @Override
    public int getLastRequestId() {

        return this.lastRequestId;
    }

    @Override
    public boolean isRequestIdInteresting(int requestId) {

        return requestId >= this.startRequestId && this.lastRequestId >= requestId;
    }

    @Override
    public void onClear() {

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onAny() {

    }

    @NonNull
    @Override
    public PluginInterests getPluginInterests() {

        return this.pluginInterests;
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    }

    @Override
    public Consumer<IPluginHost> onBackPressed() {

        return pluginHost -> {};
    }

    protected void executeOnPluginHost(@NonNull final Consumer<IPluginHost> consumer){

        this.assertUtility.ifPresent(this.pluginManagerHandler, pmh ->{

            PluginHostRunnable pluginHostRunnable = this.pluginObjectFactory.createPluginHostRunnable();
            pluginHostRunnable.setUp(consumer);
            pmh.getHandler().post(pluginHostRunnable);
        });
    }
}
