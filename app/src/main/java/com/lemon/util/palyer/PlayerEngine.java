package com.lemon.util.palyer;

import android.view.SurfaceHolder;

/**
 *
 * 播放引擎的接口
 *
 * @author geolo
 */
public interface PlayerEngine {

    /**
     *  设置回调监听
     */
    public void setSurfaceHolder(SurfaceHolder holder);

    /**
     * 开始播放音频
     */
    public void play(int playID, String playPath);

    /**
     * 判断当前播放是否正在进行
     *
     * @return boolean value
     */
    public boolean isPlaying();

    /**
     * 停止播放
     */
    public void stop();

    /**
     *暂停当前播放
     */
    public void pause();

}
