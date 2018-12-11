package com.jenny.medicationreminder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jenny.medicationreminder.Model.User;

public class ViewProfileActivity extends AppCompatActivity {
    CardView cvAppBarViewProfile, cvUserInfo, cvLoginInfo;

    TextView tvName, tvAge, tvWeight, tvDisease, tvAllergic;
    TextView tvUsername, tvPhone;

    SharedPreferences prefUser;
    private static final String USER_PREFS = "userStatus";
    String keyUser;

    FirebaseDatabase database;
    DatabaseReference userRef;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        initInstances();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            finish();
            startActivity(getIntent());
        }
    }


    private void initInstances() {
        // set color for app bar
        cvAppBarViewProfile = findViewById(R.id.cvAppBarViewProfile);
        cvAppBarViewProfile.setBackgroundResource(R.drawable.bg_appbar);

        tvName = findViewById(R.id.tvName);
        tvAge = findViewById(R.id.tvAge);
        tvWeight = findViewById(R.id.tvWeight);
        tvDisease = findViewById(R.id.tvDisease);
        tvAllergic = findViewById(R.id.tvAllergic);
        tvUsername = findViewById(R.id.tvUsername);
        tvPhone = findViewById(R.id.tvPhone);
        cvUserInfo = findViewById(R.id.cvUserInfo);
        cvLoginInfo = findViewById(R.id.cvLoginInfo);

        prefUser = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        keyUser = prefUser.getString("keyUser", "no user");

        showProfile();
    }

    private void showProfile() {
        // Progress Dialog
        progressDialog = new ProgressDialog(ViewProfileActivity.this);
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
                        tvName.setText(user.getUser_fname() + " " + user.getUser_lname());
                        tvAge.setText(user.getUser_age() + " ปี");
                        tvWeight.setText(user.getUser_weight() + " กิโลกรัม");
                        if (user.getUser_disease().isEmpty()){
                            tvDisease.setText("-");
                        } else {
                            tvDisease.setText(user.getUser_disease());
                        }
                        if (user.getUser_allergy().isEmpty()){
                            tvAllergic.setText("-");
                        } else {
                            tvAllergic.setText(user.getUser_allergy());
                        }
                        tvUsername.setText(user.getUsername());
                        tvPhone.setText("xxx-xxx" + user.getUser_phone().substring(5,9));
                        cvUserInfo.setVisibility(View.VISIBLE);
                        cvLoginInfo.setVisibility(View.VISIBLE);
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

    public void editProfile(View view) {
        Intent intent = new Intent(ViewProfileActivity.this, ProfileActivity.class);
        intent.putExtra("name", tvName.getText());
        intent.putExtra("age", tvAge.getText());
        intent.putExtra("weight", tvWeight.getText());
        intent.putExtra("allergic", tvAllergic.getText());
        intent.putExtra("disease", tvDisease.getText());
//        startActivity(intent);
        startActivityForResult(intent, 2);
    }

    public void editLogin(View view) {
        Intent intent = new Intent(ViewProfileActivity.this, EditLoginActivity.class);
        intent.putExtra("username", tvUsername.getText());
        intent.putExtra("phone", tvPhone.getText());
//        startActivity(intent);
        startActivityForResult(intent, 2);

    }
}
