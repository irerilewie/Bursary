package com.returno.bursarychecker;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminApplicationsDialog extends DialogFragment {

    private RecyclerView recyclerView;
    private AdminAdapter adapter;
    private static List<Upload> uploadList=new ArrayList<>();
    private ValueEventListener rejectListener,acceptListener;
    private MaterialToolbar toolbar;

    public static void showDialog(FragmentManager manager, List<Upload> uploads){
        AdminApplicationsDialog dialog=new AdminApplicationsDialog();
        uploadList.clear();
        uploadList.addAll(uploads);
        dialog.show(manager,"Admin Applications");

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_BursaryChecker_FullScreenDialog);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog=getDialog();
        if (dialog!=null){
            int width= ViewGroup.LayoutParams.MATCH_PARENT;
            int height=ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width,height);
            dialog.getWindow().setWindowAnimations(R.style.Theme_BursaryChecker_Slide);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.admin_applications,container,false);
recyclerView=view.findViewById(R.id.recycler);
toolbar=view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
recyclerView.setHasFixedSize(true);
recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

adapter=new AdminAdapter(getActivity(), uploadList, new ClickListener() {
    @Override
    public void onclick(Upload upload) {
new MaterialAlertDialogBuilder(getActivity())
        .setTitle("Pick Action")
        .setItems(new String[]{"Accept", "Reject"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
if (which==1){
    reject(upload);
    dialog.dismiss();
}
if (which==0){
accept(upload);
dialog.dismiss();
}
            }
        }).create()
        .show();
    }
});

        return view;

    }

    private void accept(Upload upload) {
        Dialog progressDialog=new Dialog(getActivity());
        progressDialog.setContentView(R.layout.progressdialog);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.show();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("requests");
        acceptListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
for (DataSnapshot dataSnapshot:snapshot.getChildren()){
    if (dataSnapshot.child("uploadId").getValue().toString().equals(upload.getUploadId())){
        DatabaseReference updateRef=reference.child(dataSnapshot.getKey()).child("status");
        updateRef.setValue("Accepted")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(),"Accepted",Toast.LENGTH_LONG).show();
                        reference.removeEventListener(acceptListener);
                        getActivity().recreate();
                    }
                });
    }
}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
progressDialog.dismiss();
                Toast.makeText(getActivity(),"An Error Occured",Toast.LENGTH_LONG).show();

            }
        };
        reference.addListenerForSingleValueEvent(acceptListener);

    }

    private void reject(Upload upload) {
        Dialog progressDialog=new Dialog(getActivity());
        progressDialog.setContentView(R.layout.progressdialog);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.show();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("requests");
        rejectListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if (dataSnapshot.child("uploadId").getValue().toString().equals(upload.getUploadId())){
                        DatabaseReference updateRef=reference.child(dataSnapshot.getKey()).child("status");
                        updateRef.setValue("Rejected")
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getActivity(),"Rejected",Toast.LENGTH_LONG).show();
                                        reference.removeEventListener(rejectListener);
                                        getActivity().recreate();
                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(),"An Error Occured",Toast.LENGTH_LONG).show();

            }
        };
        reference.addListenerForSingleValueEvent(rejectListener);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
recyclerView.setAdapter(adapter);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.admin_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.genderMenu){
            Dialog progressDialog=new Dialog(getActivity());
            progressDialog.setContentView(R.layout.progressdialog);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.show();

            new Fetcher().fetchByGender(new CompleteListener() {
                @Override
                public void onGenderGroup(int maleCount, int femaleCount, int others) {
                    Dialog dialog=new Dialog(getActivity());
                    dialog.setContentView(R.layout.graph_layouts);
                    PieChart pieChart=dialog.findViewById(R.id.genderCharts);

                    pieChart.setDrawHoleEnabled(true);
                    pieChart.setHoleRadius(30f);

                    ArrayList<PieEntry> entries=new ArrayList<>();
                    entries.add(new PieEntry(maleCount,"Male"));
                    entries.add(new PieEntry(femaleCount,"Female"));
                    entries.add(new PieEntry(others,"Others"));
                    progressDialog.dismiss();

                    PieDataSet dataSet=new PieDataSet(entries,"Applications By Gender");
                    dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                    PieData data=new PieData(dataSet);
                    data.setDrawValues(true);

                    pieChart.setData(data);
                    pieChart.invalidate();


                    pieChart.animate();
                    dialog.show();
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!FirebaseAuth.getInstance().getCurrentUser().getEmail().equals("muragelewy@gmail.com")){
            startActivity(new Intent(getActivity(),MainActivity.class));
        }
    }
}
