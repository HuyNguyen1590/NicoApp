package com.example.project_1.MusicPlayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.project_1.MainActivity;
import com.example.project_1.R;

import static com.example.project_1.MusicPlayer.CheckPermission.CHANNEL_ID;

public class NotifyService extends Service {
    public static Notification notification;
    public         NotificationCompat.Action action;

    public PendingIntent makePendingIntent(String name)
    {
        Intent intent = new Intent(this, MusicPlayerNotification.class);
        intent.setAction(name);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        return pendingIntent;
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String songName = intent.getStringExtra("songName");
        String artist = intent.getStringExtra("artist");
        action = new NotificationCompat.Action(android.R.drawable.ic_media_pause,"PlayOrPause",makePendingIntent("PlayOrPause"));
        if (MainActivity.player.isPlaying()){
            action = new NotificationCompat.Action(android.R.drawable.ic_media_pause,"PlayOrPause",makePendingIntent("PlayOrPause"));

        }else {
            action = new NotificationCompat.Action(android.R.drawable.ic_media_play,"PlayOrPause",makePendingIntent("PlayOrPause"));

        }
        Intent openApp = new Intent(this, MainActivity.class);
        PendingIntent pendingOpenApp = PendingIntent.getActivity(this,0,openApp,0);
        notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle(songName)
                .setContentIntent(pendingOpenApp)
                .setContentText(artist)
                .setSmallIcon(R.mipmap.ic_launcher)
                .addAction(android.R.drawable.ic_media_previous,"Prev",makePendingIntent("Prev"))
                .addAction(action)
                .addAction(android.R.drawable.ic_media_next,"Next",makePendingIntent("Next"))
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle())
                .build();

        startForeground(1,notification);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
