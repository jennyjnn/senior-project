package com.jenny.medicationreminder.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.jenny.medicationreminder.Model.User;
import com.jenny.medicationreminder.R;

import mehdi.sakout.fancybuttons.FancyButton;

public class EditLoginFragment extends Fragment {

    CardView cvAppBarEditLogin;
    TextView etUsername, etOldPassword, etNewPassword, etOldPhone, etNewPhone;
    FancyButton btnUpdateLogin, btnCancel;
    ImageView btnBack;

    SharedPreferences prefUser;
    private static final String USER_PREFS = "userStatus";
    String keyUser;
    String username;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef = database.getReference("User");

    ProgressDialog progressDialog;

    public EditLoginFragment() {
        super();
    }

    public static EditLoginFragment newInstance(String username, String phone) {
        EditLoginFragment fragment = new EditLoginFragment();
        Bundle args = new Bundle();
        args.putString("username", username);
        args.putString("phone", phone);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_login, container, false);
        initInstances(rootView);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username = getArguments().getString("username");
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        cvAppBarEditLogin = rootView.findViewById(R.id.cvAppBarEditLogin);
        cvAppBarEditLogin.setBackgroundResource(R.drawable.bg_appbar);

        etUsername = rootView.findViewById(R.id.etUsername);
        etOldPassword = rootView.findViewById(R.id.etOldPassword);
        etNewPassword = rootView.findViewById(R.id.etNewPassword);
        etOldPhone = rootView.findViewById(R.id.etOldPhone);
        etNewPhone = rootView.findViewById(R.id.etNewPhone);
        btnUpdateLogin = rootView.findViewById(R.id.btnUpdateLogin);
        btnCancel = rootView.findViewById(R.id.btnCancel);
        btnBack = rootView.findViewById(R.id.btnBack);

        prefUser = getActivity().getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        keyUser = prefUser.getString("keyUser", "no user");

        etUsername.setText(username);

        btnUpdateLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Progress Dialog
                progressDialog = new ProgressDialog(getActivity());
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
                                if (etNewPassword.getText().toString().length() < 4){
                                    progressDialog.dismiss();
                                    etNewPassword.setError("รหัสผ่านต้องมีอย่างน้อย 4 ตัวอักษร !");
                                    etNewPassword.requestFocus();
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
                                if (etNewPhone.getText().toString().length() != 10 || !etNewPhone.getText().toString().substring(0,1).equals("0")){
                                    progressDialog.dismiss();
                                    etNewPhone.setError("กรุณากรอกเบอร์โทรศัพท์ให้ถูกต้อง !");
                                    etNewPhone.requestFocus();
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
                        userRef.child(keyUser).child("user_phone").setValue(etNewPhone.getText().toString());
                    } else if (!etNewPassword.getText().toString().isEmpty() && etNewPhone.getText().toString().isEmpty()) {
                        userRef.child(keyUser).child("username").setValue(etUsername.getText().toString());
                        userRef.child(keyUser).child("password").setValue(etNewPassword.getText().toString());
                    } else {
                        userRef.child(keyUser).child("username").setValue(etUsername.getText().toString());
                        userRef.child(keyUser).child("password").setValue(etNewPassword.getText().toString());
                        userRef.child(keyUser).child("user_phone").setValue(etNewPhone.getText().toString());
                    }
                }
                getFragmentManager().popBackStack();
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
