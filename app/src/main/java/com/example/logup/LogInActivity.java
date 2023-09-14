package com.example.logup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.logup.databinding.ActivityLogInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {


    FirebaseAuth fAuth;
    ActivityLogInBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fAuth=FirebaseAuth.getInstance();






        binding.logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email= binding.email.getText().toString().trim();
                String password=binding.password.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    binding.email.setError("Email is required");
                    return;

                }
                if(TextUtils.isEmpty(password)){
                    binding.password.setError("Password is required");
                    return;
                }

                if(password.length()<6){
                    binding.password.setError("At least 6 characters ");
                    return;
                }

                binding.logInProgressBar.setVisibility(View.VISIBLE);

                //authenticate the user
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            binding.logInProgressBar.setVisibility(View.VISIBLE);
                            Toast.makeText(LogInActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LogInActivity.this,HomeActivity.class));
                            binding.logInProgressBar.setVisibility(View.GONE);

                        }else{
                            Toast.makeText(LogInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            binding.logInProgressBar.setVisibility(View.GONE);

                        }

                    }
                });

            }
        });

        binding.registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LogInActivity.this,Register.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}