package com.returno.bursarychecker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.Holder> {
    private Context context;
    private List<Upload> uploadList;
    private ClickListener listener;

    public AdminAdapter(Context context, List<Upload> uploadList, ClickListener listener) {
        this.context = context;
        this.uploadList = uploadList;
        this.listener=listener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.application_item,parent,false);
        Holder holder=new Holder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
listener.onclick(uploadList.get(holder.getAdapterPosition()));
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Upload upload=uploadList.get(position);
        holder.statusView.setText(upload.getStatus());
        holder.DateView.setText(upload.getAppDate());
        holder.IdView.setText(upload.getUploadId());

    }

    @Override
    public int getItemCount() {
        return uploadList.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
 MaterialTextView IdView,DateView,statusView;
        public Holder(@NonNull View itemView) {
            super(itemView);
            IdView =itemView.findViewById(R.id.applicationId);
            DateView=itemView.findViewById(R.id.applicationDate);
            statusView=itemView.findViewById(R.id.applicationStatus);
        }
    }
}
