package com.example.project_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.example.project_1.AlarmService.AlarmAdapter;
import com.example.project_1.DAO.AlarmDAO;
import com.example.project_1.MiniGame01.MiniGame01;
import com.example.project_1.Model.Alarm;
import com.example.project_1.MusicPlayer.CheckPermission;
import com.example.project_1.MusicPlayer.MusicPlayerActivity;
import com.example.project_1.MusicPlayer.MusicPlayerAdapter;
import com.example.project_1.MusicPlayer.MusicPlayerNotification;
import com.example.project_1.MusicPlayer.NotifyService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ImageView iv_character;
    Button btn_game;
    TextView tv_name, tv_message, clock_time;
    public static MediaPlayer voice;
    // Message array
    private ArrayList<String> message_arr = new ArrayList<>();
    private ArrayList<Integer> voice_arr = new ArrayList<>();
    // Alarm
    ListView lv_alarm;
    List<Alarm> alarmList = new ArrayList<>();
    AlarmAdapter alarmAdapter;
    ImageButton alarm_add;
    AlarmDAO alarmDAO;
    String [] days = new String[]{"Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật"};
    ArrayList<String> repeatDays = new ArrayList<>();
    String repeat = "";
    //Media player
    int UPDATE_FREQUENCY = 500;
    int POSITION=0;
    TextView tv_fileduocchon;
    SeekBar seekbar;
    MusicPlayerAdapter adapter;
    public static MediaPlayer player;
    ImageButton bt_play, bt_prev, bt_next, bt_list_music;
    ListView lv;
    boolean da_play = true;
    String filehientai = "";
    String tenfilehientai ="";
    String tentacgia = "";
    boolean dichuyenseekbar = false;
    Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Character message
        message_arr.add("みんなのアイドルにこにーだよ～にっこにっこにー☆");
        message_arr.add("にこがお手伝いしちゃうよ♪");
        message_arr.add("お呼びですかご主人様？");
        message_arr.add("きゃー、くすぐったいー");
        message_arr.add("え～、やだ、なんですか～？");
        message_arr.add("も～だめですよぉ～");
        message_arr.add("好きだよ♪");

        //Voice array
        voice_arr.add(R.raw.quote01);
        voice_arr.add(R.raw.quote02);
        voice_arr.add(R.raw.quote03);
        voice_arr.add(R.raw.quote04);
        voice_arr.add(R.raw.quote05);
        voice_arr.add(R.raw.quote06);
        voice_arr.add(R.raw.quote07);

        iv_character = findViewById(R.id.iv_character);
        btn_game = findViewById(R.id.btn_game);
        tv_name = findViewById(R.id.tv_characterName);
        tv_message = findViewById(R.id.tv_characterMess);
        clock_time = findViewById(R.id.clock_time);

        //Hide message
        tv_name.setVisibility(View.INVISIBLE);
        tv_message.setVisibility(View.INVISIBLE);


        btn_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MiniGame01.class);
                startActivity(intent);
            }
        });
        iv_character.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show
                try {
                    lv_alarm.setVisibility(View.GONE);
                }catch (Exception e){

                }
                tv_name.setVisibility(View.VISIBLE);
                tv_message.setVisibility(View.VISIBLE);
                int rnd = new Random().nextInt(message_arr.size());
                try{
                    voice.stop();
                    voice.reset();
                }catch (Exception e){

                }
                voice = MediaPlayer.create(MainActivity.this,voice_arr.get(rnd));
                voice.start();
                tv_message.setText(message_arr.get(rnd));
                jumpAnimation();
                changeMessage();
            }
        });
        //Clock
        clockHandler.postDelayed(updateClock,0);
        //Media player
        //Bắt sự kiện từ button notification
        registerReceiver(broadCastReceiver,new IntentFilter("android.MusicPauseOrPlay"));
        registerReceiver(broadCastReceiver,new IntentFilter("android.MusicNext"));
        registerReceiver(broadCastReceiver,new IntentFilter("android.MusicPrev"));

        tv_fileduocchon = (TextView) findViewById(R.id.selectedfile);
        bt_play = (ImageButton) findViewById(R.id.play);
        bt_next = (ImageButton) findViewById(R.id.next);
        bt_prev = (ImageButton) findViewById(R.id.prev);
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        lv = (ListView) findViewById(R.id.list);
        bt_list_music = findViewById(R.id.list_music);
        bt_list_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lv.getVisibility()==View.GONE)
                lv.setVisibility(View.VISIBLE);
                else
                    lv.setVisibility(View.GONE);
            }
        });

        cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.MediaColumns.TITLE + " ASC");

        adapter = new MusicPlayerAdapter(this, R.layout.music_list_item, cursor);

        lv.setAdapter(adapter);
        player = new MediaPlayer();
        tv_fileduocchon.setText(cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.TITLE)));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                filehientai = (String) view.getTag();
                POSITION = position;
                lv.setSelection(POSITION);
                tenfilehientai = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.TITLE));
                tentacgia = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
                Toast.makeText(MainActivity.this, tenfilehientai, Toast.LENGTH_SHORT).show();
                batdauphatnhac(filehientai);
            }
        });
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextMusic();
            }
        });

        bt_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevMusic();
            }
        });

        bt_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMusic();
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (dichuyenseekbar == true)
                    player.seekTo(progress);
            }



            @Override

            public void onStartTrackingTouch(SeekBar seekBar) {

                dichuyenseekbar = true;

            }



            @Override

            public void onStopTrackingTouch(SeekBar seekBar) {

                dichuyenseekbar = false;

            }

        });
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (POSITION == cursor.getCount() - 1) {
                    POSITION = 0;
                    lv.setSelection(POSITION);
                    cursor.moveToFirst();
                } else {
                    POSITION = POSITION + 1;
                    lv.setSelection(POSITION);
                    cursor.moveToPosition(POSITION);
                }
                filehientai = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
                tenfilehientai = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.TITLE));
                Toast.makeText(MainActivity.this, tenfilehientai, Toast.LENGTH_SHORT).show();
                batdauphatnhac(filehientai);
            }
        });
        // List Alarm
        lv_alarm = findViewById(R.id.alarm_list);
        alarm_add = findViewById(R.id.alarm_add);
        clock_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lv_alarm.getVisibility() == View.GONE) {
                    lv_alarm.setVisibility(View.VISIBLE);
                    alarm_add.setVisibility(View.VISIBLE);
                    tv_message.setVisibility(View.GONE);
                    tv_name.setVisibility(View.GONE);
                } else {
                    lv_alarm.setVisibility(View.GONE);
                    alarm_add.setVisibility(View.GONE);
                }
            }
        });
        alarm_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alarm_picker_dialog, null );
                final TimePicker timePicker = dialogView.findViewById(R.id.alarm_picker);
                Button btnConfirm = dialogView.findViewById(R.id.alarm_confirm);
                Button btnCancel = dialogView.findViewById(R.id.alarm_cancel);
                final TextView tvAlarmRepeat = dialogView.findViewById(R.id.alarm_repeat);

                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                tvAlarmRepeat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        repeatDays.clear();
                        repeat="";
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(v.getContext());
                        builder1.setMultiChoiceItems(days, null, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked){
                                    repeatDays.add(days[which]);
                                }else {
                                    repeatDays.remove(days[which]);
                                }
                            }
                        })
                                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Collections.sort(repeatDays);
                                        for (String day: repeatDays) {
                                            if (day==null){
                                                day="";
                                            }else {
                                                repeat+=day+" ";
                                            }
                                        }
                                        tvAlarmRepeat.setText("Lặp lại: "+repeat);
                                    }
                                })
                                .setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        builder1.show();
                    }
                });
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Alarm alarm = new Alarm();
                        alarm.setStt(alarmList.size());
                        alarm.setHour(timePicker.getHour()+"");
                        alarm.setMin(timePicker.getMinute()+"");
                        alarm.setRepeat(repeat);
                        alarm.setStatus("on");
                        alarmDAO.insertAlarm(alarm);
                        alarmList.add(alarm);
                        alarmAdapter.notifyDataSetChanged();
                        repeat="";
                        alertDialog.cancel();
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });
            }
        });
        alarmDAO = new AlarmDAO(this);
        alarmList = alarmDAO.getAllAlarm();
        alarmAdapter = new AlarmAdapter(this, R.layout.alarm_item, alarmList);
        lv_alarm.setAdapter(alarmAdapter);
    }
    private void jumpAnimation(){
        ObjectAnimator character = ObjectAnimator.ofFloat(iv_character,"translationY",-100);
        character.setDuration(500);
        ObjectAnimator character1 = ObjectAnimator.ofFloat(iv_character,"translationY",100);
        character1.setDuration(500);
        ObjectAnimator character2 = ObjectAnimator.ofFloat(iv_character,"translationY",0);
        character2.setDuration(500);

        character.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(character).before(character1);
        animatorSet.play(character2).after(character1);
        animatorSet.start();
    }
    private void changeMessage(){
        ObjectAnimator message_alpha = ObjectAnimator.ofFloat(tv_message,"alpha",0f,1f);
        message_alpha.setDuration(500);
        ObjectAnimator message_scale = ObjectAnimator.ofFloat(tv_message,"scaleX",0f,1f);
        message_scale.setDuration(500);
        ObjectAnimator characterName_alpha = ObjectAnimator.ofFloat(tv_name,"alpha",0f,1f);
        characterName_alpha.setDuration(500);
        ObjectAnimator characterName_scale = ObjectAnimator.ofFloat(tv_name,"scaleX",0f,1f);
        characterName_scale.setDuration(500);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(message_alpha).with(message_scale).with(characterName_alpha).with(characterName_scale);
        animatorSet.start();
    }
    //Media Player
    public void nextMusic() {
        if (POSITION == cursor.getCount() - 1) {
            POSITION = 0;
            lv.setSelection(POSITION);
            cursor.moveToFirst();
        } else {
            POSITION = POSITION + 1;
            lv.setSelection(POSITION);
            cursor.moveToPosition(POSITION);
        }
        filehientai = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
        tenfilehientai = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.TITLE));
        tentacgia = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
        Toast.makeText(MainActivity.this, tenfilehientai, Toast.LENGTH_SHORT).show();
        batdauphatnhac(filehientai);}

    public void prevMusic() {
        if (POSITION == 0) {
            POSITION = cursor.getCount() - 1;
            lv.setSelection(POSITION);
            cursor.moveToLast();
        } else {
            POSITION = POSITION - 1;
            lv.setSelection(POSITION);
            cursor.moveToPosition(POSITION);
        }
        filehientai = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
        tenfilehientai = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.TITLE));
        tentacgia = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
        Toast.makeText(MainActivity.this, tenfilehientai, Toast.LENGTH_SHORT).show();
        batdauphatnhac(filehientai);
    }

    public void playMusic() {
        if (player.isPlaying())//dang phat
        {
            handler.removeCallbacks(updatePositionRunnable);
            player.pause();
            bt_play.setImageResource(android.R.drawable.ic_media_play);
        } else//dang pause
        {
            if (da_play == true)//dang chay thi chay tiep
            {
                player.start();
                bt_play.setImageResource(android.R.drawable.ic_media_pause);
                updatePosition();
            } else //chua chay ma
            {
                batdauphatnhac(filehientai);
            }
        }
    }
    private final Handler handler = new Handler();
    private final Runnable updatePositionRunnable = new Runnable() {
        public void run() {
            updatePosition();
        }
    };
    private void updatePosition(){

        handler.removeCallbacks(updatePositionRunnable);

        seekbar.setProgress(player.getCurrentPosition());

        handler.postDelayed(updatePositionRunnable, UPDATE_FREQUENCY);

    }

    private void batdauphatnhac(String filehientai) {
        tv_fileduocchon.setText(tenfilehientai);
        tv_fileduocchon.setSelected(true);
        seekbar.setProgress(0);
        player.stop();
        player.reset();
        try {
            player.setDataSource(filehientai);
            player.prepare();
            player.start();
        } catch (Exception e) {
        }
        seekbar.setMax(player.getDuration());
        bt_play.setImageResource(android.R.drawable.ic_media_pause);
        da_play = true;
        startService();
        updatePosition();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.stop();
        player.reset();
        player.release();
        player = null;
        handler.removeCallbacks(updatePositionRunnable);
        unregisterReceiver(broadCastReceiver);
        stopService();
    }
    public void startService(){
        String songName = tenfilehientai;
        String artist = tentacgia;
        Intent intent = new Intent(this, NotifyService.class);
        intent.putExtra("songName", songName);
        intent.putExtra("artist",artist);
        ContextCompat.startForegroundService(this,intent);
    }
    public void stopService(){
        Intent intent = new Intent(this,NotifyService.class);
        stopService(intent);
    }

    MusicPlayerNotification broadCastReceiver = new MusicPlayerNotification(){
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case "android.MusicPauseOrPlay":
                    startService();
                    playMusic();
                    break;
                case "android.MusicNext":
                    nextMusic();
                    break;
                case "android.MusicPrev":
                    prevMusic();
                    break;
            }
        }
    };

    //clock
    int clockCheck = 0;
    final Handler clockHandler = new Handler();
    private Runnable updateClock = new Runnable()
    {
        public void run() {
            String time = updateTimer();
            String hour = time.substring(0,2);
            String min = time.substring(2,4);
            if (clockCheck == 1) {
                clockCheck--;
                clock_time.setText(hour + ":" + min);
                clockHandler.postDelayed(updateClock, 500);
            }else {
                clockCheck++;
                clock_time.setText(hour + " " + min);
                clockHandler.postDelayed(updateClock, 500);
            }
        }
    };
    private String updateTimer(){
        Date date = new Date();
        String hour = date.getHours()+"";
        String min = date.getMinutes()+"";
        if (date.getHours()<10){
            hour="0"+hour;
        }
        if (date.getMinutes()<10){
            min="0"+min;
        }
        return hour+min;
    }

}

