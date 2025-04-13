package com.mtnl.faultverse;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    LinearLayout appSonLayout,appLayout;
    private SharedPreferences sharedPreferences;
    ImageView arrowImage;
    ImageButton lang_img;
    public int logoutCount = 0;
//    private static long loginPhNo ;
//    public static final String EXTRA_KEY = "MTNLdb";
//    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
//    private  static final String URL = "jdbc:oracle:thin:@0.tcp.in.ngrok.io:10257:XE";
//    private static final String USERNAME = "system";
//    private static final String PASSWORD = "sys123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
//        Bundle data = getArguments();
//        if (data != null) {
//            loginPhNo = data.getLong(HomeActivity.EXTRA_KEY);
//        }
//        String Phn = String.valueOf(loginPhNo);

        lang_img = findViewById(R.id.lang_img);
        appSonLayout = findViewById(R.id.appSonLayout);
        arrowImage = findViewById(R.id.arrowImage);
        appLayout = findViewById(R.id.appLayout);
        lang_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLanguage();
            }

            private void changeLanguage() {
                final String languages [] = {"English","Hindi","Spanish"};
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(SettingsActivity.this);
                mBuilder.setTitle("Choose Language");
                mBuilder.setSingleChoiceItems(languages, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        if(which ==0){
                            setLocale("en");
                            recreate();
                        }else if(which ==1){
                            setLocale("hi");
                            recreate();
                        } else if (which ==2) {
                            setLocale("es");
                            recreate();
                        }
                    }
                });
                mBuilder.create();
                mBuilder.show();
            }
        });

    }


    // Inside your SettingsActivity or any activity where you handle language change

    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);

        Context context = createConfigurationContext(configuration);
        resources = context.getResources();
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }



    public void onClickApp(View view){
        if(appSonLayout.getVisibility()== View.VISIBLE){
            TransitionManager.beginDelayedTransition(appLayout,new AutoTransition());
            appSonLayout.setVisibility(View.GONE);
            arrowImage.setImageResource(R.drawable.settings_down_arrow);
        }
        else{
            TransitionManager.beginDelayedTransition(appLayout,new AutoTransition());
            appSonLayout.setVisibility(View.VISIBLE);
            arrowImage.setImageResource(R.drawable.settings_up_arrow);
        }
    }

    public void onBackButton(View view){
        onBackPressed();
    }


    public void logoutBack(View view){
        finishAffinity();
    }
    public void onAboutClick(View view){
        AboutFragment aboutFragment = new AboutFragment();
        aboutFragment.show(getSupportFragmentManager(),aboutFragment.getTag());
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}