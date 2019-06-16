package com.example.pluginsystem.utils;

import androidx.annotation.Nullable;

@FunctionalInterface
public interface Predicate<T> {

    boolean test(@Nullable T object);
}
