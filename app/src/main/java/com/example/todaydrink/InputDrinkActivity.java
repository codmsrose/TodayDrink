package com.example.todaydrink;

import static com.example.todaydrink.CheckCalculate.updateState;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class InputDrinkActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    @SuppressLint("StaticFieldLeak")
    public static Context mContext;
    public String currentUser;

    Button state1, state2, state3, state4, state5, btn_state, btn_game;

    ImageView image_cass, image_terra, image_heineken, image_iseul, image_start, image_jinro;
    TextView text_cass, text_terra, text_heineken, text_iseul, text_start, text_jinro;
    int cass_bottle, terra_bottle, heineken_bottle, iseul_bottle, start_bottle, jinro_bottle = 0;
    int cass_glass, terra_glass, heineken_glass, iseul_glass, start_glass, jinro_glass = 0;
    double cass_ml, terra_ml, heineken_ml, iseul_ml, start_ml, jinro_ml = 0;
    int state = 0;
    static int cass_state = 0;
    static int terra_state = 0;
    static int heineken_state = 0;
    static int iseul_state = 0;
    static int start_state = 0;
    static int jinro_state = 0;
    double alcohol_sum = 0;

    // 데이터베이스에 오늘 날짜에 맞게 저장하기 위해
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_drink);

        // 로그인한 ID를 Intent로 가져옴.
        Intent get_Intent = getIntent();
        currentUser = get_Intent.getStringExtra("currentUser");

        mContext = this;

        state1= findViewById(R.id.state1);
        state2= findViewById(R.id.state2);
        state3= findViewById(R.id.state3);
        state4= findViewById(R.id.state4);
        state5= findViewById(R.id.state5);

        image_cass = findViewById(R.id.image_cass);
        image_terra = findViewById(R.id.image_terra);
        image_heineken = findViewById(R.id.image_heineken);
        image_iseul = findViewById(R.id.image_iseul);
        image_start = findViewById(R.id.image_start);
        image_jinro = findViewById(R.id.image_jinro);

        btn_state = findViewById(R.id.btn_state);
        btn_game = findViewById(R.id.btn_game);
        text_cass = findViewById(R.id.text_cass);
        text_terra = findViewById(R.id.text_terra);
        text_heineken = findViewById(R.id.text_heineken);
        text_iseul = findViewById(R.id.text_iseul);
        text_start = findViewById(R.id.text_start);
        text_jinro = findViewById(R.id.text_jinro);

        // 이 화면에 처음 들어왔을 때는 현재 주량이 안보임.
        text_cass.setVisibility(View.INVISIBLE);
        text_terra.setVisibility(View.INVISIBLE);
        text_heineken.setVisibility(View.INVISIBLE);
        text_iseul.setVisibility(View.INVISIBLE);
        text_start.setVisibility(View.INVISIBLE);
        text_jinro.setVisibility(View.INVISIBLE);

        // 각 술을 마신 데이터를 가져와서 오늘 마신 양에 보여줌.
        reference.child("User").child(currentUser).child("날짜별 데이터").child(year + "년 " + (month + 1) + "월 " + day + "일").child("카스").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(Drink.class) != null) {
                    Drink drink = snapshot.getValue(Drink.class);
                    cass_bottle = drink.getBottle();
                    cass_glass = drink.getGlass();
                    cass_ml = drink.getMl();

                    if (cass_bottle > 0 || cass_glass > 0) {
                        text_cass.setText("카스 : " + cass_bottle + "병 " + cass_glass + "잔");
                        text_cass.setVisibility(View.VISIBLE);
                    }

                    cass_state = CheckCalculate.calculate(currentUser, 4, cass_ml);
                    addState();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("User").child(currentUser).child("날짜별 데이터").child(year + "년 " + (month + 1) + "월 " + day + "일").child("테라").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(Drink.class) != null) {
                    Drink drink = snapshot.getValue(Drink.class);
                    terra_bottle = drink.getBottle();
                    terra_glass = drink.getGlass();
                    terra_ml = drink.getMl();

                    if (terra_bottle > 0 || terra_glass > 0) {
                        text_terra.setText("테라 : " + terra_bottle + "병 " + terra_glass + "잔");
                        text_terra.setVisibility(View.VISIBLE);
                    }

                    terra_state = CheckCalculate.calculate(currentUser, 4.6, terra_ml);
                    addState();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("User").child(currentUser).child("날짜별 데이터").child(year + "년 " + (month + 1) + "월 " + day + "일").child("하이네켄").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(Drink.class) != null) {
                    Drink drink = snapshot.getValue(Drink.class);
                    heineken_bottle = drink.getBottle();
                    heineken_glass = drink.getGlass();
                    heineken_ml = drink.getMl();

                    if (heineken_bottle > 0 || heineken_glass > 0) {
                        text_heineken.setText("하이네켄 : " + heineken_bottle + "병 " + heineken_glass + "잔");
                        text_heineken.setVisibility(View.VISIBLE);
                    }

                    heineken_state = CheckCalculate.calculate(currentUser, 5, heineken_ml);
                    addState();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("User").child(currentUser).child("날짜별 데이터").child(year + "년 " + (month + 1) + "월 " + day + "일").child("참이슬").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(Drink.class) != null) {
                    Drink drink = snapshot.getValue(Drink.class);
                    iseul_bottle = drink.getBottle();
                    iseul_glass = drink.getGlass();
                    iseul_ml = drink.getMl();

                    if (iseul_bottle > 0 || iseul_glass > 0) {
                        text_iseul.setText("참이슬 : " + iseul_bottle + "병 " + iseul_glass + "잔");
                        text_iseul.setVisibility(View.VISIBLE);
                    }

                    iseul_state = CheckCalculate.calculate(currentUser, 16.5, iseul_ml);
                    addState();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("User").child(currentUser).child("날짜별 데이터").child(year + "년 " + (month + 1) + "월 " + day + "일").child("처음처럼").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(Drink.class) != null) {
                    Drink drink = snapshot.getValue(Drink.class);
                    start_bottle = drink.getBottle();
                    start_glass = drink.getGlass();
                    start_ml = drink.getMl();

                    if (start_bottle > 0 || start_glass > 0) {
                        text_start.setText("처음처럼 : " + start_bottle + "병 " + start_glass + "잔");
                        text_start.setVisibility(View.VISIBLE);
                    }

                    start_state = CheckCalculate.calculate(currentUser,16.5,start_ml);
                    addState();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("User").child(currentUser).child("날짜별 데이터").child(year + "년 " + (month + 1) + "월 " + day + "일").child("진로").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(Drink.class) != null) {
                    Drink drink = snapshot.getValue(Drink.class);
                    jinro_bottle = drink.getBottle();
                    jinro_glass = drink.getGlass();
                    jinro_ml = drink.getMl();

                    if (jinro_bottle > 0 || jinro_glass > 0) {
                        text_jinro.setText("진로 : " + jinro_bottle + "병 " + jinro_glass + "잔");
                        text_jinro.setVisibility(View.VISIBLE);
                    }

                    jinro_state=CheckCalculate.calculate(currentUser,16.5,jinro_ml);
                    addState();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // 카스 버튼 클릭 시
        image_cass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 잔 1잔 추가.
                cass_glass++;
                // 5잔이 된 경우 1병을 추가하고 잔을 초기화.
                if (cass_glass % 5 == 0) {
                    cass_bottle++;
                    cass_glass = 0;
                }
                cass_ml = (cass_bottle * 1000) + (cass_glass * 200);

                // 현재 마신 병 수와 잔 수를 보여줌.
                text_cass.setText("카스 : " + cass_bottle + "병 " + cass_glass + "잔");
                text_cass.setVisibility(View.VISIBLE);
                addDrink(currentUser, "카스", cass_bottle, cass_glass, cass_ml);
            }
        });

        image_heineken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 잔 1잔 추가.
                heineken_glass++;
                // 5잔이 된 경우 1병을 추가하고 잔을 초기화.
                if (heineken_glass % 4 == 0) {
                    heineken_bottle++;
                    heineken_glass = 0;
                }
                heineken_ml = (heineken_bottle * 800) + (heineken_glass * 200);

                // 현재 마신 병 수와 잔 수를 보여줌.
                text_heineken.setText("하이네켄 : " + heineken_bottle + "병 " + heineken_glass + "잔");
                text_heineken.setVisibility(View.VISIBLE);
                addDrink(currentUser, "하이네켄", heineken_bottle, heineken_glass, heineken_ml);
            }
        });

        image_terra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                terra_glass++;
                if (terra_glass % 5 == 0) {
                    terra_bottle++;
                    terra_glass = 0;
                }
                terra_ml = (terra_bottle * 1000) + (terra_glass * 200);

                text_terra.setText("테라 : " + terra_bottle + "병 " + terra_glass + "잔");
                text_terra.setVisibility(View.VISIBLE);
                addDrink(currentUser, "테라", terra_bottle, terra_glass, terra_ml);
            }
        });

        image_iseul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iseul_glass++;
                if (iseul_glass % 7 == 0) {
                    iseul_bottle++;
                    iseul_glass = 0;
                }
                iseul_ml = (iseul_bottle * 350) + (iseul_glass * 50);

                text_iseul.setText("참이슬 : " + iseul_bottle + "병 " + iseul_glass + "잔");
                text_iseul.setVisibility(View.VISIBLE);
                addDrink(currentUser, "참이슬", iseul_bottle, iseul_glass, iseul_ml);
            }
        });

        image_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_glass++;
                if (start_glass % 7 == 0) {
                    start_bottle++;
                    start_glass = 0;
                }
                start_ml = (start_bottle * 350) + (start_glass * 50);

                text_start.setText("처음처럼 : " + start_bottle + "병 " + start_glass + "잔");
                text_start.setVisibility(View.VISIBLE);
                addDrink(currentUser, "처음처럼", start_bottle, start_glass, start_ml);
            }
        });

        image_jinro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 잔 1잔 추가.
                jinro_glass++;
                // 5잔이 된 경우 1병을 추가하고 잔을 초기화.
                if (jinro_glass % 7 == 0) {
                    jinro_bottle++;
                    jinro_glass = 0;
                }
                jinro_ml = (jinro_bottle * 350) + (jinro_glass * 50);

                // 현재 마신 병 수와 잔 수를 보여줌.
                text_jinro.setText("진로 : " + jinro_bottle + "병 " + jinro_glass + "잔");
                text_jinro.setVisibility(View.VISIBLE);
                addDrink(currentUser, "진로", jinro_bottle, jinro_glass, jinro_ml);

                // 알콜 계산
            }
        });

        btn_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InputDrinkActivity.this, StateActivity.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
            }
        });

        btn_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InputDrinkActivity.this, GameActivity.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
            }
        });
    }

    public void addDrink(String currentUser, String drinkKind, int bottle, int glass, double ml) {
        Drink drink = new Drink(bottle, glass, ml);

        reference.child("User").child(currentUser).child("날짜별 데이터").child(year + "년 " + (month + 1) + "월 " + day + "일").child(drinkKind).setValue(drink);
    }

    public void addState() {
        state = cass_state + terra_state + heineken_state + iseul_state + start_state + jinro_state;

        updateState(state);
    }

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
}