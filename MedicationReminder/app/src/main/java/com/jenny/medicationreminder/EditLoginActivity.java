package com.jenny.medicationreminder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class EditLoginActivity extends AppCompatActivity {

    CardView cvAppBarEditLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_login);

        cvAppBarEditLogin = findViewById(R.id.cvAppBarEditLogin);
        cvAppBarEditLogin.setBackgroundResource(R.drawable.bg_appbar);

    }

    public void backBtn(View view) {
        finish();
    }
}
