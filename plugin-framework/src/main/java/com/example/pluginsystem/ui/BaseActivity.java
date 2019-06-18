package com.example.pluginsystem.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;
import androidx.lifecycle.ViewModelProviders;

import com.example.pluginsystem.PluginObjectFactory;
import com.example.pluginsystem.plugins.IPluginOwner;
import com.example.pluginsystem.plugins.PluginManager;
import com.example.pluginsystem.utils.AssertUtility;


public class BaseActivity extends AppCompatActivity implements IPluginOwner {

    private final AssertUtility assertUtility;
    private PluginManager pluginManager;
    private final PluginObjectFactory pluginObjectFactory;


    public BaseActivity() {

        this.pluginObjectFactory = PluginObjectFactory.getPluginObjectFactory();
        this.assertUtility = pluginObjectFactory.get(AssertUtility.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseViewModel baseViewModel = ViewModelProviders.of(this).get(BaseViewModel.class);
        this.pluginManager = baseViewModel.getPluginManager(this.pluginObjectFactory);
        if (!this.pluginManager.isInitialized()) {

            addPlugins(this.pluginManager);
        }
        this.pluginManager.onAttach(getLifecycle(), this);
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
}