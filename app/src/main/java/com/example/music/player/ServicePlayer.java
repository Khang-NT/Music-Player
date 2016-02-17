package com.example.music.player;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import java.io.IOException;

/**
 * Created on 25/01/2016.
 *
 * @author KHANG NT. Email: khang.neon.1997@gmail.com
 */
public class ServicePlayer extends Service {

    private static final String TAG = "ServicePlayer";
    private static final int NOTIFICATION_ID = 0;
    private  LocalBinder myBinder = new LocalBinder();
    MediaPlayer mp;
    Notification notification;

    enum Status {
        IS_PLAYING, PAUSED
    }

    Status status = Status.PAUSED;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notification = new NotificationCompat.Builder(this)
                .setContentTitle("Music Player")
                .setContentText("Keep service running")
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        try {
            mp = MediaPlayer.create(this, R.raw.waiting_for_love);
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public long getPosition(){
        return mp.getCurrentPosition();
    }

    public long getDuration(){
        return mp.getDuration();
    }

    public void pause(){
        if (mp.isPlaying()){
            mp.pause();
            setStatus(Status.PAUSED);
        }
    }

    public void play(){
        if (!mp.isPlaying()){
            startForeground(NOTIFICATION_ID, notification);
            mp.start();
            setStatus(Status.IS_PLAYING);
        }
    }

    public void stop(){
        if (mp.isPlaying()){
            mp.pause();
            stopForeground(true); // Stop and remove notification
            setStatus(Status.PAUSED);
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    class LocalBinder extends Binder {
        public ServicePlayer getServiceInstance(){
            return ServicePlayer.this;
        }
    }
}
