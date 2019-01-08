package com.jenny.medicationreminder.fragment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.jenny.medicationreminder.EditMedActivity;
import com.jenny.medicationreminder.ListMedActivity;
import com.jenny.medicationreminder.R;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

public class EditMedFragment extends Fragment {
    CardView cvAppbar;
    CheckBox cbMorning, cbLunch, cbEvening, cbBeforeBed;
    EditText etTimeMorning, etDateEnd, etMedName;
    TextView tvTopic;
    RadioGroup optionAlert;
    FancyButton btnCustomDays, btnConfirm;
    ImageView btnBack;

    private int mYear, mMonth, mDay, mHour, mMinute;

    String topic;

    final String [] daysArr = {"วันอาทิตย์", "วันจันทร์", "วันอังคาร", "วันพุธ", "วันพฤหัสบดี", "วันศุกร์", "วันเสาร์"};
    final boolean[] checkedDays = new boolean[7];

    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";
    public static final String FRAG_TAG_TIME_PICKER = "fragment_time_picker_name";

    public EditMedFragment() {
        super();
    }

    public static EditMedFragment newInstance() {
        EditMedFragment fragment = new EditMedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_med, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here

        cvAppbar = rootView.findViewById(R.id.cvAppBarEditMed);
        cvAppbar.setBackgroundResource(R.drawable.bg_appbar);

        etTimeMorning = rootView.findViewById(R.id.etTimeMorning);
        etDateEnd = rootView.findViewById(R.id.etDateEnd);
        optionAlert = rootView.findViewById(R.id.optionAlert);
        btnCustomDays = rootView.findViewById(R.id.btnCustomDays);
        btnCustomDays.setEnabled(false);
        btnBack = rootView.findViewById(R.id.btnBack);
        btnConfirm = rootView.findViewById(R.id.btnConfirm);

        cbMorning = rootView.findViewById(R.id.cbMorning);
        cbLunch = rootView.findViewById(R.id.cbLunch);
        cbEvening = rootView.findViewById(R.id.cbEvening);
        cbBeforeBed = rootView.findViewById(R.id.cbBeforeBed);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/th_sarabun.ttf");
        cbMorning.setTypeface(typeface, Typeface.BOLD);
        cbLunch.setTypeface(typeface, Typeface.BOLD);
        cbEvening.setTypeface(typeface, Typeface.BOLD);
        cbBeforeBed.setTypeface(typeface, Typeface.BOLD);

        etMedName = rootView.findViewById(R.id.etMedName);
        tvTopic = rootView.findViewById(R.id.tvTopic);
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
//            topic = bundle.getString("topic");
//            tvTopic.setText(topic);
//            etMedName.setText(bundle.getString("medName"));
            String qrCode = bundle.getString("qrCode");
            Toast.makeText(getContext(), qrCode, Toast.LENGTH_LONG).show();
        }

        btnCustomDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final List<String> daysList = Arrays.asList(daysArr);

                builder.setTitle("วันที่บริหารยา");

                builder.setMultiChoiceItems(daysArr, checkedDays, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checkedDays[which] = isChecked;
                        String currentItem = daysList.get(which);
                    }
                });

                builder.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                String disease = "";
//                for (int i = 0; i < checkedDays.length; i++) {
//                    boolean checked = checkedDays[i];
//                    if (checked) {
//                        disease += daysList.get(i) + ", ";
//                    }
//                }
//                if (disease.length() > 0) {
//                    disease = disease.substring(0, disease.length() - 2);
//                }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        etTimeMorning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Date from Edit text
                String[] timeSplit = etTimeMorning.getText().toString().split(":");
                mHour = Integer.parseInt(timeSplit[0]);
                mMinute = Integer.parseInt(timeSplit[1]);

                // Show time picker dialog
                RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                        .setOnTimeSetListener((RadialTimePickerDialogFragment.OnTimeSetListener) getActivity())
                        .setStartTime(mHour, mMinute)
                        .setDoneText("ตกลง")
                        .setCancelText("ยกเลิก");
                rtpd.show(getFragmentManager(), FRAG_TAG_TIME_PICKER);
            }
        });

        etDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Date from Edit text
                String[] dateSplit = etDateEnd.getText().toString().split("/");
                mYear = Integer.parseInt(dateSplit[2]);
                mMonth = Integer.parseInt(dateSplit[1]);
                mDay = Integer.parseInt(dateSplit[0]);

                // Show Calendar date picker dialog
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener((CalendarDatePickerDialogFragment.OnDateSetListener) getActivity())
                        .setFirstDayOfWeek(Calendar.SUNDAY)
                        .setPreselectedDate(mYear-543, mMonth-1, mDay)
                        .setDoneText("ตกลง")
                        .setCancelText("ยกเลิก");
                cdp.show(getFragmentManager(), FRAG_TAG_DATE_PICKER);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (topic.equals("เพิ่มยา")) {
                    Intent intent = new Intent(getActivity(), ListMedActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    getActivity().finish();
                }
            }
        });

        optionAlert.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.everyAlert:
                        btnCustomDays.setEnabled(false);
                        break;
                    case R.id.customAlert:
                        btnCustomDays.setEnabled(true);
                        break;
                }
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
