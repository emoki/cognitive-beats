package com.etek.cognitivebeats;

/**
 * Created by user on 1/31/2017.
 */

public class CognitiveBeats {

    static {
        System.loadLibrary("native-lib");
    }

    CognitiveBeats() {
        // Do dummy initialization to set up sample rate.
        construct();
    }

    public native void construct();

    public native void destroy();

    public native void initialize(double freq0, double freq1);

    public native float[] tick(int num_frames);

    public native short[] tickShorts(int num_frames);

    public native int sampleRate();

    private long nativeHandle;
}
