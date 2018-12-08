package com.jenny.medicationreminder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jenny.medicationreminder.Model.Medicine;
import com.ms.square.android.expandabletextview.ExpandableTextView;

public class MedInfoActivity extends AppCompatActivity {

    CardView cvAppBarMedInfo, cvContent, cvOthers, cvSideEff;
    CardView cvWarning, cvPreserve;
    TextView tvMedName, tvOthers, tvNoMedInfo;
    View vOthers, vSideEff, vWarning, vPreserve;

    String medName = new String();
    String intro = new String();
    String sideEffect = new String();
    String warning = new String();
    String preserve = new String();

    Boolean checkMedInfo = false;

    ProgressDialog progressDialog;

    FirebaseDatabase database;
    DatabaseReference infoRef;

    ExpandableTextView expandIntro, expandSideEff, expandWarning, expandPreserve;

//    VideoView vdMed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_info);

        Intent intent = getIntent();
        medName = intent.getStringExtra("medName");
        medName = medName.toLowerCase();

        initInstances();
    }

    private void initInstances() {
        tvNoMedInfo = findViewById(R.id.tvNoMedInfo);
        vSideEff = findViewById(R.id.vSideEff);
        cvSideEff = findViewById(R.id.cvSideEff);
        vWarning = findViewById(R.id.vWarning);
        cvWarning = findViewById(R.id.cvWarning);
        vPreserve = findViewById(R.id.vPreserve);
        cvPreserve = findViewById(R.id.cvPreserve);
        vOthers = findViewById(R.id.vOthers);
        cvContent = findViewById(R.id.cvContent);
        cvOthers = findViewById(R.id.cvOthers);
        tvOthers = findViewById(R.id.tvOthers);
        tvMedName = findViewById(R.id.tvMedName);
        expandIntro = findViewById(R.id.expandIntro);
        expandWarning = findViewById(R.id.expandWarning);
        expandSideEff = findViewById(R.id.expandSideEff);
        expandPreserve = findViewById(R.id.expandPreserve);

        // set color for app bar
        cvAppBarMedInfo = findViewById(R.id.cvAppBarMedInfo);
        cvAppBarMedInfo.setBackgroundResource(R.drawable.bg_appbar);

        // Progress Dialog
        progressDialog = new ProgressDialog(MedInfoActivity.this);
        progressDialog.setMessage("กรุณารอสักครู่");
        progressDialog.show();

        // Read information of medication from firebase database
        database = FirebaseDatabase.getInstance();
        infoRef = database.getReference("Medicine");
        infoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
//                cvOthers.setVisibility(View.GONE);
//                tvOthsers.setVisibility(View.GONE);
//                vOthers.setVisibility(View.GONE);
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Medicine medicine = snapshot.getValue(Medicine.class);
                        if (medicine.getMed_name().toLowerCase().contains(medName)) {
                            // Name
                            tvMedName.setText(medicine.getMed_name());
                            // Intro
                            intro = medicine.getMed_intro();
                            expandIntro.setText(intro);
                            // Side Effect
                            sideEffect = medicine.getMed_sideEffect();
                            if (sideEffect != null) {
                                vSideEff.setVisibility(View.VISIBLE);
                                cvSideEff.setVisibility(View.VISIBLE);
                                expandSideEff.setText(sideEffect);
                            }
                            // Warning
                            warning = medicine.getMed_warning();
                            if (warning != null) {
                                vWarning.setVisibility(View.VISIBLE);
                                cvWarning.setVisibility(View.VISIBLE);
                                expandWarning.setText(warning);
                            }
                            // Preserve
                            preserve = medicine.getMed_preserve();
                            if (preserve != null) {
                                vPreserve.setVisibility(View.VISIBLE);
                                cvPreserve.setVisibility(View.VISIBLE);
                                expandPreserve.setText(preserve);
                            }
                            Log.e("Med", medicine.getMed_name());
                            checkMedInfo = true;
                            cvContent.setVisibility(View.VISIBLE);
                        }
                    }
                    if (checkMedInfo == false) {
                        tvNoMedInfo.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //  set url to video
//        vdMed = findViewById(R.id.vdMed);

        String path = "https://www.youtube.com/watch?v=WgfS1sFO40k";
        Uri uri = Uri.parse(path);

//        vdMed.setVideoURI(uri);
//        vdMed.start();

    }


    public void backBtn(View view) {
        finish();
    }

}
