package com.example.logup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.logup.databinding.ActivityVerifyOtpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class VerifyOTP extends AppCompatActivity {

    ActivityVerifyOtpBinding binding;
    FirebaseAuth mAuth;
    String verificationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        verificationId = getIntent().getStringExtra("verificationId");

        binding.otp1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(binding.otp1.getText().toString().length()==1)     //size as per your requirement
                {
                    binding.otp2.requestFocus();
                }
            }



            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        binding.otp2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(binding.otp1.getText().toString().length()==1)     //size as per your requirement
                {
                    binding.otp3.requestFocus();
                }
            }



            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        binding.otp3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(binding.otp1.getText().toString().length()==1)     //size as per your requirement
                {
                    binding.otp4.requestFocus();
                }
            }



            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        binding.otp4.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(binding.otp1.getText().toString().length()==1)     //size as per your requirement
                {
                    binding.otp5.requestFocus();
                }
            }



            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        binding.otp5.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(binding.otp1.getText().toString().length()==1)     //size as per your requirement
                {
                    binding.otp6.requestFocus();
                }
            }



            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        


        binding.btnverifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String otpPart1 = binding.otp1.getText().toString();
                String otpPart2 = binding.otp2.getText().toString();
                String otpPart3 = binding.otp3.getText().toString();
                String otpPart4 = binding.otp4.getText().toString();
                String otpPart5 = binding.otp5.getText().toString();
                String otpPart6 = binding.otp6.getText().toString();



                String fullOTP = otpPart1 + otpPart2 + otpPart3 + otpPart4 + otpPart5 + otpPart6;


                if (TextUtils.isEmpty(fullOTP) || fullOTP.length()!=6) {
                    Toast.makeText(VerifyOTP.this, "Wrong OTP", Toast.LENGTH_SHORT).show();
                } else {
                    verifyCode(fullOTP);
                }
            }
        });
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredentials(credential);
    }

    private void signInWithCredentials(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(VerifyOTP.this, "Verification Successfull", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(VerifyOTP.this, HomeActivity.class));
                            finish();
                        } else {
                            Toast.makeText(VerifyOTP.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}