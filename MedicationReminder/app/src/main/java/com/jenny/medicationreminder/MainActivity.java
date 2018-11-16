package com.jenny.medicationreminder;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.common.util.IOUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        //insertToMedicine();
        checkLoginStatus();
    }

    private void checkLoginStatus() {
        SharedPreferences prefUser = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        Boolean isLogin = prefUser.getBoolean("statusLogin", false);

        if (isLogin) {
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void insertToMedicine() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference medicineRef = database.getReference("Medicine");

        try {
            InputStream inputStream = getAssets().open("_medName.txt");
            int result = inputStream.available();
            byte[] bytes = new byte[result];
            inputStream.read(bytes);
            inputStream.close();
            String medName = new String(bytes);

            String[] name = medName.split("\n");
            for (int i = 0; i < name.length; i++) {
                InputStream medFile = getAssets().open(name[i] + ".txt");
                int rsMed = medFile.available();
                byte[] bytesMed = new byte[rsMed];
                medFile.read(bytesMed);
                medFile.close();
                String medInfo = new String(bytesMed);


                HashMap<String, String> hmMedInfo = new HashMap<String, String>();
                String topic = new String();
                boolean checkText = false;
                String[] medSplit = medInfo.split(" ///// ");
                for (int j = 0; j < medSplit.length; j++) {
                    if(j == 0){
                        medSplit[j] = medSplit[j].replaceAll("\r\n", "");
                        hmMedInfo.put("Med_name", medSplit[j]);
                    } else if (j == 2) {
                        if (medSplit[j].contains("\n \n")){
                            medSplit[j] = medSplit[j].substring(1, medSplit[j].length()-4);
                        } else {
                            medSplit[j] = medSplit[j].substring(1, medSplit[j].length()-2);
                        }
                        hmMedInfo.put("Med_intro", medSplit[j]);
                    }
                    else {
                        if (j%2 != 0){
                            if (checkText = medSplit[j].contains("ผลไม่พึงประสงค์")){
                                topic = "Med_sideEffect";
                            } else if (checkText = medSplit[j].contains("ข้อควรระวัง")) {
                                topic = "Med_warning";
                            } else if (checkText = medSplit[j].contains("เก็บรักษา")){
                                topic = "Med_preserve";
                            }
                        } else {
                            if (checkText) {
                                if (medSplit[j].contains("\n \n")){
                                    medSplit[j] = medSplit[j].substring(1, medSplit[j].length()-4);
                                } else {
                                    medSplit[j] = medSplit[j].substring(1, medSplit[j].length()-2);
                                }
                                hmMedInfo.put(topic, medSplit[j]);
                                checkText = false;
                            }
                        }
                    }
                }

                DecimalFormat idFormat = new DecimalFormat("0000");
                String med_id = "med" + idFormat.format(i+1);
                for (Map.Entry hm:hmMedInfo.entrySet()){
                    medicineRef.child(med_id).child((String) hm.getKey()).setValue(hm.getValue());
                }
                Log.e("insert medicine", med_id);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void register(View view) {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void showDialogLogin(View view) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_login);

        FancyButton btnUserLogin = dialog.findViewById(R.id.btnUserLogin);
        FancyButton btnTelLogin = dialog.findViewById(R.id.btnTelLogin);
        ImageView btnClose = dialog.findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginUserActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        btnTelLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginTelActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
