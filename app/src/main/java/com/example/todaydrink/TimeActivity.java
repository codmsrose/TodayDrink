package com.example.todaydrink;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import android.os.CountDownTimer;

import java.util.Calendar;

public class TimeActivity extends AppCompatActivity implements View.OnClickListener{

    TextView time_text;

    AlertDialog customDialog;
    Calendar calendar = Calendar.getInstance();
    private int setHour=0;
    private int setMinute=0;
    private int setSec=0;
    private long time=0;
    private long tempTime = 0;

    private CountDownTimer Timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        //view 객체 획득
        time_text = findViewById(R.id.time_text);

        //버튼 이벤트 등록
        time_text.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int min) {
                setHour = hourOfDay;
                setMinute = min;
                time=(Long.valueOf(setHour-hour)*3600000)+(Long.valueOf(setMinute-minute)*60000)-Long.valueOf(second)*1000;
                //time_text.setText( Integer.toString(setHour) +" : "+Integer.toString(setMinute));
                TimerRest timer = new TimerRest(time, 1000);
                timer.start();
            }
        }, hour, minute,true);
        timePickerDialog.show();

    }


    class TimerRest extends CountDownTimer {
        public TimerRest(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tempTime=millisUntilFinished;
            updateTimer();
        }

        @Override
        public void onFinish() {
            time_text.setText("집에 갈 시간입니다.");
            sendNotification();
        }
    }

    private void updateTimer(){

        time_text.setText(String.valueOf(tempTime));


        int text_hour = (int)tempTime/3600000;
        int text_min = (int) tempTime % 3600000/60000;
        int text_sec = (int) tempTime%3600000%60000/1000;

        String textTime="";

        textTime=text_hour+" :  "+text_min +" : "+text_sec;

        /*분이 10보다 작으면 앞에 0 붙임;
        if(text_min<10) textTime+="0";
        textTime += text_min +":";
         */

        time_text.setText(textTime);

    }

    public void sendNotification(){
        //알림(Notification)을 관리하는 관리자 객체를 운영체제(Context)로부터 소환하기
        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //Notification 객체를 생성해주는 건축가객체 생성(AlertDialog 와 비슷)
        NotificationCompat.Builder builder= null;

        //Oreo 버전(API26 버전)이상에서는 알림시에 NotificationChannel 이라는 개념이 필수 구성요소가 됨.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            String channelID="channel_01"; //알림채널 식별자
            String channelName="MyChannel01"; //알림채널의 이름(별명)

            //알림채널 객체 만들기
            NotificationChannel channel= new NotificationChannel(channelID,channelName,NotificationManager.IMPORTANCE_DEFAULT);

            //알림매니저에게 채널 객체의 생성을 요청
            notificationManager.createNotificationChannel(channel);

            //알림건축가 객체 생성
            builder=new NotificationCompat.Builder(TimeActivity.this, channelID);


        }else{
            //알림 건축가 객체 생성
            builder= new NotificationCompat.Builder(TimeActivity.this);
        }

        //건축가에게 원하는 알림의 설정작업
        builder.setSmallIcon(android.R.drawable.ic_menu_view);

        //상태바를 드래그하여 아래로 내리면 보이는
        //알림창(확장 상태바)의 설정
        builder.setContentTitle("오늘의 술자리");//알림창 제목
        builder.setContentText("집에 가야할 사람이 있습니다!!");//알림창 내용

        //건축가에게 알림 객체 생성하도록
        Notification notification=builder.build();

        //알림매니저에게 알림(Notify) 요청
        notificationManager.notify(1, notification);

        //알림 요청시에 사용한 번호를 알림제거 할 수 있음.
        //notificationManager.cancel(1);
    }
}