package com.example.aplicacio1;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class Preferencies extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content,new PreferenciesFragment()).commit();
    }
}
