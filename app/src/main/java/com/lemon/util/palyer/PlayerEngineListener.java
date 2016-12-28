package com.lemon.util.palyer;
public interface PlayerEngineListener{

	/**
	 * 音频下载/加载中
	 */
	public void onLoading(int playID);

	/**
	 * 准备播音
	 * @param duration 音频时间长度
	 */
	public void onPrepared(int playID, long duration);

	/**
	 * 播放过程
	 * @param curPosition 音频当前播放位置
	 */
	public void onPlaying(int playID, long curPosition);

	/** 当前的缓存进度 */
	public void onBuffering(int playID, long curPosition);
	
	/**
	 * 播放完成
	 */
	public void onCompletion(int playID);
	
	/** 暂停播放 */
	public void onPause(int playID);

	/**
	 * 中断播放
	 */
	public void onInterrupt(int playID);

	/**
	 * 出错
	 * @param error 错误原因
	 */
	public void onError(int playID, String error);


}