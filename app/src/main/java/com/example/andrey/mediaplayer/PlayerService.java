package com.example.andrey.mediaplayer;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

public class PlayerService extends Service {

    MediaPlayer mediaPlayer;
    NotificationManager notify;


    @Override
    public void onCreate() {

        super.onCreate();
        notify = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


    }

    public int onStartCommand(Intent intent, int flags, int startId) {



        String url = intent.getStringExtra("url");
        String action = intent.getStringExtra("action");
        Player(url, action);


        return Service.START_NOT_STICKY;
    }

    void nowPlayingNotification() {

        //Bitmap li = new BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Notification.Builder notification_itself = new Notification.Builder(this)
               // .setLargeIcon(li)
                .setSmallIcon(R.mipmap.frog)
                .setContentTitle(MainActivity.current_notify)
                .setContentText("сейчас играет");
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.current_notify, "Now playing");
        //PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Toast.makeText(this, MainActivity.current_notify, Toast.LENGTH_SHORT).show();
        notify.notify(1, notification_itself.build());

        startForeground(1, notification_itself.build());
    }

    public IBinder onBind(Intent arg0) {
        return null;
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
                    nowPlayingNotification();
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
                stopForeground(true);
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseMP();
    }

}
