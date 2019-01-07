package com.jenny.medicationreminder.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jenny.medicationreminder.R;

import java.util.Arrays;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;


public class EditProfileFragment extends Fragment {

    CardView cvAppBarProfile;

    EditText etName, etLastName, etAge, etWeight, etDisease, etAllergic;
    FancyButton btnUpdateProfile, btnCancel;
    ImageView btnBack;

    SharedPreferences prefUser;
    private static final String USER_PREFS = "userStatus";
    String keyUser;

    FirebaseDatabase database;
    DatabaseReference userRef;

    ProgressDialog progressDialog;

    String name, age, weight, allergic;
    String disease = null;

    public EditProfileFragment() {
        super();
    }

    public static EditProfileFragment newInstance(String name, String age, String weight, String allergic, String disease) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("age", age);
        args.putString("weight", weight);
        args.putString("allergic", allergic);
        args.putString("disease", disease);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Read from arguments
        name = getArguments().getString("name");
        age = getArguments().getString("age");
        weight = getArguments().getString("weight");
        allergic = getArguments().getString("allergic");
        disease = getArguments().getString("disease");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        // set color for app bar
        cvAppBarProfile = rootView.findViewById(R.id.cvAppBarProfile);
        cvAppBarProfile.setBackgroundResource(R.drawable.bg_appbar);

        etName = rootView.findViewById(R.id.etName);
        etLastName = rootView.findViewById(R.id.etLastName);
        etAge = rootView.findViewById(R.id.etAge);
        etWeight = rootView.findViewById(R.id.etWeight);
        etDisease = rootView.findViewById(R.id.etDisease);
        etAllergic = rootView.findViewById(R.id.etAllergic);
        btnUpdateProfile = rootView.findViewById(R.id.btnUpdateProfile);
        btnCancel = rootView.findViewById(R.id.btnCancel);
        btnBack = rootView.findViewById(R.id.btnBack);

        prefUser = getActivity().getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        keyUser = prefUser.getString("keyUser", "no user");

        // Show profile
        String[] nameSplit = name.split(" ");
        etName.setText(nameSplit[0]);
        etLastName.setText(nameSplit[1]);
        etAge.setText(age.substring(0, age.length() - 3));
        etWeight.setText(weight.substring(0, weight.length() - 9));
        etDisease.setText(disease);
        etAllergic.setText(allergic);

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Progress Dialog
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("กรุณารอสักครู่");
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
                        getFragmentManager().popBackStack();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        showDiseaseDialog(disease);
    }

    private void showDiseaseDialog(String disease) {
        final String[] diseaseArr = {"โรคเกาต์", "โรคเบาหวาน", "โรคไขมันในเลือดสูง", "โรคความดันโลหิตสูง", "โรคมะเร็งต่อมลูกหมาก",
                "โรคไต", "โรคหัวใจขาดเลือด", "โรคจอประสาทตาเสื่อม", "โรคความจำเสื่อม"};

        final boolean[] checkedDisease = new boolean[9];

        for (String di : disease.split(", ")) {
            switch (di) {
                case "โรคเกาต์":
                    checkedDisease[0] = true;
                    break;
                case "โรคเบาหวาน":
                    checkedDisease[1] = true;
                    break;
                case "โรคไขมันในเลือดสูง":
                    checkedDisease[2] = true;
                    break;
                case "โรคความดันโลหิตสูง":
                    checkedDisease[3] = true;
                    break;
                case "โรคมะเร็งต่อมลูกหมาก":
                    checkedDisease[4] = true;
                    break;
                case "โรคไต":
                    checkedDisease[5] = true;
                    break;
                case "โรคหัวใจขาดเลือด":
                    checkedDisease[6] = true;
                    break;
                case "โรคจอประสาทตาเสื่อม":
                    checkedDisease[7] = true;
                    break;
                case "โรคความจำเสื่อม":
                    checkedDisease[8] = true;
                    break;
                default:
                    break;
            }
        }

        etDisease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

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
                        for (int i = 0; i < checkedDisease.length; i++) {
                            boolean checked = checkedDisease[i];
                            if (checked) {
                                disease += diseaseList.get(i) + ", ";
                            }
                        }
                        if (disease.length() > 0) {
                            disease = disease.substring(0, disease.length() - 2);
                        }
                        etDisease.setText(disease);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
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
