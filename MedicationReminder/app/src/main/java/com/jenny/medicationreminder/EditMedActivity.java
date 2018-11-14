package com.jenny.medicationreminder;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import junit.runner.Version;

import java.util.Calendar;


public class EditMedActivity extends AppCompatActivity {

    CardView cvAppbar;
    CheckBox cbMorning, cbLunch, cbEveing, cbBeforeBed;
    EditText etTimeMorning, etDateStart;
    private int mYear, mMonth, mDay, mHour, mMinute;

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
        etDateStart = findViewById(R.id.etDateStart);

        cbMorning = findViewById(R.id.cbMorning);
        cbLunch = findViewById(R.id.cbLunch);
        cbEveing = findViewById(R.id.cbEveing);
        cbBeforeBed = findViewById(R.id.cbBeforeBed);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/th_sarabun.ttf");
        cbMorning.setTypeface(typeface, Typeface.BOLD);
        cbLunch.setTypeface(typeface, Typeface.BOLD);
        cbEveing.setTypeface(typeface, Typeface.BOLD);
        cbBeforeBed.setTypeface(typeface, Typeface.BOLD);


    }

    public void setTime(View view) {

        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                etTimeMorning.setText(hourOfDay + ":" + minute);
            }
        }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    public void setDate(View view) {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(EditMedActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                etDateStart.setText(dayOfMonth + "/" + (month+1) + "/" + year);
            }
        }, mYear, mMonth, mDay);
//        datePickerDialog.setVersion(DatePickerDialog, Version.VERSION_2);
        datePickerDialog.show();
    }


    public void backBtn(View view) {
        finish();
    }
}
