package com.example.andrey.mediaplayer;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.LightingColorFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    String url;
    String title;
    static String current_notify;

    BroadcastReceiver br;
    AudioManager audioManager;
    Intent intent;
    Button start;
    Button stop;
    Button settings;
    TextView current;
    Button publish;

    String ACTION = "com.example.andrey.mediaplayer.MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        publish = (Button) findViewById(R.id.publish);

        current = (TextView) findViewById(R.id.now_playing);
        current.setText(List.nowMain);

        Intent intent = getIntent();

        String nowPlaying = intent.getStringExtra(List.nowMain);

        if (!TextUtils.isEmpty(nowPlaying))
            current.setText(nowPlaying);

        publish.setVisibility(View.INVISIBLE);

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        Intent MainActivityIntent = getIntent();
        url = MainActivityIntent.getStringExtra("url");
        title = MainActivityIntent.getStringExtra("title");

        MainActivityIntent.removeExtra("url");

        if (url != null) {
            start();
        }

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Boolean flag = intent.getBooleanExtra("isPlaying", false);
                if (flag) {
                    publish.setVisibility(View.VISIBLE);
                } else {
                    publish.setVisibility(View.INVISIBLE);
                }

            }
        };

        IntentFilter intFilt = new IntentFilter(ACTION);
        registerReceiver(br, intFilt);

        start = (Button) findViewById(R.id.start);

        start.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    start.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0x00003333));
                }
                return false;
            }
        });

        stop = (Button) findViewById(R.id.stop);

        stop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    stop.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0x00003333));
                }
                return false;
            }
        });


        settings = (Button) findViewById(R.id.settings);

        settings.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    settings.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0x00003333));
                }
                return false;
            }
        });



        publish.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    publish.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0x00330033));
                }
                return false;
            }
        });

    }




   public void onClick(View view) {

            switch (view.getId()) {

                case R.id.start:
                    current_notify = title;
                    view.getBackground().clearColorFilter();
                    start();
                    break;

                case R.id.stop:
                    intent = new Intent(this, PlayerService.class);
                    intent.putExtra("url",url);
                    intent.putExtra("title", title);
                    publish.setVisibility(View.INVISIBLE);
                    view.getBackground().clearColorFilter();
                    intent.putExtra("action", "stop");
                    current.setText("Выберите радио");
                    startService(intent);
                    break;

                case R.id.settings:
                    Intent intent = new Intent();
                    view.getBackground().clearColorFilter();
                    intent.setClass(this, RadioList.class);
                    startActivity(intent);
                    break;

                case R.id.publish:
                    Intent intent_vk = new Intent();
                    view.getBackground().clearColorFilter();
                    intent_vk.setClass(this, Publish.class);
                    startActivity(intent_vk);
                    break;
            }

    }

    @Override
    public void onBackPressed()
    {
        finishAffinity();
        super.onBackPressed();  // optional depending on your needs
    }

    public void start() {
        if (url == null) {
            Toast.makeText(this, "Не выбрано радио", Toast.LENGTH_SHORT).show();
        } else {
            intent = new Intent(this, PlayerService.class);
            intent.putExtra("url", url);
            intent.putExtra("action", "start");
            intent.putExtra("title", title);
            startService(intent);
            intent.putExtra("action", "notify");
            url = null;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }

}
