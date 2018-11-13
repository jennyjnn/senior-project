package com.jenny.medicationreminder;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
    String tel;

    FirebaseDatabase database;
    DatabaseReference loginTelRef;

    ProgressDialog progressDialog;

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

        // Validation form
        tel = String.valueOf(etLoginTel.getText());
         if (tel.isEmpty()){
             etLoginTel.setError("กรุณากรอกเบอร์โทรศัพท์ !");
             etLoginTel.requestFocus();
             return;
         }

        database = FirebaseDatabase.getInstance();
        loginTelRef = database.getReference("User");

        // Progress Dialog
        progressDialog = new ProgressDialog(LoginTelActivity.this);
        progressDialog.setMessage("กรุณารอสักครู่");
        progressDialog.show();

        // Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("หมายเลขโทรศัพท์ไม่ถูกต้อง");
        builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();

        // Query database
        Query query = loginTelRef.orderByChild("User_phone").equalTo(String.valueOf(etLoginTel.getText()));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
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
                    dialog.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
