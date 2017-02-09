package com.etek.cognitivebeats;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class LibraryActivity extends AppCompatActivity {

    private CognitiveBeats beats;
    private EntrainmentService mService;
    private boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        // Register to receive messages.
        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions named "custom-event-name".
//        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
//                new IntentFilter("com.etek.cognitivebeats.EntrainmentService"));

     //   Intent test = new Intent(this, com.etek.cognitivebeats.EntrainmentService.class);

    //    startService(test);

        final Button playButton = (Button) findViewById(R.id.play);
        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(mBound) {
                    ArrayList<BeatConfiguration.BeatParameters> list = new ArrayList<BeatConfiguration.BeatParameters>();
//                    list.add(new BeatConfiguration.BeatParameters(400, 440, 20000, "Focus!"));
                    list.add(new BeatConfiguration.BeatParameters(200, 215, 1000 * 60 * 60 * 5));
//                    list.add(new BeatConfiguration.BeatParameters(200, 213, 10000));
                    ArrayList<String> effects = new ArrayList<String>();
                    ArrayList<String> refs = new ArrayList<String>();
                    BeatConfiguration config = new BeatConfiguration("TestConfig", "TestDescription",
                            effects, refs, list);
                    mService.play(config);
                }
            }
        });
        final Button stopButton = (Button) findViewById(R.id.stop);
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(mBound) {
                    mService.stop();
                }
            }
        });
        final Button pauseButton = (Button) findViewById(R.id.pause);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(mBound) {
                    mService.pause();
                }
            }
        });
        final Button resumeButton = (Button) findViewById(R.id.resume);
        resumeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(mBound) {
                    mService.resume();
                }
            }
        });
    }


    // Our handler for received Intents. This will be called whenever an Intent
    // with an action named "custom-event-name" is broadcasted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + message);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, EntrainmentService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mBound) {
            unbindService(mConnection);
            mBound = false;
        }

    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Intent intent = new Intent(this, EntrainmentService.class);
//        stopService(intent);
//    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            EntrainmentService.EntrainmentBinder binder = (EntrainmentService.EntrainmentBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };



}
