package com.jenny.medicationreminder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.jenny.medicationreminder.fragment.MainFragment;


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
