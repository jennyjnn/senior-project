package com.jenny.medicationreminder.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jenny.medicationreminder.Holder.ListHistoryAdapter;
import com.jenny.medicationreminder.Model.ListMedHistory;
import com.jenny.medicationreminder.R;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment {

    CardView cvAppBar;
    ImageView btnBack;

    RecyclerView rcListHistory;
    RecyclerView.LayoutManager managerHistory;
    ListHistoryAdapter historyAdapter;
    List<ListMedHistory> datasetHistory;
    ListMedHistory listHistory;

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

        datasetHistory = new ArrayList<>();
        listHistory = new ListMedHistory();
        listHistory.setMedName("Paracetamol");
        listHistory.setMedProperty("แก้ปวด");
        listHistory.setMedDose("1 เม็ด");
        datasetHistory.add(listHistory);
        historyAdapter = new ListHistoryAdapter(getContext(), datasetHistory);
        rcListHistory.setAdapter(historyAdapter);

        btnBack = rootView.findViewById(R.id.btnBack);
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
