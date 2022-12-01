package com.example.todaydrink;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
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

                //리사이클러에서 보이는 큰 술 종류 추가
                // 항상 맨 마지막을 눌렀을 때 추가가능하도록 함
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
                            // (위스키, 샴페인 같은 큰 종류)
                            CheckAdapter.addItem(new CheckItems(R.drawable.pro, newDrink.getText().toString()));
                            CheckAdapter.itemPositionChange(CheckAdapter.getItemCount()-1,CheckAdapter.getItemCount()-2);
                            CheckAdapter.notifyItemMoved(CheckAdapter.getItemCount()-1, CheckAdapter.getItemCount()-2);
                        }
                    });
                    builder.show();
                }
                else{
                    Log.d("선택한대종류이름",item.getName());
                    showDialogDrink(position);}
            }
        });


        CheckAdapter.addItem(new CheckItems(R.drawable.beer, "맥주"));
        CheckAdapter.addItem(new CheckItems(R.drawable.cok, "소주"));
        CheckAdapter.addItem(new CheckItems(R.drawable.mak, "막걸리"));
        CheckAdapter.addItem(new CheckItems(R.drawable.beer, "칵테일"));
        CheckAdapter.addItem(new CheckItems(R.drawable.pro," "));

        //TODO: 지금은 리사이클러뷰에서 보일 내용을 위의 코드에서 추가했지만 데이터베이스에서 가져와서 들어갈 수 있도록 해주실 수 있나요?
        // 어려우면 카톡주세요...


    }


    //======다이얼로그==================================
    public void showDialogDrink(int position){
        checkDialog.show(); // 다이얼로그 띄우기

        //*주의 : findViewById()를 쓸 때는 -> 앞에 반드시 다이얼로그 이름을 붙이기!


        TextView check_drink_name = checkDialog.findViewById(R.id.check_drink_name);  //술 종류 선택
        EditText check_edit_amount = checkDialog.findViewById(R.id.check_edit_amount); //술 마신 양
        Button check_amount_glass= checkDialog.findViewById(R.id.check_amount_glass); //잔
        Button check_amount_bottle= checkDialog.findViewById(R.id.check_amount_bottle);//병
        Button check_amount_ml= checkDialog.findViewById(R.id.check_amount_ml);//ml





        //세부적인 술 종류 선택 - 라디오 다이얼로그
        check_drink_name.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {


                Resources res = getResources();
                String[] arrayDrink={};
                int pick;


                //TODO: 지금은 array파일에서 데이터를 가져왔지만 데이터베이스에서 가져올 수 있도록 해야 함
                // 밑에 코드는 리사이클에서 어떤 것을 선택했는지를 position을 통해 식별할 수 있었는데

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

                                //TODO 다이얼로그에 방금 어떤거 선택했는지 보이기

                            }
                        })
                        .setNeutralButton("술 종류 추가", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setContentView(R.layout.check_dialog_newdrink);
                                EditText newDetailName = findViewById(R.id.check_new_detail_name);
                                EditText newDetailDegree = findViewById(R.id.check_new_detail_degree);
                                EditText newDetailBottle = findViewById(R.id.check_new_detail_bottle);
                                EditText newDetailGlass = findViewById(R.id.check_new_detail_glass);
                                Button newDetailSave = findViewById(R.id.check_new_detail_save);

                                //여기서 입력
                                newDetailName.getText().toString();
                                newDetailDegree.getText().toString();
                                newDetailBottle.getText().toString();
                                newDetailGlass.getText().toString();

                                //확인 버튼을 누르면 저장됨
                                newDetailSave.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //TODO: 새로 입력한 디테일한 술 정보 데이터 베이스에 저장
                                        // 예: newDetailName = 카스 , newDetailDegree = 15(도) , newDetailBottle=16 (ml), newDetailGlass=3(ml)
                                    }
                                });
                            }
                        })
                        .setNegativeButton("취소", null)
                        .create().show();


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
        // 네 버튼
        checkDialog.findViewById(R.id.yesBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 지금 마신 술 데이터베이스에 저장하고 화면에 반영하기
            }
        });
    }




}
