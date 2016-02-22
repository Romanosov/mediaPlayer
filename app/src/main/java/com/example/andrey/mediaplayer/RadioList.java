package com.example.andrey.mediaplayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.Map;
import java.util.Set;

/**
 * Created by andrey on 22.02.16.
 */
public class RadioList extends Activity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    SharedPreferences sPref;
    Button button;
    BroadcastReceiver br;
    public final static String ACTION = "com.example.andrey.mediaplayer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.list);



    mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

    // если мы уверены, что изменения в контенте не изменят размер layout-а RecyclerView
    // передаем параметр true - это увеличивает производительность
    br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String url = intent.getStringExtra("url");
            removeRecord(url);
        }
    };

        IntentFilter intFilt = new IntentFilter(ACTION);
        registerReceiver(br, intFilt);

    restart();

    }

    public void removeRecord(String key) {
        System.out.print("(((((((((");
        SharedPreferences sPref = getSharedPreferences("new",MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();

        ed.remove(key);
        ed.commit();
        restart();

    }

    public void restart() {
        String [][] url;
        url = loadPreferense();
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // создаем адаптер
        final FragmentManager fm = getFragmentManager();
        mAdapter = new List(url, this);
        mRecyclerView.setAdapter(mAdapter);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addNewRadio dialog = new addNewRadio();
                dialog.show(fm, "wow");
            }
        });
    }



    public String[][] loadPreferense() {
        String[][] text = null;
        Set<String> key;
        Map<String, ? > map = null;

        try {
            sPref = getSharedPreferences("new", MODE_PRIVATE);

                map = sPref.getAll();
                if (map.size() < 4) {
                    text = createPreferense(RadioList.getDataSet());
                } else {

                    key = map.keySet();
                    map.toString();
                    System.out.print(map.size());
                    text = new String[map.size()][2];
                    int t = 0;

                    for (String str : key) {
                        text[t][0] = str;
                        text[t][1] = (map.get(str).toString());
                        t++;
                    }
                }



        }  catch (Exception e) {
            text = createPreferense(RadioList.getDataSet());
        }

        return text;

    }

    public void clearPreferense() {
        sPref = getSharedPreferences("new",MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.clear();
        ed.commit();
    }

    public String[][] createPreferense(String[][] mtext) {
        String[][] text = mtext;
        System.out.println(text[0][0]);
        sPref = getSharedPreferences("new",MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        for (int i = 0; i < text.length; i++) {
            ed.putString(text[i][0], text[i][1]);
        }

        ed.commit();
        return text;
    }


    public static String[][] getDataSet() {


        String[][] murl = {
                {"Bin Radio","http://binradio.ru:24000/bin"},
                {"Radio1 Rock","http://stream.radioreklama.bg:80/radio1rock.ogg"},
                {"Classical 96.3FM", "http://zoomcast.vs.hc1.ca/cfmz"},
                {"My Radio", "http://mortum5.fvds.ru:8000/test"}
        };
        return murl;
    }

    public void addPreferense(String first, String second) {
        String[][] text = new String[1][2];
        text[0][0] = first;
        text[0][1] = second;
        createPreferense(text);
        restart();

    }



    public class addNewRadio extends DialogFragment {
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.dialog, null))
                    // Add action buttons
                    .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            EditText title = (EditText) getDialog().findViewById(R.id.title);
                            EditText url = (EditText) getDialog().findViewById(R.id.url);
                            addPreferense(title.getText().toString(), url.getText().toString());

                        }
                    })
                    .setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            addNewRadio.this.getDialog().cancel();
                        }
                    });

            return builder.create();

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }
}


