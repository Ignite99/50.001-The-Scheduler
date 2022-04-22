package com.example.infosys_50001;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class Animatedlogin extends AppCompatActivity {
    //Bars on top
    View origin,first,second,third,fourth,fifth,sixth;
    //Text Views in middle and bottom
    TextView Schedule,GroupNo;
    //timer
    private static int timer = 1500;

    //Animation
    Animation animateTop, animateBottom, animateMiddle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_animatedlogin);

        //Animation
        animateTop = AnimationUtils.loadAnimation(this, R.anim.top__animation);
        animateMiddle = AnimationUtils.loadAnimation(this, R.anim.middle_animation);
        animateBottom = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //Bars
        origin = findViewById(R.id.originLine);
        first = findViewById(R.id.line1);
        second = findViewById(R.id.line2);
        third = findViewById(R.id.line3);
        fourth = findViewById(R.id.line4);
        fifth = findViewById(R.id.line5);
        sixth = findViewById(R.id.line6);

        Schedule = findViewById(R.id.Login_sayNameOfApp);
        GroupNo = findViewById(R.id.groupNo);

        origin.setAnimation(animateTop);
        first.setAnimation(animateTop);
        second.setAnimation(animateTop);
        third.setAnimation(animateTop);
        fourth.setAnimation(animateTop);
        fifth.setAnimation(animateTop);
        sixth.setAnimation(animateTop);

        Schedule.setAnimation(animateMiddle);
        GroupNo.setAnimation(animateBottom);

        //splash screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Animatedlogin.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, timer);
    }
}