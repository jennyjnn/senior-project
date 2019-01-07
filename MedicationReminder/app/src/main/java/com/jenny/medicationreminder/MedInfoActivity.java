package com.jenny.medicationreminder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.jenny.medicationreminder.fragment.MedInfoFragment;

public class MedInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_info);

        if (savedInstanceState == null) {
            // First Created
            // Place Fragment here
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainerMedInfo, new MedInfoFragment())
                    .commit();
        }
    }
}
