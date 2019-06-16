package com.example.pluginsystem.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;
import androidx.core.util.Preconditions;

import java.util.Map;
import java.util.Set;

class Mapper<K, T> {

    private final Map<K, T> map;
    private final AssertUtility assertUtility;

    protected Mapper(@NonNull Map<K, T> map, @NonNull AssertUtility assertUtility) {

        this.assertUtility = assertUtility;
        this.map = map;
    }

    @NonNull
    protected AssertUtility getAssertUtility() {

        return this.assertUtility;
    }

    public final void addAndProceed(@NonNull K key, @NonNull T object, @Nullable Consumer<K> consumer) {

        this.assertUtility.throwIfNull(key);
        this.assertUtility.throwIfNull(object);
        this.map.put(key, object);
        this.assertUtility.ifPresent(consumer, c -> c.accept(key));
    }

    public final void remove(@NonNull K key, @Nullable Consumer<T> consumer) {

        this.assertUtility.throwIfNull(key);
        this.assertUtility.ifPresent(consumer,
                c -> this.assertUtility.ifPresent(this.map.remove(key),
                        object -> consumer.accept(object)));
    }

    public final void getAndProceed(@NonNull K key, @NonNull Consumer<T> consumer) {

        this.assertUtility.throwIfNull(key);
        this.assertUtility.throwIfNull(consumer);
        this.assertUtility.ifPresent(this.map.get(key),
                object -> consumer.accept(object));
    }

    public final void getEntrySetAndIterate(@NonNull Consumer<Set<Map.Entry<K, T>>> consumer) {

        this.assertUtility.throwIfNull(consumer);
        consumer.accept(this.map.entrySet());
    }

    public final void iterate(@NonNull Consumer<Map.Entry<K, T>> consumer) {

        getEntrySetAndIterate(entries -> {

            for (Map.Entry<K, T> entry : entries) {

                consumer.accept(entry);
            }
        });
    }

    public final void iterateWithPredicate(@NonNull Predicate<T> predicate, @NonNull Consumer<Map.Entry<K, T>> consumer) {

        this.assertUtility.throwIfNull(predicate);
        getEntrySetAndIterate(entries -> {

            for (Map.Entry<K, T> entry : entries) {
                if (predicate.test(entry.getValue())) {

                    consumer.accept(entry);
                }
            }
        });
    }

    public int size() {

        return this.map.size();
    }
}

