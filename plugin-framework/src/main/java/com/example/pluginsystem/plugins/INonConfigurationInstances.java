package com.example.pluginsystem.plugins;

import androidx.core.util.Consumer;

import com.example.pluginsystem.utils.NonConfigurationInstances;


@FunctionalInterface
public interface INonConfigurationInstances {

    void getNonConfigurationInstance(Consumer<NonConfigurationInstances> consumer);
}
