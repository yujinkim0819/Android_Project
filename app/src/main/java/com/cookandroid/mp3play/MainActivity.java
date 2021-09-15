package com.cookandroid.mp3play;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listMusic;
    Button btnPlay, btnPause, btnStop;
    TextView txtMusicName;
    ProgressBar pbMusic;

    ArrayList<String> mp3List;
    String selectedMP3;

    // 파일경로
    String mp3Path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getPath()+"/";
    MediaPlayer mPlayer;
    Switch swtMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);
        listMusic = findViewById(R.id.listMusic);
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnStop = findViewById(R.id.btnStop);
        txtMusicName = findViewById(R.id.txtMusicName);
        pbMusic = findViewById(R.id.pbMusic);
        swtMusic = findViewById(R.id.swtMusic);

        mp3List = new ArrayList<String>();
        mPlayer = new MediaPlayer();

        final Intent in = new Intent(getApplicationContext(), MusicService.class);
        startService(in);
        swtMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(swtMusic.isChecked() == true) stopService(in);
                else startService(in);
            }
        });
        File[] listFiles = new File(mp3Path.toString()).listFiles();
        String fileName, extName;
        for(File file:listFiles){
            fileName = file.getName();
            extName = fileName.substring(fileName.length()-3);
            if(extName.equals("mp3")){
                mp3List.add(fileName);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, mp3List);
        listMusic.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listMusic.setItemChecked(0, true);
        listMusic.setAdapter(adapter);
        selectedMP3 = mp3List.get(0);
        listMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMP3 = mp3List.get(i);
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mPlayer.setDataSource(mp3Path + selectedMP3);
                    mPlayer.prepare();
                    mPlayer.start();
                    btnPlay.setClickable(false);
                    btnPause.setClickable(true);
                    btnStop.setClickable(true);
                    txtMusicName.setText(selectedMP3);
                    pbMusic.setVisibility(View.VISIBLE);
                } catch (Exception e) { e.printStackTrace(); }
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(mPlayer.isPlaying()){
                        mPlayer.pause();
                    } else {
                        mPlayer.start();
                    }

                    btnPlay.setClickable(false);
                    btnPause.setClickable(true);
                    btnStop.setClickable(true);

                    txtMusicName.setText(selectedMP3);
                    pbMusic.setVisibility(View.VISIBLE);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mPlayer.stop();
                    mPlayer.reset();
                    btnPlay.setClickable(true);
                    btnPause.setClickable(false);
                    btnStop.setClickable(true);
                    txtMusicName.setText(selectedMP3);
                    pbMusic.setVisibility(View.VISIBLE);
                } catch (Exception e) { e.printStackTrace(); }
            }
        });
    }
}
