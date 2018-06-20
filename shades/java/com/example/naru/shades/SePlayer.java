package com.example.naru.shades;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.Log;

/**
 * Created by Naru on 2017/03/26.
 */


public class SePlayer {

    private AudioAttributes audioAttributes;
    private SoundPool soundPool;

    private int sound[];

    public SePlayer(Context context){
        audioAttributes = new AudioAttributes.Builder()
                // USAGE_MEDIA
                // USAGE_GAME
                .setUsage(AudioAttributes.USAGE_GAME)
                // CONTENT_TYPE_MUSIC
                // CONTENT_TYPE_SPEECH, etc.
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                // ストリーム数に応じて
                .setMaxStreams(6)
                .build();

        sound = new int[6];

        //startボタンSE
        sound[0] = soundPool.load(context, R.raw.start_se, 1);
        //mode-select SE
        sound[1] = soundPool.load(context, R.raw.mode_select, 1);
        //ブロック統合SE
        sound[2] = soundPool.load(context, R.raw.block_join, 1);
        //ブロック最下到着SE
        sound[3] = soundPool.load(context, R.raw.bottom, 1);
        //ブロックを横一列揃えたときのSE
        sound[4] = soundPool.load(context, R.raw.blockclear, 1);
        //高速投下時SE
        sound[5] = soundPool.load(context, R.raw.swing, 1);

        // load が終わったか確認する場合
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                Log.d("debug","sampleId="+sampleId);
                Log.d("debug","status="+status);
            }
        });

    }

    public void start(int idx){
        // play(ロードしたID, 左音量, 右音量, 優先度, ループ,再生速度)
        soundPool.play(sound[idx], 1.0f, 1.0f, 0, 0, 1);
    }
    public void stop(int idx){
        soundPool.unload(sound[idx]);

    }
    public void end(){
        soundPool.release();
    }
}
