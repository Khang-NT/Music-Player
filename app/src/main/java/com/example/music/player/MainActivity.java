package com.example.music.player;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatTextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.tv_status)
    AppCompatTextView tvStatus;
    @Bind(R.id.sb_position)
    AppCompatSeekBar sbPosition;
    @Bind(R.id.b_play)
    AppCompatButton bPlay;
    @Bind(R.id.b_pause)
    AppCompatButton bPause;
    @Bind(R.id.b_stop)
    AppCompatButton bStop;

    ServicePlayer servicePlayer;

    ServiceConnection serviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ServicePlayer.LocalBinder localBinder = (ServicePlayer.LocalBinder) service;
                servicePlayer = localBinder.getServiceInstance();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                servicePlayer = null;
            }

        };

        Intent intent = new Intent(this, ServicePlayer.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    @OnClick(R.id.b_play)
    public void play(){
        updateUI(false, true, true);
        if (servicePlayer != null){
            servicePlayer.play();
        }
    }

    @OnClick(R.id.b_pause)
    public void pause(){
        updateUI(true, false, true);
        if (servicePlayer != null){
            servicePlayer.pause();
        }
    }

    @OnClick(R.id.b_stop)
    public void stop(){
        updateUI(true, false, false);
        if (servicePlayer != null){
            servicePlayer.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        servicePlayer = null;
        unbindService(serviceConnection);

    }

    private void updateUI(boolean allowPlay, boolean allowPause, boolean allowStop){
        bPause.setEnabled(allowPause);
        bPlay.setEnabled(allowPlay);
        bStop.setEnabled(allowStop);
    }
}
