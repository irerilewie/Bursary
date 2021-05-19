package com.returno.bursarychecker;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterDialog extends DialogFragment {

    private MaterialButton registerButton,loginButton;
    private TextInputEditText fNameInput,lNameInput,idInput,phoneInput,emailInput,passwordInput,confirmPasswordInput;

    public static void showDialog(FragmentManager manager){
        RegisterDialog dialog=new RegisterDialog();
        dialog.show(manager,"Register Account");

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
       View view=inflater.inflate(R.layout.register_layout,container,false);
registerButton=view.findViewById(R.id.btnSignUp);
loginButton=view.findViewById(R.id.btnSignIn);

fNameInput=view.findViewById(R.id.fNameInput);
lNameInput=view.findViewById(R.id.lNameInput);
        phoneInput=view.findViewById(R.id.phoneInput);
        emailInput=view.findViewById(R.id.emailInput);
        passwordInput=view.findViewById(R.id.passwordInput);
        confirmPasswordInput=view.findViewById(R.id.confirmPasswordInput);
        idInput=view.findViewById(R.id.idInput);


        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       registerButton.setOnClickListener(v -> {

boolean isValid=InputValidation.getInstance().validateFirstName(fNameInput)
    .validateLastName(lNameInput)
    .validateEmail(emailInput)
    .validatePhone(phoneInput)
    .validatePassword(passwordInput,confirmPasswordInput)
        .validateIdNumber(idInput)
    .get();

if (isValid){
new Authenticator().RegisterUser(getActivity(),InputValidation.user,getDialog());
}
       });
       loginButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               getDialog().dismiss();
LoginDialog.showDialog(getActivity().getSupportFragmentManager());
           }
       });

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (FirebaseAuth.getInstance().getCurrentUser()==null){
            LoginDialog.showDialog(getActivity().getSupportFragmentManager());
        }
    }
}
