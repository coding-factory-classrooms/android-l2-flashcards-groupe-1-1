package com.gwesaro.mycheeseornothing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("À propos");
        TextView versionTextView = findViewById(R.id.versionTextView);

        /**
         * get the app version from packageInfo
         */
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionTextView.setText("Version v" + pInfo.versionName);
        } 
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}