package com.jenny.medicationreminder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    CardView cvAppBar;
    SharedPreferences prefUser;
    SharedPreferences.Editor editor;
    private static final String USER_PREFS = "userStatus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initInstances();

    }

    private void initInstances() {
        cvAppBar = findViewById(R.id.cvAppBar);
        cvAppBar.setBackgroundResource(R.drawable.bg_appbar);
    }

    public void logOut(View view) {
        AlertDialog.Builder alertLogout = new AlertDialog.Builder(this);
        alertLogout.setMessage("คุณต้องการออกจากระบบใช่หรือไม่ ?");
        alertLogout.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Clear user's key and status login in SharedPreferences
                prefUser = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
                editor = prefUser.edit();
                editor.clear();
                editor.commit();

                // Back to Main Activity
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        alertLogout.setNeutralButton("ไม่ใช่", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = alertLogout.create();
        alertDialog.show();

    }

    public void addMed(View view) {
        Intent intent = new Intent(MenuActivity.this, EditMedActivity.class);
        startActivity(intent);
    }

    public void showListMed(View view) {
        Intent intent = new Intent(MenuActivity.this, ListMedActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToProfile(View view) {
        Intent intent = new Intent(MenuActivity.this, ViewProfileActivity.class);
        startActivity(intent);
    }
}
