package com.etek.cognitivebeats;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2/2/2017.
 */

public class BeatConfiguration implements Parcelable {

    public String mTitle;
    public String mDescription;
    public ArrayList<String> mEffects = new ArrayList<String>();
    public ArrayList<String> mReferences = new ArrayList<String>();
    public ArrayList<BeatParameters> mBeats = new ArrayList<BeatParameters>();
    public ArrayList<TimeSchedule> mTimes = new ArrayList<TimeSchedule>();
    public TimeSchedule mTotalTime = new TimeSchedule();
    public int mCurrentTrack = 0;

    public int describeContents() {
        return 0;
    }
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mTitle);
        out.writeString(mDescription);
        out.writeSerializable(mEffects);
        out.writeSerializable(mReferences);
        out.writeTypedList(mBeats);
    }
    public static final Parcelable.Creator<BeatParameters> CREATOR
            = new Parcelable.Creator<BeatParameters>() {
        public BeatParameters createFromParcel(Parcel in) {
            return new BeatParameters(in);
        }

        public BeatParameters[] newArray(int size) {
            return new BeatParameters[size];
        }
    };
    private BeatConfiguration(Parcel in) {
        mTitle = in.readString();
        mDescription = in.readString();
        mEffects = (ArrayList<String>)in.readSerializable();
        mReferences = (ArrayList<String>)in.readSerializable();
        in.readTypedList(mBeats, BeatParameters.CREATOR);
        mCurrentTrack = 0;
        createTimeScheduleAndDuration();
    }
    public BeatConfiguration(String title, String description, ArrayList<String> effects, ArrayList<String> references,
                             ArrayList<BeatParameters> list) {
        mTitle = title;
        mDescription = description;
        mEffects.addAll(effects);
        mReferences.addAll(references);
        mBeats.addAll(list);
        createTimeScheduleAndDuration();
    }
    private void createTimeScheduleAndDuration() {
        mTimes.clear();
        for(BeatParameters b : mBeats) {
            mTimes.add(new TimeSchedule(b));
            mTotalTime.mDuration += b.mDuration;
        }
    }
    protected void start() {
        mTotalTime.start();
        mTimes.get(mCurrentTrack).start();
    }
    public void update() {
        mTotalTime.update();
        mTimes.get(mCurrentTrack).update();
    }
    public void discardTime() {
        mTotalTime.discardTime();
        mTimes.get(mCurrentTrack).discardTime();
    }
    public boolean isTrackFinished() {
        return mTimes.get(mCurrentTrack).isFinshed();
    }
    public boolean isTotalFinished() {
        return mTotalTime.isFinshed();
    }
    public BeatParameters getFirstTrack() {
        mCurrentTrack = 0;
        start();
        return mBeats.get(mCurrentTrack);
    }
    public BeatParameters getNextTrack() {
        if(++mCurrentTrack < mBeats.size()) {
            mTimes.get(mCurrentTrack).start();
            return mBeats.get(mCurrentTrack);
        }
        else
            return null;
    }
    public void clear() {
        mBeats.clear();
        mTimes.clear();
        mTotalTime.clear();
    }

    public static class BeatParameters implements Parcelable {

        public double mFreq0;
        public double mFreq1;
        public long mDuration;

        BeatParameters(double freq0, double freq1, long duration) {
            mFreq0 = freq0;
            mFreq1 = freq1;
            mDuration = duration;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeDouble(mFreq0);
            out.writeDouble(mFreq1);
            out.writeLong(mDuration);
        }

        public static final Parcelable.Creator<BeatParameters> CREATOR
                = new Parcelable.Creator<BeatParameters>() {
            public BeatParameters createFromParcel(Parcel in) {
                return new BeatParameters(in);
            }

            public BeatParameters[] newArray(int size) {
                return new BeatParameters[size];
            }
        };

        private BeatParameters(Parcel in) {
            mFreq0 = in.readDouble();
            mFreq1 = in.readDouble();
            mDuration = in.readLong();
        }
    }

    public class TimeSchedule {
        long mTimeStart = 0;
        long mTimeElapsed = 0;
        long mDuration = 0;

        TimeSchedule() {

        }
        TimeSchedule(BeatParameters b) {
            mDuration = b.mDuration;
        }
        public void clear() {
            mTimeStart = 0;
            mTimeElapsed = 0;
            mDuration = 0;
        }
        public void start() {
            mTimeStart = System.currentTimeMillis();
            mTimeElapsed = 0;
        }
        public void update() {
            long currentTime = System.currentTimeMillis();
            mTimeElapsed = mTimeElapsed + currentTime - mTimeStart;
            mTimeStart = currentTime;
        }
        public void discardTime() {
            mTimeStart = System.currentTimeMillis();
        }
        public boolean isFinshed() {
            return mTimeElapsed > mDuration;
        }
    }
}
