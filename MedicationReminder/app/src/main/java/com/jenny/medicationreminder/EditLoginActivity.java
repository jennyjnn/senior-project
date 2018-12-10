package com.jenny.medicationreminder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.BoringLayout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jenny.medicationreminder.Model.User;

public class EditLoginActivity extends AppCompatActivity {

    CardView cvAppBarEditLogin;
    TextView etUsername, etOldPassword, etNewPassword, etOldPhone, etNewPhone;

    SharedPreferences prefUser;
    private static final String USER_PREFS = "userStatus";
    String keyUser;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef = database.getReference("User");

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_login);

        cvAppBarEditLogin = findViewById(R.id.cvAppBarEditLogin);
        cvAppBarEditLogin.setBackgroundResource(R.drawable.bg_appbar);

        etUsername = findViewById(R.id.etUsername);
        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etOldPhone = findViewById(R.id.etOldPhone);
        etNewPhone = findViewById(R.id.etNewPhone);

        prefUser = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        keyUser = prefUser.getString("keyUser", "no user");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            etUsername.setText(bundle.getString("username"));
        }

    }

    public void backBtn(View view) {
        finish();
    }

    public void checkLogin(View view) {
        // Progress Dialog
        progressDialog = new ProgressDialog(EditLoginActivity.this);
        progressDialog.setMessage("กรุณารอสักครู่");
        progressDialog.show();

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        if (etUsername.getText().toString().isEmpty()){
                            progressDialog.dismiss();
                            etUsername.setError("กรุณากรอกชื่อผู้ใช้ !");
                            etUsername.requestFocus();
                            return;
                        }
                        if (etUsername.getText().toString().equals(user.getUsername()) && !snapshot.getKey().equals(keyUser)){
                            progressDialog.dismiss();
                            etUsername.setError("ชื่อผู้ใช้นี้มีผู้ใช้แล้ว !");
                            etUsername.requestFocus();
                            return;
                        }
                        if (etNewPhone.getText().toString().equals(user.getUser_phone()) && !snapshot.getKey().equals(keyUser)){
                            progressDialog.dismiss();
                            etNewPhone.setError("เบอร์โทรศัพท์นี้มีผู้ใช้แล้ว!");
                            etNewPhone.requestFocus();
                            return;
                        }
                    }
                }
                saveLogin();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveLogin() {
        userRef.orderByKey().equalTo(keyUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        User user = snapshot.getValue(User.class);
                        if (!etNewPassword.getText().toString().equals("")) {
                            if (etOldPassword.getText().toString().isEmpty()) {
                                progressDialog.dismiss();
                                etOldPassword.setError("กรุณากรอกรหัสผ่านเดิมด้วย !");
                                etOldPassword.requestFocus();
                                return;
                            } else {
                                if (!user.getPassword().equals(etOldPassword.getText().toString())){
                                    progressDialog.dismiss();
                                    etOldPassword.setError("รหัสผ่านเดิมไม่ถูกต้อง !");
                                    etOldPassword.requestFocus();
                                    return;
                                }
                            }
                        }

                        if (!etNewPhone.getText().toString().isEmpty()) {
                            if (etOldPhone.getText() == null) {
                                progressDialog.dismiss();
                                etOldPhone.setError("กรุณากรอกเบอร์โทรศัพท์เดิมด้วย !");
                                etOldPhone.requestFocus();
                                return;
                            } else {
                                if (!etOldPhone.getText().toString().equals(user.getUser_phone())){
                                    progressDialog.dismiss();
                                    etOldPhone.setError("เบอร์โทรศัพท์เดิมไม่ถูกต้อง !");
                                    etOldPhone.requestFocus();
                                    return;
                                }
                            }
                        }
                        progressDialog.dismiss();
                    }
                    if (etNewPassword.getText().toString().isEmpty() && etNewPhone.getText().toString().isEmpty()) {
                        userRef.child(keyUser).child("username").setValue(etUsername.getText().toString());
                    } else if (etNewPassword.getText().toString().isEmpty() && !etNewPhone.getText().toString().isEmpty()) {
                        userRef.child(keyUser).child("username").setValue(etUsername.getText().toString());
                        userRef.child(keyUser).child("User_phone").setValue(etNewPhone.getText().toString());
                    } else if (!etNewPassword.getText().toString().isEmpty() && etNewPhone.getText().toString().isEmpty()) {
                        userRef.child(keyUser).child("username").setValue(etUsername.getText().toString());
                        userRef.child(keyUser).child("password").setValue(etNewPassword.getText().toString());
                    } else {
                        userRef.child(keyUser).child("username").setValue(etUsername.getText().toString());
                        userRef.child(keyUser).child("password").setValue(etNewPassword.getText().toString());
                        userRef.child(keyUser).child("User_phone").setValue(etNewPhone.getText().toString());
                    }
                }
                setResult(2);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void cancelEditProfile(View view) {
        finish();
    }
}
