package com.example.pluginsystem.utils;

import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.core.util.Consumer;

import com.example.pluginsystem.ISingletonObject;

import java.lang.reflect.Type;

public class AssertUtility implements ISingletonObject {

    public <T> void ifPresent(@Nullable T object, Consumer<T> consumer) {

        if (object != null) {

            consumer.accept(object);
        }
    }

    public <T> void ifNotPresent(@Nullable T object, Runnable runnable) {

        if (object == null) {

            runnable.run();
        }
    }

    public void ifTrue(@Nullable Boolean var, Consumer<Boolean> consumer) {

        if (var != null && var) {

            consumer.accept(true);
        }
    }

    public void ifFalse(@Nullable Boolean var, Consumer<Boolean> consumer) {

        if (var != null && !var) {

            consumer.accept(false);
        }
    }

    public <T extends String> void ifNotPresentOrEmpty(@Nullable T object, Runnable runnable) {

        if (object == null || object.isEmpty()) {

            runnable.run();
        }
    }

    public <T,U> void safeCast(T object, Class<U> uClass, Consumer<U> consumer) {

        ifTrue(object.getClass().isAssignableFrom(uClass), bool -> consumer.accept((U) object));
    }

    public <T,U> void tryCast(T object, Consumer<T> consumer) {

        try {

            consumer.accept(object);
        } catch (Exception e) {

        }
    }

    public <T> void throwIfNull(@Nullable T object) {

        ifNotPresent(object, () -> {
            throw new RuntimeException();
        });
    }

    public <T extends String> void throwIfNullOrEmpty(@Nullable T object) {

        ifNotPresentOrEmpty(object, () -> {
            throw new RuntimeException();
        });
    }

    public void throwIfRunningOnMainThread() {

        ifTrue(Looper.getMainLooper().equals(Looper.getMainLooper()), bool-> {

            throw new RuntimeException(String.format("Function must not be called from main thread"));
        });
    }
}
