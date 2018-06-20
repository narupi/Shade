package com.example.naru.shades;

/**
 * Created by Naru on 2017/03/26.
 */

import android.content.Context;
import android.media.MediaPlayer;

public class BgmPlayer
{
    private MediaPlayer mediaPlayer;

    public BgmPlayer(Context context)
    {
        // BGMファイルを読み込む
        this.mediaPlayer = MediaPlayer.create(context, R.raw.rain_bgm);
        // ループ再生
        this.mediaPlayer.setLooping(true);
        // 音量設定
        this.mediaPlayer.setVolume(1.0f, 1.0f);
    }

    /**
     * BGMを再生する
     */
    public void start()
    {
        if (!mediaPlayer.isPlaying())
        {
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        }
    }

    /**
     * BGMを停止する
     */
    public void stop()
    {
        if (mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            mediaPlayer.prepareAsync();
        }
    }
}