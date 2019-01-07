package com.jenny.medicationreminder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jenny.medicationreminder.fragment.MenuFragment;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainerMenu, new MenuFragment())
                    .commit();
        }
    }
}
