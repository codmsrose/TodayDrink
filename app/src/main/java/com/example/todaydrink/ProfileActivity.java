package com.example.todaydrink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    String userId;

    TextView nameText;
    TextView idText;
    TextView numberText;
    TextView weightText;
    Button numberBtn;
    Button weightBtn;
    String modifyNumberText;
    String modifyWeightText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent get_Intent = getIntent();
        userId = get_Intent.getStringExtra("currentUser");

        nameText = (TextView) findViewById(R.id.myNameText);
        idText = (TextView) findViewById(R.id.myIdText);
        numberText = (TextView)findViewById(R.id.myNumberText);
        weightText = (TextView)findViewById(R.id.myWeightText);
        numberBtn = (Button)findViewById(R.id.numberBtn);
        weightBtn = (Button)findViewById(R.id.weightBtn);

        numberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText numberEditText = new EditText(ProfileActivity.this);

                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("?????? ????????????");
                builder.setView(numberEditText);
                builder.setNegativeButton("??????", null);
                builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        modifyNumberText = numberEditText.getText().toString();
                        numberText.setText(modifyNumberText);

                        // ?????? ?????? ?????? ??? ????????????
                        reference.child("User").child(userId).child("?????????").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.getValue(UserAccount.class) != null) {
                                    UserAccount user = snapshot.getValue(UserAccount.class);
                                    // ?????? ????????? ?????? ????????? ????????? ??????.
                                    user.account = modifyNumberText;

                                    reference.child("User").child(userId).child("?????????").setValue(user);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                builder.show();
            }
        });

        weightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText weightEditText = new EditText(ProfileActivity.this);

                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("?????? ????????????");
                builder.setView(weightEditText);
                builder.setNegativeButton("??????", null);
                builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        modifyWeightText = weightEditText.getText().toString();
                        weightText.setText(modifyWeightText);

                        // ????????? ?????? ??? ????????????
                        reference.child("User").child(userId).child("?????????").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.getValue(UserAccount.class) != null) {
                                    UserAccount user = snapshot.getValue(UserAccount.class);
                                    user.weight = (modifyWeightText);

                                    reference.child("User").child(userId).child("?????????").setValue(user);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                builder.show();
            }
        });

        // ????????? ????????? ???????????? ??????, ID, ?????? ??????, ????????? ?????????.
        reference.child("User").child(userId).child("?????????").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(UserAccount.class) != null) {
                    UserAccount user = snapshot.getValue(UserAccount.class);
                    nameText.setText(user.name);
                    idText.setText(user.id);
                    numberText.setText(user.account);
                    weightText.setText(user.weight);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    // TODO ?????????????????? ???????????? ???????????? ?????????????????? ???????????????.
    // TODO ??????????????? ???????????? ???????????? ????????? ????????? ????????????????????? ????????????(????????? ????????? ????????? ??????->modifyWeightText, modifyNumberText)
    // TODO ?????????????????? ???????????? ????????? ????????????????????? ?????????????????? ???????????? ??????????????? ???????????? ???????????????..
}