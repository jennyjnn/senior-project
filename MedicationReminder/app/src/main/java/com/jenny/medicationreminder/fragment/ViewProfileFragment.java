package com.jenny.medicationreminder.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jenny.medicationreminder.MenuActivity;
import com.jenny.medicationreminder.Model.User;
import com.jenny.medicationreminder.R;
import com.jenny.medicationreminder.ViewProfileActivity;


public class ViewProfileFragment extends Fragment {

    CardView cvBtnEditProfile, cvEditLogin;
    CardView cvAppBarViewProfile, cvUserInfo, cvLoginInfo;

    TextView tvName, tvAge, tvWeight, tvDisease, tvAllergic;
    TextView tvUsername, tvPhone;
    ImageView btnBack;

    SharedPreferences prefUser;
    private static final String USER_PREFS = "userStatus";
    String keyUser;

    FirebaseDatabase database;
    DatabaseReference userRef;

    ProgressDialog progressDialog;

    public ViewProfileFragment() {
        super();
    }

    public static ViewProfileFragment newInstance() {
        ViewProfileFragment fragment = new ViewProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_profile, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here

        // set color for app bar
        cvAppBarViewProfile = rootView.findViewById(R.id.cvAppBarViewProfile);
        cvAppBarViewProfile.setBackgroundResource(R.drawable.bg_appbar);

        tvName = rootView.findViewById(R.id.tvName);
        tvAge = rootView.findViewById(R.id.tvAge);
        tvWeight = rootView.findViewById(R.id.tvWeight);
        tvDisease = rootView.findViewById(R.id.tvDisease);
        tvAllergic = rootView.findViewById(R.id.tvAllergic);
        tvUsername = rootView.findViewById(R.id.tvUsername);
        tvPhone = rootView.findViewById(R.id.tvPhone);
        cvUserInfo = rootView.findViewById(R.id.cvUserInfo);
        cvLoginInfo = rootView.findViewById(R.id.cvLoginInfo);
        btnBack = rootView.findViewById(R.id.btnBack);
        cvEditLogin = rootView.findViewById(R.id.cvEditLogin);

        prefUser = getContext().getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        keyUser = prefUser.getString("keyUser", "no user");

        cvBtnEditProfile = rootView.findViewById(R.id.cvBtnEditProfile);
        cvBtnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = tvName.getText().toString();
                String age = tvAge.getText().toString();
                String weight = tvWeight.getText().toString();
                String allergic = tvAllergic.getText().toString();
                String disease = tvDisease.getText().toString();

                getFragmentManager().beginTransaction()
                        .replace(R.id.contentContainerProfile,
                                EditProfileFragment.newInstance(name, age, weight, allergic, disease))
                        .addToBackStack(null)
                        .commit();
            }
        });

        cvEditLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = tvUsername.getText().toString();
                String phone = tvPhone.getText().toString();

                getFragmentManager().beginTransaction()
                        .replace(R.id.contentContainerProfile,
                                EditLoginFragment.newInstance(username, phone))
                        .addToBackStack(null)
                        .commit();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        showProfile();
    }

    private void showProfile() {
        // Progress Dialog
        progressDialog = new ProgressDialog(getActivity());
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
                        tvPhone.setText("xxx-xxx" + user.getUser_phone().substring(6,10));
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

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    /*
     * Restore Instance State Here
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore Instance State here
        }
    }
}
