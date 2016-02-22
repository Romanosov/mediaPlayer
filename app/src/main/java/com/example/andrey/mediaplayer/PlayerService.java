package com.example.andrey.mediaplayer;


import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

public class PlayerService extends Service {

    MediaPlayer mediaPlayer;


    @Override
    public void onCreate() {

        super.onCreate();


    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra("url");
        String action = intent.getStringExtra("action");
        String title = intent.getStringExtra("title");
        Player(url, action);





        return Service.START_NOT_STICKY;
    }

    void Player(String url, String action) {
        if (action.equals("stop")) {
            releaseMP();
        } else {
            if (mediaPlayer == null) {
                try {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDataSource(url);
                    mediaPlayer.prepareAsync();

                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mediaPlayer.start();
                        }
                    });
                }  catch (Exception e) {
                    Toast.makeText(this, "Ошибка, проверьте интернет подключение", Toast.LENGTH_SHORT).show();
                    releaseMP();
                }
            } else {
                releaseMP();
                Player(url, action);
            }
        }
    }

    void releaseMP() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        releaseMP();
    }
}
