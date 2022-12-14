package com.example.todaydrink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PostActivity extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();
    private EditText mTitle, mContents;
    private String currentUser;

    long now;
    Date date;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    String postTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Intent intent = getIntent();
        currentUser = intent.getStringExtra("currentUser");

        mTitle = findViewById(R.id.post_title_edit);
        mContents = findViewById(R.id.post_contents_edit);

        findViewById(R.id.post_save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                now = System.currentTimeMillis();
                date = new Date(now);
                format.format(date);
                postTime = format.format(date);

                Post post = new Post(currentUser, mTitle.getText().toString(), mContents.getText().toString(), postTime);

                reference.child("User").child(currentUser).child("게시글").child(mTitle.getText().toString()).setValue(post);

                Intent intent1 = new Intent(PostActivity.this, BoardActivity.class);
                intent1.putExtra("currentUser", currentUser);
                startActivity(intent1);
                finish();
            }
        });
    }
}