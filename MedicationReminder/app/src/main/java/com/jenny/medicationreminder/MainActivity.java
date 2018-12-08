package com.jenny.medicationreminder;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.common.util.IOUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jenny.medicationreminder.fragment.MainFragment;
import com.jenny.medicationreminder.fragment.MenuFragment;
import com.jenny.medicationreminder.fragment.RegisterFragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mehdi.sakout.fancybuttons.FancyButton;

public class MainActivity extends AppCompatActivity {

    private static final String USER_PREFS = "userStatus";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            // First Created
            // Place Fragment here
            checkLoginStatus();
        }
    }

    private void checkLoginStatus() {
        SharedPreferences prefUser = MainActivity.this.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        Boolean isLogin = prefUser.getBoolean("statusLogin", false);

        if (isLogin) {
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, new MainFragment())
                    .commit();
        }
    }
}
