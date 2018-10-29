package com.jenny.medicationreminder;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    CardView cvAppBar;

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
}
