package com.jenny.medicationreminder.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jenny.medicationreminder.Holder.ListMedAdapter;
import com.jenny.medicationreminder.ListMedActivity;
import com.jenny.medicationreminder.Model.ListMed;
import com.jenny.medicationreminder.Model.Med_Record;
import com.jenny.medicationreminder.Model.Medicine;
import com.jenny.medicationreminder.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListMedFragment extends Fragment {

    CardView cvAppBar;
    RecyclerView rcListMed;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    TextView tvNoListMed;
    ImageView btnBack;
    ProgressDialog progressDialog;


    FirebaseDatabase database;
    DatabaseReference medRecordRef;
    DatabaseReference medRef;

    ListMedAdapter adapter;

    String nameMed = new String();
    String properties;
    String descriptions;
    String time;

    List<ListMed> dataset;
    ListMed listMed;

    SharedPreferences prefUser;
    private static final String USER_PREFS = "userStatus";
    String keyUser;

    public ListMedFragment() {
        super();
    }

    public static ListMedFragment newInstance() {
        ListMedFragment fragment = new ListMedFragment();
        Bundle args = new Bundle();
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

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here

        // set color on app bar
        cvAppBar = rootView.findViewById(R.id.cvAppBarList);
        cvAppBar.setBackgroundResource(R.drawable.bg_appbar);

        tvNoListMed = rootView.findViewById(R.id.tvNoListMed);

        prefUser = getActivity().getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        keyUser = prefUser.getString("keyUser", "no user");

        // RecyclerView
        rcListMed = rootView.findViewById(R.id.rcListMed);

        mLayoutManager = new LinearLayoutManager(getContext());
        rcListMed.setLayoutManager(mLayoutManager);

        // Get current date & time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        Log.e("Date", dateFormat.format(date));

        btnBack = rootView.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        queryMedList();
    }

    private void queryMedList() {
        // Progress Dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("กรุณารอสักครู่");
        progressDialog.show();

        database = FirebaseDatabase.getInstance();
        medRecordRef = database.getReference("Med_Record");
        Query query = medRecordRef.orderByChild("User_id").equalTo(keyUser);
        medRef = database.getReference("Medicine");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataset = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Med_Record medRecord = snapshot.getValue(Med_Record.class);
                    medRef.orderByKey().equalTo(medRecord.getMed_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Medicine medicine = snapshot.getValue(Medicine.class);
                                nameMed = medicine.getMed_name();
                                properties = medicine.getMed_property();
                                descriptions = medicine.getMed_type() + ", ";

                                listMed = new ListMed();
                                listMed.setNameMed(nameMed);
                                listMed.setProperties(properties);
                                listMed.setDescriptions(descriptions);
                                listMed.setTime(medRecord.getMedRec_notiTime());
                                dataset.add(listMed);
                            }
                            adapter = new ListMedAdapter(getContext(), dataset);
                            rcListMed.setAdapter(adapter);

                            progressDialog.dismiss();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //    private List<ListMed> initListMeds() {
//        final ListMed testMed5 = new ListMed();
//
//        dataset = new ArrayList<>();
//        database = FirebaseDatabase.getInstance();
//        medRecordRef = database.getReference("Med_Record");
//        Query query = medRecordRef.orderByChild("User_id").equalTo(keyUser);
//
//
//        medRef = database.getReference("Medicine");
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        Med_Record medRecord = snapshot.getValue(Med_Record.class);
////                        medRef.orderByKey().equalTo(medRecord.getMed_id()).addListenerForSingleValueEvent(new ValueEventListener() {
////                            @Override
////                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                                if (dataSnapshot.exists()) {
////                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
////                                        Medicine medicine = snapshot.getValue(Medicine.class);
////                                        nameMed = medicine.getMed_name();
////                                        properties = medicine.getMed_property();
////                                        descriptions = medicine.getMed_type() + ", ";
//////                                        listMed.setNameMed(nameMed);
//////                                        listMed.setProperties(properties);
////
////                                        Log.e("Check", snapshot.getKey() + "---" + properties);
////                                    }
////                                }
////                            }
////
////                            @Override
////                            public void onCancelled(@NonNull DatabaseError databaseError) {
////
////                            }
////                        });
//
////                        if (medRecord.getMedRec_BefAft() != null) {
////                            descriptions += medRecord.getMedRec_BefAft() + ", " + medRecord.getMedRec_dose() + " เวลา " + medRecord.getMedRec_getTime();
////                        } else {
////                            descriptions += medRecord.getMedRec_dose() + " เวลา " + medRecord.getMedRec_getTime();
////                        }
//
//
//                        nameMed = medRecord.getMed_id();
//                        properties = medRecord.getMedRec_dose();
//                        time = medRecord.getMedRec_notiTime();
//                        descriptions = medRecord.getMedRec_BefAft();
//                        listMed = new ListMed(nameMed, properties, descriptions, time);
//
////                        listMed.setTime(time);
////                        listMed.setDescriptions(medRecord.getMedRec_BefAft());
//                        dataset.add(listMed);
//
//                        Log.e("test",nameMed + "/"+properties+"/"+descriptions+"/"+time);
////                        tvNoListMed.setText("-- " + nameMed + "\n" + properties + "\n" + descriptions + "\n" + time);
////                        Log.e("Test list med", "-- Name: " + nameMed + " P : " + properties + " D : " + descriptions + " T : " + time);
////                        tvNoListMed.setVisibility(View.VISIBLE);
////                        ListMed listMed = new ListMed(nameMed, properties, descriptions, time);
////                        dataset.add(listMed);
//                    }
//                    Log.e("test2",nameMed + "/"+properties+"/"+descriptions+"/"+time);
////                    ListMed listMed3 = new ListMed(nameMed, properties, descriptions, time);
////                    dataset.add(listMed3);
//
//
//
//                } else {
//                    tvNoListMed.setVisibility(View.VISIBLE);
//                }
//                check = true;
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//
////        if (check) {
////            nameMed = "Name";
////            properties = "Prop";
////            descriptions = "Des";
////            time = "08:00";
////
////            ListMed listMed2 = new ListMed(nameMed, properties, descriptions, time);
////            dataset.add(listMed2);
////
////
////
////        }
//
//        ListMed testMed1 = new ListMed("Metformin", "ยาลดระดับน้ำตาลในเลือด",
//                "หลังอาหาร, 1 เม็ด เวลา 08:00 น., 1 เม็ด เวลา 17:00 น.", "08:00");
//        ListMed testMed2 = new ListMed("MELOXICAM", "ยาบรรเทาอาการปวดและอักเสบ",
//                "หลังอาหาร, 1 เม็ด เวลา 08:00 น.", "08:00");
//        ListMed testMed3 = new ListMed("Regular insulin", "ยาลดระดับน้ำตาลในเลือด",
//                "ฉีด, 10 ยูนิต เวลา 08:30 น., 10 ยูนิต เวลา 17.00 น.","08:30");
//        ListMed testMed4 = new ListMed("Simvastatin", "ยาลดไขมัน",
//                "ก่อนนอน, 1 เม็ด เวลา 21:30 น.", "21:30");
//
//        testMed5.setNameMed("name");
//        testMed5.setDescriptions("des");
//        testMed5.setTime("time");
//        testMed5.setProperties("prop");
//        dataset.add(testMed5);
//        Log.e("testList", testMed5.getNameMed());
//
////        dataset.add(testMed1);
////        dataset.add(testMed2);
////        dataset.add(testMed3);
////        dataset.add(testMed4);
//        return dataset;
//
//    }

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
