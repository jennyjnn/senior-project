package com.jenny.medicationreminder.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jenny.medicationreminder.Model.Med_Record;
import com.jenny.medicationreminder.R;

import java.text.SimpleDateFormat;
import java.util.Date;


public class SelectTimeFragment extends Fragment {

    CardView cvAppBar;
    ImageView btnBack;
    TextView tvDate;
    LinearLayout LinearContent, cardMoring, cardLunch, cardEvening, cardBeforeBed;
    TextView tvCountMorning, tvCountLunch, tvCountEvening, tvCountBeforeBed;

    ProgressDialog progressDialog;

    FirebaseDatabase database;
    DatabaseReference medRecordRef;

    SharedPreferences prefUser;
    private static final String USER_PREFS = "userStatus";
    String keyUser;
    String dateMedList;

    int morning = 0;
    int lunch = 0;
    int evening = 0;
    int beforeBed = 0;

    public SelectTimeFragment() {
        super();
    }

    public static SelectTimeFragment newInstance() {
        SelectTimeFragment fragment = new SelectTimeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_select_time, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        cvAppBar = rootView.findViewById(R.id.cvAppBar);
        cvAppBar.setBackgroundResource(R.drawable.bg_appbar);

        tvDate = rootView.findViewById(R.id.tvDate);
        cardMoring = rootView.findViewById(R.id.cardMorning);
        cardLunch = rootView.findViewById(R.id.cardLunch);
        cardEvening = rootView.findViewById(R.id.cardEvening);
        cardBeforeBed = rootView.findViewById(R.id.cardBeforeBed);
        tvCountMorning = rootView.findViewById(R.id.tvCountMorning);
        tvCountLunch = rootView.findViewById(R.id.tvCountLunch);
        tvCountEvening = rootView.findViewById(R.id.tvCountEvening);
        tvCountBeforeBed = rootView.findViewById(R.id.tvCountBeforeBed);
        LinearContent = rootView.findViewById(R.id.LinearContent);


        prefUser = getContext().getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        keyUser = prefUser.getString("keyUser", "no user");

        cardMoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.contentContainerListMed, ListMedFragment.newInstance(dateMedList, "เช้า"))
                        .addToBackStack(null)
                        .commit();
            }
        });

        cardLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.contentContainerListMed, ListMedFragment.newInstance(dateMedList, "กลางวัน"))
                        .addToBackStack(null)
                        .commit();
            }
        });

        cardEvening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.contentContainerListMed, ListMedFragment.newInstance(dateMedList, "เย็น"))
                        .addToBackStack(null)
                        .commit();
            }
        });

        cardBeforeBed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.contentContainerListMed, ListMedFragment.newInstance(dateMedList, "ก่อนนอน"))
                        .addToBackStack(null)
                        .commit();
            }
        });

        btnBack = rootView.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        getDate();
    }

    private void getDate() {
        // Get current date & time
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date dateCurrent = new Date();
        tvDate.setText(dateFormat.format(dateCurrent));

        checkMed(dateFormat.format(dateCurrent));
    }

    private void checkMed(String date) {
        // Progress Dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("กรุณารอสักครู่");
        progressDialog.show();

        dateMedList = date;
        morning = 0;
        lunch = 0;
        evening = 0;
        beforeBed = 0;

        database = FirebaseDatabase.getInstance();
        medRecordRef = database.getReference("Med_Record");
        Query query = medRecordRef.orderByChild("User_id").equalTo(keyUser);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Med_Record medRecord = snapshot.getValue(Med_Record.class);
                        String dateMed = medRecord.getMedRec_startDate();
                        if (dateMed.equals(dateMedList)) {
                            String notiTime = medRecord.getMedRec_notiTime();
                            switch (notiTime) {
                                case "เช้า":
                                    morning++;
                                    break;
                                case "กลางวัน":
                                    lunch++;
                                    break;
                                case "เย็น":
                                    evening++;
                                    break;
                                case "ก่อนนอน":
                                    beforeBed++;
                                    break;
                            }
                        }
                    }
                }
                showCountMed();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showCountMed() {
        if (morning > 0) {
            tvCountMorning.setText(morning + " ตัวยา");
        } else {
            cardMoring.setBackgroundResource(R.drawable.time_of_day_gray);
            cardMoring.setEnabled(false);
            tvCountMorning.setVisibility(View.INVISIBLE);
        }
        if (lunch > 0) {
            tvCountLunch.setText(lunch + " ตัวยา");
        } else {
            cardLunch.setBackgroundResource(R.drawable.time_of_day_gray);
            cardLunch.setEnabled(false);
            tvCountLunch.setVisibility(View.INVISIBLE);
        }
        if (evening > 0) {
            tvCountEvening.setText(evening + " ตัวยา");
        } else {
            cardEvening.setBackgroundResource(R.drawable.time_of_day_gray);
            cardEvening.setEnabled(false);
            tvCountEvening.setVisibility(View.INVISIBLE);
        }
        if (beforeBed > 0) {
            tvCountBeforeBed.setText(beforeBed + " ตัวยา");
        } else {
            cardBeforeBed.setBackgroundResource(R.drawable.time_of_day_gray);
            cardBeforeBed.setEnabled(false);
            tvCountBeforeBed.setVisibility(View.INVISIBLE);
        }

        LinearContent.setVisibility(View.VISIBLE);
        progressDialog.dismiss();
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