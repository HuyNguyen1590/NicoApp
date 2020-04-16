package com.example.project_1.MiniGame01;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_1.MainActivity;
import com.example.project_1.R;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MiniGame01 extends AppCompatActivity {
    Button btn_mini_game_play, btn_mini_game_quit;
    TextView tv_score, tv_cd;
    ImageView iv_hole01, iv_hole02, iv_hole03, iv_hole04, iv_hole05, iv_hole06, iv_hole07, iv_hole08, iv_hole09;
    int score=0;
    int time = 30;
    private ArrayList<ImageView> hole_arr = new ArrayList<>();
    int checkLastRnd=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini_game01);

        final Handler handler = new Handler();


        btn_mini_game_play = findViewById(R.id.btn_mini_game_play);
        btn_mini_game_quit = findViewById(R.id.btn_mini_game_quit);

        tv_score = findViewById(R.id.tv_score);
        tv_cd = findViewById(R.id.tv_cd);

        iv_hole01 = findViewById(R.id.iv_hole_1);
        iv_hole02 = findViewById(R.id.iv_hole_2);
        iv_hole03 = findViewById(R.id.iv_hole_3);
        iv_hole04 = findViewById(R.id.iv_hole_4);
        iv_hole05 = findViewById(R.id.iv_hole_5);
        iv_hole06 = findViewById(R.id.iv_hole_6);
        iv_hole07 = findViewById(R.id.iv_hole_7);
        iv_hole08 = findViewById(R.id.iv_hole_8);
        iv_hole09 = findViewById(R.id.iv_hole_9);

        hole_arr.add(iv_hole01);
        hole_arr.add(iv_hole02);
        hole_arr.add(iv_hole03);
        hole_arr.add(iv_hole04);
        hole_arr.add(iv_hole05);
        hole_arr.add(iv_hole06);
        hole_arr.add(iv_hole07);
        hole_arr.add(iv_hole08);
        hole_arr.add(iv_hole09);

        iv_hole01.setVisibility(View.INVISIBLE);
        iv_hole02.setVisibility(View.INVISIBLE);
        iv_hole03.setVisibility(View.INVISIBLE);
        iv_hole04.setVisibility(View.INVISIBLE);
        iv_hole05.setVisibility(View.INVISIBLE);
        iv_hole06.setVisibility(View.INVISIBLE);
        iv_hole07.setVisibility(View.INVISIBLE);
        iv_hole08.setVisibility(View.INVISIBLE);
        iv_hole09.setVisibility(View.INVISIBLE);

        btn_mini_game_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_mini_game_play.setEnabled(false);
                score=0;
                time = 30;
                tv_score.setText(score+"pt");
                Toast.makeText(MiniGame01.this, "PLAY", Toast.LENGTH_SHORT).show();
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        int rnd = new Random().nextInt(hole_arr.size());
                        while (rnd==checkLastRnd){
                            rnd = new Random().nextInt(hole_arr.size());
                        }
                        checkLastRnd = rnd;
                        show(hole_arr.get(rnd));
                        handler.postDelayed(this,500);
                    }
                };
                final Runnable runnable2 = new Runnable() {
                    @Override
                    public void run() {
                        time--;
                        tv_cd.setText("Thời gian: "+time);
                        handler.postDelayed(this,1000);
                    }
                };
                handler.postDelayed(runnable,0);
                Runnable runnable1 = new Runnable() {
                    @Override
                    public void run() {
                        handler.removeCallbacks(runnable);
                        handler.removeCallbacks(runnable2);

                        iv_hole01.setVisibility(View.INVISIBLE);
                        iv_hole02.setVisibility(View.INVISIBLE);
                        iv_hole03.setVisibility(View.INVISIBLE);
                        iv_hole04.setVisibility(View.INVISIBLE);
                        iv_hole05.setVisibility(View.INVISIBLE);
                        iv_hole06.setVisibility(View.INVISIBLE);
                        iv_hole07.setVisibility(View.INVISIBLE);
                        iv_hole08.setVisibility(View.INVISIBLE);
                        iv_hole09.setVisibility(View.INVISIBLE);

                        btn_mini_game_play.setEnabled(true);
                        Toast.makeText(MiniGame01.this, "Chúc mừng bạn đã được "+score+" điểm!", Toast.LENGTH_LONG).show();
                    }
                };

                handler.postDelayed(runnable2,0);
                //Time up! runnable1 to stop the game
                handler.postDelayed(runnable1,30000);
            }
        });
        btn_mini_game_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void show(final ImageView iv){
        iv.setVisibility(View.VISIBLE);
        final ObjectAnimator alpha = ObjectAnimator.ofFloat(iv,"alpha",0f,1f);
        alpha.setDuration(1000);

        alpha.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                iv.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      score++;
                      MainActivity.voice = MediaPlayer.create(v.getContext(), R.raw.quote07);
                      MainActivity.voice.start();
                      Vibrator vibrator = (Vibrator) v.getContext().getSystemService(VIBRATOR_SERVICE);
                      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                          VibrationEffect effect = VibrationEffect.createOneShot(200,VibrationEffect.DEFAULT_AMPLITUDE);
                          vibrator.vibrate(effect);
                      }else {
                          if (vibrator.hasVibrator()) {
                              vibrator.vibrate(500);
                          }
                      }
                      tv_score.setText(score+"pt");
                      iv.setVisibility(View.INVISIBLE);
                  }
              });
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                iv.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(alpha);
        animatorSet.start();
    }
}
