package com.example.mainuddin.pomotodo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Promotodo_service extends Service {


    public static final String COUNTDOWN_BR = "com.example.czgame.wordgame.countdown_br";
    private final static String TAG = "BroadcastService";
    private final static String fileNameend = "bell";
    public static RemoteViews notificationView;
    public static RemoteViews notificationView1;
    public static PendingIntent pendingIntentPs;
    public static Intent nxReceive = new Intent();
    public static NotificationManager manager;
    public static NotificationCompat.Builder notificationBuilder;
    public static Notification notification;
    public static long total = 1800000;
    public static Intent bi = new Intent(COUNTDOWN_BR);
    public static CountDownTimer cdt;
    public static boolean ispause = true;
    public static MediaPlayer mp, mp1;
    public static String fileName = "none";
    public static boolean isStart;
    public static String start = "";

    public static void pause() {

        ispause = true;
        if (cdt != null) {

            if (!fileName.equals("none")) {
                mp.pause();
            }
            cdt.cancel();
        }
    }

    public static void resume(final Context context) {

        ispause = false;
        if (!fileName.equals("none")) {
            if (mp.isPlaying() == false) {
                mp.start();
                mp.setLooping(true);
            }
        }

        //cdt.cancel();
        cdt = new CountDownTimer(total, 1000) {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTick(long millisUntilFinished) {

                total = millisUntilFinished;
                Log.i(TAG, "Countdown seconds remaining: " + millisUntilFinished / 1000);

                bi.setAction(Promotodo_receiver.GET_TIME);
                bi.putExtra("countdown", millisUntilFinished);

                context.sendBroadcast(bi);
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onFinish() {
                if (!fileName.equals("none")) {
                    mp.stop();
                    mp1.start();
                }
                Log.i(TAG, "Timer finished");
                bi.setAction(Promotodo_receiver.SET_TIME);
                bi.putExtra("countdown", new Long(0));
                context.sendBroadcast(bi);
                total = 1800000;
            }
        };

        cdt.start();
    }

    @Override
    public void onDestroy() {

        cdt.cancel();
        if (!fileName.equals("none")) {

            mp.pause();
        }
        //  startService(new Intent(this, Promotodo_service.class));

        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);
        mp1 = new MediaPlayer();
        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC); //set streaming according to ur needs
        mp1.setAudioStreamType(AudioManager.STREAM_RING); //set streaming according to ur needs
        try {
            if (!fileName.equals("none")) {
                if (fileName.equals("rain")
                        || fileName.equals("oceanshore")
                        || fileName.equals("ticking")) {
                    mp.setDataSource(Promotodo_service.this, Uri.parse("android.resource://com.example.czgame.wordbank/raw/" + fileName));
                } else {
                    System.out.println(Environment.getExternalStorageDirectory().getAbsolutePath());
                    mp.setDataSource(Promotodo_service.this, Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() +
                            "/White_noise/" + fileName + ".mp3"));
                }
            }
            mp1.setDataSource(Promotodo_service.this, Uri.parse("android.resource://com.example.czgame.wordbank/raw/" + fileNameend));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //mp.setLooping(true);
        try {
            if (!fileName.equals("none")) {
                mp.prepare();
            }
            mp1.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (!fileName.equals("none")) {
                mp.start();
                mp.setLooping(true);
            }

        } catch (Exception e) {
            System.out.println("Unable to TunePlay: startRingtone(): " + e.toString());
        }


        isStart = true;
        if (isStart == true) {
            isStart = false;
            LocalTime localTime = LocalTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
            String time = localTime.format(dateTimeFormatter);
            start = time;
            System.out.println(start);
        }
        stattimer();
        return START_NOT_STICKY;
    }


    public void stattimer() {
        ispause = false;

        Log.i(TAG, "Starting timer...");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground();
        }

        cdt = new CountDownTimer(total, 1000) {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTick(long millisUntilFinished) {

                total = millisUntilFinished;
                Log.i(TAG, "Countdown seconds remaining: " + millisUntilFinished / 1000);


                bi.setAction(Promotodo_receiver.GET_TIME);
                bi.putExtra("countdown", millisUntilFinished);

                try {
                    bi.setClass(Promotodo_service.this, Promotodo_receiver.class);
                    sendBroadcast(bi);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onFinish() {
                if (!fileName.equals("none")) {
                    mp.stop();
                }
                mp1.start();
                Log.i(TAG, "Timer finished");

                bi.setAction(Promotodo_receiver.SET_TIME);
                bi.putExtra("countdown", new Long(0));
                bi.setClass(Promotodo_service.this, Promotodo_receiver.class);
                sendBroadcast(bi);
                total = 1800000;
            }
        };

        cdt.start();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "com.example.myapp";
        String channelName = "My Promotodo Service";

        NotificationChannel chan = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            chan.setLightColor(Color.BLUE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            chan.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        }
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(chan);
        }


        notificationView = new RemoteViews(getPackageName(), R.layout.promo_notification);
        notificationView.setTextViewText(R.id.timerview, "HH");
        notificationView.setTextViewText(R.id.timerview1, "MM");
        notificationView.setTextViewText(R.id.timerview2, "SS");

        nxReceive = new Intent();
        nxReceive.setClass(Promotodo_service.this, Promotodo_receiver.class);
        nxReceive.setAction(Promotodo_receiver.PAUSE_TIME);
        pendingIntentPs = PendingIntent.getBroadcast(this, Promotodo_receiver.REQUEST_CODE_NOTIFICATION, nxReceive, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationView.setOnClickPendingIntent(R.id.promo_bar_play, pendingIntentPs);
        notificationView1 = new RemoteViews(getPackageName(), R.layout.promo_notification_detail);
        notificationView1.setTextViewText(R.id.timerview, "HH");
        notificationView1.setTextViewText(R.id.timerview1, "MM");
        notificationView1.setTextViewText(R.id.timerview2, "SS");

        notificationView1.setOnClickPendingIntent(R.id.promo_bar_play, pendingIntentPs);

        notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);


        //notificationBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        notificationBuilder
                .setAutoCancel(false)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setSmallIcon(R.drawable.ic_timer)
                .setCustomContentView(notificationView)
                .setCustomBigContentView(notificationView1);


        notificationBuilder.setCustomContentView(notificationView);
        notificationBuilder.setCustomContentView(notificationView1);
        notification = notificationBuilder.build();
        notification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;

        startForeground(6, notification);


    }

    public void playSound(String soundPath) {
        MediaPlayer m = new MediaPlayer();

        m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }

        });

        try {

            AssetFileDescriptor descriptor = Promotodo_service.this.getAssets().openFd(soundPath);
            m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(),
                    descriptor.getLength());

            descriptor.close();

            m.prepare();
            m.setVolume(100f, 100f);
            m.setLooping(false);
            m.start();

        } catch (Exception e) {
            //Your catch code here.
        }
    }


}