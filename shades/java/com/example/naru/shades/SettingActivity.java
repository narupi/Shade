package com.example.naru.shades;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import static com.example.naru.shades.MainActivity.bgm;

public class SettingActivity extends Activity {

    private int red[] = {204,45,51} ;
    private int green[] = {102,255,153};
    private int blue[] = {102,87,204};
    private int ih = 14;
    private int iw = 5;
    private int is = 5;
    private int vh;
    private int vw;
    private Context context;
    private EditText height;
    private EditText width;
    private EditText speed;
    private SePlayer se;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        this.se = new SePlayer(this);

        View decor = this.getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION  | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        Mode mode = (Mode) this.getApplication();
        int receive = mode.getdata();
        int data = receive/10; //mode
        int ran = receive%10;

        vh = ViewSize.getHeight();
        vw = ViewSize.getWidth();

        ih = 14;
        iw = 5;
        is = 5;

        context = this;

        Button start = (Button)findViewById(R.id.start);
        height = (EditText)findViewById(R.id.edith);
        width = (EditText)findViewById(R.id.editw);
        speed = (EditText)findViewById(R.id.edits);
        TextView gh = (TextView)findViewById(R.id.textView5);
        TextView gw = (TextView)findViewById(R.id.textView6);
        TextView gs = (TextView)findViewById(R.id.textView7);


        start.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD_ITALIC));
        start.setBackgroundColor(Color.rgb(red[ran], green[ran], blue[ran]));

        gh.setTypeface(Typeface.create(Typeface.SERIF,Typeface.NORMAL));
        gh.setTextColor(Color.rgb(red[ran], green[ran], blue[ran]));

        gw.setTypeface(Typeface.create(Typeface.SERIF,Typeface.NORMAL));
        gw.setTextColor(Color.rgb(red[ran], green[ran], blue[ran]));

        gs.setTypeface(Typeface.create(Typeface.SERIF,Typeface.NORMAL));
        gs.setTextColor(Color.rgb(red[ran], green[ran], blue[ran]));


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String h = height.getText().toString();
                if (h.length() != 0) {
                    ih = Integer.parseInt(h);
                }
                if (ih > 1 && ih <= 30) {
                    MainView.game_height = ih;
                } else {
                    ih = 14;
                    Toast.makeText(context, "Unavailable value", Toast.LENGTH_LONG).show();
                }

                String w = width.getText().toString();
                if (w.length() != 0) {
                    iw = Integer.parseInt(w);
                }
                if (iw > 0 && iw <= 300) {
                    MainView.game_size = iw;
                } else {
                    iw = 5;
                    Toast.makeText(context, "Unavailable value", Toast.LENGTH_LONG).show();
                }

                String s = speed.getText().toString();

                if (s.length() != 0) {
                    is = Integer.parseInt(s);
                }

                if (is > 0 && is <= 100) {
                    MainView.speed = vh * is / 1920;
                } else {
                    is = 5;
                    Toast.makeText(context, "Unavailable value", Toast.LENGTH_LONG).show();
                }

                Log.e("debug",""+ih+" "+iw+" "+is);
                se.start(0);
                se.stop(0);
                Intent intent = new Intent(getApplication(), GameActivity.class);
                startActivity( intent );
            }
        });


    }
    @Override
    protected void onResume()
    {
        super.onResume();

        View decor = this.getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION  | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        //BGMの再生
        bgm.start();
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        //BGMの停止
        bgm.stop();
        se.end();





    }
}
