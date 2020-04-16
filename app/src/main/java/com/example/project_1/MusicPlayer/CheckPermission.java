package com.example.project_1.MusicPlayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.project_1.R;

public class CheckPermission extends AppCompatActivity {
    public static final String CHANNEL_ID="MusicPlayerChannel";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            if (grantResults.length==1 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Cấp quyền thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,MusicPlayerActivity.class);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(this, "Cấp quyền thất bại", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_permission);

        createNotificationChannel();// Tạo channel cho notification

        //check permission -> xin cấp quyền
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else {
            Intent intent = new Intent(this,MusicPlayerActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Music Player Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

    }
}
