package com.example.naru.shades;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;
import static com.example.naru.shades.MainActivity.bgm;

public class GameActivity extends Activity {

    private int red[] = {204,45,51} ;
    private int green[] = {102,255,153};
    private int blue[] = {102,87,204};
   // public static final int FW = 5; //フィールド　width
    //public static final int FH = 14; //フィールド height
    private SurfaceView surfaceView;
    private MainView mainView;
    private GestureDetector m_clGestureDetector;
    private Context context;
    static SePlayer se2;
    static boolean gameover = false;
    static Timer timer;
    private long repeatInterval = 1;
    static TextView level;
    static TextView score;
    private int highspeed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        hide_bar();

        gameover = false;

        timer = new Timer();
        TimerTask timerTask = new Task1(GameActivity.this,GameActivity.this);
        timer.scheduleAtFixedRate(timerTask, repeatInterval, repeatInterval);

        Mode mode = (Mode) this.getApplication();
        int receive = mode.getdata();
        int data = receive/10; //mode
        int ran = receive%10;

        this.se2 = new SePlayer(this);

        context = this;

        highspeed = ViewSize.getHeight()*50/1920;

        m_clGestureDetector = new GestureDetector(this, simpleOnGestureListener);

        //Mode data
        Log.d("receve_data","data -- " + data);
        Log.d("receve_data","ran -- " + ran);


        level = (TextView) findViewById(R.id.level);
        score = (TextView) findViewById(R.id.score);
        Button menu = (Button)findViewById(R.id.menu);

        level.setTextColor(Color.rgb(red[ran], green[ran], blue[ran]));
        score.setTextColor(Color.rgb(red[ran], green[ran], blue[ran]));
        menu.setTextColor(Color.rgb(red[ran], green[ran], blue[ran]));


        surfaceView = (SurfaceView)findViewById(R.id.SurfaceViewMain);
        mainView = new MainView(this,surfaceView);



        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog dialog = new MyDialog(context);
                dialog.intent(GameActivity.this);
                dialog.show();
            }
        });


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
    }


    public static void setData(){
        level.setText("level"+MainView.level);
        score.setText(""+MainView.score);
    }


    public void hide_bar(){
        View decor = this.getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION  | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
    public static void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        hide_bar();
        //BGMの再生
        bgm.start();
        MainView.isAttached = true;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        cancelTimer();
        //BGMの停止
        bgm.stop();
        se2.end();
        MainView.isAttached = false;
        saveHighScore();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        //BGMの停止
        bgm.stop();
        MainView.isAttached = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
    //    Log.e(TAG,"touch!!!!");
//        if(event.getAction() == MotionEvent.ACTION_UP) {
        hide_bar();

        float x = event.getX();
        int lx = MainView.lx;
        int ry = MainView.ry;
        int rx = MainView.rx;
        int ran = MainView.ran;
        if (lx > x && ran > 0 && !MainView.draw_map[mainView.getYidx(ry,ran)][ran-1]) {
            MainView.ran--;
        } else if (rx < x && ran < MainView.game_size-1 && !MainView.draw_map[mainView.getYidx(ry,ran)][ran+1]) {
            MainView.ran++;
        }
        m_clGestureDetector.onTouchEvent(event);
        return true;
    }



    //複雑なタッチイベント処理
    private final GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDoubleTap(MotionEvent event) {
      //      Log.d(TAG,"doubleTap4");
            float x = event.getX();
            if(MainView.lx < x && MainView.rx > x){
                se2.start(5);
                MainView.speed += highspeed;
            }
            return super.onDoubleTap(event);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent event) {
        //    Log.d(TAG,"doubleTap5");
            return super.onDoubleTapEvent(event);
        }

        @Override
        public boolean onDown(MotionEvent event) {
          // Log.d(TAG,"doubleTap1");
           return super.onDown(event);
        }

        @Override
        public void onShowPress(MotionEvent event) {
           // Log.d(TAG,"doubleTap2");
            super.onShowPress(event);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
           // Log.d(TAG,"doubleTap3");
            return super.onSingleTapUp(event);
        }

    };



}
