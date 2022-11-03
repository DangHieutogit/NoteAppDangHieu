package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        //delay khi load 1s
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser==null){
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));

                    //kiem tra tai khoan user neu nguoi dung chua co tai khoan == null thi se duoc chuyen den man hinh
                    //loginActivity

                }else {
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    //neu nguoi dung da co tai khoan duoc luu lai trong firebase user se duoc chuyen thang den Man` hinh
                    //Mainactivity
                }
                /*bat dau tu man hinh splash sau khi het thoi gian delay load
                se chuyen sang man hinh Main */
           }
        },1000);


    }
}