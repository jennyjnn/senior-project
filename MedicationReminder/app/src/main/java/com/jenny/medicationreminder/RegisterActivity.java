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

        regisRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = String.valueOf(etName.getText());
                String surname = String.valueOf(etSurname.getText());
                String age = String.valueOf(etAge.getText());
                String weight = String.valueOf(etWeight.getText());
                String disease = String.valueOf(etDisease.getText());
                String allergy = String.valueOf(etAllergic.getText());
                String username = String.valueOf(etUsername.getText());
                String password = String.valueOf(etPassword.getText());
                String tel = String.valueOf(etTel.getText());

                countUser = dataSnapshot.getChildrenCount();
                DecimalFormat idFormat = new DecimalFormat("0000");
                user_id = "user" + idFormat.format(countUser+1);
                Log.e("countUser", String.valueOf(countUser));
                Log.e("userid", user_id);
                regisRef.child(user_id).child("User_fname").setValue(name);
                regisRef.child(user_id).child("User_lname").setValue(surname);
                regisRef.child(user_id).child("User_age").setValue(age);
                regisRef.child(user_id).child("User_weight").setValue(weight);
                regisRef.child(user_id).child("User_disease").setValue(disease);
                regisRef.child(user_id).child("User_allergy").setValue(allergy);
                regisRef.child(user_id).child("username").setValue(username);
                regisRef.child(user_id).child("password").setValue(password);
                regisRef.child(user_id).child("User_phone").setValue(tel);

                //Toast
                Context context = getApplicationContext();
                Toast.makeText(context, "Register User success !", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error", databaseError.getMessage());
            }
        });
    }

    public void cancelRegister(View view) {
        finish();
    }

}
