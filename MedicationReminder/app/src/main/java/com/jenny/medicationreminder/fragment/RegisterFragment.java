package com.jenny.medicationreminder.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jenny.medicationreminder.MenuActivity;
import com.jenny.medicationreminder.Model.User;
import com.jenny.medicationreminder.R;
import com.libRG.CustomTextView;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;


public class RegisterFragment extends Fragment {

    FancyButton btnCancelRegister, btnRegister;
    CustomTextView tvToLogin;
    EditText etDisease, etUsername, etPassword, etTel;
    EditText etName, etSurname, etAge, etWeight, etAllergic;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference regisRef = database.getReference("User");

    ProgressDialog progressDialog;

    long countUser = 0;
    String user_id;

    String name;
    String surname;
    String age;
    String weight;
    String disease;
    String allergy;
    String username;
    String password;
    String tel;

    SharedPreferences prefUser, prefNotiTime;
    SharedPreferences.Editor editor, editorNotiTime;
    private static final String USER_PREFS = "userStatus";
    private static final String NOTI_PREFS = "notiTime";

    public RegisterFragment() {
        super();
    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        // Cancel for Register
        btnCancelRegister = rootView.findViewById(R.id.btnCancelRegister);
        btnCancelRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        // Set Disease
        etDisease = rootView.findViewById(R.id.etDisease);
        final String [] diseaseArr = {"โรคเกาต์", "โรคเบาหวาน", "โรคไขมันในเลือดสูง", "โรคความดันโลหิตสูง", "โรคมะเร็งต่อมลูกหมาก", "โรคไต", "โรคหัวใจขาดเลือด", "โรคจอประสาทตาเสื่อม", "โรคความจำเสื่อม"};
        final boolean[] checkedDisease = new boolean[9];
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

        // Go to Login
        tvToLogin = rootView.findViewById(R.id.tvToLogin);
        tvToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.contentContainer, LoginFragment.newInstance())
                        .commit();
            }
        });

        // Register
        etName = rootView.findViewById(R.id.etName);
        etSurname = rootView.findViewById(R.id.etSurname);
        etAge = rootView.findViewById(R.id.etAge);
        etWeight = rootView.findViewById(R.id.etWeight);
        etDisease = rootView.findViewById(R.id.etDisease);
        etAllergic = rootView.findViewById(R.id.etAllergic);
        etUsername = rootView.findViewById(R.id.etUsername);
        etPassword = rootView.findViewById(R.id.etPassword);
        etTel = rootView.findViewById(R.id.etTel);
        btnRegister = rootView.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });
    }

    public void validateForm() {

        // Validation form
        name = String.valueOf(etName.getText());
        surname = String.valueOf(etSurname.getText());
        age = String.valueOf(etAge.getText());
        weight = String.valueOf(etWeight.getText());
        disease = String.valueOf(etDisease.getText());
        allergy = String.valueOf(etAllergic.getText());
        username = String.valueOf(etUsername.getText());
        password = String.valueOf(etPassword.getText());
        tel = String.valueOf(etTel.getText());

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

        // Progress Dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("กรุณารอสักครู่");
        progressDialog.show();

        regisRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    progressDialog.dismiss();
                    etUsername.setError("ชื่อผู้ใช้นี้มีผู้ใช้แล้ว !");
                    etUsername.requestFocus();
                    return;
                } else {
                    checkTel();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkTel() {
        regisRef.orderByChild("user_phone").equalTo(tel).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    progressDialog.dismiss();
                    etTel.setError("หมายเลขโทรศัพท์นี้มีผู้ใช้แล้ว !");
                    etTel.requestFocus();
                    return;
                } else {
                    registerUser();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void registerUser() {
        // Add information to firebase database

            regisRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    countUser = dataSnapshot.getChildrenCount();
                    DecimalFormat idFormat = new DecimalFormat("0000");
                    user_id = "user" + idFormat.format(countUser + 1);
                    User newUser = new User(username, password, name, surname, disease, tel, allergy,
                            weight, age, "07:30", "08:30", "11:30", "12:30", "17:00", "18:00", "21:00");
                    regisRef.child(user_id
                    ).setValue(newUser);

                    progressDialog.dismiss();

                    // Keep user's key and status login in SharedPreferences
                    prefUser = getActivity().getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
                    editor = prefUser.edit();
                    editor.putString("keyUser", user_id);
                    editor.putBoolean("statusLogin", true);
                    editor.commit();

                    // Keep Notification time
                    prefNotiTime = getActivity().getSharedPreferences(NOTI_PREFS, Context.MODE_PRIVATE);
                    editorNotiTime = prefNotiTime.edit();
                    editorNotiTime.putString("morning_bf", "07:30");
                    editorNotiTime.putString("morning_af", "08:30");
                    editorNotiTime.putString("noon_bf", "11:30");
                    editorNotiTime.putString("noon_af", "12:30");
                    editorNotiTime.putString("evening_bf", "17:00");
                    editorNotiTime.putString("evening_af", "18:00");
                    editorNotiTime.putString("bed_bf", "21:00");
                    editorNotiTime.commit();

                    // Start Menu Activity
                    Intent intent = new Intent(getActivity(), MenuActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Error", databaseError.getMessage());
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
