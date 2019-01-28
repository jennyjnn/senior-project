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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jenny.medicationreminder.MenuActivity;
import com.jenny.medicationreminder.Model.User;
import com.jenny.medicationreminder.R;
import com.libRG.CustomTextView;

import mehdi.sakout.fancybuttons.FancyButton;


public class LoginFragment extends Fragment {
    FancyButton btnLoginTel, btnLoginUser;
    EditText etLoginTel, etLoginUser, etLoginPass;
    CustomTextView tvToRegister;
    String tel, username, password;
    FirebaseDatabase database;
    DatabaseReference loginTelRef, loginUserRef;

    ProgressDialog progressDialog;

    SharedPreferences prefUser, prefNotiTime;
    SharedPreferences.Editor editor, editorNotiTime;
    private static final String USER_PREFS = "userStatus";
    private static final String NOTI_PREFS = "notiTime";

    public LoginFragment() {
        super();
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        // Login with phone number
        btnLoginTel = rootView.findViewById(R.id.btnLoginTel);
        etLoginTel = rootView.findViewById(R.id.etLoginTel);
        btnLoginTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginTel();
            }
        });

        // Login with username and password
        btnLoginUser = rootView.findViewById(R.id.btnLoginUser);
        etLoginUser = rootView.findViewById(R.id.etLoginUser);
        etLoginPass = rootView.findViewById(R.id.etLoginPass);
        btnLoginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        // Go to Register
        tvToRegister = rootView.findViewById(R.id.tvToRegister);
        tvToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.contentContainer, RegisterFragment.newInstance())
                        .commit();
            }
        });

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("กรุณารอสักครู่");
    }

    public void loginTel() {

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
        progressDialog.show();

        // Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setMessage("หมายเลขโทรศัพท์ไม่ถูกต้อง");
        builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();

        // Query database
        Query query = loginTelRef.orderByChild("user_phone").equalTo(String.valueOf(etLoginTel.getText()));
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

                        // Keep user's key and status login in SharedPreferences
                        prefUser = getActivity().getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
                        editor = prefUser.edit();
                        editor.putString("keyUser", String.valueOf(snapshot.getKey()));
                        editor.putBoolean("statusLogin", true);
                        editor.commit();

                        // Keep Notification time
                        prefNotiTime = getActivity().getSharedPreferences(NOTI_PREFS, Context.MODE_PRIVATE);
                        editorNotiTime = prefNotiTime.edit();
                        editorNotiTime.putString("morning_bf", user.getMorning_bf());
                        editorNotiTime.putString("morning_af", user.getMorning_af());
                        editorNotiTime.putString("noon_bf", user.getNoon_bf());
                        editorNotiTime.putString("noon_af", user.getNoon_af());
                        editorNotiTime.putString("evening_bf", user.getEvening_bf());
                        editorNotiTime.putString("evening_af", user.getEvening_af());
                        editorNotiTime.putString("bed_bf", user.getBed_bf());
                        editorNotiTime.commit();

                        // Start Menu
                        Intent intent = new Intent(getActivity(), MenuActivity.class);
                        startActivity(intent);
                        getActivity().finish();
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

    public void loginUser() {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setMessage("ชื่อผู้ใช้งานหรือรหัสผ่านไม่ถูกต้อง");
        builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();

        // Progress Dialog
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

                            // Keep user's key and status login in SharedPreferences
                            prefUser = getActivity().getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
                            editor = prefUser.edit();
                            editor.putString("keyUser", String.valueOf(snapshot.getKey()));
                            editor.putBoolean("statusLogin", true);
                            editor.commit();

                            // Keep Notification time
                            prefNotiTime = getActivity().getSharedPreferences(NOTI_PREFS, Context.MODE_PRIVATE);
                            editorNotiTime = prefNotiTime.edit();
                            editorNotiTime.putString("morning_bf", user.getMorning_bf());
                            editorNotiTime.putString("morning_af", user.getMorning_af());
                            editorNotiTime.putString("noon_bf", user.getNoon_bf());
                            editorNotiTime.putString("noon_af", user.getNoon_af());
                            editorNotiTime.putString("evening_bf", user.getEvening_bf());
                            editorNotiTime.putString("evening_af", user.getEvening_af());
                            editorNotiTime.putString("bed_bf", user.getBed_bf());
                            editorNotiTime.commit();

                            // Start Menu Activity
                            Intent intent = new Intent(getActivity(), MenuActivity.class);
                            startActivity(intent);
                            getActivity().finish();
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
