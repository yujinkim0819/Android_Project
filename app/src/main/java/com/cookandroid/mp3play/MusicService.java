package com.cookandroid.mp3play;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MusicService extends Service {
    MediaPlayer mp;
    @Nullable
    @Override
    public IBinder onBind(Intent Intent){ return null; }
    @Override
    public void onCreate(){
        super.onCreate();
        // 서비스에서 가장 먼저 호출됨(최초에 한 번만)
        mp = MediaPlayer.create(this, R.raw.elephant);
        mp.setLooping(false); // 반복재생
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        // 서비스가 호출될 때 마다 실행
        mp.start(); // 노래 시작
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 서비스가 종료될 때 실ㄹ행
        mp.stop();
    }

}
