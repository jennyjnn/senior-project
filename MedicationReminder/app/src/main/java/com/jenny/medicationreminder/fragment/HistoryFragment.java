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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jenny.medicationreminder.Holder.ListHistoryAdapter;
import com.jenny.medicationreminder.Model.ListMedHistory;
import com.jenny.medicationreminder.Model.Med_Record;
import com.jenny.medicationreminder.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;


public class HistoryFragment extends Fragment {

    CardView cvAppBar;
    ImageView btnBack;

    RecyclerView rcListHistory;
    RecyclerView.LayoutManager managerHistory;
    ListHistoryAdapter historyAdapter;
    List<ListMedHistory> datasetHistory;
    ListMedHistory listHistory;

    LinkedHashSet<String> medNameSet, medIDSet, medDoseSet, medPropSet;
    FirebaseDatabase database;
    DatabaseReference medRecordRef;

    ProgressDialog progressDialog;

    SharedPreferences prefUser;
    private static final String USER_PREFS = "userStatus";
    String keyUser;

    public HistoryFragment() {
        super();
    }

    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        cvAppBar = rootView.findViewById(R.id.cvAppBar);
        cvAppBar.setBackgroundResource(R.drawable.bg_appbar);

        // RecyclerView
        rcListHistory = rootView.findViewById(R.id.rcListHistory);
        managerHistory = new LinearLayoutManager(getContext());
        rcListHistory.setLayoutManager(managerHistory);

        // SharedPreferences
        prefUser = getActivity().getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        keyUser = prefUser.getString("keyUser", "no user");

        btnBack = rootView.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        queryHistListMed();
    }

    private void queryHistListMed() {
        // Progress Dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("กรุณารอสักครู่");
        progressDialog.show();

        database = FirebaseDatabase.getInstance();
        medRecordRef = database.getReference("Med_Record");
        medRecordRef.orderByChild("user_id").equalTo(keyUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                medIDSet = new LinkedHashSet<>();
                medDoseSet = new LinkedHashSet<>();
                datasetHistory = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Med_Record medRecord = snapshot.getValue(Med_Record.class);
                    medIDSet.add(medRecord.getMed_id());
                    medDoseSet.add(medRecord.getMedRec_dose());
                }

                Iterator<String> iterator = medIDSet.iterator();                while (iterator.hasNext()) {
                    listHistory = new ListMedHistory();
                    listHistory.setMedID(iterator.next());
                    datasetHistory.add(listHistory);
                }

                historyAdapter = new ListHistoryAdapter(getContext(), datasetHistory);
                rcListHistory.setAdapter(historyAdapter);
                progressDialog.dismiss();
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
