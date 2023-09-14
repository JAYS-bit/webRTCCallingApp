package com.example.logup.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.logup.R;
import com.example.logup.databinding.ActivityWebRtcUiactivityBinding;
import com.example.logup.repository.MainRepository;
import com.permissionx.guolindev.PermissionX;

public class webRtcUIActivity extends AppCompatActivity {

    ActivityWebRtcUiactivityBinding binding;
    private MainRepository mainRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityWebRtcUiactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }


    private  void init(){
     mainRepository=MainRepository.getInstance();
     binding.enterBtn.setOnClickListener(v->{
         PermissionX.init(this)
                 .permissions(Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO)
                         .request((allGranted, grantedList, deniedList) -> {

                             if(allGranted){
                                 mainRepository.login(
                                         binding.username.getText().toString(),getApplicationContext(),()->{
                                             //on success start activity
                                             startActivity(new Intent(webRtcUIActivity.this, CallActivity.class));
                                         }
                                 );
                             }

                         });
         //login to firebase here


     });
    }
}