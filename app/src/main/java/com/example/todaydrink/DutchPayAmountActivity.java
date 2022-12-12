package com.example.todaydrink;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DutchPayAmountActivity extends AppCompatActivity {

    TextView payPriceText;
    TextView accountNumberText;
    String[] price_list;
    ImageView copyIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dutch_pay_amount);
        //TODO 주선자의 계좌번호 가져와서 accountNumberText에다가 보여주기
        // 여기서 계좌번호 복사??

        payPriceText = (TextView)findViewById(R.id.payPriceText);
        accountNumberText = (TextView)findViewById(R.id.accountNumberText);
        copyIcon = (ImageView)findViewById(R.id.copyIcon);

        Intent intent = getIntent();
        price_list = intent.getStringArrayExtra("price");

        // 아이디가 주선자라면
        payPriceText.setText(price_list[0]);

        // 주선자가 아니라면


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