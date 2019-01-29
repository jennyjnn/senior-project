package com.jenny.medicationreminder.Holder;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jenny.medicationreminder.EditMedActivity;
import com.jenny.medicationreminder.MedInfoActivity;
import com.jenny.medicationreminder.Model.ListMed;
import com.jenny.medicationreminder.Model.ListMedHistory;
import com.jenny.medicationreminder.Model.Med_Record;
import com.jenny.medicationreminder.Model.Medicine;
import com.jenny.medicationreminder.R;
import com.jenny.medicationreminder.fragment.HistoryMonthFragment;
import com.jenny.medicationreminder.fragment.HistoryWeekFragment;
import com.jenny.medicationreminder.fragment.ListMedFragment;
import com.jenny.medicationreminder.fragment.RegisterFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

public class ListHistoryAdapter extends RecyclerView.Adapter<ListHistoryAdapter.ViewHolder> {

    private List<ListMedHistory> mHistory;
    private Context context;

    public ListHistoryAdapter(Context context, List<ListMedHistory> mHistory) {
        this.mHistory = mHistory;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvMedNameHist, tvMedPropertyHist, tvMedDoseHist;
        public Button btnHistWeeks, btnHistMonths;
        public CardView cvHistoryList;
        public ProgressDialog progressDialog;

        public ViewHolder(View itemView) {
            super(itemView);
            tvMedNameHist = itemView.findViewById(R.id.tvMedNameHist);
            tvMedPropertyHist = itemView.findViewById(R.id.tvMedPropertyHist);
            tvMedDoseHist = itemView.findViewById(R.id.tvMedDoseHist);
            btnHistWeeks = itemView.findViewById(R.id.btnHistWeeks);
            btnHistMonths = itemView.findViewById(R.id.btnHistMonths);
            cvHistoryList = itemView.findViewById(R.id.cvHistoryList);

            // progress dialog
            progressDialog = new ProgressDialog(itemView.getContext());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("กรุณารอสักครู่");
            progressDialog.show();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_med_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final ListMedHistory medHistory = mHistory.get(position);
        String medID = medHistory.getMedID();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference medRef = database.getReference("Medicine");
        medRef.orderByKey().equalTo(medID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Medicine medicine = snapshot.getValue(Medicine.class);
                    String medName = medicine.getMed_name();
                    String medProperty = medicine.getMed_property();
                    holder.tvMedNameHist.setText(medName);
                    if (medProperty == null) {
                        holder.tvMedPropertyHist.setVisibility(View.GONE);
                    } else {
                        holder.tvMedPropertyHist.setText(medProperty);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference recordRef = database.getReference("Med_Record");
        recordRef.orderByChild("med_id").equalTo(medID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Med_Record med_record = snapshot.getValue(Med_Record.class);
                    String medDose = med_record.getMedRec_dose();
                    holder.tvMedDoseHist.setText(medDose);
                }
                holder.cvHistoryList.setVisibility(View.VISIBLE);
                holder.progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.btnHistWeeks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                HistoryWeekFragment historyWeekFragment = new HistoryWeekFragment();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentContainerHistory, historyWeekFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        holder.btnHistMonths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                HistoryMonthFragment historyMonthFragment = new HistoryMonthFragment();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentContainerHistory, historyMonthFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mHistory.size();
    }


}
