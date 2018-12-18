package com.urbanpoint.UrbanPoint;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.urbanpoint.UrbanPoint.RegistrationAuxiliries.GetStartedFragment;
import com.urbanpoint.UrbanPoint.RegistrationAuxiliries.VerifyMemberFragment;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.yqritc.scalablevideoview.ScalableVideoView;

import java.io.IOException;

public class SignupActivity extends AppCompatActivity
{
    private ScalableVideoView mAppIntroVideo;

    private FragmentManager mSupportFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);
        initView();
        startVideo();
    }

    private void initView()
    {

        mAppIntroVideo=(ScalableVideoView)findViewById(R.id.appIntroVideoView);

        mSupportFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mSupportFragmentManager.beginTransaction();


            mFragmentTransaction.add(R.id.containerIntroFragments, new GetStartedFragment());


        mFragmentTransaction.commit();
    }

    private void startVideo() {
        // Load and start the movie
        // Uri video1 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.intro_video);

      /*  mAppIntroVideo.setVideoURI(video1);
        mAppIntroVideo.start();
        mAppIntroVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
*/

        try {
            //   mAppIntroVideo.setRawData(R.raw.intro_video);
            mAppIntroVideo.setRawData(R.raw.video1);
            mAppIntroVideo.setVolume(0, 0);
            mAppIntroVideo.setLooping(true);
            mAppIntroVideo.prepare(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mAppIntroVideo.start();
                }
            });
        } catch (IOException ioe) {
            ioe.printStackTrace();
            //ignore
        }
    }

}
