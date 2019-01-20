package com.jenny.medicationreminder.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jenny.medicationreminder.EditMedActivity;
import com.jenny.medicationreminder.ListMedActivity;
import com.jenny.medicationreminder.MainActivity;
import com.jenny.medicationreminder.Model.Med_Record;
import com.jenny.medicationreminder.Model.Medicine;
import com.jenny.medicationreminder.R;

import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

public class EditMedFragment extends Fragment {
    CardView cvAppbar;
    CheckBox cbMorning, cbLunch, cbEvening, cbBeforeBed;
    EditText etDateEnd, etMedName, etDose, etDateStart, etProperty;
    TextView tvTopic, tvUnit;
    RadioGroup optionAlert;
    RadioButton beforeMeal, afterMeal;
    FancyButton btnCustomDays, btnConfirm;
    ImageView btnBack;
    LinearLayout linearTimeOfDay, linearEditMed, linearProperty;
    View lineProperty, lineTime;

    String qrCode = new String();
    String medID = new String();
    String property = new String();
    String medName = new String();
    String medDose = new String();
    String medTime = new String();
    String startDate = new String();
    Date notiDate;
    int amountDay;
    String endDate = new String();

    long countMedRec = 0;
    String medRecID;

    FirebaseDatabase database;
    DatabaseReference medRef, mRecordRef;

    ProgressDialog progressDialog;

    private int mYear, mMonth, mDay;

    String topic;

    SharedPreferences prefMed;
    SharedPreferences.Editor editor;
    SharedPreferences prefUser;
    private static final String USER_PREFS = "userStatus";
    String keyUser;

    private static final String MED_PREFS = "medID";

//    final String [] daysArr = {"วันอาทิตย์", "วันจันทร์", "วันอังคาร", "วันพุธ", "วันพฤหัสบดี", "วันศุกร์", "วันเสาร์"};
//    final boolean[] checkedDays = new boolean[7];

    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";

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

        etDateStart = rootView.findViewById(R.id.etDateStart);
        etDateEnd = rootView.findViewById(R.id.etDateEnd);
//        optionAlert = rootView.findViewById(R.id.optionAlert);
//        btnCustomDays = rootView.findViewById(R.id.btnCustomDays);
//        btnCustomDays.setEnabled(false);
        btnBack = rootView.findViewById(R.id.btnBack);
        btnConfirm = rootView.findViewById(R.id.btnConfirm);
        beforeMeal = rootView.findViewById(R.id.beforeMeal);
        afterMeal = rootView.findViewById(R.id.afterMeal);
        cbMorning = rootView.findViewById(R.id.cbMorning);
        cbLunch = rootView.findViewById(R.id.cbLunch);
        cbEvening = rootView.findViewById(R.id.cbEvening);
        cbBeforeBed = rootView.findViewById(R.id.cbBeforeBed);
        linearTimeOfDay = rootView.findViewById(R.id.linearTimeOfDay);
        etDose = rootView.findViewById(R.id.etDose);
        tvUnit = rootView.findViewById(R.id.tvUnit);
        etProperty = rootView.findViewById(R.id.etProperty);
        linearEditMed = rootView.findViewById(R.id.linearEditMed);
        linearProperty = rootView.findViewById(R.id.linearProperty);
        lineProperty = rootView.findViewById(R.id.lineProperty);
        lineTime = rootView.findViewById(R.id.lineTime);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/th_sarabun.ttf");
        cbMorning.setTypeface(typeface, Typeface.BOLD);
        cbLunch.setTypeface(typeface, Typeface.BOLD);
        cbEvening.setTypeface(typeface, Typeface.BOLD);
        cbBeforeBed.setTypeface(typeface, Typeface.BOLD);

