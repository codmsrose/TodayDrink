package com.example.todaydrink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class CheckActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    String currentUser;

    RecyclerView recyclerView;
    CheckAdapter CheckAdapter;
    private ArrayList<CheckItems> mArrayList;

    Dialog checkDialog; // 술 종류 클릭하면 나오는 다이얼로그
    Dialog dialogNew;

    CheckCalculate checkCalculate; //혈중알콜농도 계산

     Button state1;
     Button state2;
     Button state3;
     Button state4;
     Button state5;

     int todayState;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        Intent get_Intent = getIntent();
        currentUser = get_Intent.getStringExtra("currentUser");

        recyclerView = findViewById(R.id.check_recyclerView);

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


        state1= findViewById(R.id.state1);
        state2= findViewById(R.id.state2);
        state3= findViewById(R.id.state3);
        state4= findViewById(R.id.state4);
        state5= findViewById(R.id.state5);





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

                    //TODO: 여기 수정함     파이어베이스에 newDetailDrink도 같이 들어가게 하면 됨
                    final EditText newDrink = new EditText(CheckActivity.this);
                    newDrink.setHint(" 술 종류 (예-소주)");
                    final EditText newDetailDrink = new EditText(CheckActivity.this);
                    newDetailDrink.setHint("술 이름 (예-처음처럼)");

                    AlertDialog.Builder builder = new AlertDialog.Builder(CheckActivity.this);
                    builder.setMessage("추가하고 싶은 술을 입력하세요")
                            .setView(newDrink)
                            .setView(newDetailDrink)
                            .setNegativeButton("취소", null)
                            .setPositiveButton("저장", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 술 종류 추가한거 데이터베이스에 저장
                            // (위스키, 샴페인 같은 큰 종류)
                            CheckAdapter.addItem(new CheckItems(R.drawable.new_drink_bottle, newDrink.getText().toString()));
                            CheckAdapter.itemPositionChange(CheckAdapter.getItemCount()-1,CheckAdapter.getItemCount()-2);
                            CheckAdapter.notifyItemMoved(CheckAdapter.getItemCount()-1, CheckAdapter.getItemCount()-2);
                        }
                    });
                    builder.show();
                }
                else{
                    //TODO: 지금은 리사이클러뷰에서 선택한 아이템을 position으로 접근할 수 있는데
                    // 데이터베이스에서는 n번째 데이터에 접근하기<<같은 것은 안된다고 하셨으니까
                    // 밑의 showDialogDrink(position)에서 positon을 item.getName()으로 바꾸시고
                    // showDialogDrink함수를 수정하시면 됩니다. 함수는 밑에 있어요
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

        // 이런 식으로 하면 아마 가져올 수 있을 겁니다.
        reference.child("User").child(currentUser).child("회원이 추가한 술 종류").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    NewDrinkDetail newDrinkDetail = dataSnapshot.getValue(NewDrinkDetail.class);
                    CheckAdapter.addItem(new CheckItems(R.drawable.pro, newDrinkDetail.name));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        //계산을 위한 객체
        checkCalculate = new CheckCalculate();


    }



    //상태바 업데이트 하는 것
    void updateState(int state) {



        if (state >= 5) {
            state5.setBackgroundColor(getResources().getColor(R.color.purple_500));
            state4.setBackgroundColor(getResources().getColor(R.color.purple_500));
            state3.setBackgroundColor(getResources().getColor(R.color.purple_500));
            state2.setBackgroundColor(getResources().getColor(R.color.purple_500));
            state1.setBackgroundColor(getResources().getColor(R.color.purple_500));
        } else if (state >= 4) {
            state4.setBackgroundColor(getResources().getColor(R.color.purple_500));
            state3.setBackgroundColor(getResources().getColor(R.color.purple_500));
            state2.setBackgroundColor(getResources().getColor(R.color.purple_500));
            state1.setBackgroundColor(getResources().getColor(R.color.purple_500));
        } else if (state >= 3) {

            state3.setBackgroundColor(getResources().getColor(R.color.purple_500));
            state2.setBackgroundColor(getResources().getColor(R.color.purple_500));
            state1.setBackgroundColor(getResources().getColor(R.color.purple_500));
        } else if (state >= 2) {
            state2.setBackgroundColor(getResources().getColor(R.color.purple_500));
            state1.setBackgroundColor(getResources().getColor(R.color.purple_500));
        } else if (state >= 1) {
            state1.setBackgroundColor(getResources().getColor(R.color.purple_500));
        }


    }



    //======다이얼로그==================================


    // TODO: 수정해야 하는 다이얼로그 함수입니다.
    //  일단 매개변수인 int position 을 String drink이런식으로  String을 매개변수로 받게 만들어주세요.

    public void showDialogDrink(int position){
        checkDialog.show(); // 다이얼로그 띄우기


        // TODO: 1. 세부적인 술 종류 선택 - 라디오 다이얼로그
        TextView check_drink_name = checkDialog.findViewById(R.id.check_drink_name);  //술 종류 선택

        check_drink_name.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {


                Resources res = getResources();
                String[] arrayDrink={};
                int pick;

                //TODO: 지금은 array파일에서 데이터를 가져와서 String[] arrayDrink에 저장했지만
                // 데이터베이스에서 가져올 수 있도록 해야 하는데 술 종류(예시 :소주, 맥주)는 이 함수의 매개변수로 데이터베이스에서 검색하면 될 것 같아요!


                // 단순히 arrayDrink에 술 이름을 넣으려고 하니 에러가 떠서 이런 식으로 사용함.
                String[] finalArrayDrink = arrayDrink;
                reference.child("User").child(currentUser).child("회원이 추가한 술 종류").child(check_drink_name.getText().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        NewDrinkDetail newDrinkDetail = snapshot.getValue(NewDrinkDetail.class);
                        finalArrayDrink[0] = newDrinkDetail.name;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                switch(position){
                    //TODO: case 뒤에 arrayDrink[0]과 같은 방식으로는 에러가 뜨네요.. 특정한 "문자열" 식으로 넣어야 하는 듯 합니다.
                    // 새로 추가한 술들까지 case 뒤에 설정해 놔야 하는데 이 부분은 힘들지 않나 싶습니다...
                    case  0:
                        arrayDrink=res.getStringArray(R.array.beer);
                        break;
                    case  1:
                        arrayDrink=res.getStringArray(R.array.soju);
                        break;
                }
                // TODO: 수정헤야하는 코드는 여기까지



                final int[] selectedIndex = {0};   //선택한 디테일한 술이 큰 종류의 술 리스트에서 몇 번째 인지

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

                                //TODO check_drink_name(edit text view)에 어떤 술 선택했는지 보여주기 위해서 데이터베이스에서 꺼내오기(예- 처음처럼)


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
                                String name = newDetailName.getText().toString();
                                String degree = newDetailDegree.getText().toString();
                                String bottle = newDetailBottle.getText().toString();
                                String glass = newDetailGlass.getText().toString();

                                //확인 버튼을 누르면 저장됨
                                newDetailSave.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // 추가할 주종을 데이터베이스에 저장.
                                        //TODO: 위에 큰 종류로 넣는 게 있는데 거기서 값을 넣으면 그 아래로 데이터베이스를 추가할 수 없어서
                                        // 술 추가 버튼 눌렀을 때 정보를 다 받는 식으로 해야할 거 같습니다. (큰 종류는 받을 필요가 없어짐.)
                                        // (키와 값을 따로따로 넣을 수는 없고 한 번에 넣어야 생성됩니다...)
                                        NewDrinkDetail newDrinkDetail = new NewDrinkDetail(name, degree, bottle, glass);
                                        reference.child("User").child(currentUser).child("회원이 추가한 술 종류").child(name).setValue(newDrinkDetail);


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

        //2. 술 마신 양
        EditText check_edit_amount = checkDialog.findViewById(R.id.check_edit_amount); //술 마신 양

        check_edit_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_edit_amount.getText().toString();
            }
        });

        //3. 술 마신 양의 단위  -> 잔, 병, ml 버튼 중 1개만 선택되도록 함
        Button check_amount_glass= checkDialog.findViewById(R.id.check_amount_glass); //잔
        Button check_amount_bottle= checkDialog.findViewById(R.id.check_amount_bottle);//병
        Button check_amount_ml= checkDialog.findViewById(R.id.check_amount_ml);//ml

        //잔,병,ml 중 선택된 것이 pressed에 들어감
        final Button[] pressed = new Button[1];

        class checkAmountClickListener implements Button.OnClickListener {
            @Override
            public void onClick(View view) {

                check_amount_glass.setBackgroundColor(getResources().getColor(R.color.purple_500));
                check_amount_bottle.setBackgroundColor(getResources().getColor(R.color.purple_500));
                check_amount_ml.setBackgroundColor(getResources().getColor(R.color.purple_500));


                if (check_amount_glass.equals(view)) {
                 pressed[0] =check_amount_glass;
                 check_amount_glass.setBackgroundColor(getResources().getColor(R.color.purple_700));
                }
                else if (check_amount_bottle.equals(view)){
                    pressed[0] =check_amount_glass;
                    check_amount_bottle.setBackgroundColor(getResources().getColor(R.color.purple_700));
                }
                else if (check_amount_ml.equals(view)){
                    pressed[0] =check_amount_glass;
                    check_amount_ml.setBackgroundColor(getResources().getColor(R.color.purple_700));
                }
            }
        }




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

                     Calendar calendar = Calendar.getInstance();
                     int year = calendar.get(Calendar.YEAR);
                     int month = calendar.get(Calendar.MONTH);
                     int day = calendar.get(Calendar.DAY_OF_MONTH);
                     // TODO 지금 마신 술 데이터베이스에 저장하기
                     //  마신 술 이름이랑 입력한 양, 단위는 위에서 찾으면 됨
                     //  저장하고 화면에 반영하기 -> activity_check에 있는 show_amout textview에 쓰면 됨
                     //  state=checkCalculate.calculate(  데이터베이스에서 방금 선택한 술의 도수 받아오기, 방금 마신 술 ml);



                     // 오늘 날짜에 어떤 술을 마셨는지 술 이름에 접근.
                     reference.child("User").child(currentUser).child("날짜별 데이터").child(year + "년 " + (month + 1) + "월 " + day + "일")
                             .child(check_drink_name.getText().toString()).addValueEventListener(new ValueEventListener() {
                                 String strBottle;
                                 String strGlass;
                                 // 한 병에 몇 ml, 한 잔에 몇 ml 인지 알아오기 위해
                                 int bottle;
                                 int glass;
                                 int amount;

                         @Override
                         public void onDataChange(@NonNull DataSnapshot snapshot) {
                             // 잔, 병, ML 중 어느 것인지 unit으로 받음.
                             String unit = pressed[0].getText().toString();
                             // 입력 값을 받아옴.
                             String strAmount = check_edit_amount.getText().toString();
                             // 데이터베이스에서 입력한 술의 기본 정보에 접근.
                             reference.child("User").child(currentUser).child("회원이 추가한 술 종류")
                                     .child(check_drink_name.getText().toString()).addValueEventListener(new ValueEventListener() {
                                         @Override
                                         public void onDataChange(@NonNull DataSnapshot snapshot) {
                                             NewDrinkDetail newDrinkDetail = snapshot.getValue(NewDrinkDetail.class);
                                             strBottle = newDrinkDetail.bottle;
                                             strGlass = newDrinkDetail.glass;
                                             bottle = Integer.parseInt(strBottle);
                                             glass = Integer.parseInt(strGlass);
                                             amount = Integer.parseInt(strAmount);
                                         }

                                         @Override
                                         public void onCancelled(@NonNull DatabaseError error) {

                                         }
                                     });
                             // 잔, 병, ML에 따라 단위를 계산하여 데이터베이스에 마신 양 넣어줌.
                             switch (unit) {
                                 case "잔":
                                     Drink drink = new Drink( (amount * glass) / bottle, amount / ((amount * glass) / bottle), amount * glass);
                                     reference.child("User").child(currentUser).child("날짜별 데이터").child(year + "년 " + (month + 1) + "월 " + day + "일")
                                             .child(check_drink_name.getText().toString()).setValue(drink);
                                     break;
                                 case "병":
                                     Drink drink1 = new Drink(amount, 0, amount * bottle);
                                     reference.child("User").child(currentUser).child("날짜별 데이터").child(year + "년 " + (month + 1) + "월 " + day + "일")
                                             .child(check_drink_name.getText().toString()).setValue(drink1);
                                     break;
                                 case "ML":
                                     Drink drink2 = new Drink(amount / bottle, (amount % bottle) / glass, amount);
                                     reference.child("User").child(currentUser).child("날짜별 데이터").child(year + "년 " + (month + 1) + "월 " + day + "일")
                                             .child(check_drink_name.getText().toString()).setValue(drink2);
                                     break;
                             }
                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError error) {

                         }
                     });


                    //TODO:여기서 계산하는 함수 호출
                     // database에서 불러와야 함

                     int tempState;


                     //TODO: currentUser...
                     tempState=CheckCalculate.calculate("0",0,0);
                     if(todayState >= tempState)
                     {
                         todayState = tempState;
                         updateState(todayState);
                     }

                 }
             }
            );
        }
    }