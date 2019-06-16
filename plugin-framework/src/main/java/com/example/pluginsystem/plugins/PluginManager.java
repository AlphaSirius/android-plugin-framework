package com.example.pluginsystem.plugins;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.arch.core.util.Function;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import com.example.pluginsystem.PluginObjectFactory;
import com.example.pluginsystem.ui.BaseActivity;
import com.example.pluginsystem.utils.NonConfigurationInstances;
import com.example.pluginsystem.utils.AssertUtility;
import com.example.pluginsystem.utils.ExecutorHelper;
import com.example.pluginsystem.utils.Predicate;
import com.example.pluginsystem.utils.SafeMapper;

import java.lang.ref.WeakReference;
import java.util.Map;

public class PluginManager implements IPluginHost {

    private final AssertUtility assertUtility;
    private final SafeMapper<String, IPlugin> pluginSafeMapper;
    private final ExecutorHelper executorHelper;
    private final NonConfigurationInstances nonConfigurationInstances;
    private final PluginManagerHandler pluginManagerHandler;
    private final Runnable requestFinish = () -> finishImmediately();
    private WeakReference<Lifecycle> lifecycleWeakReference;
    private WeakReference<BaseActivity> baseActivityWeakReference;
    private volatile boolean isValid = false;

    public static final String PLUGIN_MANAGER_HANDLER_NAME = "pluginManagerHandler";


    public PluginManager(@NonNull PluginObjectFactory pluginObjectFactory) {

        this.assertUtility = pluginObjectFactory.get(AssertUtility.class);
        this.pluginSafeMapper = pluginObjectFactory.createSafeMapper();
        this.executorHelper = pluginObjectFactory.get(ExecutorHelper.class);
        this.nonConfigurationInstances = pluginObjectFactory.geNonConfigurationInstances();
        this.pluginManagerHandler = pluginObjectFactory.getPluginManagerHandler(String.format("%s_%s", PLUGIN_MANAGER_HANDLER_NAME, pluginObjectFactory.getUUID()), this);
    }

    public boolean isInitialized() {

        return pluginSafeMapper.size() != 0;
    }

    @Override
    public void getNonConfigurationInstance(Consumer<NonConfigurationInstances> consumer) {

        this.assertUtility.ifPresent(this.nonConfigurationInstances, nci -> consumer.accept(nci));
    }

    @UiThread
    public void onAttach(@NonNull Lifecycle lifecycle, @NonNull BaseActivity baseActivity) {

        this.assertUtility.throwIfNull(lifecycle);
        this.assertUtility.throwIfNull(baseActivity);
        this.lifecycleWeakReference = new WeakReference<>(lifecycle);
        this.baseActivityWeakReference = new WeakReference<>(baseActivity);
        this.isValid = true;
        lifecycle.addObserver(this);
    }

    @Override
    public void runInactivityContext(@NonNull Consumer<BaseActivity> consumer) {

        this.assertUtility.ifPresent(baseActivityWeakReference.get(), baseActivity -> {

            consumer.accept(baseActivity);
        });
    }

    @UiThread
    public void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {

        this.assertUtility.ifTrue(this.isValid, bool -> {

            Predicate<IPlugin> interestedPluginPredicate = p -> p.getPluginInterests().isInterestedInActivityResult()
                    && p.isRequestIdInteresting(requestCode);
            broadcastNotification(interestedPluginPredicate, plugin -> plugin.onActivityResult(requestCode, resultCode, data));
        });
    }

