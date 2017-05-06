package com.rastersoft.bareplayer;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        String fullPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();
        TextView text = (TextView) findViewById(R.id.helpView);
        text.setText(String.format(this.getResources().getString(R.string.helpText),fullPath, Environment.DIRECTORY_MUSIC));
    }
}
