package com.jenny.medicationreminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jenny.medicationreminder.Holder.ListMedAdapter;
import com.jenny.medicationreminder.Model.ListMed;

import java.util.ArrayList;
import java.util.List;

public class ListMedActivity extends AppCompatActivity {

    CardView cvAppBar;
    RecyclerView rcListMed;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_med);

        initInstances();
    }

    private void initInstances() {
        // set color on app bar
        cvAppBar = findViewById(R.id.cvAppBarList);
        cvAppBar.setBackgroundResource(R.drawable.bg_appbar);

        // RecyclerView
        rcListMed = findViewById(R.id.rcListMed);
        rcListMed.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        rcListMed.setLayoutManager(mLayoutManager);

        mAdapter = new ListMedAdapter(this, initListMeds());
        rcListMed.setAdapter(mAdapter);

    }

    private List<ListMed> initListMeds() {
        ListMed testMed1 = new ListMed("Metformin", "ยาลดระดับน้ำตาลในเลือด",
                "หลังอาหาร, 1 เม็ด เวลา 08:00 น., 1 เม็ด เวลา 17:00 น.", "08:00");
        ListMed testMed2 = new ListMed("MELOXICAM", "ยาบรรเทาอาการปวดและอักเสบ",
                "หลังอาหาร, 1 เม็ด เวลา 08:00 น.", "08:00");
        ListMed testMed3 = new ListMed("Regular insulin", "ยาลดระดับน้ำตาลในเลือด", "ฉีด, 10 ยูนิต เวลา 08:30 น., 10 ยูนิต เวลา 17.00 น.","08:30");
        ListMed testMed4 = new ListMed("Simvastatin", "ยาลดไขมัน", "ก่อนนอน, 1 เม็ด เวลา 21:30 น.", "21:30");
        ListMed testMed5 = new ListMed("Sulfonylurea", "ยาเบาหวาน", "ก่อนอาหาร, 1 เม็ด เวลา 07:15 น.", "07:15");

        List<ListMed> dataset = new ArrayList<>();

        dataset.add(testMed1);
        dataset.add(testMed2);
        dataset.add(testMed3);
        dataset.add(testMed4);
        dataset.add(testMed5);

        return dataset;
    }

    public void backToHome(View view) {
        Intent intent = new Intent(ListMedActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}
