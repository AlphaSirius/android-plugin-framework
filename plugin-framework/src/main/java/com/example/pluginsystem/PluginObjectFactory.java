package com.example.pluginsystem;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;

import com.example.pluginsystem.plugins.PluginManager;
import com.example.pluginsystem.plugins.PluginManagerHandler;
import com.example.pluginsystem.plugins.PluginManagerHandlerThread;
import com.example.pluginsystem.plugins.core.AndroidPermissionPlugin;
import com.example.pluginsystem.plugins.core.Navigator;
import com.example.pluginsystem.utils.AssertUtility;
import com.example.pluginsystem.utils.ExecutorHelper;
import com.example.pluginsystem.utils.NonConfigurationInstances;
import com.example.pluginsystem.utils.SafeMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PluginObjectFactory {

    private static PluginObjectFactory pluginObjectFactory;
    private final PluginObjectFactoryCache pluginObjectFactoryCache;

    private PluginObjectFactory() {

        this.pluginObjectFactoryCache = new PluginObjectFactoryCache();
    }

    private void populateSingletons() {

        pluginObjectFactoryCache.add(new AssertUtility());
        pluginObjectFactoryCache.add(new ExecutorHelper(new Handler(Looper.getMainLooper())));
    }

    public static PluginObjectFactory getPluginObjectFactory() {

        if(pluginObjectFactory == null) {

            synchronized (PluginObjectFactory.class) {

                if(pluginObjectFactory == null) {

                    pluginObjectFactory = new PluginObjectFactory();
                    pluginObjectFactory.populateSingletons();
                }
            }
        }

        return pluginObjectFactory;
    }

    private class PluginObjectFactoryCache {

        private final Map<String, ISingletonObject> cache;

        private PluginObjectFactoryCache() {

            this.cache = new HashMap<>();
        }

        private <T extends ISingletonObject> void add(T object) {

            this.cache.put(object.getClass().getName(), object);
        }

        private <T extends ISingletonObject> void ifPresent(Class<T> tClass, Consumer<T> consumer) {

            T object = (T) this.cache.get(tClass.getName());
            if (object != null) {

                consumer.accept(object);
            }
        }

        private <T extends ISingletonObject> T ifPresent(Class<T> tClass) {

            return (T) this.cache.get(tClass.getName());
        }
    }

    public <T extends ISingletonObject> void proceedIfPresent(Class<T> tClass, Consumer<T> consumer) {

        this.pluginObjectFactoryCache.ifPresent(tClass, consumer);
    }

    @Nullable
    public <T extends ISingletonObject> T get(@NonNull Class<T> tClass) {

        return (T) this.pluginObjectFactoryCache.ifPresent(tClass);
    }

    public <K, T> SafeMapper<K, T> createSafeMapper() {

        return new SafeMapper<>(get(AssertUtility.class));
    }

    @NonNull
    public NonConfigurationInstances geNonConfigurationInstances() {

        return new NonConfigurationInstances(get(AssertUtility.class), createSafeMapper());
    }

    public PluginManagerHandlerThread getPluginManagerHandler(@NonNull String name, @NonNull Handler.Callback callback) {

        PluginManagerHandlerThread pluginManagerHandlerThread = new PluginManagerHandlerThread(name, callback);
        pluginManagerHandlerThread.start();
        return pluginManagerHandlerThread;
    }

    public String getUUID() {

        return UUID.randomUUID().toString();
    }

    public AndroidPermissionPlugin createAndroidPermissionPlugin(@NonNull PluginManagerHandler pluginManagerHandler) {

        return new AndroidPermissionPlugin(pluginManagerHandler, this);
    }

    public Navigator createNavigatorPlugin(@NonNull PluginManagerHandler pluginManagerHandler) {

        return new Navigator(pluginManagerHandler, this);
    }

    public PluginManager createPluginManager() {

        return new PluginManager(this);
    }
}
