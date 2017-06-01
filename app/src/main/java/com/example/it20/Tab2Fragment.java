package com.example.it20;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * Created by User on 2/28/2017.
 */

public class Tab2Fragment extends Fragment {
    private static final String TAG = "Tab2Fragment";

    private Button bClear;
    int inService;
    public RelativeLayout rel;
    public static int faceDHours, faceDMinutes, faceDSeconds,
            twitDHours, twitDMinutes, twitDSeconds,
            instDHours, instDMinutes, instDSeconds,
            vkDHours, vkDMinutes, vkDSeconds;
    TextView facebookTextHour, twitterTextHour, instagramTextHour, vkontakteTextHour, facebookTextMin,
            twitterTextMin, instagramTextMin, vkontakteTextMin, facebookTextSec, twitterTextSec,
            instagramTextSec, vkontakteTextSec,s1,s2,s3;
    private ImageView mImage1,mImage2,mImage3,mImage4;
    long i1,i2,i3,i4;
    long a[]= new long[4];
    TextView myStatementNow, totalAllText, useForDay;
    SharedPreferences sharedPreferences;
    String isStarted, myCondition, allSocials, serviceHours, serviceMinutes, serviceSeconds;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2_fragment,container,false);

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

        s1 = (TextView) view.findViewById(R.id.textView);
        s2 = (TextView) view.findViewById(R.id.textView5);
        s3 = (TextView) view.findViewById(R.id.textView3);

        rel=(RelativeLayout)view.findViewById(R.id.rel1);

        totalAllText = (TextView) view.findViewById(R.id.textView15);
        myStatementNow = (TextView) view.findViewById(R.id.textView13);
        useForDay = (TextView) view.findViewById(R.id.textView17);
        bClear=(Button)view.findViewById(R.id.clear);
        LoadPreferences();
        bClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        if(allSocials.isEmpty()){
                            Toast.makeText(getActivity(),"Nope",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getActivity(),allSocials,Toast.LENGTH_SHORT).show();
                        }

                         SavePreferences("faceSeconds", "00");
                         SavePreferences("faceMinutes", "00");
                         SavePreferences("faceHours", "00");

                         SavePreferences("twitSeconds", "00");
                         SavePreferences("twitMinutes", "00");
                         SavePreferences("twitHours", "00");

                         SavePreferences("instaSeconds", "00");
                         SavePreferences("instaMinutes", "00");
                         SavePreferences("instaHours", "00");

                         SavePreferences("vkSeconds", "00");
                         SavePreferences("vkMinutes", "00");
                         SavePreferences("vkHours", "00");

                         SavePreferences("servicesec", "00");
                         SavePreferences("servicemin", "00");
                         SavePreferences("servicehour", "00");

                         SavePreferences("myStatementNow", "low");
                         SavePreferences("allSocials", "0");
                         facebookTextHour .setText("00");
                         facebookTextMin .setText("00");
                         facebookTextSec .setText("00");
                         twitterTextHour .setText("00");
                         twitterTextMin  .setText("00");
                         twitterTextSec .setText("00");
                         instagramTextHour.setText("00");
                         instagramTextMin .setText("00");
                         instagramTextSec .setText("00");
                         vkontakteTextHour.setText("00");
                         vkontakteTextMin .setText("00");
                         vkontakteTextSec .setText("00");

                         myStatementNow.setText("Низкий");

                         totalAllText.setText("0" + "Часов");
                         useForDay.setText(" ... 24ч");

            }
        });
        mImage1 = (ImageView) view.findViewById(R.id.imageView5);
        mImage2 = (ImageView) view.findViewById(R.id.imageView6);
        mImage3 = (ImageView) view.findViewById(R.id.imageView7);
        mImage4 = (ImageView) view.findViewById(R.id.imageView8);
        //TODO переделать
        i1=faceDHours*3600+faceDMinutes*60+faceDSeconds;
        i2=twitDHours*3600+twitDMinutes*60+twitDSeconds;
        i3=instDHours*3600+instDMinutes*60+instDSeconds;
        i4=vkDHours*3600+vkDMinutes*60+vkDSeconds;
        for(int i=0;i<4;i++) {
            if (i1 == i2 || i1 == i3 || i1 == i4) {
                i1 += 2;
            }
            if (i2 == i3 || i2 == i4) {
                i2 += 2;
            }
            if (i4 == i3) {
                i3 += 2;
            }
        }
        a[0]=i1;
        a[1]=i2;
        a[2]=i3;
        a[3]=i4;
        Arrays.sort(a);

        if(a[0]==i1){
            mImage1.setImageResource(R.drawable.facebook);
        }
        if(a[0]==i2){
            mImage1.setImageResource(R.drawable.twitter);
        }
        if(a[0]==i3){
            mImage1.setImageResource(R.drawable.instagram);
        }
        if(a[0]==i4){
            mImage1.setImageResource(R.drawable.rsz_vk_icon);
        }
        if(a[1]==i1){
            mImage2.setImageResource(R.drawable.facebook);
        }
        if(a[1]==i2){
            mImage2.setImageResource(R.drawable.twitter);
        }
        if(a[1]==i4){
            mImage2.setImageResource(R.drawable.rsz_vk_icon);
        }
        if(a[1]==i3){
            mImage2.setImageResource(R.drawable.instagram);
        }
        if(a[2]==i1){
            mImage3.setImageResource(R.drawable.facebook);
        }
        if(a[2]==i4){
            mImage3.setImageResource(R.drawable.rsz_vk_icon);
        }
        if(a[2]==i3){
            mImage3.setImageResource(R.drawable.instagram);
        }
        if(a[2]==i2){
            mImage3.setImageResource(R.drawable.twitter);
        }
        if(a[3]==i4){
            mImage4.setImageResource(R.drawable.rsz_vk_icon);
        }
        if(a[3]==i3){
            mImage4.setImageResource(R.drawable.instagram);
        }
        if(a[3]==i2){
            mImage4.setImageResource(R.drawable.twitter);
        }
        if(a[3]==i1){
            mImage4.setImageResource(R.drawable.facebook);
        }

        return view;
    }
    public void LoadPreferences() {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        isStarted = sharedPreferences.getString("compoundButton", "false");
        myCondition = sharedPreferences.getString("myStatementNow", "low");
        allSocials = sharedPreferences.getString("allSocials", "0");

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

        if (myCondition.isEmpty()) {
            SavePreferences("myStatementNow", "low");
        }
        myCondition = sharedPreferences.getString("myStatementNow", "low");
            //TODO Add colors
        if (myCondition.equals("low")) {
            myStatementNow.setText("Низкий");
            myStatementNow.setTextColor(Color.parseColor("#ffffff"));
            totalAllText.setTextColor(Color.parseColor("#ffffff"));
            useForDay.setTextColor(Color.parseColor("#ffffff"));
            s1.setTextColor(Color.parseColor("#ffffff"));
            s2.setTextColor(Color.parseColor("#ffffff"));
            s3.setTextColor(Color.parseColor("#ffffff"));
            rel.setBackgroundColor(Color.parseColor("#2e7d32"));
        } else if (myCondition.equals("Average")) {
            myStatementNow.setText("Ненормальный");
            myStatementNow.setTextColor(Color.parseColor("#ffffff"));
            totalAllText.setTextColor(Color.parseColor("#ffffff"));
            useForDay.setTextColor(Color.parseColor("#ffffff"));
            s1.setTextColor(Color.parseColor("#ffffff"));
            s2.setTextColor(Color.parseColor("#ffffff"));
            s3.setTextColor(Color.parseColor("#ffffff"));
           rel.setBackgroundColor(Color.parseColor("#6c6f00"));
        } else if (myCondition.equals("attention")) {
            myStatementNow.setText("Внимание");
            myStatementNow.setTextColor(Color.parseColor("#000000"));
            totalAllText.setTextColor(Color.parseColor("#000000"));
            useForDay.setTextColor(Color.parseColor("#000000"));
            s1.setTextColor(Color.parseColor("#000000"));
            s2.setTextColor(Color.parseColor("#000000"));
            s3.setTextColor(Color.parseColor("#000000"));
           rel.setBackgroundColor(Color.parseColor("#ff833a"));
        } else if (myCondition.equals("Addicted")) {
            myStatementNow.setText("Зависимость");
            myStatementNow.setTextColor(Color.parseColor("#000000"));
            totalAllText.setTextColor(Color.parseColor("#000000"));
            useForDay.setTextColor(Color.parseColor("#000000"));
            s1.setTextColor(Color.parseColor("#000000"));
            s2.setTextColor(Color.parseColor("#000000"));
            s3.setTextColor(Color.parseColor("#000000"));
            rel.setBackgroundColor(Color.parseColor("#e65100"));
        } else if (myCondition.equals("DANGER")) {
            myStatementNow.setText("Опасность");
            myStatementNow.setTextColor(Color.parseColor("#ffffff"));
            totalAllText.setTextColor(Color.parseColor("#ffffff"));
            useForDay.setTextColor(Color.parseColor("#ffffff"));
            s1.setTextColor(Color.parseColor("#ffffff"));
            s2.setTextColor(Color.parseColor("#ffffff"));
            s3.setTextColor(Color.parseColor("#ffffff"));
            rel.setBackgroundColor(Color.parseColor("#dd2c00"));
        } else {
            myStatementNow.setText("Стандартный");
            myStatementNow.setTextColor(Color.parseColor("#ffffff"));
            totalAllText.setTextColor(Color.parseColor("#ffffff"));
            useForDay.setTextColor(Color.parseColor("#ffffff"));
            s1.setTextColor(Color.parseColor("#ffffff"));
            s2.setTextColor(Color.parseColor("#ffffff"));
            s3.setTextColor(Color.parseColor("#ffffff"));
            rel.setBackgroundColor(Color.parseColor("#2e7d32"));
        }



        if (allSocials.isEmpty()) {
            SavePreferences("allSocials", "0 Часов");
        } else {
            DecimalFormat doubleHelper = new DecimalFormat("#.##");
            totalAllText.setText(doubleHelper.format(Double.parseDouble(allSocials)) + "Часов");

            if (inService > 24) {
                useForDay.setText(doubleHelper.format((Double.parseDouble(allSocials) / Double.parseDouble(doubleHelper.format((double) inService / 24.0))))
                        + "Часов/День");
            }
        }



    }
    public void SavePreferences(String stringName, String stringValue) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(stringName, stringValue);
        edit.commit();

    }

    @Override
    public void onResume() {
        super.onResume();
        LoadPreferences();
    }
}