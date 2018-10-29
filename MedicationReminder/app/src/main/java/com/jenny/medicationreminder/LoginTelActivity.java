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
import com.jenny.medicationreminder.Model.User;

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

        Query query = loginTelRef.orderByChild("User_phone").equalTo(String.valueOf(etLoginTel.getText()));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        Log.e("phone", user.getUser_phone());
                        Log.e("fname", user.getUser_fname());
                        Log.e("key", String.valueOf(snapshot.getKey()));

                        Intent intent = new Intent(LoginTelActivity.this, MenuActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(LoginTelActivity.this, "ไม่มีหมายเลขโทรศัพท์นี้", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        
    }
}
