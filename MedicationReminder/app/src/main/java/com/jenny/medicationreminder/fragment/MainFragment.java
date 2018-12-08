package com.jenny.medicationreminder.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jenny.medicationreminder.LoginTelActivity;
import com.jenny.medicationreminder.LoginUserActivity;
import com.jenny.medicationreminder.MainActivity;
import com.jenny.medicationreminder.MenuActivity;
import com.jenny.medicationreminder.R;
import com.jenny.medicationreminder.RegisterActivity;

import mehdi.sakout.fancybuttons.FancyButton;


public class MainFragment extends Fragment {

    private static final String USER_PREFS = "userStatus";
    FancyButton btnToRegister, btnToLogin;

    public MainFragment() {
        super();
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        // Register Button
        btnToRegister = rootView.findViewById(R.id.btnToRegister);
        btnToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.contentContainer, RegisterFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Login Button
        btnToLogin = rootView.findViewById(R.id.btnToLogin);
        btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.contentContainer, LoginFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
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



//
//    public void showDialogLogin(View view) {
//        final Dialog dialog = new Dialog(MainActivity.this);
//        dialog.setContentView(R.layout.dialog_login);
//
//        FancyButton btnUserLogin = dialog.findViewById(R.id.btnUserLogin);
//        FancyButton btnTelLogin = dialog.findViewById(R.id.btnTelLogin);
//        ImageView btnClose = dialog.findViewById(R.id.btnClose);
//
//        btnClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        btnUserLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, LoginUserActivity.class);
//                startActivity(intent);
//                dialog.dismiss();
//            }
//        });
//
//        btnTelLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, LoginTelActivity.class);
//                startActivity(intent);
//                dialog.dismiss();
//            }
//        });
//
//        dialog.show();
//    }


}
