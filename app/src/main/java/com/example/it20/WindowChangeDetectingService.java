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
    public static int serviceDHours, serviceDMinutes, serviceDSeconds;
    long timeNow=0,timeBefore=0;
    String preApp="com.android.it20";

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
                        setChangeTime(preApp,"com.twitter.android",0+3,1+3,2+3,"twitSeconds","twitMinutes","twitHours");
                        setChangeTime(preApp,"com.instagram.android",3+3,4+3,5+3,"instaSeconds","instaMinutes","instaHours");
                        setChangeTime(preApp,"com.vkontakte.android",6+3,7+3,8+3,"vkSeconds","vkMinutes","vkHours");



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