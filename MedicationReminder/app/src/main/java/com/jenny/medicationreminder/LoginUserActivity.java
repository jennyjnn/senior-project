package com.jenny.medicationreminder;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jenny.medicationreminder.Model.User;

public class LoginUserActivity extends AppCompatActivity {

    EditText etLoginUser;
    EditText etLoginPass;

    String username, password;

    FirebaseDatabase database;
    DatabaseReference loginUserRef;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);
    }

    public void cancelLoginUser(View view) {
        finish();
    }

    public void goToRegister(View view) {
        Intent intent = new Intent(LoginUserActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    public void loginUser(View view) {
        etLoginUser = findViewById(R.id.etLoginUser);
        etLoginPass = findViewById(R.id.etLoginPass);

        // Validate form
        username = String.valueOf(etLoginUser.getText());
        password = String.valueOf(etLoginPass.getText());

        if (username.isEmpty()){
            etLoginUser.setError("กรุณากรอกชื่อผู้ใช้งาน !");
            etLoginUser.requestFocus();
            return;
        }
        if (password.isEmpty()){
            etLoginPass.setError("กรุณากรอกรหัสผ่าน !");
            etLoginPass.requestFocus();
            return;
        }

        database = FirebaseDatabase.getInstance();
        loginUserRef = database.getReference("User");

        // Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("ชื่อผู้ใช้งานหรือรหัสผ่านไม่ถูกต้อง");
        builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();

        // Progress Dialog
        progressDialog = new ProgressDialog(LoginUserActivity.this);
        progressDialog.setMessage("กรุณารอสักครู่");
        progressDialog.show();

        // Query database
        loginUserRef.orderByChild("username").equalTo(etLoginUser.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        User user = snapshot.getValue(User.class);
                        if (user.getPassword().equals(etLoginPass.getText().toString())){
                            Intent intent = new Intent(LoginUserActivity.this, MenuActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            dialog.show();
                        }
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
