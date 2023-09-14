package com.example.logup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.logup.databinding.ActivityHomeBinding;
import com.example.logup.ui.webRtcUIActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    ActivityHomeBinding binding;
    FirebaseFirestore fStore;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth=FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID= mAuth.getCurrentUser().getUid();

        //retrive the data;

        DocumentReference documentReference = fStore.collection("users")
                        .document(userID);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                binding.email.setText(value.getString("email"));
                binding.name.setText(value.getString("fName"));
            }
        });







        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(HomeActivity.this, Register.class));
                finish();
            }
        });
        binding.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MapActivity.class));
            }
        });

        binding.call.setOnClickListener(v->{

            startActivity(new Intent(HomeActivity.this, webRtcUIActivity.class));
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(HomeActivity.this,LogInActivity.class));
    }
}