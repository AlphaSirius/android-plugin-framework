package com.projectm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.os.Handler;

import com.example.pluginsystem.ui.BaseActivity;
import com.example.pluginsystem.utils.LamdaExpDataHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends BaseActivity {

    private MainActivityViewModel viewModel;

    private List<String[]> ar = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPluginManager(pluginManager -> {

            this.viewModel.passControl(pluginManager);
        });
    }
}
