package com.example.todaydrink;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CheckActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CheckAdapter CheckAdapter;
    private ArrayList<CheckItems> mArrayList;




    Dialog checkDialog; // 술 종류 클릭하면 나오는 다이얼로그
    Dialog dialogNew;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        recyclerView = findViewById(R.id.recyclerView);

        //매니저
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        //리사이클뷰에 어댑터
        CheckAdapter = new CheckAdapter(getApplicationContext());
        recyclerView.setAdapter(CheckAdapter);


        //다이얼얼로그
        checkDialog = new Dialog(CheckActivity.this);       // Dialog 초기화
        checkDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        checkDialog.setContentView(R.layout.check_dialog_pick);             // xml 레이아웃 파일과 연결



        //아이템 선택
        CheckAdapter.setOnItemClicklistener(new OnCheckItemClickListener() {
            @Override
            public void onItemClick(CheckAdapter.CheckViewHolder holder, View view, int position) {
                CheckItems item = CheckAdapter.getItem(position);
                Toast.makeText(getApplicationContext(),"아이템 선택 " + item.getName(),
                        Toast.LENGTH_LONG).show();

                //추가
                if(position==CheckAdapter.getItemCount()-1){
                    final EditText newDrink = new EditText(CheckActivity.this);

                    AlertDialog.Builder builder = new AlertDialog.Builder(CheckActivity.this);
                    builder.setMessage("추가하고 싶은 술의 종류를 입력하세요");
                    builder.setView(newDrink);

                    builder.setNegativeButton("취소", null);
                    builder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //TODO : 술 종류 추가한거 데이터베이스에 저장
                            CheckAdapter.addItem(new CheckItems(R.drawable.pro, newDrink.getText().toString()));
                            CheckAdapter.itemPositionChange(CheckAdapter.getItemCount()-1,CheckAdapter.getItemCount()-2);
                            CheckAdapter.notifyItemMoved(CheckAdapter.getItemCount()-1, CheckAdapter.getItemCount()-2);
                        }
                    });
                    builder.show();
                }



                else{
                    showDialogDrink(position);}
            }
        });


        CheckAdapter.addItem(new CheckItems(R.drawable.beer, "맥주"));
        CheckAdapter.addItem(new CheckItems(R.drawable.cok, "소주"));
        CheckAdapter.addItem(new CheckItems(R.drawable.mak, "막걸리"));
        CheckAdapter.addItem(new CheckItems(R.drawable.beer, "칵테일"));
        CheckAdapter.addItem(new CheckItems(R.drawable.pro," "));

    }


    //======다이얼로그==================================
    public void showDialogDrink(int position){
        checkDialog.show(); // 다이얼로그 띄우기

        //*주의 : findViewById()를 쓸 때는 -> 앞에 반드시 다이얼로그 이름을 붙여야 한다.


        TextView check_drink_name = checkDialog.findViewById(R.id.check_drink_name);  //술 종류 선택
        EditText check_edit_amount = checkDialog.findViewById(R.id.check_edit_amount); //술 마신 양
        Button check_amount_glass= checkDialog.findViewById(R.id.check_amount_glass); //잔
        Button check_amount_bottle= checkDialog.findViewById(R.id.check_amount_bottle);//병
        Button check_amount_ml= checkDialog.findViewById(R.id.check_amount_ml);//ml





        //술 종류 선택 - 라디오 다이얼로그
        check_drink_name.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {


                Resources res = getResources();
                String[] arrayDrink={};
                int pick;

                switch(position){
                    case  0:
                        arrayDrink=res.getStringArray(R.array.beer);
                        break;
                    case  1:
                        arrayDrink=res.getStringArray(R.array.soju);
                        break;
                }

                final int[] selectedIndex = {0};

                AlertDialog.Builder dialog = new AlertDialog.Builder(CheckActivity.this);
                dialog.setTitle("종류를 선택하세요.")
                        .setSingleChoiceItems(arrayDrink,
                                0,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        selectedIndex[0] = which;
                                    }
                                })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO 다이얼로그에 어떤거 선택했는지 보이기

                            }
                        }).create().show();


            }
        });




        // 아니오 버튼  ->다이얼로그 닫기
        Button noBtn = checkDialog.findViewById(R.id.noBtn);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 원하는 기능 구현
                checkDialog.dismiss(); // 다이얼로그 닫기
            }
        });
        // 네 버튼  -> 앱종료
        checkDialog.findViewById(R.id.yesBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 지금 마신 술 데이터베이스에 저장하고 화면에 반영하기
            }
        });
    }




}
