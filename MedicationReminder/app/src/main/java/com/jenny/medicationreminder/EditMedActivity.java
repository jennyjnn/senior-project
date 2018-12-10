package com.jenny.medicationreminder;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;


public class EditMedActivity extends AppCompatActivity
        implements CalendarDatePickerDialogFragment.OnDateSetListener, RadialTimePickerDialogFragment.OnTimeSetListener {

    CardView cvAppbar;
    CheckBox cbMorning, cbLunch, cbEvening, cbBeforeBed;
    EditText etTimeMorning, etDateEnd, etMedName;
    TextView tvTopic;
    RadioGroup optionAlert;
    FancyButton btnCustomDays;

    private int mYear, mMonth, mDay, mHour, mMinute;

    final String [] daysArr = {"วันอาทิตย์", "วันจันทร์", "วันอังคาร", "วันพุธ", "วันพฤหัสบดี", "วันศุกร์", "วันเสาร์"};
    final boolean[] checkedDays = new boolean[7];

    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";
    public static final String FRAG_TAG_TIME_PICKER = "fragment_time_picker_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_med);

        initInstances();
    }


    private void initInstances() {
        cvAppbar = findViewById(R.id.cvAppBarEditMed);
        cvAppbar.setBackgroundResource(R.drawable.bg_appbar);

        etTimeMorning = findViewById(R.id.etTimeMorning);
        etDateEnd = findViewById(R.id.etDateEnd);
        optionAlert = findViewById(R.id.optionAlert);
        btnCustomDays = findViewById(R.id.btnCustomDays);
        btnCustomDays.setEnabled(false);

        cbMorning = findViewById(R.id.cbMorning);
        cbLunch = findViewById(R.id.cbLunch);
        cbEvening = findViewById(R.id.cbEvening);
        cbBeforeBed = findViewById(R.id.cbBeforeBed);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/th_sarabun.ttf");
        cbMorning.setTypeface(typeface, Typeface.BOLD);
        cbLunch.setTypeface(typeface, Typeface.BOLD);
        cbEvening.setTypeface(typeface, Typeface.BOLD);
        cbBeforeBed.setTypeface(typeface, Typeface.BOLD);

        etMedName = findViewById(R.id.etMedName);
        tvTopic = findViewById(R.id.tvTopic);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tvTopic.setText(bundle.getString("topic"));
            etMedName.setText(bundle.getString("medName"));
        }

    }

    public void setTime(View view) {

        // Get Date from Edit text
        String[] timeSplit = etTimeMorning.getText().toString().split(":");
        mHour = Integer.parseInt(timeSplit[0]);
        mMinute = Integer.parseInt(timeSplit[1]);

        // Show time picker dialog
        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                .setOnTimeSetListener(EditMedActivity.this)
                .setStartTime(mHour, mMinute)
                .setDoneText("ตกลง")
                .setCancelText("ยกเลิก");
        rtpd.show(getSupportFragmentManager(), FRAG_TAG_TIME_PICKER);
    }

    public void setDate(View view) {
        // Get Date from Edit text
        String[] dateSplit = etDateEnd.getText().toString().split("/");
        mYear = Integer.parseInt(dateSplit[2]);
        mMonth = Integer.parseInt(dateSplit[1]);
        mDay = Integer.parseInt(dateSplit[0]);

        // Show Calendar date picker dialog
        CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                .setOnDateSetListener(EditMedActivity.this)
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setPreselectedDate(mYear-543, mMonth-1, mDay)
                .setDoneText("ตกลง")
                .setCancelText("ยกเลิก");
        cdp.show(getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);
    }



    public void backBtn(View view) {
        finish();
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        etDateEnd.setText(String.format("%02d", dayOfMonth) + "/" + String.format("%02d", monthOfYear+1) + "/" + (year+543));
    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        etTimeMorning.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));
    }

    public void setDays(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(EditMedActivity.this);
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

    public void checkRadioBtn(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.everyAlert:
                if (checked)
                    btnCustomDays.setEnabled(false);
                break;
            case R.id.customAlert:
                if (checked)
                    btnCustomDays.setEnabled(true);
                break;
        }
    }
}
