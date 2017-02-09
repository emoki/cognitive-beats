package com.etek.cognitivebeats;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.util.ArrayList;



public class EntrainmentService extends Service implements AudioPlayer.AudioListener {

    private AudioPlayer mPlayer;

    private NotificationManager mNotificationManager;
    NotificationCompat.Builder mNotificationBuilder;
   // private static final int NOTIFY_ID = R.layout.;

    PowerManager.WakeLock mWakeLock;

    private final IBinder mBinder = new EntrainmentBinder();

    int currentlyBound = 0;
    boolean isCurrentlyBound() { return currentlyBound != 0; }

    class EntrainmentBinder extends Binder {
        EntrainmentService getService() {
            return EntrainmentService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // If first binding start the service to ensure
        // we can continue to play audio after activity unbinds.
        if(++currentlyBound == 1) {
            Intent test = new Intent(this, com.etek.cognitivebeats.EntrainmentService.class);
            startService(test);
        }
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // Stop the service if no activities are bound and we are
        // not currently playing audio.
        if(--currentlyBound == 0 && !mPlayer.isAlive())
            stopSelf();
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mPlayer = new AudioPlayer();
        mPlayer.addAudioListener(this);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mNotificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_test)
                        .setContentTitle("")
                        .setContentText("We're playing motherfucker!");
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, LibraryActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(LibraryActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mNotificationBuilder.setContentIntent(resultPendingIntent);

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "EntrainmentServiceWakeLock");
        mWakeLock.acquire();
    }

    @Override
    public void onDestroy() {
        mPlayer.destroy();
        //mNotificationManager.cancel(NOTIFICATION_SERVICE);
        mWakeLock.release();
        super.onDestroy();
    }

    public void finished() {
        stopForeground(true);
        if(!isCurrentlyBound())
            stopSelf();
    }

    public void play(BeatConfiguration config) {
        startForeground (R.string.notification_id, getNotification(config.mTitle));
        mPlayer.postPlay(config);
    }

    private Notification getNotification(String title) {
        mNotificationBuilder.setContentText(title);
        return mNotificationBuilder.build();
    }

    public void pause() {
        mPlayer.postPause();
    }

    public void resume() {
        mPlayer.postResume();
    }

    public void stop() {
        mPlayer.postStop();
    }
}
