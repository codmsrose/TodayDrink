package com.example.todaydrink;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Random;

public class GameNumberActivity extends AppCompatActivity {

    View my_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View my_view = new View(this);
        setContentView(my_view);
    }
}

