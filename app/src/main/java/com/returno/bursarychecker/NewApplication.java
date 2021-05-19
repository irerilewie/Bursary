package com.returno.bursarychecker;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.opensooq.supernova.gligar.GligarPicker;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewApplication extends DialogFragment {

    private MaterialButton selectId,selectReport,selectCertificate,selectFees,genderButton,applyButton;
    private ImageView idImage,reportView,certView,feeView;
    private Uri idUri,certUri,feeUri,reportUri;
    List<String> downloadUrls=new ArrayList<>();


    public static void showDialog(FragmentManager manager){
        NewApplication dialog=new NewApplication();
        dialog.show(manager,"New Application");

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_BursaryChecker_FullScreenDialog);
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
       View view=inflater.inflate(R.layout.upload_file_layout,container,false);
selectId=view.findViewById(R.id.selectNId);
selectCertificate=view.findViewById(R.id.selectBCert);
selectFees=view.findViewById(R.id.selectFee);
selectReport=view.findViewById(R.id.selectReport);
genderButton=view.findViewById(R.id.gender);

idImage=view.findViewById(R.id.nIdImage);
reportView=view.findViewById(R.id.reportImage);
certView=view.findViewById(R.id.bCertImage);
feeView=view.findViewById(R.id.feeImage);
applyButton=view.findViewById(R.id.apply);

return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              new GligarPicker().withFragment(NewApplication.this).limit(1).requestCode(Constants.ID_IMG_REQUEST_CODE)
                      .show();
            }
        });

        selectReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GligarPicker().withFragment(NewApplication.this).limit(1).requestCode(Constants.REPORT_IMAGE_REQUEST_CODE)
                        .show();
            }
        });

        selectFees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GligarPicker().withFragment(NewApplication.this).limit(1).requestCode(Constants.FEE_IMG_REQUEST_CODE)
                        .show();
            }
        });
        selectCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GligarPicker().withFragment(NewApplication.this).limit(1).requestCode(Constants.CERT_IMAGE_REQUEST_CODE)
                        .show();
            }
        });

        genderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] items=new String[]{"Male", "Female", "Others"};
                new MaterialAlertDialogBuilder(getActivity())
                        .setTitle("Select Gender")
                        .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
genderButton.setText(items[which]);
                            }
                        }).create()
                        .show();
            }
        });

 applyButton.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
if(idUri==null || certUri==null || feeUri==null || reportUri==null || genderButton.getText().toString().equals(getResources().getString(R.string.select_gender))){
    Toast.makeText(getActivity(),"One or more images not Set Yet",Toast.LENGTH_LONG).show();
    return;
}
         Dialog progressDialog=new Dialog(getActivity());
         progressDialog.setContentView(R.layout.progressdialog);
         progressDialog.setCancelable(false);
         progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
         progressDialog.show();
new UploadUtils().uploadIdPhoto(idUri, new CompleteListener() {
    @Override
    public void onComplete(String downLoadUrl) {
downloadUrls.add(downLoadUrl);
new UploadUtils().uploadCert(certUri, new CompleteListener() {
    @Override
    public void onComplete(String downLoadUrl1) {
downloadUrls.add(downLoadUrl1);
new UploadUtils().uploadfee(feeUri, new CompleteListener() {
    @Override
    public void onComplete(String downLoadUrl2) {
        downloadUrls.add(downLoadUrl2);
        new UploadUtils().uploadReport(reportUri, new CompleteListener() {
            @Override
            public void onComplete(String downLoadUrl3) {
                downloadUrls.add(downLoadUrl3);
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("requests")
                        .push();
                Upload upload=new Upload(downloadUrls,FirebaseAuth.getInstance().getCurrentUser().getUid(),String.valueOf(System.currentTimeMillis()),genderButton.getText().toString(),"Pending", Calendar.getInstance().getTime().toString());
                reference.setValue(upload)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(),"Sent Successfully",Toast.LENGTH_LONG).show();
                                getDialog().dismiss();
                            }
                        });
            }
        });
    }
});
    }
});
    }
});

     }
 });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode!= AppCompatActivity.RESULT_OK){
return;
        }

        if (requestCode==Constants.ID_IMG_REQUEST_CODE){
            String[] idPath = data.getExtras().getStringArray(GligarPicker.IMAGES_RESULT);
            idUri=Uri.fromFile(new File(idPath[0]));
            idImage.setImageURI(idUri);

        }

        if (requestCode==Constants.FEE_IMG_REQUEST_CODE){
            String[] feePath = data.getExtras().getStringArray(GligarPicker.IMAGES_RESULT);
            feeUri=Uri.fromFile(new File(feePath[0]));
            feeView.setImageURI(feeUri);


        }
        if (requestCode==Constants.CERT_IMAGE_REQUEST_CODE){
            String[] certPath = data.getExtras().getStringArray(GligarPicker.IMAGES_RESULT);
            certUri=Uri.fromFile(new File(certPath[0]));
            certView.setImageURI(certUri);

        }

        if (requestCode==Constants.REPORT_IMAGE_REQUEST_CODE){
            String[] reportPath = data.getExtras().getStringArray(GligarPicker.IMAGES_RESULT);
            reportUri=Uri.fromFile(new File(reportPath[0]));
            reportView.setImageURI(reportUri);

        }
    }
}
