package com.example.andrey.mediaplayer;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.CollationElementIterator;

import static com.example.andrey.mediaplayer.R.id.now_playing;


public class MainActivity extends AppCompatActivity {



    String url;
    String title;
    static String current_notify;

    AudioManager audioManager;
    Intent intent;
    Button start;
    Button stop;
    Button settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView current = (TextView) findViewById(R.id.now_playing);
        current.setText(List.nowMain);

        Intent intent = getIntent();

        String nowPlaying = intent.getStringExtra(List.nowMain);
        current_notify = nowPlaying;
        if (!TextUtils.isEmpty(nowPlaying))
            current.setText(nowPlaying);

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        Intent MainActivityIntent = getIntent();
        url = MainActivityIntent.getStringExtra("url");
        title = MainActivityIntent.getStringExtra("title");
        MainActivityIntent.removeExtra("url");
        if (url != null) {
            start();
        }

        start = (Button) findViewById(R.id.start);
        start.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    start.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0x00000033));
                }
                return false;
            }
        });
        stop = (Button) findViewById(R.id.stop);
        stop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    stop.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0x00330033));
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

    }




   public void onClick(View view) {

            switch (view.getId()) {
                case R.id.start:
                    view.getBackground().clearColorFilter();
                    start();

                    break;
                case R.id.stop:
                    intent = new Intent(this, PlayerService.class);
                    intent.putExtra("url",url);
                    intent.putExtra("title",title);
                    view.getBackground().clearColorFilter();
                    intent.putExtra("action", "stop");
                    startService(intent);
                    break;
                case R.id.settings:
                    view.getBackground().clearColorFilter();
                    final int SHOW_SUBACTIVITY = 1;
                    Intent intent = new Intent();

// определение класса запускаемой активности
                    intent.setClass(this, RadioList.class);
// вызов активности
                    startActivity(intent);
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
            intent.putExtra("title",title);
            startService(intent);
            url = null;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
