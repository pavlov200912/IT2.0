package com.example.it20;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import java.util.ArrayList;

/**
 * Created by пк on 02.06.2017.
 */

public class WindowChangeDetectingService extends AccessibilityService {
    SharedPreferences sharedPreferences;
    String isStarted="false";
    public static ArrayList<Integer> iSocials=new ArrayList<>();
    long timeNow=0,timeBefore=0;
    String preApp="com.android.it20";
    boolean isFirst=true;
    double condition;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //Configure these here for compatibility with API 13 and below.
        AccessibilityServiceInfo config = new AccessibilityServiceInfo();
        config.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        config.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        Log.d("myLogs","onServiceConnected");
        if (Build.VERSION.SDK_INT >= 16)
            //Just in case this helps
            config.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;

        setServiceInfo(config);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        LoadPreferences();

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH && isStarted.equals("true")) {
            LoadPreferences();

             String appName = "", s;
            if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                if (event.getPackageName() != null && event.getClassName() != null) {
                    ComponentName componentName = new ComponentName(
                            event.getPackageName().toString(),
                            event.getClassName().toString()
                    );

                    ActivityInfo activityInfo = tryGetActivity(componentName);
                    boolean isActivity = activityInfo != null;
                    if (isActivity) {
                        timeNow = System.currentTimeMillis();
                        s = componentName.flattenToShortString();
                        for (int i = 0; i < componentName.flattenToShortString().length(); i++) {
                            if (s.charAt(i) != '/') {
                                appName += "" + s.charAt(i);
                            } else {
                                break;
                            }
                        }
                        timeBefore=Long.parseLong(sharedPreferences.getString("time", "0"));

                        setChangeTime(preApp,"com.facebook.katana",0,1,2,"faceSeconds","faceMinutes","faceHours");
                        setChangeTime(preApp,"com.twitter.android", 3, 4, 5,"twitSeconds","twitMinutes","twitHours");
                        setChangeTime(preApp,"com.instagram.android", 6, 7, 8,"instaSeconds","instaMinutes","instaHours");
                        setChangeTime(preApp,"com.vkontakte.android", 9, 10, 11,"vkSeconds","vkMinutes","vkHours");


                        if(timeBefore!=0){
                            SavePreferences("allService", String.valueOf((Integer.parseInt(sharedPreferences.getString("allService","0"))+(timeNow-timeBefore))));
                            SavePreferences("servicesec",sharedPreferences.getString("allService","0"));
                        }

                        condition=Double.parseDouble(sharedPreferences.getString("allSocials","0"))/(double)Double.parseDouble(sharedPreferences.getString("allService","0"));
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
                        SavePreferences("time", String.valueOf(timeNow));
                        Log.d("myLogs", appName);
                        Log.d("myLogs", String.valueOf(timeNow-timeBefore));
                        Log.d("myLogs",preApp);
                        preApp=appName;

                    }
                }
            }
        }
    }

    private ActivityInfo tryGetActivity(ComponentName componentName) {
        try {
            return getPackageManager().getActivityInfo(componentName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    @Override
    public void onInterrupt() {
    }

    public void LoadPreferences() {

        isStarted = sharedPreferences.getString("compoundButton", "false");
        if(isFirst) {
            isFirst=false;
            iSocials.add(Integer.parseInt(sharedPreferences.getString("faceHours", "0")));
            iSocials.add(Integer.parseInt(sharedPreferences.getString("faceMinutes", "0")));
            iSocials.add(Integer.parseInt(sharedPreferences.getString("faceSeconds", "0")));

            SavePreferences("allSocials","0");
            SavePreferences("allService","0");

            iSocials.add(Integer.parseInt(sharedPreferences.getString("twitHours", "0")));
            iSocials.add(Integer.parseInt(sharedPreferences.getString("twitMinutes", "0")));
            iSocials.add(Integer.parseInt(sharedPreferences.getString("twitSeconds", "0")));

            iSocials.add(Integer.parseInt(sharedPreferences.getString("instaHours", "0")));
            iSocials.add(Integer.parseInt(sharedPreferences.getString("instaMinutes", "0")));
            iSocials.add(Integer.parseInt(sharedPreferences.getString("instaSeconds", "0")));

            iSocials.add(Integer.parseInt(sharedPreferences.getString("vkHours", "0")));
            iSocials.add(Integer.parseInt(sharedPreferences.getString("vkMinutes", "0")));
            iSocials.add(Integer.parseInt(sharedPreferences.getString("vkSeconds", "0")));
        }
        else{
            iSocials.set(0,Integer.parseInt(sharedPreferences.getString("faceHours", "0")));
            iSocials.set(1,Integer.parseInt(sharedPreferences.getString("faceMinutes", "0")));
            iSocials.set(2,Integer.parseInt(sharedPreferences.getString("faceSeconds", "0")));

            iSocials.set(3,Integer.parseInt(sharedPreferences.getString("twitHours", "0")));
            iSocials.set(4,Integer.parseInt(sharedPreferences.getString("twitMinutes", "0")));
            iSocials.set(5,Integer.parseInt(sharedPreferences.getString("twitSeconds", "0")));

            iSocials.set(6,Integer.parseInt(sharedPreferences.getString("instaHours", "0")));
            iSocials.set(7,Integer.parseInt(sharedPreferences.getString("instaMinutes", "0")));
            iSocials.set(8,Integer.parseInt(sharedPreferences.getString("instaSeconds", "0")));

            iSocials.set(9,Integer.parseInt(sharedPreferences.getString("vkHours", "0")));
            iSocials.set(10,Integer.parseInt(sharedPreferences.getString("vkMinutes", "0")));
            iSocials.set(11,Integer.parseInt(sharedPreferences.getString("vkSeconds", "0")));
        }


    }

    public void SavePreferences(String key, String value) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        edit.commit();

    }
    private  void setChangeTime(String appName,String needApp,int hou,int min,int sec,String nameS,String nameM,String nameH){

        if(appName.equals(needApp)) {
        if(appName=="com.facebook.katana")Log.d("myLogs","Yes Facebook is detected");
            if(timeBefore!=0){
                long ds=(timeNow-timeBefore)/1000,dm=iSocials.get(min),dh=iSocials.get(hou);
                ds+=iSocials.get(sec);
                SavePreferences("allSocials", String.valueOf((Double.parseDouble(sharedPreferences.getString("allSocials","0"))+ds)/(double)3600));
                if(ds>=60)
                {
                    dm=ds/60;
                    ds=ds%60;
                }
                if(dm>=60)
                {
                    dh=dm/60;
                    dm=dm%60;
                }
                if(dh>=24)
                {
                    ds=0;dm=0;dh=0;
                }
                if (ds < 10) {
                    SavePreferences(nameS, "0"+ds);
                }

                if (dm < 10) {
                    SavePreferences(nameM, "0"+dm);
                }

                if (dh < 10) {
                    SavePreferences(nameH, "0"+dh);
                }
                if(ds>=10)
                {
                    SavePreferences(nameS, ""+ds);
                }
                if (dm >= 10) {
                    SavePreferences(nameM, ""+dm);
                }

                if (dh >= 10) {
                    SavePreferences(nameH, ""+dh);
                }

            }
        }
    }
}