package com.example.huongthutran.gomoku;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private Button btn2Player,btn1Player;
    //@SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1Player = findViewById(R.id.btn1Player);
        btn2Player = findViewById(R.id.btn2Player);
        btn2Player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent=new Intent(MainActivity.this, TwoPlayerActivity.class);
                startActivity(myIntent);
            }
        });
        btn1Player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent=new Intent(MainActivity.this, onePlayerActivity.class);
                startActivity(myIntent);
            }
        });
    }

}
