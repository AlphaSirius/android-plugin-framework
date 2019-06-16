package com.example.pluginsystem.utils;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;

import com.example.pluginsystem.utils.AssertUtility;
import com.example.pluginsystem.utils.SafeMapper;

public class NonConfigurationInstances {

    private final SafeMapper<String, Object> safeMapper;
    private final AssertUtility assertUtility;

    public NonConfigurationInstances(@NonNull AssertUtility assertUtility, @NonNull SafeMapper<String, Object> safeMapper) {

        this.assertUtility = assertUtility;
        this.safeMapper = safeMapper;
    }

    public <T> void save(@NonNull T object) {

        this.assertUtility.throwIfNull(object);
        this.safeMapper.addAndProceed(object.getClass().getName(), object, null);
    }

    public <T> void removeAndProceed(@NonNull Class<T> tClass, @NonNull Consumer<T> consumer) {

        this.assertUtility.throwIfNull(tClass);
        this.assertUtility.throwIfNull(consumer);
        this.safeMapper.remove(tClass.getName(), c -> consumer.accept((T) c));
    }
}
