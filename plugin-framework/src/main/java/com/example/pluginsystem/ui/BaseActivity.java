package com.example.pluginsystem.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;

import com.example.pluginsystem.PluginObjectFactory;
import com.example.pluginsystem.R;
import com.example.pluginsystem.plugins.IPluginManagerStore;
import com.example.pluginsystem.plugins.IPluginOwner;
import com.example.pluginsystem.plugins.PluginManager;
import com.example.pluginsystem.plugins.PluginManagerProvider;
import com.example.pluginsystem.plugins.core.AndroidPermissionPlugin;
import com.example.pluginsystem.utils.AssertUtility;


public class BaseActivity extends AppCompatActivity implements IPluginOwner, IPluginManagerStore {

    public static final String BASE_ACTIVITY_SIGNATURE_KEY = "32e67ae6-92f4-4a47-a377-019b35fc0581";
    private final AssertUtility assertUtility;
    private PluginManager pluginManager;
    private final PluginObjectFactory pluginObjectFactory;
    private final PluginManagerProvider pluginManagerProvider;
    private Bundle savedInstanceState;


    public BaseActivity() {

        this.pluginObjectFactory = PluginObjectFactory.getPluginObjectFactory();
        this.assertUtility = pluginObjectFactory.get(AssertUtility.class);
        this.pluginManagerProvider = pluginObjectFactory.get(PluginManagerProvider.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        this.pluginManagerProvider.getPluginManager(this, pm -> {

            this.pluginManager = pm;
            if (!pm.isInitialized()) {

                addPlugins(pm);
            }
            pm.onAttach(getLifecycle(), this);
        });
    }

    protected void addPlugins(@NonNull PluginManager pluginManager) {

        this.assertUtility.ifPresent(pluginManager, pm -> {

            this.assertUtility.ifPresent(this.pluginObjectFactory, pof -> {

                pm.pluginManagerHandler(pmh -> {

                    pm.registerPlugin(pof.createAndroidPermissionPlugin(pmh));
                    pm.registerPlugin(pof.createNavigatorPlugin(pmh));
                });
            });
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.pluginManager.onDestroy();
        if (this.pluginManager != null && !isChangingConfigurations()) {

            this.pluginManager.onClear();
        }
    }

    @Override
    public void getPluginManager(Consumer<PluginManager> consumer) {

        this.assertUtility.ifPresent(this.pluginManager, pm -> consumer.accept(pm));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.pluginManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        this.pluginManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        this.pluginManager.onBackpressed();
        super.onBackPressed();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {

        super.onSaveInstanceState(bundle);
        bundle.putString(BASE_ACTIVITY_SIGNATURE_KEY, this.pluginManagerProvider.storePluginManager(this.pluginManager));
    }

    @Override
    public void getLastSavedInstanceState(@NonNull Consumer<Bundle> consumer) {

        this.assertUtility.ifPresent(this.savedInstanceState, sis -> {

            consumer.accept(sis);
        });
    }

    @NonNull
    @Override
    public String getSignature() {

        return BASE_ACTIVITY_SIGNATURE_KEY;
    }
}