    @UiThread
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        this.assertUtility.ifTrue(this.isValid, bool -> {

            Predicate<IPlugin> interestedPluginPredicate = p -> p.getPluginInterests().isInterestedInPermissionResult()
                    && p.isRequestIdInteresting(requestCode);
            broadcastNotification(interestedPluginPredicate, plugin -> plugin.onRequestPermissionsResult(requestCode, permissions, grantResults));
        });
    }

    @UiThread
    public void onBackpressed() {

        this.assertUtility.ifTrue(this.isValid, bool -> {

            Predicate<IPlugin> interestedPluginPredicate = p -> p.getPluginInterests().isInterestedBackPressEvent();
            broadcastNotification(interestedPluginPredicate, plugin -> plugin.onBackPressed().accept(this));
        });
    }

    @UiThread
    private void broadcastNotification(@NonNull Predicate<IPlugin> pluginPredicate, Consumer<IPlugin> consumer) {

        this.pluginSafeMapper.iterateWithPredicate(pluginPredicate, entry -> {

            consumer.accept(entry.getValue());
        });
    }

    @UiThread
    public void onDestroy() {

        this.assertUtility.ifPresent(this.lifecycleWeakReference.get(), lifecycle -> lifecycle.removeObserver(this));
        this.isValid = false;
    }

    private void broadcastLifecycleEvent(@NonNull Consumer<IPlugin> consumer) {

        Predicate<IPlugin> pluginPredicate = p -> p.getPluginInterests().isInterestedInLifecycle();
        this.assertUtility.ifTrue(this.isValid, bool -> broadcastNotification(pluginPredicate, consumer));
    }

    public void onClear() {

        broadcastNotification(plugin -> true, plugin -> plugin.onClear());
        pluginManagerHandler.getHandler().getLooper().quitSafely();
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void create() {

        this.assertUtility.ifTrue(this.isValid, bool -> {

            broadcastLifecycleEvent(p -> p.onCreate());
        });
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void start() {

        this.assertUtility.ifTrue(this.isValid, bool -> {

            broadcastLifecycleEvent(p -> p.onStart());
        });
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void resume() {

        this.assertUtility.ifTrue(this.isValid, bool -> {

            broadcastLifecycleEvent(p -> p.onResume());
        });
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void pause() {

        this.assertUtility.ifTrue(this.isValid, bool -> {

            broadcastLifecycleEvent(p -> p.onPause());
        });
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void stop() {

        this.assertUtility.ifTrue(this.isValid, bool -> {

            broadcastLifecycleEvent(p -> p.onStop());
        });
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void destroy() {

        this.assertUtility.ifTrue(this.isValid, bool -> {

            broadcastLifecycleEvent(p -> p.onDestroy());
        });
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    public void any() {

        this.assertUtility.ifTrue(this.isValid, bool -> {

            broadcastLifecycleEvent(p -> p.onAny());
        });
    }


    @Override
    public void registerPlugin(@NonNull IPlugin plugin) {

        this.assertUtility.throwIfNull(plugin);
        this.pluginSafeMapper.getAndProceed(getPluginStorageKey(plugin.getClass()), p -> {

            throw new RuntimeException(String.format("plugin is already registered"));
        });

        Predicate<IPlugin> predicate = pluginInMap ->
                plugin.getStartRequestId() < plugin.getLastRequestId()
                && (pluginInMap.isRequestIdInteresting(plugin.getStartRequestId())
                        || pluginInMap.isRequestIdInteresting(plugin.getLastRequestId()));

        this.pluginSafeMapper.iterateWithPredicate(predicate, p ->{

            throw new RuntimeException(String.format("Plugin [%s] requestId ranges overlapped by [%s]", plugin.toString(), p.toString()));
        });

        this.pluginSafeMapper.addAndProceed(getPluginStorageKey(plugin.getClass()), plugin, null);
    }

    @Override
    public void requestFinish() {

        this.assertUtility.ifTrue(this.isValid, bool -> {

            this.executorHelper.getMainThreadHandler().removeCallbacks(requestFinish);
            this.executorHelper.getMainThreadHandler().post(requestFinish);
        });
    }

    @Override
    public void finishImmediately() {


        this.assertUtility.ifTrue(this.isValid, bool -> {

            this.assertUtility.ifPresent(this.baseActivityWeakReference.get(), baseActivity -> {

                baseActivity.finish();
            });
        });
    }

    @Override
    public <T extends IPlugin> void ifPresent(@NonNull Class<T> tClass, Consumer<T> consumer) {

        this.pluginSafeMapper.getAndProceed(getPluginStorageKey(tClass), plugin -> {

            this.assertUtility.safeCast(plugin, tClass, consumer);
        });
    }

    @Override
    public void requestPermissions(@NonNull String[] permissions, int requestCode) {

        this.assertUtility.ifPresent(baseActivityWeakReference.get(), baseActivity -> {

            this.assertUtility.ifTrue(this.isValid, bool -> {

                ActivityCompat.requestPermissions(baseActivity, permissions, requestCode);
            });
        });
    }

    @Override
    public void startActivity(@NonNull Intent intent, @Nullable Bundle options) {

        this.assertUtility.ifTrue(this.isValid, bool -> {

            this.assertUtility.ifPresent(this.baseActivityWeakReference.get(), baseActivity -> {

                ActivityCompat.startActivity(baseActivity, intent, options);
            });
        });
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {

        this.assertUtility.ifTrue(this.isValid, bool -> {

            this.assertUtility.ifPresent(this.baseActivityWeakReference.get(), baseActivity -> {

                ActivityCompat.startActivityForResult(baseActivity, intent, requestCode, options);
            });
        });
    }

    @Override
    public void pluginManagerHandler(@NonNull Consumer<PluginManagerHandler> consumer) {

        this.assertUtility.ifPresent(this.pluginManagerHandler, pmh -> {

            consumer.accept(pmh);
        });
    }

    @Override
    public boolean handleMessage(@NonNull final Message message) {

        this.assertUtility.ifPresent(message.getCallback(), runnable -> {

            this.assertUtility.safeCast(runnable, PluginHostRunnable.class, pluginHostRunnable -> {

                this.assertUtility.ifTrue(this.isValid, bool -> {

                    this.executorHelper.getMainThreadHandler().post(() -> {

                        pluginHostRunnable.run(this);
                    });
                });
            });
        });

        return true;
    }

    private <T> String getPluginStorageKey(@NonNull Class<T> tClass) {

        return tClass.getCanonicalName();
    }
}
