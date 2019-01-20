package com.jenny.medicationreminder.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jenny.medicationreminder.EditMedActivity;
import com.jenny.medicationreminder.Holder.ListMedAdapter;
import com.jenny.medicationreminder.ListMedActivity;
import com.jenny.medicationreminder.MainActivity;
import com.jenny.medicationreminder.Model.ListMed;
import com.jenny.medicationreminder.Model.Med_Record;
import com.jenny.medicationreminder.Model.Medicine;
import com.jenny.medicationreminder.Model.Notification_Time;
import com.jenny.medicationreminder.Model.User;
import com.jenny.medicationreminder.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListMedFragment extends Fragment implements RadialTimePickerDialogFragment.OnTimeSetListener {

    CardView cvAppBar;
    RecyclerView rcListMedBefore, rcListMedAfter;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManagerBefore, mLayoutManagerAfter;
    ImageView btnBack;
    ProgressDialog progressDialog;
    TextView tvTime, tvBeforeMeal, tvBeforeTime, tvAfterTime;
    LinearLayout linearBefore, linearAfter;
    View vLine;

    FirebaseDatabase database;
    DatabaseReference medRecordRef;
    DatabaseReference medRef;
    DatabaseReference notiTimeRef;

    ListMedAdapter adapterBefore, adapterAfter;

    String nameMed = new String();
    String properties;
    String descriptions;
    String date, time;
    String notiTime;
    String timeBefAft;
    int countBefore = 0;
    int countAfter = 0;

    List<ListMed> datasetBefore, datasetAfter;
    ListMed listMed;

    SharedPreferences prefUser;
    private static final String USER_PREFS = "userStatus";
    String keyUser;
    public static final String FRAG_TAG_TIME_PICKER = "fragment_time_picker_name";
    private int mHour, mMinute;

    public ListMedFragment() {
        super();
    }

    public static ListMedFragment newInstance(String date, String time) {
        ListMedFragment fragment = new ListMedFragment();
        Bundle args = new Bundle();
        args.putString("date", date);
        args.putString("time", time);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_med, container, false);
        initInstances(rootView);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        date = getArguments().getString("date");
        time = getArguments().getString("time");
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here

        tvTime = rootView.findViewById(R.id.tvTime);
        tvTime.setText(time);

        tvAfterTime = rootView.findViewById(R.id.tvAfterTime);
        tvBeforeTime = rootView.findViewById(R.id.tvBeforeTime);
        tvBeforeMeal = rootView.findViewById(R.id.tvBeforeMeal);
        linearAfter = rootView.findViewById(R.id.linearAfter);
        linearBefore = rootView.findViewById(R.id.linearBefore);
        vLine = rootView.findViewById(R.id.vLine);

        // set color on app bar
        cvAppBar = rootView.findViewById(R.id.cvAppBarList);
        cvAppBar.setBackgroundResource(R.drawable.bg_appbar);

        prefUser = getActivity().getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        keyUser = prefUser.getString("keyUser", "no user");

        // RecyclerView
        rcListMedBefore = rootView.findViewById(R.id.rcListMedBefore);
        rcListMedAfter = rootView.findViewById(R.id.rcListMedAfter);

        mLayoutManagerBefore = new LinearLayoutManager(getContext());
        rcListMedBefore.setLayoutManager(mLayoutManagerBefore);
        mLayoutManagerAfter = new LinearLayoutManager(getContext());
        rcListMedAfter.setLayoutManager(mLayoutManagerAfter);

        if (time.equals("เช้า")) {
            notiTime = "morning";
        } else if (time.equals("กลางวัน")) {
            notiTime = "noon";
        } else if (time.equals("เย็น")) {
            notiTime = "evening";
        } else {
            notiTime = "bed";
            tvBeforeMeal.setText("ก่อนนอน");
        }

        tvBeforeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeBefAft = "_bf";
                setTime(tvBeforeTime.getText().toString());
            }
        });

        tvAfterTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeBefAft = "_af";
                setTime(tvAfterTime.getText().toString());
            }
        });

        btnBack = rootView.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        getNotiTime();
    }

    private void setTime(String time) {
        // Get Date from Edit text
        String[] timeSplit = time.split(":");
        mHour = Integer.parseInt(timeSplit[0]);
        mMinute = Integer.parseInt(timeSplit[1]);

        // Show time picker dialog
        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                .setOnTimeSetListener(ListMedFragment.this)
                .setStartTime(mHour, mMinute)
                .setDoneText("ตกลง")
                .setCancelText("ยกเลิก");
        rtpd.show(getFragmentManager(), FRAG_TAG_TIME_PICKER);
    }

    private void getNotiTime() {
        // Progress Dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("กรุณารอสักครู่");
        progressDialog.show();

        database = FirebaseDatabase.getInstance();
        notiTimeRef = database.getReference("User");
        notiTimeRef.orderByKey().equalTo(keyUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    switch (notiTime) {
                        case "morning":
                            tvBeforeTime.setText(user.getMorning_bf());
                            tvAfterTime.setText(user.getMorning_af());
                            break;
                        case "noon":
                            tvBeforeTime.setText(user.getNoon_bf());
                            tvAfterTime.setText(user.getNoon_af());
                            break;
                        case "evening":
                            tvBeforeTime.setText(user.getEvening_bf());
                            tvAfterTime.setText(user.getEvening_af());
                            break;
                        case "bed":
                            tvBeforeTime.setText(user.getBed_bf());
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        queryMedList();
    }

    private void queryMedList() {
        database = FirebaseDatabase.getInstance();
        medRecordRef = database.getReference("Med_Record");
        Query query = medRecordRef.orderByChild("user_id").equalTo(keyUser);
        medRef = database.getReference("Medicine");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                datasetBefore = new ArrayList<>();
                datasetAfter= new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Med_Record medRecord = snapshot.getValue(Med_Record.class);
                    String dateMed = medRecord.getMedRec_notiDate();
                    String timeMed = medRecord.getMedRec_notiTime();
                    if (dateMed.equals(date) && timeMed.equals(time)) {
                        if (!medRecord.getMedRec_getTime().equals("false")) {
                            medRef.orderByKey().equalTo(medRecord.getMed_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        Medicine medicine = snapshot.getValue(Medicine.class);

                                        nameMed = medicine.getMed_name();
                                        properties = medicine.getMed_property();
                                        if (medicine.getMed_type() == null) {
                                            descriptions = medRecord.getMedRec_dose();
                                        } else {
                                            descriptions = medicine.getMed_type() + " " + medRecord.getMedRec_dose();
                                        }
                                        listMed = new ListMed();
                                        listMed.setNameMed(nameMed);
                                        listMed.setProperties(properties);
                                        listMed.setDescriptions(descriptions);
                                        listMed.setMedID(medRecord.getMed_id());
                                        if (medRecord.getMedRec_getTime().equals("none")) {
                                            listMed.setGetMed(false);
                                        } else {
                                            listMed.setGetMed(true);
                                        }

                                        String befAft = medRecord.getMedRec_BefAft();
                                        if (befAft.equals("ก่อนอาหาร") || befAft.equals("ก่อนนอน")) {
                                            datasetBefore.add(listMed);
                                            linearBefore.setVisibility(View.VISIBLE);
                                            countBefore++;
                                        } else {
                                            datasetAfter.add(listMed);
                                            linearAfter.setVisibility(View.VISIBLE);
                                            countAfter++;
                                        }
                                    }
                                    if (countBefore > 0 && countAfter > 0) {
                                        vLine.setVisibility(View.VISIBLE);
                                    }
                                    adapterBefore = new ListMedAdapter(getContext(), datasetBefore);
                                    rcListMedBefore.setAdapter(adapterBefore);
                                    adapterAfter = new ListMedAdapter(getContext(), datasetAfter);
                                    rcListMedAfter.setAdapter(adapterAfter);

                                    progressDialog.dismiss();
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void refreshMedList() {
        // Reload current fragment
        Fragment frg = null;
        frg = getFragmentManager().findFragmentByTag("ListMedFragment");
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
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

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        final String newTime = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute);
        AlertDialog.Builder alertTimeNoti = new AlertDialog.Builder(getContext());
        alertTimeNoti.setMessage("คุณต้องการเปลี่ยนเวลาการแจ้งเตือนเป็น "
                + newTime + " น. ใช่หรือไม่ ?");
        alertTimeNoti.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                notiTimeRef.orderByKey().equalTo(keyUser).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        notiTimeRef.child(keyUser).child(notiTime+timeBefAft).setValue(newTime);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        alertTimeNoti.setNeutralButton("ไม่ใช่", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = alertTimeNoti.create();
        alertDialog.show();
    }
}
