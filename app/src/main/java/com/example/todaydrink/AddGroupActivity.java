package com.example.todaydrink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class AddGroupActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private EditText edit_search;
    private ImageView image_profile;
    private TextView text_id, text_name;
    private CheckBox check_add;
    private String searchId, searchName;
    public static Context mContext;
    String currentUserName;

    // 방 번호
    static int groupNumber = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        mContext = this;

        Intent get_Intent = getIntent();
        String currentUser = get_Intent.getStringExtra("currentUser");
        int roomNumber = get_Intent.getIntExtra("groupNumber", 0);

        databaseReference.child("User").child(currentUser).child("프로필").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccount leader = snapshot.getValue(UserAccount.class);
                currentUserName = leader.getName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Button btn_save = findViewById(R.id.btn_save);
        Button btn_search = findViewById(R.id.btn_search);

        image_profile = findViewById(R.id.image_profile);
        edit_search = findViewById(R.id.edit_search);
        text_id = findViewById(R.id.text_id);
        text_name = findViewById(R.id.text_name);
        check_add = findViewById(R.id.check_add);

        // 처음 화면에 들어왔을 때는 안보이게
        image_profile.setVisibility(View.INVISIBLE);
        text_id.setVisibility(View.INVISIBLE);
        text_name.setVisibility(View.INVISIBLE);
        check_add.setVisibility(View.INVISIBLE);

        // 검색 버튼 클릭 시
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 검색한 ID 읽음.
                searchId = edit_search.getText().toString();
                check_add.setChecked(false);

                // 검색한 ID가 데이터 베이스에 있는지
                databaseReference.child("User").child(searchId).child("프로필").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue(UserAccount.class) != null) {
                            UserAccount userAccount = snapshot.getValue(UserAccount.class);

                            assert userAccount != null;
                            // 있다면 ID, 이름을 TextView에 설정해주고 보이게 함.
                            if (userAccount.id.equals(searchId)) {
                                text_id.setText(userAccount.id);
                                text_name.setText(userAccount.name);

                                searchName = userAccount.name;

                                image_profile.setVisibility(View.VISIBLE);
                                text_id.setVisibility(View.VISIBLE);
                                text_name.setVisibility(View.VISIBLE);
                                check_add.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(getApplicationContext(), searchId + "와 같은 아이디는 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), searchId + "와 같은 아이디는 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("AddGroupActivity", "데이터베이스 접근 불가");
                    }
                });
            }
        });

        // 검색 결과에 있는 체크박스를 누를 시
        check_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 그 이용자의 ID, 이름을 받아서 참가자 역할에 넣음.
                GroupMember member = new GroupMember(searchId, searchName);
                databaseReference.child("방").child("방" + (roomNumber + 1)).child("참가자").child(searchId).setValue(member);
            }
        });

        // 저장을 누르면 메인 화면으로 이동.

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupMember leaderData = new GroupMember(currentUser, currentUserName);
                databaseReference.child("방").child("방" + (roomNumber + 1)).child("주선자").child(currentUser).setValue(leaderData);

                Intent intent = new Intent(AddGroupActivity.this, MainActivity.class);
                intent.putExtra("groupNumber", (roomNumber + 1));
                setResult(RESULT_OK, intent);
                groupNumber++;
                finish();
            }
        });
    }
}