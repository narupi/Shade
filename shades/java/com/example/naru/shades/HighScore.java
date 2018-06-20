package com.example.naru.shades;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.TextView;

/**
 * Created by Naru on 2017/03/28.
 */

public class HighScore extends Application{

    static int HighScore = 0;

    public static int getHighScore() {
        return HighScore;
    }

    public static void setHighScore(int score) {
        if(score > HighScore) {
            HighScore = score;
        }
    }


}
