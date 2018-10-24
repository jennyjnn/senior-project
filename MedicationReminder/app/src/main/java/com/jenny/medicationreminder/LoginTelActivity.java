package com.jenny.medicationreminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginTelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_tel);
    }

    public void cancelLoginTel(View view) {
        finish();
    }

    public void goToRegister(View view) {
        Intent intent = new Intent(LoginTelActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}
