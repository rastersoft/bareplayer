package com.rastersoft.bareplayer;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class FileChooserActivity extends AppCompatActivity implements Comparator<File>, View.OnClickListener {

    private String currentPath;
    private ArrayList<File> paths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_chooser);

        this.paths = null;

        Intent workIntent = getIntent();
        this.currentPath = workIntent.getStringExtra("music_path");
        this.fillPath();

    }

    public void onSDCardClicked(View view) {
        this.currentPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();
        this.fillPath();
    }

    public void onOkClicked(View view) {
        Intent data = new Intent();
        data.putExtra("newPath",this.currentPath);
        this.setResult(Activity.RESULT_OK,data);
        finish();
    }

    public void onCancelClicked(View view) {
        Intent data = new Intent();
        this.setResult(Activity.RESULT_CANCELED,data);
        finish();
    }

    public void onClick(View view) {
        int id = view.getId();

        File file = this.paths.get(id);
        if (file == null) {
            String paths[] = this.currentPath.split("/");
            int len = paths.length;
            if (len > 2) {
                len--;
                this.currentPath = "";
                for(int i=1; i<len; i++) {
                    this.currentPath += "/"+paths[i];
                }
            } else {
                this.currentPath = "/";
            }
        } else {
            this.currentPath = file.getAbsolutePath();
        }
        this.fillPath();
    }

    private void fillPath() {

        TextView currentPathView = (TextView) findViewById(R.id.currentPathView);
        currentPathView.setText(this.currentPath);
        this.paths = new ArrayList<File>();
        LinearLayout content = (LinearLayout) findViewById(R.id.filechooserContent);
        content.removeAllViews();

        File directory = new File(this.currentPath);
        if (directory == null) {
            directory = new File("/");
        }
        File[] tmpFiles = directory.listFiles();
        List<File> files = new ArrayList<File>();
        if (tmpFiles != null) {
            for (File file : tmpFiles) {
                files.add(file);
            }
        }
        Collections.sort(files,this);
        int i = 0;
        Button btnTag;
        if (0 != this.currentPath.compareTo("/")) {
            btnTag = new Button(this);
            btnTag.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
            btnTag.setText("..");
            btnTag.setTextAlignment(LinearLayoutCompat.TEXT_ALIGNMENT_TEXT_START);
            btnTag.setId(i);
            i++;
            this.paths.add(null);
            btnTag.setOnClickListener(this);
            content.addView(btnTag);
        }
        for (File file : files) {
            String name = file.getName();
            if (file.isDirectory()) {
                btnTag = new Button(this);
                btnTag.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
                btnTag.setText(name+"/");
                btnTag.setTextAlignment(LinearLayoutCompat.TEXT_ALIGNMENT_TEXT_START);
                btnTag.setId(i);
                i++;
                this.paths.add(file);
                btnTag.setOnClickListener(this);
                content.addView(btnTag);
            } else {
                TextView txt = new TextView(this);
                txt.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
                txt.setText(name);
                txt.setInputType(InputType.TYPE_NULL);
                content.addView(txt);
            }
        }

    }

    public int compare(File file1, File file2) {

        boolean file1IsDir = file1.isDirectory();
        boolean file2IsDir = file2.isDirectory();

        if (file1IsDir != file2IsDir) {
            if (file1IsDir && !file2IsDir) {
                return -1;
            } else {
                return 1;
            }
        }

        return file1.getName().compareTo(file2.getName());
    }

}
