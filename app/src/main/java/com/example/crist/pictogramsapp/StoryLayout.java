package com.example.crist.pictogramsapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase representa al controlador del visor de los cuentos
 *
 * @author Cristian Gonzalez Lopez
 */
public class StoryLayout extends AppCompatActivity{


    private static MediaPlayer voice;
    private List<StoryPage> ITEM_LIST;
    private Story story;
    private String title;
    private FloatingActionButton volButon;
    private ViewPager viewPager;
    private StoryPageAdapter storyPageAdapter;
    private ImageView left_but;
    private ImageView right_but;
    private ImageView exit_but;
    private ImageView speaker_but;
    private Boolean istouched;
    private Thread thread;
    private TextToSpeech textToSpeech;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.story_layout);


        istouched = false;

        Intent intent = getIntent();

        if (intent != null){
            story = (Story) intent.getSerializableExtra("story");
            title = story.getTittle();
            ITEM_LIST = story.getPages();
        }else{
            ITEM_LIST = new ArrayList<>();
        }


        left_but = findViewById(R.id.story_lay_left_but);
        right_but = findViewById(R.id.story_lay_right_but);
        exit_but = findViewById(R.id.story_lay_exit_but);
        speaker_but = findViewById(R.id.story_lay_speaker_but);

        StoryPage titlePage = new StoryPage(null, title, null);
        ITEM_LIST.add(0,titlePage);



        viewPager = (ViewPager) findViewById(R.id.story_lay_viewPager);
        storyPageAdapter = new StoryPageAdapter(getSupportFragmentManager(), ITEM_LIST, StoryLayout.this);
        viewPager.setAdapter(storyPageAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (thread.isAlive()){
                    thread.interrupt();
                }
                istouched = false;
                left_but.setVisibility(View.GONE);
                right_but.setVisibility(View.GONE);
                exit_but.setVisibility(View.GONE);
                speaker_but.setVisibility(View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (!istouched) {

                    istouched = true;
                    int page = viewPager.getCurrentItem();
                    if (page != 0) {
                        left_but.setVisibility(View.VISIBLE);
                    }

                    if (page != (ITEM_LIST.size() - 1)) {
                        right_but.setVisibility(View.VISIBLE);
                    }

                    exit_but.setVisibility(View.VISIBLE);
                    speaker_but.setVisibility(View.VISIBLE);

                    thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    left_but.setVisibility(View.GONE);
                                    right_but.setVisibility(View.GONE);
                                    exit_but.setVisibility(View.GONE);
                                    speaker_but.setVisibility(View.GONE);
                                    istouched = false;
                                }
                            });
                        }
                    };
                    thread.start();

                }
                return false;
            }
        });



        speaker_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (voice.isPlaying()){
                    voice.pause();
                    speaker_but.setImageResource(R.drawable.ic_stat_speaker_off);
                }else{
                    voice.start();
                    speaker_but.setImageResource(R.drawable.ic_stat_speaker_on);
                }
            }
        });

        exit_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voice.stop();
                finish();
            }
        });

        left_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int page = viewPager.getCurrentItem();
                left_but.setVisibility(View.GONE);
                right_but.setVisibility(View.GONE);
                exit_but.setVisibility(View.GONE);
                speaker_but.setVisibility(View.GONE);
                viewPager.setCurrentItem(page-1, true);

            }
        });

        right_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int page = viewPager.getCurrentItem();
                left_but.setVisibility(View.GONE);
                right_but.setVisibility(View.GONE);
                exit_but.setVisibility(View.GONE);
                speaker_but.setVisibility(View.GONE);
                viewPager.setCurrentItem(page+1);
            }
        });

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    /**
     * Reproduce la musica de fondo
     *
     * @param context  el contexto de la aplicacion
     * @param rawVoice el elemento de sonido
     */
    public static void PlayVoice(final Context context, int rawVoice) {
        voice = MediaPlayer.create(context, rawVoice);
        voice.setVolume(0.25f, 0.25f);
        voice.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (voice != null) {
                    voice.release();
                }
            }
        });
        voice.start();
    }

    @Override
    public void onBackPressed() {
        voice.stop();
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        PlayVoice(this, R.raw.funny_music);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (voice.isPlaying()){
            voice.pause();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (voice != null){
            voice.start();
        }
    }
}


