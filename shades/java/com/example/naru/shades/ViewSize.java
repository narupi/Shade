package com.example.naru.shades;

import android.app.Application;

/**
 * Created by Naru on 2017/03/21.
 */

public class ViewSize extends Application {

    static int width = 0;
    static int height = 0;

    public static int getHeight() {
        return height;
    }

    public static void setHeight(int data) {
        height = data;
    }
    public static int getWidth() {
        return width;
    }

    public static void setWidth(int data) {
        width = data;
    }


}
