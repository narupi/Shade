package com.example.naru.shades;

import android.app.Application;

/**
 * Created by Naru on 2017/03/15.
 */

public class Mode extends Application {

    static int defaultdata = 1;

     public static int getdata() {
        return defaultdata;
    }

    public static void setdata(int data) {
        defaultdata = data;
    }

}
