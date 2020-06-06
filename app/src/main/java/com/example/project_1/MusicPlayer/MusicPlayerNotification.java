package com.example.project_1.MusicPlayer;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


public class MusicPlayerNotification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getAction();
        switch (name) {
            case "PlayOrPause":
                context.sendBroadcast(new Intent("android.MusicPauseOrPlay"));
                break;
            case "Next":
                context.sendBroadcast(new Intent("android.MusicNext"));
                break;
            case "Prev":
                context.sendBroadcast(new Intent("android.MusicPrev"));
                break;
            case "Stop":
                context.sendBroadcast(new Intent("android.MusicStop"));
                break;
        }
    }

}
