package com.returno.bursarychecker;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;

public class LoginDialog extends DialogFragment {

    private MaterialButton registerButton,loginButton;
    private TextInputEditText emailInput,passwordInput;
    private MaterialTextView resetView;

    public static void showDialog(FragmentManager manager){
        LoginDialog dialog=new LoginDialog();
        dialog.show(manager,"Login Account");

    }

    @Override
    public void onResume() {
        super.onResume();
        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
if (getDialog().isShowing())getDialog().dismiss();
            Toast.makeText(getActivity(),"You are Already Logged In",Toast.LENGTH_LONG).show();
        }
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
       View view=inflater.inflate(R.layout.layout_login,container,false);
registerButton=view.findViewById(R.id.btnSignUp);
loginButton=view.findViewById(R.id.btnSignIn);
resetView=view.findViewById(R.id.resetPassword);

        emailInput=view.findViewById(R.id.emailInput);
        passwordInput=view.findViewById(R.id.passwordInput);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       registerButton.setOnClickListener(v -> {
           getDialog().dismiss();
RegisterDialog.showDialog(getActivity().getSupportFragmentManager());

       });
       loginButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               new Authenticator().LoginUser(getActivity(),emailInput.getText().toString(),passwordInput.getText().toString(),getDialog());

           }
       });

       resetView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Dialog resetDialog=new Dialog(getActivity());
               resetDialog.setContentView(R.layout.reset_password);
               TextInputEditText emailEdit=resetDialog.findViewById(R.id.emailInput);
               MaterialButton resetButton=resetDialog.findViewById(R.id.btnReset);

               resetButton.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       String email=emailEdit.getText().toString();
                       if (!TextUtils.isEmpty(email)){
new Authenticator().sendPasswordResetEmail(getActivity(),email,resetDialog);
                       }
                   }
               });
               resetDialog.show();
           }
       });

    }
}
