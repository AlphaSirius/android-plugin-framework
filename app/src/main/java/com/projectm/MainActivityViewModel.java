package com.projectm;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModel;

import com.example.pluginsystem.PluginObjectFactory;
import com.example.pluginsystem.plugins.IPluginHost;
import com.example.pluginsystem.plugins.core.AndroidPermissionPlugin;
import com.example.pluginsystem.plugins.core.Navigator;

public class MainActivityViewModel extends ViewModel {

    void passControl(@NonNull IPluginHost pluginHost) {

       pluginHost.ifPresent(Navigator.class, navigator -> {

          pluginHost.runInactivityContext(baseActivity -> {

              navigator.startActivity(new Intent(baseActivity, MainActivity.class), null);
          });
       });
    }
}
