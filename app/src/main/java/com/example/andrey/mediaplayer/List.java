package com.example.andrey.mediaplayer;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by andrey on 22.02.16.
 */
public class List extends RecyclerView.Adapter<List.ViewHolder>  {


    String[][] murls;

    Context mcontext;
    static String nowMain = "Выберите радио";
    static String link = "Выберите радио";

    // класс view holder-а с помощью которого мы получаем ссылку на каждый элемент
    // отдельного пункта списка
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        // наш пункт состоит только из одного TextView
        public TextView title;
        public TextView url;
        public LinearLayout linear;


        public ViewHolder(View v) {
            super(v);
            linear = (LinearLayout) v.findViewById(R.id.item);
            title = (TextView) v.findViewById(R.id.title);
            url = (TextView) v.findViewById(R.id.url);
            linear.setOnClickListener(this);
            linear.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                switch (v.getId()) {
                    case R.id.item:


                        startNew(url.getText().toString(), title.getText().toString());
                        nowMain = title.getText().toString();
                        link = url.getText().toString();
                        break;

                }
            }
        }

            @Override
            public boolean onLongClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) switch (v.getId()) {
                    case R.id.item:

                        Toast.makeText(mcontext, title.getText().toString(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RadioList.ACTION);
                        intent.putExtra("url",title.getText().toString());
                        mcontext.sendBroadcast(intent);
                        break;

                }
                return true;
            }

    }





    // Конструктор
    public List(String[][] urls, Context context) {
        mcontext = context;


        murls = urls;
    }

    public void startNew(String url, String title) {
        Intent intent = new Intent(mcontext,MainActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("title",title);

        mcontext.startActivity(intent);
    }



    // Создает новые views (вызывается layout manager-ом)
    @Override
    public List.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        // тут можно программно менять атрибуты лэйаута (size, margins, paddings и др.)

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Заменяет контент отдельного view (вызывается layout manager-ом)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.title.setText(murls[position][0]);
        holder.url.setText(murls[position][1]);



    }

    // Возвращает размер данных (вызывается layout manager-ом)
    @Override
    public int getItemCount() {
        return murls.length;
    }
}

