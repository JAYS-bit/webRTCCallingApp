package com.example.logup;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.logup.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    ActivityRegisterBinding binding;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();

        if (fAuth.getCurrentUser() != null) {
            Intent i = new Intent(Register.this, LogInActivity.class);
            i.putExtra("email", binding.email.getText().toString());
            i.putExtra("password", binding.password.getText().toString());
            startActivity(i);
        }



        binding.registerProgressBar.setVisibility(View.GONE);
        //actual code starts here ...
        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = binding.fullName.getText().toString();
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

               binding.registerProgressBar.setVisibility(View.VISIBLE);




               //Register the user goes here :

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {

                       if(task.isSuccessful()){

                           Toast.makeText(Register.this, "Registered", Toast.LENGTH_SHORT).show();
                           //Collections to store the data :
                           userID = fAuth.getCurrentUser().getUid();
                           DocumentReference documentReference = fstore.collection("users")
                                           .document(userID);

                           //to store the data; //use hashmap
                           Map<String,Object> user = new HashMap<>();
                           user.put("fName",fullName);
                           user.put("email",email);

                           documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void unused) {

                                   Log.d(TAG,"onSuccess: data has been added in firestore for "+userID);
                               }
                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   Log.d(TAG,"ONfAILURE :"+e.toString());
                               }
                           });



                           startActivity(new Intent(Register.this,GetOTP.class));

                       }else{

                           Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                           binding.registerProgressBar.setVisibility(View.GONE);
                       }

                   }
               }) ;




            }
        });

       binding.logInText.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(Register.this, LogInActivity.class));
           }
       });

    }
}