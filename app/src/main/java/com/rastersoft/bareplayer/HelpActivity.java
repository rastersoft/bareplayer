package com.rastersoft.bareplayer;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Intent workIntent = getIntent();
        String fullPath = workIntent.getStringExtra("music_path");
        TextView text = (TextView) findViewById(R.id.helpView);
        text.setText(String.format(this.getResources().getString(R.string.helpText),fullPath));
    }
}
