package com.etek.cognitivebeats;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by user on 2/2/2017.
 */

public class AudioPlayer {
    private static final String TAG = "AudioPlayer";

    protected final int PLAY = 0;
    protected final int PAUSE = 1;
    protected final int STOP = 2;
    protected final int RESUME = 3;
    protected final int CONTINUE = 4;
    protected final int WHAT = 0;

    protected CognitiveBeats mBeats = new CognitiveBeats();
    protected BeatConfiguration mBeatConfiguration;

    static final private int mNumFrames = 512;
    protected int mFrameBufferSize = AudioTrack.getMinBufferSize(mBeats.sampleRate(),
            AudioFormat.CHANNEL_IN_STEREO,
            AudioFormat.ENCODING_PCM_16BIT) * 2;

    protected AudioTrack mTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
            mBeats.sampleRate(),
            AudioFormat.CHANNEL_OUT_STEREO,
            AudioFormat.ENCODING_PCM_16BIT,
            mFrameBufferSize,
            AudioTrack.MODE_STREAM);

    private AudioListener mAudioListener;
    public interface AudioListener {
        void finished();
    }

    public void addAudioListener(AudioListener t) {
        mAudioListener = t;
    }

    AudioPlayer() {
        mBeats.construct();
        startThread();
    }

    public void destroy() {
        mBeats.destroy();
        mThread.quit();
    }

    protected HandlerThread mThread;
    protected Looper mServiceLooper;
    protected ServiceHandler mServiceHandler;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            try {
                switch(msg.arg1) {
                    case PLAY:
                        BeatConfiguration config = msg.getData().getParcelable("BeatConfiguration");
                        startPlaying(config);
                        break;
                    case RESUME:
                        resume();
                        break;
                    case CONTINUE:
                        continuePlaying();
                        break;
                    case STOP:
                        stop();
                        break;
                    case PAUSE:
                        pause();
                        break;
                }
            } catch (InterruptedException e) {
                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }
        }
    }

    protected void startPlaying(BeatConfiguration config) {
        mBeatConfiguration = config;

        BeatConfiguration.BeatParameters param = mBeatConfiguration.getFirstTrack();

        mBeats.initialize(param.mFreq0, param.mFreq1);

        mTrack.flush();

        short[] audioData = mBeats.tickShorts(mFrameBufferSize);

        int bytesWritten = mTrack.write(audioData, 0, audioData.length);

        Log.v(TAG, "Started playing. BytesWritten = " + bytesWritten);

        mTrack.play();

        mBeatConfiguration.start();

        postContinue();
    }

    protected void continuePlaying() {
        short[] audioData = mBeats.tickShorts(mNumFrames);

        int bytesWritten = mTrack.write(audioData, 0, audioData.length);

        Log.v(TAG, "Continuing to play. BytesWritten = " + bytesWritten);

        mBeatConfiguration.update();

        if(mBeatConfiguration.isTrackFinished()) {
            Log.v(TAG, "Track finished.");
            BeatConfiguration.BeatParameters param = mBeatConfiguration.getNextTrack();
            if(param != null) {
                Log.v(TAG, "Initializing next track with " + param.mFreq0 + " and " + param.mFreq1);
                mBeats.initialize(param.mFreq0, param.mFreq1);
            }
        }

        if(mBeatConfiguration.isTotalFinished()) {
            Log.v(TAG, "Completely finished. Posting stop.");
            postStop();
        }
        else
            postContinue();
    }

    protected void resume() {
        mBeatConfiguration.discardTime();
        if(mBeatConfiguration.mBeats.size() != 0)
            continuePlaying();
    }

    protected void pause() throws InterruptedException {
        mServiceHandler.removeMessages(WHAT);
        Thread.sleep(400);
    }

    protected void stop() {
        mServiceHandler.removeMessages(WHAT);
        mTrack.stop();
        mTrack.flush();
        mBeatConfiguration.clear();
        mThread.quit();
        if(mAudioListener != null)
            mAudioListener.finished();
    }
    // This is only used internally to further the loading of audio data.
    protected void postContinue() {
        Message msg = mServiceHandler.obtainMessage();
        msg.what = WHAT;
        msg.arg1 = CONTINUE;
        mServiceHandler.sendMessage(msg);
    }

    public void postPlay(BeatConfiguration config) {
        if(!mThread.isAlive())
            startThread();
        Message msg = mServiceHandler.obtainMessage();
        msg.what = WHAT;
        msg.arg1 = PLAY;
        msg.getData().putParcelable("BeatConfiguration", config);
        mServiceHandler.sendMessage(msg);
    }

    void startThread() {
        mThread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        mThread.start();
        mServiceLooper = mThread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    public void postPause() {
        Message msg = mServiceHandler.obtainMessage();
        msg.what = WHAT;
        msg.arg1 = PAUSE;
        mServiceHandler.sendMessage(msg);
    }
    public void postResume() {
        Message msg = mServiceHandler.obtainMessage();
        msg.what = WHAT;
        msg.arg1 = RESUME;
        mServiceHandler.sendMessage(msg);
    }


    public void postStop() {
        Message msg = mServiceHandler.obtainMessage();
        msg.what = WHAT;
        msg.arg1 = STOP;
        mServiceHandler.sendMessage(msg);
    }

    public boolean isAlive() {
        return mThread.isAlive();
    }

}


