package com.example.naru.shades;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;


import java.util.Random;
import java.util.logging.Handler;

import static android.content.ContentValues.TAG;
import static com.example.naru.shades.GameActivity.se2;


/**
 * Created by Naru on 2017/03/20.
 */
// SurfaceViewクラスを継承したクラスを作成
public class MainView extends SurfaceView implements SurfaceHolder.Callback,Runnable {

    private SurfaceHolder holder = null;
    //   private int[] color = {Color.YELLOW,Color.BLUE,Color.RED,Color.GREEN};
    //   private int idx = 0;
    static boolean isAttached = true;
    private Thread thread = null;
    static int lx = 0,rx,ly,ry;
    private  long t1 = 0,t2  = 0 ;
    // private int idx = 0;
    static boolean draw_map[][]; //どこにブロックを描画するか
    private int view_height = 0;
    private int view_width = 0;
    static int game_size ; //5
    static int game_height ; //14
    private int game_speed ;
    static int speed = 0;
    private int x_point[] ; //ブロックの左上の点のx座標
    private int y_point[][]; //左上の点のy座標　
    private int right_x;
    private int right_y;
    static int ran;
    private Paint pt = new Paint();
    private int point = 0;
    private int mode = 1;
    private int color_idx = 0;
    private int color_rnd = 0;
    private Random rnd = new Random();
    private String color_map[][] = { {"#ffbdbe","#ff757b","#be0b0d","#73090a","#39080a"} ,{"#f2fec7","#a2e777","#01ab01","#016401","#013b00"},{"#8cf4fe","#47abe2","#2a68a0","#1a3d62","#092543"}};
    private int block_color[][];
    private final int color_density  = 4; //色の濃さの数  //5種類
    private int max_height ;
    static boolean retry=false;
    private boolean gameover = false;
    static int score = 0;
    static int level = 0;
    private int chain_bonus[] = {0,4,16,32,64,128,256,512};
    static boolean score_add = false;
    private Paint select;


    // MainViewクラスのコンストラクタ
    public MainView(Context context, SurfaceView sv) {
        super(context);

        // getHolder()メソッドを用いてSurfaceHolderオブジェクトを取得
        holder = sv.getHolder();
        // addCallback()を用いて、SurfaceHolder.Callbackオブジェクトをリスナー登録する
        holder.addCallback(this);

        holder.setFixedSize(getWidth(), getHeight());


    }


    public void init(){

        select = new Paint();
        select.setColor(0x28FFFFFF);

        retry = false;

        score = 0;
        level = 0;


        isAttached = true;

        int receive = Mode.getdata();
        mode = receive/10;
        color_idx = receive%10;

        Log.e("lx","aj"+lx);
        //    Log.d("mode:color","a" + mode + color_idx);

        lx = 0;
        ly = 0;

        ViewSize viewSize = new ViewSize();

        view_height = viewSize.getHeight();
        view_width = viewSize.getWidth();


        switch (mode){
            case 0:
                game_size = 5;
                game_height = 14;
                speed = 0;
                game_speed = view_height*2/1920;
                speed = game_speed;
                break;
            case 1:
                game_size = 5;
                game_height = 14;
                speed = 0;
                game_speed = view_height*5/1920;
                speed = game_speed;
                break;
            case 2:
                game_size = 5;
                game_height = 14;
                speed = 0;
                game_speed = view_height*10/1920;
                speed = game_speed;
                break;
            case 3:
                game_speed = speed;
                break;
        }

        max_height = game_height;



        Log.e("VIEW_SIZE","   "+view_height+view_width);

        x_point = new int[game_size+1];
        y_point = new int[game_height+1][game_size+1];
        draw_map = new boolean[game_height+1][game_size+1];
        block_color = new int[game_height+1][game_size+1];


        //ブロックを描画する際の右下の座標を算出するための定数
        right_x = view_width/game_size;
        right_y = view_height/game_height;


        //解像度に合わせて座標を計算
        for(int i=0;i<game_size; i++){
            x_point[i] = point;
            point += right_x;
            //       Log.e("POINT",x_point[i]+"A");
        }

        point = 0;
        for(int i=0;i<=game_height;i++){
            for(int j=0;j<=game_size;j++) {
                block_color[i][j] = -1;
                draw_map[i][j] = false ;
                y_point[i][j] = point;
                //     Log.e("POINT",y_point[i][j]+"B");
            }
            point += view_height/game_height;
        }

        Random rnd = new Random();
        ran = rnd.nextInt(game_size);
    }

