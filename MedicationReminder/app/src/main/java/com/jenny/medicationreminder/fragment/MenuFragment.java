package com.jenny.medicationreminder.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jenny.medicationreminder.EditMedActivity;
import com.jenny.medicationreminder.ListMedActivity;
import com.jenny.medicationreminder.MainActivity;
import com.jenny.medicationreminder.MenuActivity;
import com.jenny.medicationreminder.R;
import com.jenny.medicationreminder.ScanQrCodeActivity;
import com.jenny.medicationreminder.ViewProfileActivity;


public class MenuFragment extends Fragment {

    CardView cvAppBar, cvListMed, cvProfile;
    ImageView imgLogout;
    LinearLayout btnAddMed;
    SharedPreferences prefUser;
    SharedPreferences.Editor editor;
    private static final String USER_PREFS = "userStatus";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    public MenuFragment() {
        super();
    }

    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        // Ser color for App bar
        cvAppBar = rootView.findViewById(R.id.cvAppBar);
        cvAppBar.setBackgroundResource(R.drawable.bg_appbar);

        imgLogout = rootView.findViewById(R.id.imgLogout);
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertLogout = new AlertDialog.Builder(getContext());
                alertLogout.setMessage("คุณต้องการออกจากระบบใช่หรือไม่ ?");
                alertLogout.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Clear user's key and status login in SharedPreferences
                        prefUser = getActivity().getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
                        editor = prefUser.edit();
                        editor.clear();
                        editor.commit();

                        // Back to Main Activity
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });

                alertLogout.setNeutralButton("ไม่ใช่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog alertDialog = alertLogout.create();
                alertDialog.show();
            }
        });

        btnAddMed = rootView.findViewById(R.id.btnAddMed);
        btnAddMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionCamera();
            }
        });

        cvListMed = rootView.findViewById(R.id.cvListMed);
        cvListMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListMedActivity.class);
                startActivity(intent);
            }
        });

        cvProfile = rootView.findViewById(R.id.cvProfile);
        cvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkPermissionCamera() {
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CAMERA);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA)) {
                showMessageOKCancel("อนุญาตให้เข้าถึงการใช้งานกล้อง",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[] {Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS);
                            }
                        });
                return;
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[] {Manifest.permission.CAMERA},
                        REQUEST_CODE_ASK_PERMISSIONS);
                Log.e("permission2", "allow2");
            }
        } else {
            toScanQrCode();
        }
    }

    private void toScanQrCode() {
        Intent intent = new Intent(getActivity(), ScanQrCodeActivity.class);
        intent.putExtra("topic", "เพิ่มยา");
        intent.putExtra("medName", "Meloxicam");
        startActivity(intent);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton("อนุญาต", okListener)
                .setNegativeButton("ปฏิเสธ", null)
                .create()
                .show();
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
