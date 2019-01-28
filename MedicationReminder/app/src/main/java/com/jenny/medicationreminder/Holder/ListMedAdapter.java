package com.jenny.medicationreminder.Holder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jenny.medicationreminder.EditMedActivity;
import com.jenny.medicationreminder.ListMedActivity;
import com.jenny.medicationreminder.MainActivity;
import com.jenny.medicationreminder.MedInfoActivity;
import com.jenny.medicationreminder.MenuActivity;
import com.jenny.medicationreminder.Model.ListMed;
import com.jenny.medicationreminder.Model.Med_Record;
import com.jenny.medicationreminder.R;
import com.jenny.medicationreminder.fragment.ListMedFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import mehdi.sakout.fancybuttons.FancyButton;

public class ListMedAdapter extends RecyclerView.Adapter<ListMedAdapter.ViewHolder> {

    private List<ListMed> mMeds;
    private Context context;
    private static final String USER_PREFS = "userStatus";

    public ListMedAdapter(Context context, List<ListMed> mMeds) {
        this.mMeds = mMeds;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvMedName_list, tvMedProp_list, tvMedDes_list, tvMedStatus_list;
        public Button btnViewMed, btnEditMed, btnDelMed;
        public ImageView imgStatus;
        public LinearLayout linearListMed;
        public FancyButton btnTakeMed;

        public ViewHolder(View itemView) {
            super(itemView);

            tvMedName_list = itemView.findViewById(R.id.tvMedName_list);
            tvMedProp_list = itemView.findViewById(R.id.tvMedProp_list);
            tvMedDes_list = itemView.findViewById(R.id.tvMedDes_list);
            tvMedStatus_list = itemView.findViewById(R.id.tvMedStatus_list);
            imgStatus = itemView.findViewById(R.id.imgStatus);
            linearListMed = itemView.findViewById(R.id.linearListMed);

            btnViewMed = itemView.findViewById(R.id.btnViewMed);
            btnEditMed = itemView.findViewById(R.id.btnEditMed);
            btnDelMed = itemView.findViewById(R.id.btnDelMed);
            btnTakeMed = itemView.findViewById(R.id.btnTakeMed);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.medication_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final ListMed med = mMeds.get(position);

        final String medName = med.getNameMed();
        final String medID = med.getMedID();
        final String medRecordID = med.getMedRecordID();
        String dateMedList = med.getDateMedList();

        final Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String today = dateFormat.format(currentTime);

        holder.tvMedName_list.setText(medName);
        holder.tvMedProp_list.setText(med.getProperties());
        holder.tvMedDes_list.setText(med.getDescriptions());
        if (med.getGetMed() == true) {
            holder.btnTakeMed.setVisibility(View.GONE);
            holder.linearListMed.setBackgroundResource(R.drawable.taked_med_border);
        } else {
            if (!dateMedList.equals(today)) {
                holder.imgStatus.setImageResource(R.drawable.circle_status_red);
                holder.tvMedStatus_list.setText("ยังไม่ได้รับยา");
                holder.btnTakeMed.setVisibility(View.GONE);
            } else {
                holder.imgStatus.setVisibility(View.GONE);
                holder.tvMedStatus_list.setVisibility(View.GONE);
            }
        }

        holder.btnTakeMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateFormat timeFormat = new SimpleDateFormat("HH:mm");
                final String timeNow = timeFormat.format(currentTime);

                AlertDialog.Builder alertTakeMed = new AlertDialog.Builder(v.getContext());
                alertTakeMed.setMessage("คุณรับยา " + medName + " แล้วใช่หรือไม่ ?");
                alertTakeMed.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        final DatabaseReference medRef = database.getReference("Med_Record");
                        medRef.orderByKey().equalTo(medRecordID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                medRef.child(medRecordID).child("medRec_getTime").setValue(timeNow);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
                alertTakeMed.setNeutralButton("ไม่ใช่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog alertDialog = alertTakeMed.create();
                alertDialog.show();
            }
        });

        holder.btnViewMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MedInfoActivity.class);
                intent.putExtra("medName", medName);
                context.startActivity(intent);
            }
        });

        holder.btnEditMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEdit = new Intent(v.getContext(), EditMedActivity.class);
                intentEdit.putExtra("medName", medName);
                intentEdit.putExtra("topic", "แก้ไขแจ้งเตือน");
                intentEdit.putExtra("medID", medID);
                intentEdit.putExtra("medProperty", holder.tvMedProp_list.getText());
                context.startActivity(intentEdit);
            }
        });

         holder.btnDelMed.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(final View v) {
                 SharedPreferences prefUser = v.getContext().getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);;
                 final String keyUser = prefUser.getString("keyUser", "no user");

                 AlertDialog.Builder alertDelete = new AlertDialog.Builder(v.getContext());
                 alertDelete.setMessage("คุณต้องการลบการแจ้งเตือนสำหรับ " + medName + " ใช่หรือไม่ ?");
                 alertDelete.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         // progress dialog
                         final ProgressDialog progressDialog = new ProgressDialog(v.getContext());
                         progressDialog.setCancelable(false);
                         progressDialog.setMessage("กรุณารอสักครู่");
                         progressDialog.show();

                         FirebaseDatabase database = FirebaseDatabase.getInstance();
                         final DatabaseReference medRef = database.getReference("Med_Record");
                         medRef.orderByChild("user_id").equalTo(keyUser).addListenerForSingleValueEvent(new ValueEventListener() {
                             @Override
                             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                 for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                     Med_Record med_record = snapshot.getValue(Med_Record.class);
                                     if (med_record.getMed_id().equals(medID) && med_record.getMedRec_getTime().equals("none")) {
                                         String mrID = snapshot.getKey();
                                         medRef.child(mrID).child("medRec_getTime").setValue("false");
                                     }
                                 }
                                 progressDialog.dismiss();
                             }

                             @Override
                             public void onCancelled(@NonNull DatabaseError databaseError) {

                             }
                         });
                     }
                 });

                 alertDelete.setNeutralButton("ไม่ใช่", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                     }
                 });

                 AlertDialog alertDialog = alertDelete.create();
                 alertDialog.show();

             }
         });

    }

    @Override
    public int getItemCount() {
        return mMeds.size();
    }


}
