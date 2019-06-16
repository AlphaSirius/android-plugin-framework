package com.example.pluginsystem.plugins;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;

import com.example.pluginsystem.ISingletonObject;
import com.example.pluginsystem.PluginObjectFactory;
import com.example.pluginsystem.utils.AssertUtility;
import com.example.pluginsystem.utils.LamdaExpDataHelper;
import com.example.pluginsystem.utils.SafeMapper;

import java.util.UUID;

public class PluginManagerProvider implements ISingletonObject {

    private final AssertUtility assertUtility;
    private SafeMapper<String, PluginManager> pluginsManagersMap;
    private final PluginManagerFactory pluginManagerFactory;

    public PluginManagerProvider(@NonNull PluginManagerFactory pluginManagerFactory, @NonNull PluginObjectFactory pluginObjectFactory) {

        this.assertUtility = pluginObjectFactory.get(AssertUtility.class);
        this.pluginsManagersMap = pluginObjectFactory.createSafeMapper();
        this.pluginManagerFactory = pluginManagerFactory;
    }

    public String storePluginManager(@NonNull PluginManager pluginManager) {

        String uuid = UUID.randomUUID().toString();
        this.assertUtility.ifPresent(pluginManager, pm->{

            this.pluginsManagersMap.addAndProceed(uuid, pluginManager, null);
        });

        return uuid;
    }

    private void removeAndResume(@NonNull String key, @NonNull Consumer<PluginManager> consumer) {

        this.assertUtility.ifPresent(key, k->{

            this.pluginsManagersMap.remove(k, consumer);
        } );

    }

    public void getPluginManager(@NonNull IPluginManagerStore pluginManagerStore, @NonNull Consumer<PluginManager> consumer) {

        this.assertUtility.throwIfNull(pluginManagerStore);
        this.assertUtility.throwIfNull(consumer);
        final LamdaExpDataHelper<Boolean> requestHandled = new LamdaExpDataHelper<>(false);

        pluginManagerStore.getLastSavedInstanceState(bundle ->{

            removeAndResume(bundle.getString(pluginManagerStore.getSignature()), pm -> {

                requestHandled.set(true);
                consumer.accept(pm);
            });
        });

        this.assertUtility.ifFalse(requestHandled.get(), bool ->{

            consumer.accept(this.pluginManagerFactory.createPluginManager());
        });
    }

}
