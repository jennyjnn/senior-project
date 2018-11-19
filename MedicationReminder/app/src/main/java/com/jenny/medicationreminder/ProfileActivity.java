package com.jenny.medicationreminder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jenny.medicationreminder.Model.User;

import java.util.Arrays;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    CardView cvAppBarProfile;

    EditText etName, etLastName, etAge, etWeight, etDisease, etAllergic;

    SharedPreferences prefUser;
    private static final String USER_PREFS = "userStatus";
    String keyUser;

    FirebaseDatabase database;
    DatabaseReference userRef;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initInstances();
    }

    private void initInstances() {
        // set color for app bar
        cvAppBarProfile = findViewById(R.id.cvAppBarProfile);
        cvAppBarProfile.setBackgroundResource(R.drawable.bg_appbar);

        etName = findViewById(R.id.etName);
        etLastName = findViewById(R.id.etLastName);
        etAge = findViewById(R.id.etAge);
        etWeight = findViewById(R.id.etWeight);
        etDisease = findViewById(R.id.etDisease);
        etAllergic = findViewById(R.id.etAllergic);

        prefUser = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        keyUser = prefUser.getString("keyUser", "no user");

        getProfile();
        showDiseaseDialog();
    }

    private void showDiseaseDialog() {
        final String [] diseaseArr = {"โรคเกาต์", "โรคเบาหวาน", "โรคไขมันในเลือดสูง", "โรคความดันโลหิตสูง", "โรคมะเร็งต่อมลูกหมาก", "โรคไต", "โรคหัวใจขาดเลือด", "โรคจอประสาทตาเสื่อม", "โรคอัลไซเมอร์"};

        final boolean[] checkedDisease = new boolean[9];
        etDisease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);

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

    private void getProfile() {
        // Progress Dialog
        progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setMessage("กรุณารอสักครู่");
        progressDialog.show();

        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("User");

        userRef.orderByKey().equalTo(keyUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        progressDialog.dismiss();
                        User user = snapshot.getValue(User.class);
                        etName.setText(user.getUser_fname());
                        etLastName.setText(user.getUser_lname());
                        etAge.setText(user.getUser_age());
                        etWeight.setText(user.getUser_weight());
                        if (user.getUser_disease().isEmpty()){
                            etDisease.setText("-");
                        } else {
                            etDisease.setText(user.getUser_disease());
                        }
                        if (user.getUser_allergy().isEmpty()){
                            etAllergic.setText("-");
                        } else {
                            etAllergic.setText(user.getUser_allergy());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void backBtn(View view) {
        finish();
    }

    public void cancelEditProfile(View view) {
        finish();
    }

    public void editProfile(View view) {

        progressDialog.show();

        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("User");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userRef.child(keyUser).child("User_fname").setValue(etName.getText().toString());
                userRef.child(keyUser).child("User_lname").setValue(etLastName.getText().toString());
                userRef.child(keyUser).child("User_age").setValue(etAge.getText().toString());
                userRef.child(keyUser).child("User_weight").setValue(etWeight.getText().toString());
                userRef.child(keyUser).child("User_disease").setValue(etDisease.getText().toString());
                userRef.child(keyUser).child("User_allergy").setValue(etAllergic.getText().toString());

                progressDialog.dismiss();
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
