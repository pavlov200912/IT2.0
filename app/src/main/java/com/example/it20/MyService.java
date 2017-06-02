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
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class MyService extends Service {
    private static final int nSocials=4;

    public static String isStarted, startedApp;
    public static int serviceDHours, serviceDMinutes, serviceDSeconds;
    public static ArrayList<Integer> iSocials=new ArrayList<>();

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


        iSocials.add(Integer.parseInt(sharedPreferences.getString("faceHours", "0")));
        iSocials.add(Integer.parseInt(sharedPreferences.getString("faceMinutes", "0")));
        iSocials.add(Integer.parseInt(sharedPreferences.getString("faceSeconds", "0")));

        iSocials.add(Integer.parseInt(sharedPreferences.getString("twitHours", "0")));
        iSocials.add(Integer.parseInt(sharedPreferences.getString("twitMinutes", "0")));
        iSocials.add(Integer.parseInt(sharedPreferences.getString("twitSeconds", "0")));

        iSocials.add(Integer.parseInt(sharedPreferences.getString("instaHours", "0")));
        iSocials.add(Integer.parseInt(sharedPreferences.getString("instaMinutes", "0")));
        iSocials.add(Integer.parseInt(sharedPreferences.getString("instaSeconds", "0")));

        iSocials.add(Integer.parseInt(sharedPreferences.getString("vkHours", "0")));
        iSocials.add(Integer.parseInt(sharedPreferences.getString("vkMinutes", "0")));
        iSocials.add(Integer.parseInt(sharedPreferences.getString("vkSeconds", "0")));

        serviceDSeconds = Integer.parseInt(sharedPreferences.getString("servicesec", "0"));
        serviceDMinutes = Integer.parseInt(sharedPreferences.getString("servicemin", "0"));
        serviceDHours = Integer.parseInt(sharedPreferences.getString("servicehour", "0"));

    }

    @Override
    public void onCreate() {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Log.d("myLogs","onCreate");
        String uxo=sharedPreferences.getString("compoundButton", "false");
        if(!uxo.equals("false")) showNotification("m");
    }

    @SuppressLint({"HandlerLeak", "SimpleDateFormat"})
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("myLogs","onStartCommand");
        super.onStartCommand(intent, flags, startId);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        serviceControler = new Handler() {
            @SuppressWarnings("deprecation")
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    if(isStarted.equals("false")){
                     stopSelf();
                    }
                    LoadPreferences();
                    SavePreferences("allSocials", String.valueOf((iSocials.get(0) + iSocials.get(3) +iSocials.get(6) +iSocials.get(9)+
                            ( (double) (iSocials.get(0+1) + iSocials.get(3+1) +iSocials.get(6+1) +iSocials.get(9+1)) / 60) +
                            ( (double) (iSocials.get(0+2) + iSocials.get(3+2) +iSocials.get(6+2) +iSocials.get(9+2)) / 3600))));
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
                    condition = ( (iSocials.get(0) + iSocials.get(3) +iSocials.get(6) +iSocials.get(9))*3600 +
                            (iSocials.get(0+1) + iSocials.get(3+1) +iSocials.get(6+1) +iSocials.get(9+1)) * 60 +
                            (iSocials.get(0+2) + iSocials.get(3+2) +iSocials.get(6+2) +iSocials.get(9+2)) ) /
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

                if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    final int PROCESS_STATE_TOP = 2;
                    ActivityManager.RunningAppProcessInfo currentInfo = null;
                    Field field = null;
                    try {
                        field = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");
                    } catch (Exception ignored) {
                    }
                    ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
                    List<ActivityManager.RunningAppProcessInfo> appList = am.getRunningAppProcesses();
                    for (ActivityManager.RunningAppProcessInfo app : appList) {
                        if (app.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                                && app.importanceReasonCode == ActivityManager.RunningAppProcessInfo.REASON_UNKNOWN) {
                            Integer state = null;
                            try {
                                state = field.getInt(app);
                            } catch (Exception e) {
                            }
                            if (state != null && state == PROCESS_STATE_TOP) {
                                currentInfo = app;
                                break;
                            }
                        }
                    }

                    func1(String.valueOf(currentInfo));
                }
                else {
                    ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Activity.ACTIVITY_SERVICE);
                    startedApp = am.getRunningTasks(1).get(0).topActivity.getPackageName();
                    Log.d("myLogs",startedApp);
                    func1(startedApp);
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
    private ActivityManager activityManager() {

        return (ActivityManager) getApplicationContext().getSystemService(
                Context.ACTIVITY_SERVICE);
    }

    public void func1(String startedApp) {
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

    @SuppressLint("InlinedApi")


    public void onDestroy() {
        super.onDestroy();
        String close = sharedPreferences.getString("InTheEnd", "no");

        if (isStarted.equals("true") && close.equals("no")) {
            nm.cancelAll();
                //startService(new Intent(MyService.this, MyService.class));

        } else if (isStarted.equals("true") && close.equals("yes")) {

            nm.cancelAll();

        } else {

            nm.cancelAll();
        }
        stopSelf();
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

                setChangeTime(0,1,2,"faceSeconds","faceMinutes","faceHours");
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

                setChangeTime(0+3,1+3,2+3,"twitSeconds","twitMinutes","twitHours");
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
                setChangeTime(3+3,4+3,5+3,"instaSeconds","instaMinutes","instaHours");
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

                setChangeTime(6+3,7+3,8+3,"vkSeconds","vkMinutes","vkHours");
                vkControl.postDelayed(this, 0);

            }
        }

    };

    private  void setChangeTime(int hou,int min,int sec,String nameS,String nameM,String nameH){

        iSocials.set(sec,iSocials.get(sec)+1);
        if(iSocials.get(sec)>=60){
            iSocials.set(sec,0);
            iSocials.set(min,iSocials.get(min)+1);
        }
        if(iSocials.get(min)>=60){
            iSocials.set(min,0);
            iSocials.set(hou,iSocials.get(hou)+1);
        }
        if (iSocials.get(sec) < 10) {
            SavePreferences(nameS, "0"+iSocials.get(sec));
        }

        if (iSocials.get(min) < 10) {
            SavePreferences(nameM, "0"+iSocials.get(min));
        }

        if (iSocials.get(hou) < 10) {
            SavePreferences(nameH, "0"+iSocials.get(hou));
        }
        if(iSocials.get(sec)>=10)
        {
            SavePreferences(nameS, ""+iSocials.get(sec));
        }
        if (iSocials.get(min) >= 10) {
            SavePreferences(nameM, ""+iSocials.get(min));
        }

        if (iSocials.get(hou) >= 10) {
            SavePreferences(nameH, ""+iSocials.get(hou));
        }
    }
}