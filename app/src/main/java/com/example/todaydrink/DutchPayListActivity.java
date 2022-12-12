package com.example.todaydrink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ContentFrameLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.Distribution;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DutchPayListActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    // TODO 몇 번 방인지 메인 화면에서부터 데이터를 가져오는 식으로 수정해야 함.
    int groupNumber;
    // 멤버 리스트에 0행부터 저장하기 위해.
    int member = 0;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String[] menu_list = intent.getStringArrayExtra("menu");
        String[] food_list = new String[menu_list.length];  //음식메뉴만
        String[] price_list = new String[menu_list.length]; //가격만
        String[] price_index_list;

        groupNumber = intent.getIntExtra("groupNumber", 1);

        // 참가자가 몇 명인지 알기 위해.
        reference.child("방").child("방" + groupNumber).child("참가자").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GroupMember groupMember = dataSnapshot.getValue(GroupMember.class);
                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // 참가자 명수만큼의 행을 가진 멤버 리스트 생성.
        String[] member_list = new String[100];

        LinearLayout rootLinear = new LinearLayout(this);
        rootLinear.setOrientation(LinearLayout.VERTICAL);
        ContentFrameLayout.LayoutParams rootLp = new ContentFrameLayout.LayoutParams(
                ContentFrameLayout.LayoutParams.MATCH_PARENT, ContentFrameLayout.LayoutParams.MATCH_PARENT);

        TextView menuTitleTv = new TextView(this);
        menuTitleTv.setText("메뉴 목록");
        menuTitleTv.setTextSize(30);
        rootLinear.addView(menuTitleTv);

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
        LinearLayout.LayoutParams menuTvLp = new LinearLayout.LayoutParams(
                900,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        for(int i=0;i<menuTv.length;i++){
            LinearLayout menuSelectLinear = new LinearLayout(this);
            menuSelectLinear.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams menuSelectLp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            Button peopleBtn = new Button(this);  //인원버튼
            peopleBtn.setText("인원");


            // TODO 화면에 어떤 방식으로 이름을 보이게 할 것인지에 따라 변형.
            peopleBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 주선자의 ID를 가져옴.
                    reference.child("방").child("방" + groupNumber).child("주선자").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                GroupMember leader = dataSnapshot.getValue(GroupMember.class);
                                String leaderName = leader.getName();
                                Toast.makeText(getApplicationContext(), leaderName, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    reference.child("방").child("방" + groupNumber).child("참가자").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // 참가자 아래에 있는 ID들에 하나씩 접근.
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                // 각각의 ID 아래에 있는 데이터들에 접근.
                                GroupMember groupMember = dataSnapshot.getValue(GroupMember.class);
                                // 참가자들의 이름을 멤버 리스트에 저장.
                                String none = groupMember.id;
                                member_list[member] = groupMember.getName();
                                member++;
                            }
                            for (int i = 0; i < member; i++) {
                                // 참가자들의 이름을 리스트에서 꺼내서 토스트로 출력.
                                Toast.makeText(getApplicationContext(), member_list[i], Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
            // 버튼 누르면 방에 있는 인원 명단 띄워서 선택가능하게 하기



            menuSelectLinear.addView(menuTv[i], menuTvLp);
            menuSelectLinear.addView(peopleBtn);

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