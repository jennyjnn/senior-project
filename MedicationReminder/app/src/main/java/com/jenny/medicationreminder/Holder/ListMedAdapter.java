package com.jenny.medicationreminder.Holder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jenny.medicationreminder.EditMedActivity;
import com.jenny.medicationreminder.ListMedActivity;
import com.jenny.medicationreminder.MedInfoActivity;
import com.jenny.medicationreminder.MenuActivity;
import com.jenny.medicationreminder.Model.ListMed;
import com.jenny.medicationreminder.R;

import java.util.List;

public class ListMedAdapter extends RecyclerView.Adapter<ListMedAdapter.ViewHolder> {

    private List<ListMed> mMeds;
    private Context context;

    public ListMedAdapter(Context context, List<ListMed> mMeds) {
        this.mMeds = mMeds;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvMedName_list, tvMedProp_list, tvMedDes_list, tvMedTime_list;
        public Button btnViewMed, btnEditMed;
        public ViewHolder(View itemView) {
            super(itemView);

            tvMedName_list = itemView.findViewById(R.id.tvMedName_list);
            tvMedProp_list = itemView.findViewById(R.id.tvMedProp_list);
            tvMedDes_list = itemView.findViewById(R.id.tvMedDes_list);
            tvMedTime_list = itemView.findViewById(R.id.tvMedTime_list);

            btnViewMed = itemView.findViewById(R.id.btnViewMed);
            btnEditMed = itemView.findViewById(R.id.btnEditMed);
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListMed med = mMeds.get(position);

        final String medName = med.getNameMed();

        holder.tvMedName_list.setText(medName);
        holder.tvMedProp_list.setText(med.getProperties());
        holder.tvMedDes_list.setText(med.getDescriptions());
        holder.tvMedTime_list.setText(med.getTime());

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
                context.startActivity(intentEdit);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mMeds.size();
    }


}
