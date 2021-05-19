package com.returno.bursarychecker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    MaterialToolbar toolbar;
    MaterialTextView newApplicationView,myApplicationsView,adminView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.toolbar);
        newApplicationView=findViewById(R.id.newApplication);
        myApplicationsView=findViewById(R.id.myApplications);
        adminView=findViewById(R.id.admin);

        setSupportActionBar(toolbar);

        newApplicationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
NewApplication.showDialog(getSupportFragmentManager());
            }
        });

        myApplicationsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog progressDialog=new Dialog(MainActivity.this);
                progressDialog.setContentView(R.layout.progressdialog);
                progressDialog.setCancelable(false);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                progressDialog.show();

                new Fetcher().fetchApplications(new CompleteListener() {
                    @Override
                    public void onUploadFetched(List<Upload> uploads) {
                        progressDialog.dismiss();
                        MyApplicationsDialog.showDialog(getSupportFragmentManager(),uploads);
                    }
                });
                }
        });

        adminView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog progressDialog=new Dialog(MainActivity.this);
                progressDialog.setContentView(R.layout.progressdialog);
                progressDialog.setCancelable(false);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                progressDialog.show();

                new Fetcher().fetchApplications(new CompleteListener() {
                    @Override
                    public void onUploadFetched(List<Upload> uploads) {
                        progressDialog.dismiss();
                        AdminApplicationsDialog.showDialog(getSupportFragmentManager(),uploads);
                    }
                });
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser()==null){
            RegisterDialog.showDialog(getSupportFragmentManager());
        }
       /* if (!FirebaseAuth.getInstance().getCurrentUser().getEmail().equals("muragelewy@gmail.com")){
            adminView.setVisibility(View.GONE);
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.menuSignIn){
            LoginDialog.showDialog(getSupportFragmentManager());
        }
        if (item.getItemId()==R.id.menusignOut){
            FirebaseAuth.getInstance().signOut();
            LoginDialog.showDialog(getSupportFragmentManager());
        }
        return super.onOptionsItemSelected(item);
    }
}