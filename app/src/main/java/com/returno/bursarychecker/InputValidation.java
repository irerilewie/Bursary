package com.returno.bursarychecker;

import android.text.TextUtils;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidation {
    private static InputValidation inputValidation;
    private boolean isDataValid=true;
    public static User user=new User();

    public static InputValidation getInstance() {
        if (inputValidation==null){
            inputValidation=new InputValidation();
        }
        return inputValidation;
    }

    public InputValidation validateFirstName(TextInputEditText fNameEdit){
        String fName=fNameEdit.getText().toString();
        if (TextUtils.isEmpty(fName) || fName.length()<3){
            fNameEdit.setError("The First Name Is Invalid");
            fNameEdit.requestFocus();
        }else {
            user.setfName(fName);
        }


       return this;
    }

    public InputValidation validateLastName(TextInputEditText lNameEdit){
        String lName=lNameEdit.getText().toString();

        if (TextUtils.isEmpty(lName) || lName.length()<3){     isDataValid=false;
            lNameEdit.setError("The Last Name is Invalid");
            lNameEdit.requestFocus();
        }else {
            user.setlName(lName);
        }


        return this;
    }

    public InputValidation validatePhone(TextInputEditText phoneEdit){
        String phone=phoneEdit.getText().toString();
        if (!TextUtils.isDigitsOnly(phone) || phone.length()!=10){
            isDataValid=false;
            phoneEdit.setError("The Phone Number is Invalid");
            phoneEdit.requestFocus();
        }else {
            user.setPhoneNumber(phone);
        }

        return this;
    }

    public InputValidation validateEmail(TextInputEditText emailEdit){
        Pattern emailPattern=Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",Pattern.CASE_INSENSITIVE);
        String email=emailEdit.getText().toString();
        Matcher matcher=emailPattern.matcher(email);
        if (!matcher.find()){
            isDataValid=false;
            emailEdit.setError("The Email Address is Invalid");
            emailEdit.requestFocus();
        }else {
            user.setEmail(email);
        }

        return this;
    }

    public InputValidation validatePassword(TextInputEditText p1,TextInputEditText p2){
        String pass1=p1.getText().toString();
        String pass2=p2.getText().toString();

        if (!pass1.equals(pass2)){
            isDataValid=false;
            p1.setError("Passwords Do Not Match");

        }

        if (pass1.length()<8){
            isDataValid=false;
            p1.setError("The Password is too short");
        }else {
            user.setPassword(pass1);
        }
        return this;
    }

    public InputValidation validateIdNumber(TextInputEditText idInput){
        String idNumber=idInput.getText().toString();
        if (!TextUtils.isDigitsOnly(idNumber) || idNumber.length()<5){
            isDataValid=false;
            idInput.setError("This Id number is invalid");
            idInput.requestFocus();
        }else{
            user.setIdNumber(Integer.parseInt(idNumber));
        }
        return this;
    }

    public boolean get(){
        return isDataValid;
    }
}
