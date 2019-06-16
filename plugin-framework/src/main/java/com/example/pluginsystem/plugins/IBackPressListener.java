package com.example.pluginsystem.plugins;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;

import java.lang.ref.WeakReference;

@FunctionalInterface
public interface IBackPressListener {

    Consumer<IPluginHost> onBackPressed();
}
