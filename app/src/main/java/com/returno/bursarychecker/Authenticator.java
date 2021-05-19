package com.returno.bursarychecker;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Authenticator {
    public void RegisterUser(Context context,User user, Dialog dialog){
        Dialog progressDialog=new Dialog(context);
        progressDialog.setContentView(R.layout.progressdialog);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.show();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.getEmail(),user.getPassword())
                .addOnSuccessListener(authResult -> {
                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users").push();
                    reference.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            Toast.makeText(context,"Registered Successfully",Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    });
                    progressDialog.dismiss();
                    Toast.makeText(context,"Registered Successfully",Toast.LENGTH_LONG).show();
dialog.dismiss();
                }).addOnFailureListener(e -> {
            Log.e("error",e.getMessage());
            progressDialog.dismiss();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        });
    }

    public void LoginUser(Context context,String email,String password,Dialog dialog){
        Dialog progressDialog=new Dialog(context);
        progressDialog.setContentView(R.layout.progressdialog);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.show();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnSuccessListener(authResult -> {
                    Toast.makeText(context,"Login Success",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();

dialog.dismiss();
                }).addOnFailureListener(e -> {
                    Log.e("error",e.getMessage());
                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();

                });
    }

    public void sendPasswordResetEmail(Context context,String email,Dialog dialog){
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context,"A password reset email has been sent to your email ",Toast.LENGTH_LONG).show();
                    if (dialog.isShowing())dialog.dismiss();
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,"An Error Occurred",Toast.LENGTH_LONG).show();
                dialog.dismiss();

            }
        });
    }


}
