package com.example.todaydrink;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ContentFrameLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.api.Distribution;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DutchPayListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String[] menu_list = intent.getStringArrayExtra("menu");
        String[] food_list = new String[menu_list.length];
        String[] price_list = new String[menu_list.length];
        String[] price_index_list;

        LinearLayout rootLinear = new LinearLayout(this);
        rootLinear.setOrientation(LinearLayout.VERTICAL);
        ContentFrameLayout.LayoutParams rootLp = new ContentFrameLayout.LayoutParams(
                ContentFrameLayout.LayoutParams.MATCH_PARENT, ContentFrameLayout.LayoutParams.MATCH_PARENT);

        TextView menuTitleTv = new TextView(this);
        menuTitleTv.setText("메뉴 목록");
        menuTitleTv.setTextSize(30);

        TextView[] menuTv = new TextView[menu_list.length];
        for(int i=0;i<menu_list.length;i++){
            menuTv[i] = new TextView(this);
            price_index_list = menu_list[i].split(" ");
            price_list[i] = price_index_list[price_index_list.length-1];
            price_list[i] = getOnlyNumber(price_list[i]);
            food_list[i] = getOnlyKorean(menu_list[i]);
            menuTv[i].setText(food_list[i]+" "+price_list[i]);
            menuTv[i].setTextSize(20);
        }

        for(int i=0;i<menuTv.length;i++){
            LinearLayout menuSelectLinear = new LinearLayout(this);
            menuSelectLinear.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams menuSelectLp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            menuSelectLinear.addView(menuTv[i]);

            Button peopleBtn = new Button(this);
            peopleBtn.setText("인원");


            rootLinear.addView(menuSelectLinear, menuSelectLp);
        }
        Button calculateBtn = new Button(this);
        calculateBtn.setText("계산하기");

        rootLinear.addView(calculateBtn);
        setContentView(rootLinear, rootLp);

    }

    public static String getOnlyKorean(String str){

        StringBuffer sb=new StringBuffer();

        if(str!=null && str.length()!=0){
            Pattern p = Pattern.compile("[가-힣]");
            Matcher m = p.matcher(str);
            while(m.find()){
                sb.append(m.group());
            }
        }
        return sb.toString();
    }
    public static String getOnlyNumber(String str){

        StringBuffer sb=new StringBuffer();

        if(str!=null && str.length()!=0){
            Pattern p = Pattern.compile("[0-9]");
            Matcher m = p.matcher(str);
            while(m.find()){
                sb.append(m.group());
            }
        }
        return sb.toString();
    }

}