        etMedName = rootView.findViewById(R.id.etMedName);
        tvTopic = rootView.findViewById(R.id.tvTopic);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("กรุณารอสักครู่");

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            topic = bundle.getString("topic");
            tvTopic.setText(topic);
            etMedName.setText(bundle.getString("medName"));
            qrCode = bundle.getString("qrCode");
        }
        if (topic.equals("เพิ่มยา")) {
            // Progress Dialog
            progressDialog.show();

            String[] qrCodeSplit = qrCode.split(" // ");
            // Name
            medName = qrCodeSplit[0].toLowerCase();
            etMedName.setText(medName);

            // Property
            // Read information of medication from firebase database
            database = FirebaseDatabase.getInstance();
            medRef = database.getReference("Medicine");
            medRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Medicine medicine = snapshot.getValue(Medicine.class);
                        if (medicine.getMed_name().toLowerCase().contains(medName)) {
                            medID = snapshot.getKey();
                            property = medicine.getMed_property();

                            prefMed = getActivity().getSharedPreferences(MED_PREFS, Context.MODE_PRIVATE);
                            editor = prefMed.edit();
                            editor.putString("medID", medID);
                            editor.commit();

                            if (property != null) {
                                etProperty.setText(property);
                                linearProperty.setVisibility(View.VISIBLE);
                                linearProperty.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    linearEditMed.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            // Before or After
            if (!qrCodeSplit[1].equals("-")) {
                if (qrCodeSplit[1].equals("ก่อนอาหาร")) {
                    beforeMeal.setChecked(true);
//                    afterMeal.setChecked(false);
                } else {
                    afterMeal.setChecked(true);
                }
            } else {
                linearTimeOfDay.setVisibility(View.GONE);
                lineTime.setVisibility(View.GONE);
            }

            // Time of day
            medTime = qrCodeSplit[2];
            if (medTime.contains("เช้า")) {
                cbMorning.setChecked(true);
            }
            if (medTime.contains("กลางวัน")) {
                cbLunch.setChecked(true);
            }
            if (medTime.contains("เย็น")) {
                cbEvening.setChecked(true);
            }
            if (medTime.contains("ก่อนนอน")) {
                cbBeforeBed.setChecked(true);
            }

            // Dose
            medDose = qrCodeSplit[3];
            String[] quantity = medDose.split(" ");
            etDose.setText(quantity[0]);
            tvUnit.setText(quantity[1]);

            // amount of days
            amountDay = Integer.parseInt(qrCodeSplit[4]);
            getDate(amountDay-1);
        }

//        btnCustomDays.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                final List<String> daysList = Arrays.asList(daysArr);
//
//                builder.setTitle("วันที่บริหารยา");
//
//                builder.setMultiChoiceItems(daysArr, checkedDays, new DialogInterface.OnMultiChoiceClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
//                        checkedDays[which] = isChecked;
//                        String currentItem = daysList.get(which);
//                    }
//                });
//
//                builder.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                String disease = "";
////                for (int i = 0; i < checkedDays.length; i++) {
////                    boolean checked = checkedDays[i];
////                    if (checked) {
////                        disease += daysList.get(i) + ", ";
////                    }
////                }
////                if (disease.length() > 0) {
////                    disease = disease.substring(0, disease.length() - 2);
////                }
//                    }
//                });
//
//                AlertDialog dialog = builder.create();
//                dialog.show();
//            }
//        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
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

                DateTime now = DateTime.now();
                MonthAdapter.CalendarDay minDate = new MonthAdapter.CalendarDay(now.getYear(), now.getMonthOfYear()-1, now.getDayOfMonth());

                // Show Calendar date picker dialog
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener((CalendarDatePickerDialogFragment.OnDateSetListener) getActivity())
                        .setFirstDayOfWeek(Calendar.SUNDAY)
                        .setPreselectedDate(mYear, mMonth-1, mDay)
                        .setDateRange(minDate, null)
                        .setDoneText("ตกลง")
                        .setCancelText("ยกเลิก");
                cdp.show(getFragmentManager(), FRAG_TAG_DATE_PICKER);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (topic.equals("เพิ่มยา")) {
                    progressDialog.show();
                    AddMedToDatabase();
//                    Intent intent = new Intent(getActivity(), ListMedActivity.class);
//                    startActivity(intent);
//                    getActivity().finish();
                } else {
                    getActivity().finish();
                }
            }
        });

//        optionAlert.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch(checkedId) {
//                    case R.id.everyAlert:
//                        btnCustomDays.setEnabled(false);
//                        break;
//                    case R.id.customAlert:
//                        btnCustomDays.setEnabled(true);
//                        break;
//                }
//            }
//        });
    }

    private void AddMedToDatabase() {
        prefUser = getContext().getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        keyUser = prefUser.getString("keyUser", "no user");
        prefMed = getContext().getSharedPreferences(MED_PREFS, Context.MODE_PRIVATE);
        medID = prefMed.getString("medID", "no ID");

        database = FirebaseDatabase.getInstance();
        mRecordRef = database.getReference("Med_Record");
        mRecordRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                notiDate = new Date();
                Calendar cal = Calendar.getInstance();

                endDate = etDateEnd.getText().toString();
                countMedRec = dataSnapshot.getChildrenCount();
                DecimalFormat idFormat = new DecimalFormat("000000");
                String[] medTimeSplit = medTime.split(",");
                while (!dateFormat.format(notiDate).equals(endDate)) {
                    for (String time:medTimeSplit) {
                        medRecID = "mr" + idFormat.format(countMedRec+1);
                        Med_Record med_record = new Med_Record("none", medName, medDose, etDateStart.getText().toString(),
                                etDateEnd.getText().toString(),time, dateFormat.format(notiDate), keyUser, medID);
                        mRecordRef.child(medRecID).setValue(med_record);
                        Log.e("Med Record", medRecID+" -- "+medName + ", " + medDose + ", " + etDateStart.getText().toString()
                                + ", " + etDateEnd.getText().toString() + ", " + time + ", " + dateFormat.format(notiDate) + ", " + keyUser + medID);
                        countMedRec++;
                    }
                    cal.setTime(notiDate);
                    cal.add(Calendar.DATE, 1);
                    notiDate = cal.getTime();
                }
                for (String time:medTimeSplit) {
                    medRecID = "mr" + idFormat.format(countMedRec+1);
                    Med_Record med_record = new Med_Record("none", medName, medDose, etDateStart.getText().toString(),
                            etDateEnd.getText().toString(),time, dateFormat.format(notiDate), keyUser, medID);
                        mRecordRef.child(medRecID).setValue(med_record);
                    Log.e("Med Record", medRecID+" -- "+medName + ", " + medDose + ", " + etDateStart.getText().toString()
                            + ", " + etDateEnd.getText().toString() + ", " + time + ", " + dateFormat.format(notiDate) + ", " + keyUser + medID);
                    countMedRec++;
                }

                progressDialog.dismiss();
                Intent intent = new Intent(getActivity(), ListMedActivity.class);
                startActivity(intent);
                getActivity().finish();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getDate(int amountDay) {
        // Get current date & time
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date dateCurrent = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateCurrent);
        cal.add(Calendar.DATE, amountDay);
        Date tomorrow = cal.getTime();
        startDate = dateFormat.format(dateCurrent);
        endDate = dateFormat.format(tomorrow);
        etDateStart.setText(startDate);
        etDateEnd.setText(endDate);
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
