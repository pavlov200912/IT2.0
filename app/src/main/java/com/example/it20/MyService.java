package com.example.it20;

/**
 * Created by пк on 27.05.2017.
 */


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MyService extends Service {

    public static String isStarted, startedApp, reBoot;
    public static int faceDHours, faceDMinutes, faceDSeconds,
            twitDHours, twitDMinutes, twitDSeconds,
            instDHours, instDMinutes, instDSeconds,
            vkDHours, vkDMinutes, vkDSeconds,
            serviceDHours, serviceDMinutes, serviceDSeconds;


    int isFirstFacebook, isFirstTwitter, isFirstInstagram, isFirstVkontakte;
    double condition;


    private Handler serviceControler;

    private Handler faceControl = new Handler();
    private Handler twitControl = new Handler();
    private Handler instControl = new Handler();
    private Handler vkControl = new Handler();

    Boolean facebookB, twitterB, instagramB, vkontakteB;

    NotificationManager nm;

    SharedPreferences sharedPreferences;

    @Override
    public IBinder onBind(Intent arg0) {

        return null;
    }

    public void LoadPreferences() {

        isStarted = sharedPreferences.getString("compoundButton", "false");

        faceDHours = Integer.parseInt(sharedPreferences.getString("faceHours", "0"));
        faceDMinutes = Integer.parseInt(sharedPreferences.getString("faceMinutes", "0"));
        faceDSeconds = Integer.parseInt(sharedPreferences.getString("faceSeconds", "0"));

        twitDHours = Integer.parseInt(sharedPreferences.getString("twitHours", "0"));
        twitDMinutes = Integer.parseInt(sharedPreferences.getString("twitMinutes", "0"));
        twitDSeconds = Integer.parseInt(sharedPreferences.getString("twitSeconds", "0"));

        instDHours = Integer.parseInt(sharedPreferences.getString("instaHours", "0"));
        instDMinutes = Integer.parseInt(sharedPreferences.getString("instaMinutes", "0"));
        instDSeconds = Integer.parseInt(sharedPreferences.getString("instaSeconds", "0"));

        vkDHours = Integer.parseInt(sharedPreferences.getString("vkHours", "0"));
        vkDMinutes = Integer.parseInt(sharedPreferences.getString("vkMinutes", "0"));
        vkDSeconds = Integer.parseInt(sharedPreferences.getString("vkSeconds", "0"));
        Log.d("myLogs","load");
        serviceDSeconds = Integer.parseInt(sharedPreferences.getString("servicesec", "0"));
        serviceDMinutes = Integer.parseInt(sharedPreferences.getString("servicemin", "0"));
        serviceDHours = Integer.parseInt(sharedPreferences.getString("servicehour", "0"));

    }

    @Override
    public void onCreate() {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        reBoot = sharedPreferences.getString("reBoot", "false");
        if (reBoot.isEmpty()) {
            SavePreferences("reBoot", "false");
        }
        Log.d("myLogs","onCreate");
        String uxo=sharedPreferences.getString("compoundButton", "false");
        if(!uxo.equals("false")) showNotification("m");
    }

    @SuppressLint({"HandlerLeak", "SimpleDateFormat"})
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        serviceControler = new Handler() {
            @SuppressWarnings("deprecation")
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {

                    LoadPreferences();
                    SavePreferences("allSocials", String.valueOf((faceDHours + twitDHours + instDHours + vkDHours +
                            ( (double) (faceDMinutes + twitDMinutes + instDMinutes + vkDMinutes) / 60) +
                            ( (double) (faceDSeconds + twitDSeconds + instDSeconds + vkDSeconds) / 3600))));
                    serviceDSeconds++;

                    if (serviceDSeconds >= 60) {
                        serviceDSeconds = 00;
                        serviceDMinutes ++;

                    }

                    if (serviceDMinutes >= 60) {
                        serviceDMinutes = 00;
                        serviceDHours ++;

                    }

                    SavePreferences("servicesec", String.valueOf(serviceDSeconds));
                    SavePreferences("servicemin", String.valueOf(serviceDMinutes));
                    SavePreferences("servicehour", String.valueOf(serviceDHours));
                    condition = ( (faceDHours + twitDHours + instDHours + vkDHours)*3600 +
                            (faceDMinutes + twitDMinutes + instDMinutes + vkDMinutes) * 60 +
                            (faceDSeconds + twitDSeconds + instDSeconds + vkDSeconds) ) /
                            (double) (serviceDHours*3600+serviceDMinutes*60+serviceDSeconds);

                } catch (NullPointerException nullPointerException) {

                }

                if (serviceDHours >= 0) {

                    if (condition <= 0.2) {

                        SavePreferences("myStatementNow", "low");

                    } else {

                        if (condition > 0.2 && condition < 0.35) {

                            SavePreferences("myStatementNow", "Average");

                        } else {

                            if (condition >= 0.35 && condition < 0.5) {

                                SavePreferences("myStatementNow", "attention");

                            } else {

                                if (condition >= 0.5 && condition < 0.75) {

                                    SavePreferences("myStatementNow", "Addicted");

                                } else {

                                    if (condition >= 0.75) {

                                        SavePreferences("myStatementNow", "DANGER");

                                    }
                                }
                            }
                        }
                    }
                }

                ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Activity.ACTIVITY_SERVICE);
                startedApp = am.getRunningTasks(1).get(0).topActivity.getPackageName();

                Log.d("myLogs",startedApp);
                if (isStarted.equals("true")) {
                    if (startedApp.equals("com.facebook.katana") || startedApp.equals("com.facebook.lite")) {
                        if (isFirstFacebook == 0) {
                            isFirstFacebook = 1;
                            facebookB = true;
                            faceControl.postDelayed(timerFacebook, 0);
                        }
                    } else {
                        if (isFirstFacebook == 1) {
                            facebookB = false;
                            faceControl.removeCallbacks(timerFacebook);
                            isFirstFacebook = 0;
                        }
                    }

                    if (startedApp.equals("com.android.calendar")) {
                        if (isFirstTwitter == 0) {
                            isFirstTwitter = 1;
                            twitterB = true;
                            twitControl.postDelayed(timerTwitter, 0);
                        }
                    } else {
                        if (isFirstTwitter == 1) {
                            twitterB = false;
                            twitControl.removeCallbacks(timerTwitter);
                            isFirstTwitter = 0;
                        }
                    }
                    if (startedApp.equals("com.instagram.android")) {
                        if (isFirstInstagram == 0) {
                            isFirstInstagram = 1;
                            instagramB = true;
                            instControl.postDelayed(timerInstagram, 0);
                        }
                    } else {
                        if (isFirstInstagram == 1) {
                            instagramB = false;
                            instControl.removeCallbacks(timerInstagram);
                            isFirstInstagram = 0;
                        }
                    }
                    if (startedApp.equals("com.vkontakte.android")) {
                        if (isFirstVkontakte == 0) {
                            isFirstVkontakte = 1;
                            vkontakteB = true;
                            vkControl.postDelayed(timerVkontakte, 0);
                        }
                    } else {
                        if (isFirstVkontakte == 1) {
                            vkontakteB = false;
                            vkControl.removeCallbacks(timerVkontakte);
                            isFirstVkontakte = 0;
                        }
                    }
                }

            }

        };

        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        serviceControler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
        }).start();

        return START_STICKY;
    }

    @SuppressLint("InlinedApi")


    public void onDestroy() {
        super.onDestroy();

        String close = sharedPreferences.getString("InTheEnd", "no");
        Log.d("myLogs","onDestroy");
        if (isStarted.equals("true") && close.equals("no")) {
            nm.cancelAll();
            startService(new Intent(MyService.this, MyService.class));

        } else if (isStarted.equals("true") && close.equals("yes")) {

            nm.cancelAll();

        } else {

            nm.cancelAll();
        }

    }

    public void SavePreferences(String key, String value) {
        Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        edit.commit();

    }
    private void showNotification(String m) {

        LoadPreferences();

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.iconit)
                        .setContentTitle("InternetTime")
                        .setContentText("Сервис запущен");

        Intent targetIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(0, builder.build());

    }

    private Runnable timerFacebook = new Runnable() {

        public void run() {

            try {

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (facebookB = true) {

                setChangeTime(faceDSeconds,faceDMinutes,faceDHours,"faceSeconds","faceMinutes","faceHours");
                faceControl.postDelayed(this, 0);

            }
        }

    };

    private Runnable timerTwitter = new Runnable() {

        public void run() {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (twitterB = true) {

                setChangeTime(twitDSeconds,twitDMinutes,twitDHours,"twitSeconds","twitMinutes","twitHours");
                twitControl.postDelayed(this, 0);

            }
        }

    };


    private Runnable timerInstagram = new Runnable() {
        public void run() {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (instagramB = true) {
                setChangeTime(instDSeconds,instDMinutes,instDHours,"instaSeconds","instaMinutes","instaHours");
                instControl.postDelayed(this, 0);

            }
        }

    };


    private Runnable timerVkontakte = new Runnable() {

        public void run() {

            try {

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (vkontakteB = true) {

                setChangeTime(vkDSeconds,vkDMinutes,vkDHours,"vkSeconds","vkMinutes","vkHours");
                vkControl.postDelayed(this, 0);

            }
        }

    };

    private  void setChangeTime(int sec,int min,int hou,String nameS,String nameM,String nameH){
        sec++;
        if(sec>=60){
            sec=0;
            min++;
        }
        if(min>=60){
            min=0;
            hou++;
        }
        if (sec < 10) {
            SavePreferences(nameS, "0"+sec);
        }

        if (min < 10) {
            SavePreferences(nameM, "0"+min);
        }

        if (hou < 10) {
            SavePreferences(nameH, "0"+hou);
        }
        if(sec>=10)
        {
            SavePreferences(nameS, ""+sec);
        }
        if (min >= 10) {
            SavePreferences(nameM, ""+min);
        }

        if (hou >= 10) {
            SavePreferences(nameH, ""+hou);
        }
    }
}