package com.example.todaydrink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DutchPayAmountActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    TextView payText;
    TextView payPriceText;
    TextView accountNumberText;
    String[] price_list;
    String[] member_list;
    ImageView copyIcon;

    int groupNumber = 1;
    String leader_Id, leader_Name, currentUser, currentUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dutch_pay_amount);

        Intent intent = getIntent();
        currentUser = intent.getStringExtra("currentUser");
        member_list = intent.getStringArrayExtra("member_list");
        price_list = intent.getStringArrayExtra("price");

        // 주선자 계좌 번호 가져오기.
        reference.child("방").child("방" + groupNumber).child("주선자").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.getValue(GroupMember.class) != null) {
                        GroupMember leader = dataSnapshot.getValue(GroupMember.class);
                        String leaderId = leader.id;

                        reference.child("User").child(leaderId).child("프로필").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.getValue(UserAccount.class) != null) {
                                    UserAccount user = snapshot.getValue(UserAccount.class);

                                    accountNumberText.setText(user.account);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //TODO 주선자의 계좌번호 가져와서 accountNumberText에다가 보여주기
        // 여기서 계좌번호 복사??

        payText = (TextView)findViewById(R.id.payText);
        payPriceText = (TextView)findViewById(R.id.payPriceText);
        accountNumberText = (TextView)findViewById(R.id.accountNumberText);
        copyIcon = (ImageView)findViewById(R.id.copyIcon);

        // 주선자 ID 가져옴.
        // 주선자 데이터에 금액 넣음.
        reference.child("방").child("방" + groupNumber).child("주선자").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.getValue(GroupMember.class) != null) {
                        GroupMember groupMember = dataSnapshot.getValue(GroupMember.class);
                        leader_Id = groupMember.id;
                        leader_Name = groupMember.name;
                    }
                }
            }

                @Override
                public void onCancelled (@NonNull DatabaseError error){
            }
        });

        // 참가자들의 이름 member_list에 저장.


        // 현재 사용자의 이름을 가져옴.
        reference.child("User").child(currentUser).child("프로필").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(UserAccount.class) != null) {
                    UserAccount user = snapshot.getValue(UserAccount.class);

                    currentUserName = user.name;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("User").child(currentUser).child("내어야할 금액").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(PayAmount.class) != null) {
                    PayAmount amount = snapshot.getValue(PayAmount.class);

                    String payAmount = amount.payAmount;

                    payText.setText(currentUserName + "님이 내셔야할 금액은");
                    payPriceText.setText(groupNumber + "차 : " + payAmount+" 원");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void copyClick(View view) {
        if (view == copyIcon) {
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("CODE", accountNumberText.getText().toString().trim()); //클립보드에 ID라는 이름표로 id 값을 복사하여 저장
            clipboardManager.setPrimaryClip(clipData);

            Toast.makeText(getApplicationContext(), "코드가 복사되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }

}