package com.example.pluginsystem.utils;

import androidx.annotation.NonNull;

import java.util.concurrent.ConcurrentHashMap;

public class SafeMapper<K,T> extends Mapper<K,T>  {

    public SafeMapper(@NonNull AssertUtility assertUtility) {

        super(new ConcurrentHashMap<K, T>(), assertUtility);
    }
}
