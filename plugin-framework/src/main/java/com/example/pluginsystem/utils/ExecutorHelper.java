package com.example.pluginsystem.utils;

import android.os.Handler;

import androidx.annotation.NonNull;

import com.example.pluginsystem.ISingletonObject;


public class ExecutorHelper implements ISingletonObject {

    private final Handler mainThreadHandler;
    public ExecutorHelper(@NonNull Handler mainThreadHandler) {

        this.mainThreadHandler = mainThreadHandler;
    }

    public Handler getMainThreadHandler(){

        return this.mainThreadHandler;
    }
}
