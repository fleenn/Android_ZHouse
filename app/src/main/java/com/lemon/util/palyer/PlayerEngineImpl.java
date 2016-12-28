package com.lemon.util.palyer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.lemon.util.ParamUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class PlayerEngineImpl implements PlayerEngine {

    private static final String TAG = "PlayerEngineImpl.java";
    private static final long INTERVALTIME = 500L;//刷新的时间
    private static final long WAIT_HANDLE_TIME = 300L;//等待用户操作的时间间隔

    private static final int WHAT_LOAD_VOICE = 0x000;
    private static final int WHAT_COUNT_PLAY = 0x001;
    private static final int WHAT_WAIT_HANDLE = 0x0002;

    private static PlayerEngineImpl mPlayerEngineImpl;

    /**
     * 时间间隔，用于计算每次统计失败的时间间隔
     */
    private static final long FAIL_TIME_FRAME = 1000;

    /**
     * 在FAIL_TIME_FRAME接受失败的数量
     */
    private static final int ACCEPTABLE_FAIL_NUMBER = 2;

    /**
     * 最后一次失败的格林时间统计
     */
    private long mLastFailTime;

    /**
     * 在FAIL_TIME_FRAME失败的次数
     */
    private long mTimesFailed;

    private Context mContext;

    private Set<PlayerEngineListener> mPlayerEngineListeners;

    private InternalMediaPlayer mCurrentMediaPlayer;

    /**
     * 打算准备要播放的id，当时不一定能成功播放，所以和mCurrentMediaPlayer是不一定对应的
     */
    private int mPreparePlayID;
    private String mPreparePlayPath;

    /**
     * 当前播放的音频的总长度
     */
    private int mCurrentDuration;

    /**
     * 用户三秒后都没再操作，就开始播放
     */
    private boolean isWaitStartPlay;
    //是否播放完毕；
    /**
     * 判断是否来电暂停了播放
     */
    private boolean callplayerpuaseflag = false;

    /**
     * 视频处理
     */
    private SurfaceHolder mHolder;
    private SurfaceView mSurfaceView;

    private Handler mPostHandler;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_LOAD_VOICE:
                    if (mCurrentMediaPlayer == null) {
                        sendEmptyMessageDelayed(WHAT_LOAD_VOICE, INTERVALTIME);
                        onLoading(mPreparePlayID);
                    }
                    break;
                case WHAT_COUNT_PLAY:
                    if (mPlayerEngineListeners != null) {
                        sendEmptyMessageDelayed(WHAT_COUNT_PLAY, INTERVALTIME);
                        onPlayingNotify(mCurrentMediaPlayer);
                    }
                    break;
                case WHAT_WAIT_HANDLE:
                    //用户点击开始播放之后，三秒内用户不再操作就进行播放，否则重新计时，避免用户高频繁的恶意操作
                    if (isWaitStartPlay) {
                        removeMessages(WHAT_WAIT_HANDLE);
                        if (mPreparePlayID != 0 && !TextUtils.isEmpty(mPreparePlayPath)) {
                            playStart(mPreparePlayID, mPreparePlayPath);
                        }
                    } else {
                        isWaitStartPlay = true;
                        sendEmptyMessageDelayed(WHAT_WAIT_HANDLE, WAIT_HANDLE_TIME);
                    }
                    break;
            }
        }

    };

    private PlayerEngineImpl(Context context) {
        mContext = context;
        mLastFailTime = 0;
        mTimesFailed = 0;
        isWaitStartPlay = true;
        mPlayerEngineListeners = new HashSet<PlayerEngineListener>();
        mPostHandler = new Handler(Looper.getMainLooper());
        //		TelephonyManager manager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        //		manager.listen(new CyclePhoneListener(), PhoneStateListener.LISTEN_CALL_STATE);
    }

    public static final PlayerEngineImpl getInstance(Context context) {
        if (mPlayerEngineImpl == null) {
            mPlayerEngineImpl = new PlayerEngineImpl(context);
        }
        return mPlayerEngineImpl;
    }

    @Override
    public void play(int playID, String playPath) {
        Log.i(TAG, "play: "+playPath);
        if (isWaitStartPlay) {
            if (mCurrentMediaPlayer != null) {
                if (mCurrentMediaPlayer.id != playID) {//如果不是当前的id，就中止播放，并打算播放新的
                    stop();
                    //中断播放后，就播放下一个了
                } else {//如果是当前的id，就是暂停播放了
                    pause();
                    return;//保证isWaitStartPlay的值还是true
                }
            }
            mHandler.sendEmptyMessageDelayed(WHAT_WAIT_HANDLE, WAIT_HANDLE_TIME);
        }

        if (mPreparePlayID == playID) {
            onInterruptNotify(null);
            mPreparePlayID = 0;
            mPreparePlayPath = "";
            mHandler.removeMessages(WHAT_LOAD_VOICE);
            isWaitStartPlay = true;
            Log.v(TAG, "******* mPreparePlayID = 0 *******");
        } else {
            mPreparePlayID = playID;
//            if(!TextUtils.isEmpty(playPath) && (URLUtil.isHttpUrl(playPath) ||  URLUtil.isHttpsUrl(playPath))){
//                mPreparePlayPath = playPath + "&audition=1";//试听地址，后面要加尾巴
//            }else{
//            }
            if (!ParamUtils.isEmpty(playPath)) {
                mPreparePlayPath = playPath;    //本地地址
            }
            mHandler.sendEmptyMessage(WHAT_LOAD_VOICE);
            isWaitStartPlay = false;
            Log.v(TAG, "******* mPreparePlayID != 0 *******");
        }
    }

    public int getPlayID() {
        return mPreparePlayID;
    }

    public void setPlayingListening(PlayerEngineListener playingListening) {
        if (mPlayerEngineListeners == null) {
            mPlayerEngineListeners = new HashSet<PlayerEngineListener>();
        }
        if (mPlayerEngineListeners != null) {
            mPlayerEngineListeners.add(playingListening);
        }
    }

    private void playStart(int playID, String playPath) {
        if (mCurrentMediaPlayer == null) {
            mCurrentMediaPlayer = build(playID, playPath);
        }

        if (mCurrentMediaPlayer != null && mCurrentMediaPlayer.id != playID) {
            //判断当前播放器里的id，是否是我们现在需要播放的
            cleanUp(); // this will do the cleanup job
            mCurrentMediaPlayer = build(playID, playPath);
        }

        if (mCurrentMediaPlayer == null) {
            return;
        }

        if (!mCurrentMediaPlayer.preparing) {
            //判断当前的播放器，是不是还在 加载/准备 音频中
            // prevent double-press
            if (!mCurrentMediaPlayer.isPlaying()) {
                Log.i(TAG, "Player [playing path] " + playPath);

                // starting timer
                starting();
            }
        } else {
            //告诉播放器，歌曲准备完成之后，就播放当前的音频
            mCurrentMediaPlayer.playAfterPrepare = true;
        }
    }

    @Override
    public void setSurfaceHolder(SurfaceHolder holder) {
        this.mHolder = holder;
    }

    public void setSurfaceView(SurfaceView surfaceView) {
        this.mSurfaceView = surfaceView;
    }

    private void starting() {
        Log.e(TAG, "************** jiangWB starting() *********************");
        mHandler.removeMessages(WHAT_LOAD_VOICE);
        mHandler.sendEmptyMessage(WHAT_COUNT_PLAY);
        mCurrentDuration = mCurrentMediaPlayer.getDuration();
        mCurrentMediaPlayer.start();
    }

    @Override
    public void pause() {
        Log.v(TAG, "******* pause() *******");
        if (mCurrentMediaPlayer != null) {

            if (mCurrentMediaPlayer.preparing) {//当前播放仍然在准备中，不能进行其他api操作
                mCurrentMediaPlayer.playAfterPrepare = !mCurrentMediaPlayer.playAfterPrepare;
                if (mCurrentMediaPlayer.playAfterPrepare) {
                    onLoading(mPreparePlayID);
                    mHandler.sendEmptyMessage(WHAT_LOAD_VOICE);
                    Log.v(TAG, "******* pause() -- 01 *******  mPreparePlayID:" + mPreparePlayID);
                } else {
                    onPause(mCurrentMediaPlayer);
                    Log.v(TAG, "******* pause() -- 02 *******");
                }
                return;
            }

            if (mCurrentMediaPlayer.isPlaying()) {
                mCurrentMediaPlayer.pause();
                onPause(mCurrentMediaPlayer);
                Log.v(TAG, "******* pause() -- 03 *******");
            } else {
                starting();
                Log.v(TAG, "******* pause() -- 04 *******");
            }
        }
    }

    @Override
    public boolean isPlaying() {
        if (mCurrentMediaPlayer == null)
            return false;

        if (mCurrentMediaPlayer.preparing) {
            return false;
        }

        return mCurrentMediaPlayer.isPlaying();
    }

    @Override
    public void stop() {
        Log.v(TAG, "******* stop() *******");
        mHandler.removeMessages(WHAT_LOAD_VOICE);
        mHandler.removeMessages(WHAT_COUNT_PLAY);
        onInterruptNotify(mCurrentMediaPlayer);
        cleanUp();
        mPreparePlayID = 0;
        mPreparePlayPath = "";
    }

    /**
     * 停止或者销毁当前的MediaPlayer
     */
    private void cleanUp() {
        if (mCurrentMediaPlayer != null)
            try {
                Log.v(TAG, "********** mCurrentMediaPlayer.stop() start ****************");
                mCurrentMediaPlayer.stop();
                Log.v(TAG, "********** mCurrentMediaPlayer.stop() end ****************");
            } catch (IllegalStateException e) {
                // 这边总是是不是有些小错误提示,请无视它
            } finally {
                Log.v(TAG, "********** mCurrentMediaPlayer.release() start ****************");
                mCurrentMediaPlayer.playAfterPrepare = false;
                final InternalMediaPlayer mp = mCurrentMediaPlayer;
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        if (mp != null) {
                            Log.v(TAG, "********** mCurrentMediaPlayer.release()  thread start ****************");
                            mp.release();
                            Log.v(TAG, "********** mCurrentMediaPlayer.release()  thread end ****************");
                        }
                    }
                }).start();
                mCurrentMediaPlayer = null;
                Log.v(TAG, "********** mCurrentMediaPlayer.release() end ****************");
            }
    }

    public void onDestroy() {
        stop();
        if (mPlayerEngineListeners != null) {
            mPlayerEngineListeners.clear();
        }
        mPlayerEngineListeners = null;
    }

    private InternalMediaPlayer build(int playID, String playPath) {
        final InternalMediaPlayer mediaPlayer = new InternalMediaPlayer();
        try {
            mediaPlayer.setDataSource(playPath);
            mediaPlayer.id = playID;
            mediaPlayer.path = playPath;
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    if (mediaPlayer != null) {
                        mediaPlayer.preparing = false;
                        if (mPreparePlayID == mediaPlayer.id && mediaPlayer.playAfterPrepare) {
                            mediaPlayer.playAfterPrepare = false;
                            playStart(mediaPlayer.id, mediaPlayer.path);
                        }
                        onPreparedNotify(mediaPlayer);
                    }
                }
            });

            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    //既然正常播放到完整停止，那就表示，当前播放的就是mCurrentMediaPlayer
                    //stop();
                    onCompletionNotify(mediaPlayer);
                }
            });

            mediaPlayer.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

                @Override
                public void onBufferingUpdate(MediaPlayer mp, int curPosition) {
                    onBuffering(mediaPlayer, curPosition);
                }
            });

            mediaPlayer.setOnErrorListener(new OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.e(TAG, "onError what: " + what + " , extra:" + extra);
                    if (what == MediaPlayer.MEDIA_ERROR_UNKNOWN) {
                        // 可能没有网络
                        //onErrorNotify(mediaPlayer , "" + mContext.getString(R.string.ringtone_play_net_fail));
                        stop();
                        return true;
                    }

                    // not sure what error code -1 exactly stands for but it causes player to start to jump songs
                    // if there are more than 5 jumps without playback during 1 second then we abort
                    // further playback
                    if (what == -1) {
                        long failTime = System.currentTimeMillis();
                        if (failTime - mLastFailTime > FAIL_TIME_FRAME) {
                            // outside time frame
                            mTimesFailed = 1;
                            mLastFailTime = failTime;
                            Log.w(TAG, "PlayerEngineImpl " + mTimesFailed + " fail within FAIL_TIME_FRAME");
                        } else {
                            // inside time frame
                            mTimesFailed++;
                            if (mTimesFailed > ACCEPTABLE_FAIL_NUMBER) {
                                Log.w(TAG, "PlayerEngineImpl too many fails, aborting playback");
                                //onErrorNotify(mediaPlayer ,  "" + mContext.getString(R.string.ringtone_play_fail));
                                stop();
                                return true;
                            }
                        }
                    }
                    return false;
                }
            });
            if (mHolder != null) {
                mediaPlayer.setDisplay(mHolder);
                Log.e(TAG, "************** jiangWB mediaPlayer.setDisplay(mHolder) *********************");
            } else if (mSurfaceView != null) {
                mediaPlayer.setDisplay(mSurfaceView.getHolder());
                Log.e(TAG, "************** jiangWB mediaPlayer.setDisplay(mSurfaceView.getHolder()) *********************");
            }
            mediaPlayer.preparing = true;
            mediaPlayer.prepareAsync();
            return mediaPlayer;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private final class CyclePhoneListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            Log.d(TAG, "************ 电话啊电话 state:" + state + " ********************");
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    //来电时player在播放就暂停
                    callplayerpuaseflag = true;
                    pause();
                    Log.d(TAG, "************ 来电话啦 ********************");
                    break;

                case TelephonyManager.CALL_STATE_IDLE:
                    //挂电话时，这时候就复杂些，1.判断是否歌曲一次播放并且非人为暂停，2判断来电前是否已经暂停。
                    if (callplayerpuaseflag) {
                        callplayerpuaseflag = false;
                        pause();
                    }
                    Log.d(TAG, "************ 挂电话啦 ********************");
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }


    /**
     * 通知每个监听，当前状态是：音频文件加载中
     */
    private void onLoading(int playID) {
        if (mPlayerEngineListeners != null && playID > 0) {
            Log.v(TAG, "onLoading(playID:" + playID + ")");
            for (PlayerEngineListener listen : mPlayerEngineListeners) {
                if (listen != null) {
                    listen.onLoading(playID);
                }
            }
        } else {
            mHandler.removeMessages(WHAT_LOAD_VOICE);
        }
    }

    /**
     * 通知每个监听，当前状态是：音频设备准备播放中
     */
    private void onPreparedNotify(InternalMediaPlayer mediaPlayer) {
        if (mPlayerEngineListeners != null) {
            int playID = mediaPlayer.id;
            Log.v(TAG, "onPrepared(playID:" + playID + " )");
            for (PlayerEngineListener listen : mPlayerEngineListeners) {
                if (listen != null) {
                    listen.onPrepared(playID, mediaPlayer.getDuration());
                }
            }
        }
    }

    /**
     * 通知每个监听，当前状态是：音频设备正在暂停中
     */
    private void onPause(InternalMediaPlayer mediaPlayer) {
        try {
            for (PlayerEngineListener listen : mPlayerEngineListeners) {
                if (listen != null) {
                    listen.onPause(mediaPlayer.id);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception-- isPreviousLoaded:", e);
        }
    }

    /**
     * 通知每个监听，当前状态是：音频设备正在缓存中
     */
    private void onBuffering(InternalMediaPlayer mediaPlayer, int curPosition) {

        if (mPlayerEngineListeners != null) {
            try {
                int palyID = mCurrentMediaPlayer.id;
                Log.v(TAG, "onPlaying(playID:" + palyID + " curBufferingPosition: " + curPosition + ")");
                if (mCurrentDuration > 0) {
                    for (PlayerEngineListener listen : mPlayerEngineListeners) {
                        if (listen != null) {
                            listen.onBuffering(palyID, curPosition);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception-- isPreviousLoaded:", e);
            }
        }
    }

    /**
     * 通知每个监听，当前状态是：音频设备正在播放中
     */
    private void onPlayingNotify(InternalMediaPlayer mediaPlayer) {
        int postion = 0;
        if (mPlayerEngineListeners != null) {
            try {
                int palyID = mCurrentMediaPlayer.id;
                postion = mCurrentMediaPlayer.getCurrentPosition();
                Log.v(TAG, "onPlaying(playID:" + palyID + " postion: " + postion + ")");
                if (mCurrentDuration > 0) {
                    if (mCurrentDuration == postion) {
                        mHandler.removeMessages(WHAT_COUNT_PLAY);
                    }
                    for (PlayerEngineListener listen : mPlayerEngineListeners) {
                        if (listen != null) {
                            listen.onPlaying(palyID, postion * 100 / mCurrentDuration);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception-- isPreviousLoaded:", e);
            }
        }
    }

    /**
     * 当前视频播放完成
     */
    private void onCompletionNotify(final InternalMediaPlayer mediaPlayer) {
        if (mPlayerEngineListeners != null) {
            try {
                int palyID = mCurrentMediaPlayer.id;
                Log.v(TAG, "onCompletion(playID:" + palyID + ")");
                for (PlayerEngineListener listen : mPlayerEngineListeners) {
                    if (listen != null) {
                        listen.onCompletion(palyID);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception-- isPreviousLoaded:", e);
            }
        }

    }

    /**
     * 通知每个监听，当前状态是：当前播放被中断:</br>&nbsp&nbsp（1）用户播放新音频中断当前</br>&nbsp&nbsp（2）出现异常
     */
    private void onInterruptNotify(final InternalMediaPlayer mediaPlayer) {
        int playID = -1;
        if (mediaPlayer != null) {
            playID = mediaPlayer.id;
        }
        if (playID == 0 || playID == -1) {
            playID = mPreparePlayID;
        }
        if (playID != 0 && playID != -1) {
            final int interruptID = playID;
            Log.v(TAG, "onInterrupt(playID:" + interruptID + ")");
            mPostHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mPlayerEngineListeners != null) {
                        for (PlayerEngineListener listen : mPlayerEngineListeners) {
                            if (listen != null) {
                                listen.onInterrupt(interruptID);
                            }
                        }
                    }
                }
            });
        }

    }

    /**
     * 通知每个监听，当前状态是：音频设备出现错误
     */
    private void onErrorNotify(final InternalMediaPlayer mediaPlayer, final String errorStr) {//loop
        //ToastUtil.showToast(mContext,  errorStr);
        mHandler.removeMessages(WHAT_LOAD_VOICE);
        mHandler.removeMessages(WHAT_COUNT_PLAY);
        if (mPlayerEngineListeners != null) {
            final int playID = mediaPlayer.id;
            Log.v(TAG, "onError(playID:" + playID + " error: " + errorStr + ")");
            mPostHandler.post(new Runnable() {
                @Override
                public void run() {
                    for (PlayerEngineListener listen : mPlayerEngineListeners) {
                        if (listen != null) {
                            listen.onError(playID, errorStr);
                        }
                    }
                }
            });

        }
    }

}
