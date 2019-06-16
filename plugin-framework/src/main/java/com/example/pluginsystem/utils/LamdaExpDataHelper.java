package com.example.pluginsystem.utils;

import androidx.annotation.NonNull;

public class LamdaExpDataHelper<T> {

    private T value;

    public LamdaExpDataHelper(@NonNull T object) {

        this.value = object;
    }

    public T get(){

        return this.value;
    }

    public void set(T value){

        this.value = value;
    }
}
