package com.example.pluginsystem.plugins;

import android.preference.PreferenceManager;

import androidx.core.app.ActivityCompat;
import androidx.core.util.Consumer;
import androidx.lifecycle.LifecycleObserver;

public interface IPlugin extends ActivityCompat.OnRequestPermissionsResultCallback, PreferenceManager.OnActivityResultListener, IBackPressListener {

    int getStartRequestId();

    int getLastRequestId();

    boolean isRequestIdInteresting(int requestId);

    void onClear();

    void onCreate();

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void onAny();

    PluginInterests getPluginInterests();
}
