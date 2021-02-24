package com.gwesaro.mycheeseornothing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String path = "bleu_d_auvergne.jpg";
        String name = path.split("\\.")[0];
        int drawableResourceId = this.getResources().getIdentifier(name, "drawable", this.getPackageName());
    }
}