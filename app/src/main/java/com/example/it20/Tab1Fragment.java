package com.example.it20;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/*
   Основной фрагмент с таймерами
   В дальнейшем SP-Shared Preferences
 */

public class Tab1Fragment extends Fragment {

    boolean isFirst=true;
    TextView facebookTextHour, twitterTextHour, instagramTextHour, vkontakteTextHour, facebookTextMin,
            twitterTextMin, instagramTextMin, vkontakteTextMin, facebookTextSec, twitterTextSec,
            instagramTextSec, vkontakteTextSec;

    String faceSeconds, twitSeconds, instaSeconds, vkSeconds,
            faceMinutes, twitMinutes, instaMinutes, vkMinutes,
            faceHours, twitHours, instaHours, vkHours,
            isStarted, myCondition, allSocials, serviceHours, serviceMinutes, serviceSeconds;

    int inService;
    SharedPreferences sharedPreferences;
    CompoundButton compoundButton;
    private static final String TAG="Tab1Fragment";

    //При Resume все данные также подгружаются
    @Override
    public void onResume() {
        LoadPreferences();
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.tab1_fragment,container,false);
        //Инициализация
        facebookTextHour = (TextView)view. findViewById(R.id.facebook1);
        facebookTextMin = (TextView)view. findViewById(R.id.facebook2);
        facebookTextSec = (TextView) view.findViewById(R.id.facebook3);
        twitterTextHour = (TextView)view. findViewById(R.id.twitter1);
        twitterTextMin = (TextView) view.findViewById(R.id.twitter2);
        twitterTextSec = (TextView) view.findViewById(R.id.twitter3);
        instagramTextHour = (TextView) view.findViewById(R.id.instagram1);
        instagramTextMin = (TextView) view.findViewById(R.id.instagram2);
        instagramTextSec = (TextView) view.findViewById(R.id.instagram3);
        vkontakteTextHour = (TextView) view.findViewById(R.id.vkontakte1);
        vkontakteTextMin = (TextView) view.findViewById(R.id.vkontakte2);
        vkontakteTextSec = (TextView) view.findViewById(R.id.vkontakte3);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            compoundButton = (Switch) view.findViewById(R.id.startButton);
        } else {
            compoundButton = (CheckBox) view.findViewById(R.id.startButton);
        }
        if(isFirst){
            //При первом запуске Сервиса необходимо создать SP
            isFirst=false;
            SavePreferences("compoundButton", "false");
            SavePreferences("compoundButton", "false");
            SavePreferences("myStatementNow", "low");
            SavePreferences("allSocials", "0");

            SavePreferences("servicehour", "0");
            SavePreferences("servicemin", "0");
            SavePreferences("servicesec", "0");

            isEmpty(faceSeconds,facebookTextSec,"faceSeconds");
            isEmpty(faceMinutes,facebookTextMin,"faceMinutes");
            isEmpty(faceHours,facebookTextHour,"faceHour");

            isEmpty(twitSeconds,twitterTextSec,"twitSeconds");
            isEmpty(twitMinutes,twitterTextMin,"twitMinutes");
            isEmpty(twitHours,twitterTextHour,"twitHour");

            isEmpty(instaSeconds,instagramTextSec,"instaSeconds");
            isEmpty(instaMinutes,instagramTextMin,"instaMinutes");
            isEmpty(instaHours,instagramTextHour,"instaHour");

            isEmpty(vkSeconds,vkontakteTextSec,"vkSeconds");
            isEmpty(vkMinutes,vkontakteTextMin,"vkMinutes");
            isEmpty(vkHours,vkontakteTextHour,"vkHour");

        }
        //Подгрузка данных
        LoadPreferences();

        //Обработчик Switch Button при нажатии запускает сервис и Toast
        compoundButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton myButton, boolean isSwitched) {
                if (isSwitched == true) {
                    Log.d("myLogs","Clicked");
                    SavePreferences("compoundButton", "true");
                    Intent intent = new Intent(getActivity(), MyService.class);
                    getActivity().startService(intent);
                    if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        Intent intent1 = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        startActivity(intent1);
                    }
                    Toast.makeText(getActivity(), "InternetTime запущено", Toast.LENGTH_SHORT).show();
                } else {
                    SavePreferences("compoundButton", "false");
                    getActivity().stopService(new Intent(getActivity(), MyService.class));
                }
            }
        });
        return view;
    }
    //Стандартный метод для сохранения SP
    public void SavePreferences(String stringName, String stringValue) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(stringName, stringValue);
        edit.commit();
    }
    //Метод обновления всех SP
    public void LoadPreferences() {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        isStarted = sharedPreferences.getString("compoundButton", "false");
        myCondition = sharedPreferences.getString("myStatementNow", "low");
        allSocials = sharedPreferences.getString("allSocials", "0");

        serviceHours = sharedPreferences.getString("servicehour", "0");
        serviceMinutes = sharedPreferences.getString("servicemin", "0");
        serviceSeconds = sharedPreferences.getString("servicesec", "0");

        if (serviceHours.isEmpty()) {
            SavePreferences("servicehour", "0");
        } else {
            inService = Integer.parseInt(serviceHours);
        }

        if (serviceMinutes.isEmpty()) {
            SavePreferences("servicemin", "0");
        }

        if (serviceSeconds.isEmpty()) {
            SavePreferences("servicesec", "0");
        }


        if (allSocials.isEmpty()) {
            SavePreferences("allSocials", "0 Hours");
        }
        if (myCondition.isEmpty()) {
            SavePreferences("myStatementNow", "low");
        }
        myCondition = sharedPreferences.getString("myStatementNow", "low");

        if (isStarted.equals("true") && isMyServiceRunning(MyService.class)
                && isStarted.equals("true")) {
            compoundButton.setChecked(true);
        } else {
            compoundButton.setChecked(false);
        }

        isEmpty(faceSeconds,facebookTextSec,"faceSeconds");
        isEmpty(faceMinutes,facebookTextMin,"faceMinutes");
        isEmpty(faceHours,facebookTextHour,"faceHour");

        isEmpty(twitSeconds,twitterTextSec,"twitSeconds");
        isEmpty(twitMinutes,twitterTextMin,"twitMinutes");
        isEmpty(twitHours,twitterTextHour,"twitHour");

        isEmpty(instaSeconds,instagramTextSec,"instaSeconds");
        isEmpty(instaMinutes,instagramTextMin,"instaMinutes");
        isEmpty(instaHours,instagramTextHour,"instaHour");

        isEmpty(vkSeconds,vkontakteTextSec,"vkSeconds");
        isEmpty(vkMinutes,vkontakteTextMin,"vkMinutes");
        isEmpty(vkHours,vkontakteTextHour,"vkHour");
    }

    //Функция , изменяющая значение TextView для каждого таймера
    private void isEmpty(String shared, TextView text, String name)
    {
        shared=sharedPreferences.getString(name, "00");
        if(shared.isEmpty()){
            SavePreferences(name,"00");
        }
        else{
            text.setText(shared);
        }
    }
    //Функция , используемая для возрождения сервиса
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }



}
