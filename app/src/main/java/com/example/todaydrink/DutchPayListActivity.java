package com.example.todaydrink;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ContentFrameLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DutchPayListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String[] menu_list = intent.getStringArrayExtra("menu");

        LinearLayout rootLinear = new LinearLayout(this);
        rootLinear.setOrientation(LinearLayout.VERTICAL);
        ContentFrameLayout.LayoutParams rootLp = new ContentFrameLayout.LayoutParams(
                ContentFrameLayout.LayoutParams.MATCH_PARENT, ContentFrameLayout.LayoutParams.MATCH_PARENT);

        TextView menuTitleTv = new TextView(this);
        menuTitleTv.setText("메뉴 목록");
        menuTitleTv.setTextSize(20);

        TextView[] menuTv = new TextView[menu_list.length];
        for(int i=0;i<menu_list.length;i++){
            menuTv[i] = new TextView(this);
            menuTv[i].setText(menu_list[i]);
        }

        Button calculateBtn = new Button(this);
        calculateBtn.setText("계산하기");

        rootLinear.addView(menuTitleTv);
        for(int i=0;i<menuTv.length;i++){
            rootLinear.addView(menuTv[i]);
        }
        rootLinear.addView(calculateBtn);
        setContentView(rootLinear, rootLp);

    }
}