    // SurfaceViewのサイズなどが変更されたときに呼び出されるメソッド
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
        Log.e(TAG,"surfaceChanged");
        MainView.isAttached = true;
    }

    // SurfaceViewが最初に生成されたときに呼び出されるメソッド
    @Override
    public void surfaceCreated(SurfaceHolder holder){

        Log.e(TAG,"surfaceCreated");
        // draw();
        this.holder = holder;
        thread = new Thread(this);
        thread.start();
    }

    // SurfaceViewが破棄されるときに呼び出されるメソッド
    @Override
    public void  surfaceDestroyed(SurfaceHolder holder){
        Log.e(TAG,"surfaceDestroyed");
        game_size = 5;
        game_height = 14;
        isAttached = false;
        thread = null;
    }


    @Override
    public void run(){
        init();
        while(isAttached ) {
            if(retry){
                retry();
            }
        //    Log.e(TAG,"draw");
            draw();
            //alldraw();
        }
        if(gameover) {
            if(HighScore.getHighScore() < score){
                HighScore.setHighScore(score);
            }
            GameActivity.gameover = true;
        }
    }




    private void retry(){
        draw_map = new boolean[game_height+1][game_size+1];
        block_color = new int[game_height+1][game_size+1];
        for(int i=0;i<=game_height;i++){
            for(int j=0;j<=game_size;j++) {
                block_color[i][j] = -1;
                draw_map[i][j] = false ;
            }
        }
        ly = 0;
        lx = 0;
        retry = false;
    }

    private int getXidx(int lx){
        int temp=0,i=0,point = view_width/game_size;
        while(lx != temp){
            i++;
            temp += point;
        }
        return i;
    }
    public int getYidx(int ly,int x){
        int ret = 0;
         for(int i=0;i<game_height;i++){
             if (y_point[i][x] <= ly && y_point[i+1][x] >= ly){
                 ret = i;
             }
        }
        return ret;
    }

    //グリッド線表示(座標チェック)
    private void alldraw(){
        Canvas canvas = holder.lockCanvas();
        // 画面全体を一色で塗りつぶすdrawColor()メソッド
        canvas.drawColor(0xFF494949);

        // Paintクラスをインスタンス化
        Paint paint = new Paint();

        paint.setStyle(Paint.Style.STROKE);

        paint.setColor(Color.parseColor(color_map[color_idx][3]));

        for(int i=0;i<game_height;i++){
            for(int j=0;j<game_size;j++){
                canvas.drawRect(x_point[j], y_point[i][j], x_point[j] + right_x, y_point[i][j]+right_y, paint);
            }
        }

        holder.unlockCanvasAndPost(canvas);
    }

    private void nowstack(Canvas canvas){

        for(int i=0;i<game_height;i++){
            for(int j=0;j<game_size;j++){
                if(draw_map[i][j]){
                    //Log.d("HEY ","Now here");
                    pt.setColor(Color.parseColor(color_map[color_idx][block_color[i][j]]));
                    canvas.drawRect(x_point[j],y_point[i][j],x_point[j]+right_x,y_point[i][j]+right_y,pt);
                }
            }
        }
    }

    public void draw() {

       //Log.d(TAG,"draw() ; ; ; ");
        // lockCanvas()メソッドを使用して、描画するためのCanvasオブジェクトを取得する
        Canvas canvas = holder.lockCanvas();

        lx = x_point[ran];
        //if (canvas != null) {
        ly+=speed; //スピード
        rx = lx+right_x;
        ry = ly+right_y;

        // 画面全体を一色で塗りつぶすdrawColor()メソッド
        canvas.drawColor(0xFF494949);

        // Paintクラスをインスタンス化
        Paint paint = new Paint();

        // カラーを指定
        paint.setColor(Color.parseColor(color_map[color_idx][color_rnd]));

        // Canvas.drawRect()を呼び出すと、正方形や長方形を描画することが可能になる
        // （left：左辺,top：上辺,right：右辺,bottom：下辺,Paintインスタンス）
        //左ｘ上y　右x↓
        canvas.drawRect(lx,0,rx,view_height,select);
        canvas.drawRect(lx, ly, rx, ry, paint);

        nowstack(canvas);

        // Canvasオブジェクトへの描画完了したら、unlockCanvasAndPost()メソッドを呼び出し、引数にはCanvasオブジェクトを指定する
        holder.unlockCanvasAndPost(canvas);

        int x_idx = getXidx(lx);
        int y_idx = getYidx(ly,x_idx);

    //    Log.d("x_idx,y_idx","x "+x_idx+" Y "+y_idx);


        if(ry >= view_height || draw_map[y_idx+1][x_idx]){
            addScore(0,0);

            Log.e(TAG,"game speed"+game_speed);
            speed = game_speed;
            ly = 0;
            draw_map[y_idx][x_idx] = true;
            ran = rnd.nextInt(game_size);
            block_color[y_idx][x_idx] = color_rnd;
            se2.start(3);

            colorCheck(x_idx, y_idx);//統合
            clearBlock(canvas);//クリア

            mostHeight();

            if(max_height == 0){
                isAttached = false;
                gameover = true;
            }

        }


        // スリープ
        t2 = System.currentTimeMillis();
        if(t2 - t1 < 33){ // 1000 / 60 = 16.6666 ←60FPS 1000/30 = 33.333 ←30FPS
            try {
                Thread.sleep(16 - (t2 - t1));
            } catch (InterruptedException e) {
            }
        }

    }

    private void addScore(int type_,int chain){
        //モードごとに点数にボーナス
        if(mode == 2) {
            switch (type_) {
                case 0:
                    score += 4;
                    break;
                case 1:
                    score += chain_bonus[chain]*2;
                    break;
                case 2:
                    score += 400;
                    break;
            }
        }
        if(mode == 3) {
            switch (type_) {
                case 0:
                    score += 2;
                    break;
                case 1:
                    score += chain_bonus[chain];
                    break;
                case 2:
                    score += 200;
                    break;
            }
        }
        else {
            switch (type_) {
                case 0:
                    score += 1+mode;
                    break;
                case 1:
                    score += chain_bonus[chain]/2*(mode+1);
                    break;
                case 2:
                    score += 100*mode+1;
                    break;
            }
        }


        int temp = level;
        if(temp != score/500){
            game_speed += 1;
        }
        level = score/500;
        score_add = true;

    }

    private void clearBlock(Canvas canvas){
        int horizon_color = 0;
        for(int i=max_height;i<game_height;i++){
            int color = block_color[i][0];
            for(int j=0;j<game_size;j++){
                if(block_color[i][j] == color && block_color[i][j] != -1){
                    horizon_color++;
                }
            }
            if(horizon_color == game_size ){
           //     Log.e(TAG,"horizoncolor"+horizon_color);
             //   Log.e(TAG,"gamesize"+game_size);
                horizon_color = 0 ;
                for(int n=0;n<game_size;n++){
                   for (int k=i;k>0; k--){
                       block_color[k][n] = block_color[k-1][n];
                       draw_map[k][n] = draw_map[k-1][n];
                   }
                }
                try{
                    Thread.sleep(200);
                    se2.start(4);
                    addScore(2,0);
                    nowstack(canvas);
               //     draw();
                    return ;
                }catch (InterruptedException e){
                }
            }
            horizon_color = 0;
        }


    }

    private void mostHeight(){
        for(int i=0;i<game_height;i++){
            for(int j=0;j<game_size;j++){
                if(draw_map[i][j]){
                    if(max_height > i){
                        max_height = i;
                        return;
                    }
                }
            }
        }

    }


    private void colorCheck(int x,int y) {
      //  int color = color_rnd; //落ちてきているブロックの色
        color_rnd = rnd.nextInt(color_density+1);
        switch (level){
            case 0:
                color_rnd = rnd.nextInt(3);//薄い色しか出さない
                break;
            case 1:
                if(color_rnd == color_density){
                    color_rnd = rnd.nextInt(color_density+1);//一番濃い色だった場合再抽選
                }
                break;
            case 2:
                if(color_rnd > 2){
                    color_rnd = rnd.nextInt(color_density+1);//濃い色が出る確率を少し減らす
                }
                break;
        }
     //   Log.e(TAG,"a"+color_rnd);
        int chain=0;
        while(block_color[y][x] == block_color[y + 1][x] && block_color[y][x] != color_density && y != game_height-1){
           // Log.e(TAG,"now_block_c "+block_color[y][x] + " next_c "+block_color[y+1][x]);
           // Log.e(TAG,"color "+color);
           // Log.e(TAG,"y "+y);
            draw_map[y][x] = false;
            block_color[y+1][x] = block_color[y][x] + 1;
            block_color[y][x] = -1;
            //color ++;
            y++;
            //block_color[y][x] = color ;
            try{
                Thread.sleep(200);
                se2.start(2);
                chain ++ ;
                addScore(1,chain);
               // nowstack(canvas);
                draw();
            }catch (InterruptedException e){
            }
        }
/*
        if(max_height > y){
            max_height = y;
        }*/
    }






}



