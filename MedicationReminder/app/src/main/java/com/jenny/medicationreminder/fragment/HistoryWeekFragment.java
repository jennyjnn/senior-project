package com.jenny.medicationreminder.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jenny.medicationreminder.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Calendar;
import java.util.Date;


public class HistoryWeekFragment extends Fragment {

    CardView cvAppBar;
    ImageView btnBack;
    GraphView graphWeek;

    public HistoryWeekFragment() {
        super();
    }

    public static HistoryWeekFragment newInstance() {
        HistoryWeekFragment fragment = new HistoryWeekFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history_week, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        // Init 'View' instance(s) with rootView.findViewById here
        cvAppBar = rootView.findViewById(R.id.cvAppBar);
        cvAppBar.setBackgroundResource(R.drawable.bg_appbar);

        graphWeek = rootView.findViewById(R.id.graphWeek);
        initGraph(graphWeek);

        btnBack = rootView.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
    }

    private void initGraph(GraphView graphWeek) {
        graphWeek.setTitle("METFORMIN");
        graphWeek.setTitleTextSize(55);
        graphWeek.setTitleColor(getResources().getColor(R.color.colorDarkGreen));

        // generate Dates
        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE, -1);
        Date d2 = calendar.getTime();
        calendar.add(Calendar.DATE, -1);
        Date d3 = calendar.getTime();
        calendar.add(Calendar.DATE, -1);
        Date d4 = calendar.getTime();
        calendar.add(Calendar.DATE, -1);
        Date d5 = calendar.getTime();
        calendar.add(Calendar.DATE, -1);
        Date d6 = calendar.getTime();
        calendar.add(Calendar.DATE, -1);
        Date d7 = calendar.getTime();

        // set manual X bounds
        graphWeek.getViewport().setYAxisBoundsManual(true);
        graphWeek.getViewport().setMinY(0);
        graphWeek.getViewport().setMaxY(24);

        graphWeek.getViewport().setXAxisBoundsManual(true);
        graphWeek.getViewport().setMinX(1);
        graphWeek.getViewport().setMaxX(8);
        // enable scaling and scrolling
//        graphWeek.getViewport().setScalable(true);
//        graphWeek.getViewport().setScalableY(true);
        graphWeek.getViewport().setScrollable(true);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(1, 18),
                new DataPoint(2, 20),
                new DataPoint(3, 3),
                new DataPoint(4, 12),
                new DataPoint(5, 6),
                new DataPoint(6, 10),
                new DataPoint(7, 7)
        });
        series.setTitle("เช้า");
        series.setAnimated(true);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(15);
        series.setColor(getResources().getColor(R.color.colorBtnRed));

        graphWeek.addSeries(series);
        graphWeek.getLegendRenderer().setVisible(true);
        graphWeek.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graphWeek.getLegendRenderer().setMargin(30);

        // set date label formatter
//        graphWeek.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
//        graphWeek.getGridLabelRenderer().setNumHorizontalLabels(4); // only 4 because of the space
//        graphWeek.getGridLabelRenderer().setHumanRounding(false);

        graphWeek.getGridLabelRenderer().setVerticalAxisTitle("เวลา");
        graphWeek.getGridLabelRenderer().setVerticalAxisTitleColor(getResources().getColor(R.color.colorDarkGreen));
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
