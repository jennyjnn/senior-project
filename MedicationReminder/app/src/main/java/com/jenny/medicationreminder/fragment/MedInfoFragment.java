package com.jenny.medicationreminder.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jenny.medicationreminder.MedInfoActivity;
import com.jenny.medicationreminder.Model.Medicine;
import com.jenny.medicationreminder.R;
import com.ms.square.android.expandabletextview.ExpandableTextView;

public class MedInfoFragment extends Fragment {

    CardView cvAppBarMedInfo, cvContent, cvOthers, cvSideEff;
    CardView cvWarning, cvPreserve;
    TextView expandable_text, tvMedName, tvOthers, tvNoMedInfo;
    ImageView btnBack;
    View vOthers, vSideEff, vWarning, vPreserve;
    //    YouTubePlayerView youTubeVdo;
    WebView wvVideo;

    //    private final String API_KEY = "AIzaSyA9phrfpPpbiywCUWBhZ_ekX7n6Uc9pggk";
    String videoCode = "";
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


    public MedInfoFragment() {
        super();
    }

    public static MedInfoFragment newInstance() {
        MedInfoFragment fragment = new MedInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_med_info, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        tvNoMedInfo = rootView.findViewById(R.id.tvNoMedInfo);
        vSideEff = rootView.findViewById(R.id.vSideEff);
        cvSideEff = rootView.findViewById(R.id.cvSideEff);
        vWarning = rootView.findViewById(R.id.vWarning);
        cvWarning = rootView.findViewById(R.id.cvWarning);
        vPreserve = rootView.findViewById(R.id.vPreserve);
        cvPreserve = rootView.findViewById(R.id.cvPreserve);
        vOthers = rootView.findViewById(R.id.vOthers);
        cvContent = rootView.findViewById(R.id.cvContent);
        cvOthers = rootView.findViewById(R.id.cvOthers);
        tvOthers = rootView.findViewById(R.id.tvOthers);
        tvMedName = rootView.findViewById(R.id.tvMedName);
        expandable_text = rootView.findViewById(R.id.expandable_text);
        expandIntro = rootView.findViewById(R.id.expandIntro);
        expandWarning = rootView.findViewById(R.id.expandWarning);
        expandSideEff = rootView.findViewById(R.id.expandSideEff);
        expandPreserve = rootView.findViewById(R.id.expandPreserve);
//        youTubeVdo = findViewById(R.id.youTubeVdo);
        wvVideo = rootView.findViewById(R.id.wvVideo);
        btnBack = rootView.findViewById(R.id.btnBack);

        Intent intent = getActivity().getIntent();
        medName = intent.getStringExtra("medName");
        medName = medName.toLowerCase();

        // Set Font
//        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/th_sarabun.ttf");
//        tvMedName.setTypeface(typeface, Typeface.BOLD);
//        tvNoMedInfo.setTypeface(typeface, Typeface.BOLD);
//        tvOthers.setTypeface(typeface, Typeface.BOLD);
//        expandable_text.setTypeface(typeface, Typeface.BOLD);

        // set color for app bar
        cvAppBarMedInfo = rootView.findViewById(R.id.cvAppBarMedInfo);
        cvAppBarMedInfo.setBackgroundResource(R.drawable.bg_appbar);

        // Progress Dialog
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("กรุณารอสักครู่");
        progressDialog.show();

        // Read information of medication from firebase database
        database = FirebaseDatabase.getInstance();
        infoRef = database.getReference("Medicine");
        infoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
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
                            // Others
                            videoCode = medicine.getMed_video();
                            if (videoCode != null) {
                                vOthers.setVisibility(View.VISIBLE);
                                cvOthers.setVisibility(View.VISIBLE);
                                tvOthers.setVisibility(View.VISIBLE);
                                wvVideo.setVisibility(View.VISIBLE);
                                // Show video

                                wvVideo.getSettings().setJavaScriptEnabled(true);
                                wvVideo.loadData("<iframe width=\"100%\" src=\"https://www.youtube.com/embed/WgfS1sFO40k\" frameborder=\"0\" allowfullscreen></iframe>",
                                        "text/html" , "utf-8");
                                wvVideo.setWebChromeClient(new WebChromeClient());

//                                youTubeVdo.initialize(API_KEY, new YouTubePlayer.OnInitializedListener() {
//                                    @Override
//                                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//                                        if (!b) {
//                                            youTubePlayer.cueVideo(videoCode);
//                                            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//                                        Toast.makeText(MedInfoActivity.this, youTubeInitializationResult.toString(),
//                                                Toast.LENGTH_LONG).show();
//                                    }
//                                });
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

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
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
