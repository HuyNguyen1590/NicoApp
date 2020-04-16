package com.example.project_1.MusicPlayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_1.R;

public class MusicPlayerActivity extends AppCompatActivity {
    int UPDATE_FREQUENCY = 500;
    int STEP_VALUE = 4000;
    int POSITION=0;
    TextView tv_fileduocchon;
    SeekBar seekbar;
    public static MediaPlayer player;
    ImageButton bt_play, bt_prev, bt_next, bt_rew, bt_ff;
    ListView lv;
    MusicPlayerAdapter adapter;
    boolean da_play = true;
    String filehientai = "";
    String tenfilehientai ="";
    String tentacgia = "";
    boolean dichuyenseekbar = false;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        //Bắt sự kiện từ button notification
        registerReceiver(broadCastReceiver,new IntentFilter("android.MusicPauseOrPlay"));
        registerReceiver(broadCastReceiver,new IntentFilter("android.MusicNext"));
        registerReceiver(broadCastReceiver,new IntentFilter("android.MusicPrev"));

        tv_fileduocchon = (TextView) findViewById(R.id.selectedfile);
        bt_play = (ImageButton) findViewById(R.id.play);
        bt_next = (ImageButton) findViewById(R.id.next);
        bt_ff = (ImageButton) findViewById(R.id.ff);
        bt_prev = (ImageButton) findViewById(R.id.prev);
        bt_rew = (ImageButton) findViewById(R.id.rew);
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        lv = (ListView) findViewById(R.id.list);

            cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.MediaColumns.TITLE + " ASC");

            adapter = new MusicPlayerAdapter(this, R.layout.music_list_item, cursor);

            lv.setAdapter(adapter);
            player = new MediaPlayer();

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    filehientai = (String) view.getTag();
                    POSITION = position;
                    lv.setSelection(POSITION);
                    tenfilehientai = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.TITLE));
                    tentacgia = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
                    Toast.makeText(MusicPlayerActivity.this, tenfilehientai, Toast.LENGTH_SHORT).show();
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
            bt_ff.setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View v) {
                    int seekto = player.getCurrentPosition() + STEP_VALUE;
                    if (seekto > player.getDuration())
                        seekto = player.getDuration();
                    player.pause();

                    player.seekTo(seekto);

                    player.start();

                    updatePosition();

                }

            });
            bt_rew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int seekto = player.getCurrentPosition() - STEP_VALUE;
                    if (seekto < 0)
                        seekto = 0;
                    player.pause();
                    player.seekTo(seekto);
                    player.start();
                    updatePosition();
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
                Toast.makeText(MusicPlayerActivity.this, tenfilehientai, Toast.LENGTH_SHORT).show();
                batdauphatnhac(filehientai);
            }
        });
    }

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
        Toast.makeText(MusicPlayerActivity.this, tenfilehientai, Toast.LENGTH_SHORT).show();
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
        Toast.makeText(MusicPlayerActivity.this, tenfilehientai, Toast.LENGTH_SHORT).show();
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
}
