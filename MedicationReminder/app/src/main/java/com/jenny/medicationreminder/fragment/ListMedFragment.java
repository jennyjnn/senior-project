package com.jenny.medicationreminder.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jenny.medicationreminder.Holder.ListMedAdapter;
import com.jenny.medicationreminder.Model.ListMed;
import com.jenny.medicationreminder.Model.Med_Record;
import com.jenny.medicationreminder.Model.Medicine;
import com.jenny.medicationreminder.R;

import java.util.ArrayList;
import java.util.List;

public class ListMedFragment extends Fragment implements RadialTimePickerDialogFragment.OnTimeSetListener {

    CardView cvAppBar;
    RecyclerView rcListMedBefore, rcListMedAfter;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManagerBefore, mLayoutManagerAfter;
    ImageView btnBack;
    ProgressDialog progressDialog;
    TextView tvTime, tvBeforeMeal, tvBeforeTime, tvAfterTime;
    LinearLayout linearBefore, linearAfter;
    View vLine;

    FirebaseDatabase database;
    DatabaseReference medRecordRef;
    DatabaseReference medRef;
    DatabaseReference notiTimeRef;

    ListMedAdapter adapterBefore, adapterAfter;

    AlertDialog.Builder alertTimeNoti;
    AlertDialog dialogAlertNotification;

    String nameMed = new String();
    String properties;
    String descriptions;
    String date, time;
    String notiTime;
    String timeBefAft;
    int countBefore = 0;
    int countAfter = 0;

    List<ListMed> datasetBefore, datasetAfter;
    ListMed listMed;

    SharedPreferences prefUser, prefNotiTime;
    SharedPreferences.Editor editorNotiTime;
    private static final String USER_PREFS = "userStatus";
    private static final String NOTI_PREFS = "notiTime";
    String keyUser;

    String morning_bf, morning_af, noon_bf, noon_af, evening_bf, evening_af, bed_bf;

    public static final String FRAG_TAG_TIME_PICKER = "fragment_time_picker_name";
    private int mHour, mMinute;

    public ListMedFragment() {
        super();
    }

    public static ListMedFragment newInstance(String date, String time) {
        ListMedFragment fragment = new ListMedFragment();
        Bundle args = new Bundle();
        args.putString("date", date);
        args.putString("time", time);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_med, container, false);
        initInstances(rootView);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        date = getArguments().getString("date");
        time = getArguments().getString("time");
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here

        tvTime = rootView.findViewById(R.id.tvTime);
        tvTime.setText(time);

        tvAfterTime = rootView.findViewById(R.id.tvAfterTime);
        tvBeforeTime = rootView.findViewById(R.id.tvBeforeTime);
        tvBeforeMeal = rootView.findViewById(R.id.tvBeforeMeal);
        linearAfter = rootView.findViewById(R.id.linearAfter);
        linearBefore = rootView.findViewById(R.id.linearBefore);
        vLine = rootView.findViewById(R.id.vLine);

        // set color on app bar
        cvAppBar = rootView.findViewById(R.id.cvAppBarList);
        cvAppBar.setBackgroundResource(R.drawable.bg_appbar);

        prefUser = getActivity().getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        keyUser = prefUser.getString("keyUser", "no user");

        // RecyclerView
        rcListMedBefore = rootView.findViewById(R.id.rcListMedBefore);
        rcListMedAfter = rootView.findViewById(R.id.rcListMedAfter);

        mLayoutManagerBefore = new LinearLayoutManager(getContext());
        rcListMedBefore.setLayoutManager(mLayoutManagerBefore);
        mLayoutManagerAfter = new LinearLayoutManager(getContext());
        rcListMedAfter.setLayoutManager(mLayoutManagerAfter);

        if (time.equals("เช้า")) {
            notiTime = "morning";
        } else if (time.equals("กลางวัน")) {
            notiTime = "noon";
        } else if (time.equals("เย็น")) {
            notiTime = "evening";
        } else {
            notiTime = "bed";
            tvBeforeMeal.setText("ก่อนนอน");
        }

        tvBeforeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeBefAft = "_bf";
                setTime(tvBeforeTime.getText().toString());
            }
        });

        tvAfterTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeBefAft = "_af";
                setTime(tvAfterTime.getText().toString());
            }
        });

        btnBack = rootView.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        getNotiTime();
    }

    private void setTime(String time) {
        // Get Date from Edit text
        String[] timeSplit = time.split(":");
        mHour = Integer.parseInt(timeSplit[0]);
        mMinute = Integer.parseInt(timeSplit[1]);

        // Show time picker dialog
        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                .setOnTimeSetListener(ListMedFragment.this)
                .setStartTime(mHour, mMinute)
                .setDoneText("ตกลง")
                .setCancelText("ยกเลิก");
        rtpd.show(getFragmentManager(), FRAG_TAG_TIME_PICKER);
    }

    private void getNotiTime() {
        // Progress Dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("กรุณารอสักครู่");
        progressDialog.show();

        prefNotiTime = getContext().getSharedPreferences(NOTI_PREFS, Context.MODE_PRIVATE);
        morning_bf = prefNotiTime.getString("morning_bf", "07:30");
        morning_af = prefNotiTime.getString("morning_af", "08:30");
        noon_bf = prefNotiTime.getString("noon_bf", "11:30");
        noon_af = prefNotiTime.getString("noon_af", "12:30");
        evening_bf = prefNotiTime.getString("evening_bf", "17:00");
        evening_af = prefNotiTime.getString("evening_af", "18:00");
        bed_bf = prefNotiTime.getString("bed_bf", "21:00");

        switch (notiTime) {
            case "morning":
                tvBeforeTime.setText(morning_bf);
                tvAfterTime.setText(morning_af);
                break;
            case "noon":
                tvBeforeTime.setText(noon_bf);
                tvAfterTime.setText(noon_af);
                break;
            case "evening":
                tvBeforeTime.setText(evening_bf);
                tvAfterTime.setText(evening_af);
                break;
            case "bed":
                tvBeforeTime.setText(bed_bf);
        }

        queryMedList();
    }

    private void queryMedList() {
        database = FirebaseDatabase.getInstance();
        medRecordRef = database.getReference("Med_Record");
        Query query = medRecordRef.orderByChild("user_id").equalTo(keyUser);
        medRef = database.getReference("Medicine");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                datasetBefore = new ArrayList<>();
                datasetAfter= new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Med_Record medRecord = snapshot.getValue(Med_Record.class);
                    String dateMed = medRecord.getMedRec_notiDate();
                    String timeMed = medRecord.getMedRec_notiTime();
                    if (dateMed.equals(date) && timeMed.equals(time)) {
                        if (!medRecord.getMedRec_getTime().equals("false")) {
                            medRef.orderByKey().equalTo(medRecord.getMed_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        Medicine medicine = snapshot.getValue(Medicine.class);

                                        nameMed = medicine.getMed_name();
                                        properties = medicine.getMed_property();
                                        if (medicine.getMed_type() == null) {
                                            descriptions = medRecord.getMedRec_dose();
                                        } else {
                                            descriptions = medicine.getMed_type() + " " + medRecord.getMedRec_dose();
                                        }
                                        listMed = new ListMed();
                                        listMed.setNameMed(nameMed);
                                        listMed.setProperties(properties);
                                        listMed.setDescriptions(descriptions);
                                        listMed.setMedID(medRecord.getMed_id());
                                        if (medRecord.getMedRec_getTime().equals("none")) {
                                            listMed.setGetMed(false);
                                        } else {
                                            listMed.setGetMed(true);
                                        }

                                        String befAft = medRecord.getMedRec_BefAft();
                                        if (befAft.equals("ก่อนอาหาร") || befAft.equals("ก่อนนอน")) {
                                            datasetBefore.add(listMed);
                                            linearBefore.setVisibility(View.VISIBLE);
                                            countBefore++;
                                        } else {
                                            datasetAfter.add(listMed);
                                            linearAfter.setVisibility(View.VISIBLE);
                                            countAfter++;
                                        }
                                    }
                                    if (countBefore > 0 && countAfter > 0) {
                                        vLine.setVisibility(View.VISIBLE);
                                    }
                                    adapterBefore = new ListMedAdapter(getContext(), datasetBefore);
                                    rcListMedBefore.setAdapter(adapterBefore);
                                    adapterAfter = new ListMedAdapter(getContext(), datasetAfter);
                                    rcListMedAfter.setAdapter(adapterAfter);

                                    progressDialog.dismiss();
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void refreshMedList() {
        // Reload current fragment
        Fragment frg = null;
        frg = getFragmentManager().findFragmentByTag("ListMedFragment");
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
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

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        final String newTime = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute);

        int hourMorningBf = Integer.parseInt(morning_bf.substring(0,2));
        int minuteMorningBf = Integer.parseInt(morning_bf.substring(3));
        int hourMorningAf = Integer.parseInt(morning_af.substring(0,2));
        int minuteMorningAf = Integer.parseInt(morning_af.substring(3));
        int hourNoonBf = Integer.parseInt(noon_bf.substring(0,2));
        int minuteNoonBf = Integer.parseInt(noon_bf.substring(3));
        int hourNoonAf = Integer.parseInt(noon_af.substring(0,2));
        int minuteNoonAf = Integer.parseInt(noon_af.substring(3));
        int hourEveningBf = Integer.parseInt(evening_bf.substring(0,2));
        int minuteEveningBf = Integer.parseInt(evening_bf.substring(3));
        int hourEveningAf = Integer.parseInt(evening_af.substring(0,2));
        int minuteEveningAf = Integer.parseInt(evening_af.substring(3));
        int hourBedBf = Integer.parseInt(bed_bf.substring(0,2));
        int minuteBedBf = Integer.parseInt(bed_bf.substring(3));

        alertTimeNoti = new AlertDialog.Builder(getContext());
        alertTimeNoti.setCancelable(false);
        alertTimeNoti.setTitle("เวลาแจ้งเตือนไม่ถูกต้อง");

        if (notiTime.equals("morning")) {
            if (timeBefAft.equals("_bf")) {
                if (hourOfDay == hourMorningAf) {
                    if (minute >= minuteMorningAf) {
                        alertTimeNoti.setMessage("กรุณาตั้งเวลาไม่เกินเวลาแจ้งเตือนหลังอาหารเช้า");
                        alertTimeWrong(newTime);
                    } else {
                        updateNotificaction(newTime);
                    }
                } else if (hourOfDay > hourMorningAf) {
                    alertTimeNoti.setMessage("กรุณาตั้งเวลาไม่เกินเวลาแจ้งเตือนหลังอาหารเช้า");
                    alertTimeWrong(newTime);
                } else if (hourOfDay < 4) {
                    alertTimeNoti.setMessage("กรุณาตั้งเวลาแจ้งเตือนตั้งแต่\n04:00 น. เป็นต้นไป");
                    alertTimeWrong(newTime);
                } else {
                    updateNotificaction(newTime);
                }
            } else {
                if (hourOfDay == hourNoonBf) {
                    if (minute >= minuteNoonBf) {
                        alertTimeNoti.setMessage("กรุณาตั้งเวลาไม่เกินเวลาแจ้งเตือนก่อนอาหารกลางวัน");
                        alertTimeWrong(newTime);
                    } else {
                        updateNotificaction(newTime);
                    }
                } else if (hourOfDay > hourNoonBf) {
                    alertTimeNoti.setMessage("กรุณาตั้งเวลาไม่เกินเวลาแจ้งเตือนก่อนอาหารกลางวัน");
                    alertTimeWrong(newTime);
                } else if (hourOfDay == hourMorningBf) {
                    if (minute <= minuteMorningBf) {
                        alertTimeNoti.setMessage("กรุณาตั้งเวลาหลังจากเวลาแจ้งเตือนก่อนอาหารเช้า");
                        alertTimeWrong(newTime);
                    } else {
                        updateNotificaction(newTime);
                    }
                } else if (hourOfDay < hourMorningBf) {
                    alertTimeNoti.setMessage("กรุณาตั้งเวลาหลังจากเวลาแจ้งเตือนก่อนอาหารเช้า");
                    alertTimeWrong(newTime);
                }
                else {
                    updateNotificaction(newTime);
                }
            }
        } else if (notiTime.equals("noon")) {
            if (timeBefAft.equals("_bf")) {
                if (hourOfDay == hourNoonAf) {
                    if (minute >= minuteNoonAf) {
                        alertTimeNoti.setMessage("กรุณาตั้งเวลาไม่เกินเวลาแจ้งเตือนหลังอาหารกลางวัน");
                        alertTimeWrong(newTime);
                    } else {
                        updateNotificaction(newTime);
                    }
                } else if (hourOfDay > hourNoonAf) {
                    alertTimeNoti.setMessage("กรุณาตั้งเวลาไม่เกินเวลาแจ้งเตือนหลังอาหารกลางวัน");
                    alertTimeWrong(newTime);
                } else if (hourOfDay == hourMorningAf) {
                    if (minute <= minuteMorningAf) {
                        alertTimeNoti.setMessage("กรุณาตั้งเวลาหลังจากเวลาแจ้งเตือนหลังอาหารเช้า");
                        alertTimeWrong(newTime);
                    } else {
                        updateNotificaction(newTime);
                    }
                } else if (hourOfDay < hourMorningBf) {
                    alertTimeNoti.setMessage("กรุณาตั้งเวลาหลังจากเวลาแจ้งเตือนหลังอาหารเช้า");
                    alertTimeWrong(newTime);
                }
                else {
                    updateNotificaction(newTime);
                }
            } else {
                if (hourOfDay == hourEveningBf) {
                    if (minute >= minuteEveningBf) {
                        alertTimeNoti.setMessage("เกรุณาตั้งเวลาไม่เกินเวลาแจ้งเตือนก่อนอาหารเย็น");
                        alertTimeWrong(newTime);
                    } else {
                        updateNotificaction(newTime);
                    }
                } else if (hourOfDay > hourEveningBf) {
                    alertTimeNoti.setMessage("กรุณาตั้งเวลาไม่เกินเวลาแจ้งเตือนก่อนอาหารเย็น");
                    alertTimeWrong(newTime);
                } else if (hourOfDay == hourNoonBf) {
                    if (minute <= minuteNoonBf) {
                        alertTimeNoti.setMessage("กรุณาตั้งเวลาหลังจากเวลาแจ้งเตือนก่อนอาหารกลางวัน");
                        alertTimeWrong(newTime);
                    } else {
                        updateNotificaction(newTime);
                    }
                } else if (hourOfDay < hourNoonBf) {
                    alertTimeNoti.setMessage("กรุณาตั้งเวลาหลังจากเวลาแจ้งเตือนก่อนอาหารกลางวัน");
                    alertTimeWrong(newTime);
                }
                else {
                    updateNotificaction(newTime);
                }
            }
        } else if (notiTime.equals("evening")) {
            if (timeBefAft.equals("_bf")) {
                if (hourOfDay == hourEveningAf) {
                    if (minute >= minuteEveningAf) {
                        alertTimeNoti.setMessage("กรุณาตั้งเวลาไม่เกินเวลาแจ้งเตือนหลังอาหารเย็น");
                        alertTimeWrong(newTime);
                    } else {
                        updateNotificaction(newTime);
                    }
                } else if (hourOfDay > hourEveningAf) {
                    alertTimeNoti.setMessage("กรุณาตั้งเวลาไม่เกินเวลาแจ้งเตือนหลังอาหารเย็น");
                    alertTimeWrong(newTime);
                } else if (hourOfDay == hourNoonAf) {
                    if (minute <= minuteNoonAf) {
                        alertTimeNoti.setMessage("กรุณาตั้งเวลาหลังจากเวลาแจ้งเตือนหลังอาหารกลางวัน");
                        alertTimeWrong(newTime);
                    } else {
                        updateNotificaction(newTime);
                    }
                } else if (hourOfDay < hourNoonAf) {
                    alertTimeNoti.setMessage("กรุณาตั้งเวลาหลังจากเวลาแจ้งเตือนหลังอาหารกลางวัน");
                    alertTimeWrong(newTime);
                }
                else {
                    updateNotificaction(newTime);
                }
            } else {
                if (hourOfDay == hourBedBf) {
                    if (minute >= minuteBedBf) {
                        alertTimeNoti.setMessage("กรุณาตั้งเวลาไม่เกินเวลาแจ้งเตือนก่อนนอน");
                        alertTimeWrong(newTime);
                    } else {
                        updateNotificaction(newTime);
                    }
                } else if (hourOfDay > hourBedBf) {
                    alertTimeNoti.setMessage("กรุณาตั้งเวลาไม่เกินเวลาแจ้งเตือนก่อนนอน");
                    alertTimeWrong(newTime);
                } else if (hourOfDay == hourEveningBf) {
                    if (minute <= minuteEveningBf) {
                        alertTimeNoti.setMessage("กรุณาตั้งเวลาจากเวลาแจ้งเตือนก่อนอาหารเย็น");
                        alertTimeWrong(newTime);
                    } else {
                        updateNotificaction(newTime);
                    }
                } else if (hourOfDay < hourEveningBf) {
                    alertTimeNoti.setMessage("กรุณาตั้งเวลาหลังจากเวลาแจ้งเตือนก่อนอาหารเย็น");
                    alertTimeWrong(newTime);
                }
                else {
                    updateNotificaction(newTime);
                }
            }
        } else {
            if (hourOfDay == hourEveningAf) {
                if (minute <= minuteEveningAf) {
                    alertTimeNoti.setMessage("กรุณาตั้งเวลาหลังจากเวลาแจ้งเตือนหลังอาหารเย็น");
                    alertTimeWrong(newTime);
                } else {
                    updateNotificaction(newTime);
                }
            } else if (hourOfDay < hourEveningAf) {
                alertTimeNoti.setMessage("กรุณาตั้งเวลาหลังจากเวลาแจ้งเตือนหลังอาหารเย็น");
                alertTimeWrong(newTime);
            } else {
                updateNotificaction(newTime);
            }
        }

    }

    private void alertTimeWrong(final String newTime) {
        alertTimeNoti.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                setTime(newTime);
            }
        });
        dialogAlertNotification = alertTimeNoti.create();
        dialogAlertNotification.show();
    }

    private void updateNotificaction(final String newTime) {
        final AlertDialog.Builder alertConfirmTime = new AlertDialog.Builder(getContext());
        alertConfirmTime.setMessage("คุณต้องการเปลี่ยนเวลาการแจ้งเตือนเป็น "
                + newTime + " น. ใช่หรือไม่ ?");
        alertConfirmTime.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                notiTimeRef = database.getReference("User");
                notiTimeRef.orderByKey().equalTo(keyUser).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String editNotification = notiTime + timeBefAft;
                        notiTimeRef.child(keyUser).child(editNotification).setValue(newTime);

                        // Edit Notification time
                        prefNotiTime = getActivity().getSharedPreferences(NOTI_PREFS, Context.MODE_PRIVATE);
                        editorNotiTime = prefNotiTime.edit();
                        editorNotiTime.putString(editNotification, newTime);
                        editorNotiTime.commit();

                        refreshMedList();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        alertConfirmTime.setNeutralButton("ไม่ใช่", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = alertConfirmTime.create();
        alertDialog.show();
    }
}
