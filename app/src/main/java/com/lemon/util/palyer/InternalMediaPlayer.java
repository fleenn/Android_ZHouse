package com.lemon.util.palyer;

import android.media.MediaPlayer;

/**
 * 简单的MediaPlayer扩展，增加了新的规则的参考
 * @author geolo
 */
public class InternalMediaPlayer extends MediaPlayer {
	
	public int id;
	
	public String path;

	/**
	 * 仍然在缓存数据中，数据准备中
	 */
	public boolean preparing = false;

	/**
	 * 是否要在准备之后播放音频
	 * 例如 准备下一首音频的时候不应该开始播放，因为旧的仍然在播放中
	 */
	public boolean playAfterPrepare = false;

}