package com.jenny.medicationreminder;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.jenny.medicationreminder.fragment.EditMedFragment;
import com.jenny.medicationreminder.fragment.ViewProfileFragment;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;


public class EditMedActivity extends AppCompatActivity
        implements CalendarDatePickerDialogFragment.OnDateSetListener, RadialTimePickerDialogFragment.OnTimeSetListener {

    EditText etTimeMorning, etDateEnd, etMedName;
    FancyButton btnCustomDays;

    // Alert Dialog
    AlertDialog.Builder alertTime;
    AlertDialog dialogAlertTime;

    private int mHour, mMinute;

    public static final String FRAG_TAG_TIME_PICKER = "fragment_time_picker_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_med);

        JodaTimeAndroid.init(this);

        if (savedInstanceState == null) {
            // First Created
            // Place Fragment here
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainerEditMed, new EditMedFragment())
                    .commit();
        }

        initInstances();
    }


    private void initInstances() {
        alertTime = new AlertDialog.Builder(EditMedActivity.this);
        alertTime.setCancelable(false);
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


    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        etDateEnd = findViewById(R.id.etDateEnd);
        etDateEnd.setText(String.format("%02d", dayOfMonth) + "/" + String.format("%02d", monthOfYear+1) + "/" + (year));
    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
//        if (hourOfDay < 4 || hourOfDay > 10) {
//            alertTime.setTitle("เวลาแจ้งเตือนไม่ถูกต้อง");
//            alertTime.setMessage("กรุณาเลือกเวลาแจ้งเตือนระหว่าง\n04:00 - 10.59 น.");
//            alertTime.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    dialog.dismiss();
//                    setTime(etTimeMorning);
//                }
//            });
//            dialogAlertTime = alertTime.create();
//            dialogAlertTime.show();
//        } else {
//            etTimeMorning = findViewById(R.id.etTimeMorning);
//            etTimeMorning.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));
//        }
    }
}
