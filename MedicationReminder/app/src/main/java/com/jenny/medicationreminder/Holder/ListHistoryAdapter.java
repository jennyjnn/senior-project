package com.jenny.medicationreminder.Holder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import com.jenny.medicationreminder.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

public class ListHistoryAdapter extends RecyclerView.Adapter<ListHistoryAdapter.ViewHolder> {

    private List<ListMedHistory> mHistory;
    private Context context;
    private static final String USER_PREFS = "userStatus";

    public ListHistoryAdapter(Context context, List<ListMedHistory> mHistory) {
        this.mHistory = mHistory;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvMedNameHist, tvMedPropertyHist, tvMedDoseHist;
        public Button btnHistWeeks, btnHistMonths;

        public ViewHolder(View itemView) {
            super(itemView);
            tvMedNameHist = itemView.findViewById(R.id.tvMedNameHist);
            tvMedPropertyHist = itemView.findViewById(R.id.tvMedPropertyHist);
            tvMedDoseHist = itemView.findViewById(R.id.tvMedDoseHist);
            btnHistWeeks = itemView.findViewById(R.id.btnHistWeeks);
            btnHistMonths = itemView.findViewById(R.id.btnHistMonths);
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListMedHistory medHistory = mHistory.get(position);
        String medName = medHistory.getMedName();
        String medProperty = medHistory.getMedProperty();
        String medDose = medHistory.getMedDose();

        holder.tvMedNameHist.setText(medName);
        holder.tvMedPropertyHist.setText(medProperty);
        holder.tvMedDoseHist.setText(medDose);

    }

    @Override
    public int getItemCount() {
        return mHistory.size();
    }


}
