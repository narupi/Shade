package com.example.naru.shades;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.TimerTask;

/**
 * Created by Naru on 2017/03/27.
 */
public class Task1 extends TimerTask {

    private Handler handler;
    private Context context;
    private GameOver dialog2;

    public Task1(Context context,Activity activity) {
        handler = new Handler();
        dialog2= new GameOver(context);
        dialog2.intent(activity);
        dialog2.setCanceledOnTouchOutside(false);
        this.context = context;
    }


    @Override
    public void run() {
        handler.post(new Runnable() {
            @Override
            public void run() {
               // Log.e("a","ssrsts");
                if(MainView.score_add) {
                    GameActivity.setData();
                    MainView.score_add=false;
                }
              if(GameActivity.gameover){
                  GameActivity.gameover = false;
                    Log.e("a","ssfdfsfsg");
                 // GameActivity.cancelTimer();
                  dialog2.show();
              }
            }
        });
    }
}