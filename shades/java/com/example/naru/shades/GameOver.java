package com.example.naru.shades;

/**
 * Created by Naru on 2017/03/27.
 */


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.TextView;

public class GameOver extends Dialog{

    private Activity mActivity;
    private int score = 0;

    public GameOver(Context context) {
        super(context);
    }

    public void intent(Activity activity){
        mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // タイトルなし
        // (これしないとグレーのタイトルが付く)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //ナビゲーションバー非表示
        View decor = this.getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION  | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        // layout.xml を利用する
        setContentView(R.layout.gameover);
        // 画面の大きさに合わせる
        LayoutParams lp = getWindow().getAttributes();
        lp.width = LayoutParams.FILL_PARENT;
        getWindow().setAttributes(lp);
        score = HighScore.getHighScore();
        TextView textView = (TextView)findViewById(R.id.text);
        textView.setText("your best score "+score);
        // homeボタン
        findViewById(R.id.home2).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        mActivity.startActivity(new Intent(mActivity, MainActivity.class));
                        dismiss();
                    }

                });
    }
}