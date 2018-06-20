package com.example.naru.shades;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.support.annotation.DrawableRes;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import static java.security.AccessController.getContext;

public class MainActivity extends Activity{

    private Button start;
    private Button easy;
    private Button normal;
    private Button hard;
    private Button setting;
    private int red[] = {204,45,51} ;
    private int green[] = {102,255,153};
    private int blue[] = {102,87,204};
    //private int mode = 1;
    private int data;
    private Mode mode;
    private ConstraintLayout layout;
    static BgmPlayer bgm;
    private SePlayer se;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);


        this.bgm = new BgmPlayer(this);
        this.se = new SePlayer(this);

        loadHighScore();

        View decor = this.getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION  | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        mode = (Mode) this.getApplication();
        data = 1;

        //Randomクラスのインスタンス化
        Random rnd = new Random();
        final int ran = rnd.nextInt(3);

        layout = (ConstraintLayout)findViewById(R.id.layout);

        start = (Button)findViewById(R.id.start);
        easy = (Button)findViewById(R.id.easy);
        normal = (Button)findViewById(R.id.normal);
        hard = (Button)findViewById(R.id.hard);
        setting = (Button)findViewById(R.id.setting);

        // ボタンの設定
        start.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD_ITALIC));
        start.setBackgroundColor(Color.rgb(red[ran], green[ran], blue[ran]));


        easy.setTypeface(Typeface.create(Typeface.SERIF,Typeface.NORMAL));
        easy.setBackgroundColor(Color.argb(0,0,0,0));

        normal.setTextColor(Color.rgb(red[ran], green[ran], blue[ran]));
        normal.setTypeface(Typeface.create(Typeface.SERIF,Typeface.NORMAL));

        normal.setBackgroundColor(Color.argb(0,0,0,0));

        hard.setTypeface(Typeface.create(Typeface.SERIF,Typeface.NORMAL));
        hard.setBackgroundColor(Color.argb(0,0,0,0));



        //ボタンイベント
        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data != 0) {
                    easy.setTextColor(Color.rgb(red[ran], green[ran], blue[ran]));
                    normal.setTextColor(Color.rgb(0, 0, 0));
                    hard.setTextColor(Color.rgb(0, 0, 0));
                    se.start(1);
                }
                data = 0;
            }
        });

        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data != 1){
                    normal.setTextColor(Color.rgb(red[ran], green[ran], blue[ran]));
                    easy.setTextColor(Color.rgb(0, 0, 0));
                    hard.setTextColor(Color.rgb(0, 0, 0));
                    se.start(1);
                }
                data = 1;
            }
        });

        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data != 2){
                    hard.setTextColor(Color.rgb(red[ran], green[ran], blue[ran]));
                    easy.setTextColor(Color.rgb(0, 0, 0));
                    normal.setTextColor(Color.rgb(0, 0, 0));
                    se.start(1);
                }
                data = 2;
            }
        });


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                se.start(0);
                se.stop(0);
                se.stop(1);
                int send_data = data*10 + ran;
                Log.e("kdjfskj",send_data+"");
                Mode.setdata(send_data);

                Intent intent = new Intent(getApplication(), GameActivity.class);
                startActivity( intent );
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                se.start(0);
                se.stop(0);
                se.stop(1);
                data = 3;
                int send_data = data*10 + ran;
                Log.e("kdjfskj",send_data+"");
                Mode.setdata(send_data);

                Intent intent = new Intent(getApplication(),SettingActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        ViewSize viewSize = new ViewSize();
        viewSize.setHeight(layout.getHeight());
        viewSize.setWidth(layout.getWidth());
        Log.e("width: " , "a "+layout.getWidth());
        Log.e("height: " , "b "+ layout.getHeight());

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
        saveHighScore();
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        //BGMの停止
        bgm.stop();

    }

    private void saveHighScore(){
        FileOutputStream fileOutputstream = null;
        String score = HighScore.getHighScore()+"";
        try {
            fileOutputstream = openFileOutput("score", Context.MODE_PRIVATE);
            fileOutputstream.write(score.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Log.e("dtatat",HighScore.getHighScore()+"");
    }

    private void loadHighScore(){
        FileInputStream fileInputStream;
        String text = null;
        int score = 0;

        try {
            fileInputStream = openFileInput("score");
            String lineBuffer = null;

            BufferedReader reader= new BufferedReader(new InputStreamReader(fileInputStream,"UTF-8"));
            while( (lineBuffer = reader.readLine()) != null ) {
                text = lineBuffer ;
            }
            score = Integer.parseInt(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HighScore.setHighScore(score);
       // Log.e("dtatat",HighScore.getHighScore()+"");

    }


}
