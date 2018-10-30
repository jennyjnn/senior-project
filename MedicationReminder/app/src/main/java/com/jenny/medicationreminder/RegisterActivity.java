package com.jenny.medicationreminder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
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
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

public class RegisterActivity extends AppCompatActivity {

    TextView etDisease;
    EditText etName;
    EditText etSurname;
    EditText etAge;
    EditText etWeight;
    EditText etAllergic;
    EditText etUsername;
    EditText etPassword;
    EditText etTel;
    long countUser = 0;
    String user_id;

    FirebaseDatabase database;
    DatabaseReference regisRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initInstances();
    }

    private void initInstances() {
        etDisease = findViewById(R.id.etDisease);

        final String [] diseaseArr = {"โรคเกาต์", "โรคเบาหวาน", "โรคไขมันในเลือดสูง", "โรคความดันโลหิตสูง", "โรคมะเร็งต่อมลูกหมาก", "โรคไต", "โรคหัวใจขาดเลือด", "โรคจอประสาทตาเสื่อม", "โรคอัลไซเมอร์"};

        final boolean[] checkedDisease = new boolean[9];
        etDisease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);

                final List<String> diseaseList = Arrays.asList(diseaseArr);

                builder.setTitle("โรคประจำตัว");

                builder.setMultiChoiceItems(diseaseArr, checkedDisease, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checkedDisease[which] = isChecked;
                        String currentItem = diseaseList.get(which);
                    }
                });

                builder.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String disease = "";
                        for (int i=0; i<checkedDisease.length; i++){
                            boolean checked = checkedDisease[i];
                            if (checked){
                                disease += diseaseList.get(i) + ", ";
                            }
                        }
                        if (disease.length() > 0){
                            disease = disease.substring(0, disease.length()-2);
                        }
                        etDisease.setText(disease);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void registerUser(View view) {
        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        etAge = findViewById(R.id.etAge);
        etWeight = findViewById(R.id.etWeight);
        etDisease = findViewById(R.id.etDisease);
        etAllergic = findViewById(R.id.etAllergic);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etTel = findViewById(R.id.etTel);

        database = FirebaseDatabase.getInstance();
        regisRef = database.getReference("User");

        // Validation form
        final String name = String.valueOf(etName.getText());
        final String surname = String.valueOf(etSurname.getText());
        final String age = String.valueOf(etAge.getText());
        final String weight = String.valueOf(etWeight.getText());
        final String disease = String.valueOf(etDisease.getText());
        final String allergy = String.valueOf(etAllergic.getText());
        final String username = String.valueOf(etUsername.getText());
        final String password = String.valueOf(etPassword.getText());
        final String tel = String.valueOf(etTel.getText());

        if (name.isEmpty()) {
            etName.setError("กรุณากรอกชื่อ !");
            etName.requestFocus();
            return;
        }
        if (surname.isEmpty()) {
            etSurname.setError("กรุณากรอกนามสกุล !");
            etSurname.requestFocus();
            return;
        }
        if (age.isEmpty()) {
            etAge.setError("กรุณากรอกอายุ !");
            etAge.requestFocus();
            return;
        }
        if (weight.isEmpty()) {
            etWeight.setError("กรุณากรอกน้ำหนัก !");
            etWeight.requestFocus();
            return;
        }
        if (username.isEmpty()) {
            etUsername.setError("กรุณากรอกชื่อผู้ใช้ !");
            etUsername.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            etPassword.setError("กรุณากรอกรหัสผ่าน !");
            etPassword.requestFocus();
            return;
        } else if (password.length() < 4){
            etPassword.setError("รหัสผ่านต้องมีอย่างน้อย 4 ตัวอักษร !");
            etPassword.requestFocus();
            return;
        }
        if (tel.isEmpty()) {
            etTel.setError("กรุณากรอกเบอร์โทรศัพท์ !");
            etTel.requestFocus();
            return;
        } else if (tel.length() != 10 || !tel.substring(0,1).equals("0")){
            etTel.setError("กรุณากรอกเบอร์โทรศัพท์ให้ถูกต้อง !");
            etTel.requestFocus();
            return;
        }

        regisRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    etUsername.setError("ชื่อผู้ใช้นี้มีผู้ใช้แล้ว !");
                    etUsername.requestFocus();
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        regisRef.orderByChild("User_phone").equalTo(tel).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    etTel.setError("หมายเลขโทรศัพท์นี้มีผู้ใช้แล้ว !");
                    etTel.requestFocus();
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        // Add information to firebase database

//
//        regisRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                countUser = dataSnapshot.getChildrenCount();
//                DecimalFormat idFormat = new DecimalFormat("0000");
//                user_id = "user" + idFormat.format(countUser+1);
//                Log.e("countUser", String.valueOf(countUser));
//                Log.e("userid", user_id);
//                regisRef.child(user_id).child("User_fname").setValue(name);
//                regisRef.child(user_id).child("User_lname").setValue(surname);
//                regisRef.child(user_id).child("User_age").setValue(age);
//                regisRef.child(user_id).child("User_weight").setValue(weight);
//                regisRef.child(user_id).child("User_disease").setValue(disease);
//                regisRef.child(user_id).child("User_allergy").setValue(allergy);
//                regisRef.child(user_id).child("username").setValue(username);
//                regisRef.child(user_id).child("password").setValue(password);
//                regisRef.child(user_id).child("User_phone").setValue(tel);
//
//                Intent intent = new Intent(RegisterActivity.this, MenuActivity.class);
//                startActivity(intent);
//                finish();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e("Error", databaseError.getMessage());
//            }
//        });
    }

    public void cancelRegister(View view) {
        finish();
    }

}
