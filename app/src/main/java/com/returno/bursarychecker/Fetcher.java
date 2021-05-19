package com.returno.bursarychecker;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Fetcher {

    private ValueEventListener eventListener,genderListener;
    public void fetchApplications(CompleteListener listener){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("requests");
eventListener=new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {

        List<Upload> uploadList=new ArrayList<>();
for (DataSnapshot dataSnapshot:snapshot.getChildren()){
    Log.e("data",dataSnapshot.toString());
    Upload upload=dataSnapshot.getValue(Upload.class);
    uploadList.add(upload);
}
Log.e("uploadlist",uploadList.toString());
reference.removeEventListener(eventListener);
listener.onUploadFetched(uploadList);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
};
reference.addListenerForSingleValueEvent(eventListener);
    }

    public void fetchByGender(CompleteListener listener){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("requests");
        genderListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<Upload> femaledList=new ArrayList<>();
                List<Upload> maleUpload=new ArrayList<>();
                List<Upload> othersUpload=new ArrayList<>();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Log.e("data",dataSnapshot.toString());
                    Upload upload=dataSnapshot.getValue(Upload.class);
                    if (upload.getGender().equals("Male")){
                        maleUpload.add(upload);
                    }
                    if (upload.getGender().equals("Female")){
                        femaledList.add(upload);
                    }
                    if (upload.getGender().equals("Others")){
                        othersUpload.add(upload);
                    }
                }

                reference.removeEventListener(genderListener);
                listener.onGenderGroup(maleUpload.size(),femaledList.size(),othersUpload.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        reference.addListenerForSingleValueEvent(genderListener);
    }
}
