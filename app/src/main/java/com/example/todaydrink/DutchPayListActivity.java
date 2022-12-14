
package com.example.todaydrink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ContentFrameLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    String[] menu_list;
    String[] food_list;
    String[] price_list;

    int groupNumber;
    int memberNumber;
    int member=0;
    String leader_Id, currentUser;

    boolean[][] tf;
    boolean[][] checkedId;
    Button peopleBtn;
    String[] member_list;
    int[] howManyTrue;
    int[] receipt;
    String[] receipt_s;
    int person = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        menu_list = intent.getStringArrayExtra("menu");
        currentUser = intent.getStringExtra("currentUser");
        groupNumber = intent.getIntExtra("groupNumber", 1);
        memberNumber = intent.getIntExtra("memberNumber", 0);
        food_list = new String[menu_list.length];
        price_list = new String[menu_list.length];
        String[] price_index_list;

        member_list = new String[memberNumber];
        checkedId = new boolean[menu_list.length][memberNumber];
        tf = new boolean[menu_list.length][memberNumber];
        for(int i=0;i<menu_list.length;i++){
            for(int j=0;j<member_list.length;j++){
                tf[i][j] = true;
            }
        }
        int[] array = new int[menu_list.length];
        for(int i=0;i<array.length;i++){
            array[i] = i;
        }

        howManyTrue = new int[menu_list.length];
        receipt = new int[memberNumber];
        receipt_s = new String[memberNumber];

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

        //데이터베이스에서 방에 있는 사람들 아이디 뺴오기
        reference.child("방").child("방" + groupNumber).child("주선자").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GroupMember leader = dataSnapshot.getValue(GroupMember.class);
                    member_list[member] = leader.name;
                    leader_Id = leader.id;
                    member++;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("방").child("방" + groupNumber).child("참가자").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // 참가자 아래에 있는 ID에 하나씩 접근.
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // 각각의 ID 아래에 있는 데이터들에 접근.
                    GroupMember groupMember = dataSnapshot.getValue(GroupMember.class);
                    // 참가자들의 이름을 멤버 리스트에 저장.
                    member_list[member] = groupMember.getName();
                    member++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //나타날 화면
        for(int num : array){
            for(int i=0;i<member_list.length;i++){
                checkedId[num][i] = true;
            }
            LinearLayout menuSelectLinear = new LinearLayout(this);
            menuSelectLinear.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams menuSelectLp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            peopleBtn = new Button(this);
            peopleBtn.setText("인원");
            peopleBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder build = new AlertDialog.Builder(DutchPayListActivity.this);
                    build.setTitle("인원 선택");
                    build.setMultiChoiceItems(member_list, checkedId[num], new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            checkedId[num][which] = isChecked;
                        }
                    });
                    build.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for(int i=0; i<checkedId[num].length; i++){
                                if(!checkedId[num][i]){
                                    checkedId[num][i] = false;
                                }
                            }
                            tf[num] = checkedId[num];
                        }
                    });
                    build.setNegativeButton("취소", null);
                    build.show();
                }
            });
            menuSelectLinear.addView(menuTv[num], menuTvLp);
            menuSelectLinear.addView(peopleBtn);

            rootLinear.addView(menuSelectLinear, menuSelectLp);
        }

        Button calculateBtn = new Button(this);
        calculateBtn.setText("계산하기");
        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<menu_list.length;i++) {
                    for (int j = 0; j < memberNumber; j++) {
                        if (tf[i][j]) {
                            howManyTrue[i]+=1;
                        }
                    }
                }
                for(int i=0;i<menu_list.length;i++) {
                    //receipt 내야 할 금액
                    int onePersonPrice = Integer.parseInt(price_list[i]) / (howManyTrue[i]);
                    for (int j = 0; j < memberNumber; j++) {
                        if (tf[i][j])
                            receipt[j] += onePersonPrice;
                    }
                }

                for (int i = 0; i < receipt.length; i++) {
                    receipt_s[i] = String.valueOf(receipt[i]);
                    Log.d("price", String.valueOf(receipt[i]));
                }

                PayAmount amount = new PayAmount(receipt_s[0]);
                reference.child("User").child(leader_Id).child("내어야할 금액").setValue(amount);

                reference.child("방").child("방" + groupNumber).child("참가자").addValueEventListener(new ValueEventListener() {
                    int i = 1;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            GroupMember groupMember = dataSnapshot.getValue(GroupMember.class);
                            String member_Id = groupMember.id;

                            PayAmount payAmount = new PayAmount(receipt_s[i]);
                            i++;

                            reference.child("User").child(member_Id).child("내어야할 금액").setValue(payAmount);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Intent intent = new Intent(DutchPayListActivity.this, DutchPayAmountActivity.class);
                intent.putExtra("price", receipt_s);
                intent.putExtra("member_list", member_list);
                intent.putExtra("groupNumber", groupNumber);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
            }
        });

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