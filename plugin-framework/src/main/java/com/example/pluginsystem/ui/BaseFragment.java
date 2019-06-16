package com.example.pluginsystem.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.util.Consumer;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pluginsystem.PluginObjectFactory;
import com.example.pluginsystem.R;
import com.example.pluginsystem.plugins.IPluginOwner;
import com.example.pluginsystem.plugins.PluginManager;
import com.example.pluginsystem.utils.AssertUtility;


public class BaseFragment extends Fragment implements IPluginOwner {

    private final AssertUtility assertUtility = PluginObjectFactory.getPluginObjectFactory().get(AssertUtility.class);
    private PluginManager pluginManager;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.assertUtility.safeCast(context, IPluginOwner.class, pluginOwner -> {

            pluginOwner.getPluginManager(pm -> {

                this.pluginManager = pm;
            });
        });
    }

    @Override
    public void getPluginManager(Consumer<PluginManager> consumer) {

        this.assertUtility.ifPresent(this.pluginManager, pm -> consumer.accept(pm));
        getPluginManager(pm -> {

            pm.requestFinish();
        });
    }
}
