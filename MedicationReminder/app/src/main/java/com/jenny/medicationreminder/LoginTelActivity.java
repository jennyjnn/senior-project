package com.jenny.medicationreminder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginTelActivity extends AppCompatActivity {

    EditText etLoginTel;

    FirebaseDatabase database;
    DatabaseReference loginTelRef;

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

    public void loginTel(View view) {
        etLoginTel = findViewById(R.id.etLoginTel);

        database = FirebaseDatabase.getInstance();
        loginTelRef = database.getReference("User");

//        loginTelRef.orderByChild("User_phone").equalTo(String.valueOf(etLoginTel)).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String data = dataSnapshot.getValue(String.class);
//                if (count == 0) {
//                    Toast.makeText(LoginTelActivity.this, "Error: incorrect phone number.", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(LoginTelActivity.this, "Login success", Toast.LENGTH_LONG).show();
//                }
//                Log.e("data", data);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

    }
}
