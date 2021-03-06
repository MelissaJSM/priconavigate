package com.example.pirconevigate;

import static android.app.SearchManager.QUERY;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;




import java.util.Timer;
import java.util.TimerTask;

import static com.example.pirconevigate.Recognition.SpeakString;
import static com.example.pirconevigate.Recognition.speakready;

import com.example.pirconevigate.Constants;

import org.w3c.dom.Text;

public class MainKokkoroActivity extends AppCompatActivity implements View.OnClickListener {


    //fab ??????????????? ??? ??????
    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2, fab3, fab4;
    private SharedPreferences character_select;


    /// ????????? ?????? ??????
    ImageButton bt_internet;
    ImageButton bt_navi;
    ImageButton bt_bluetooth;
    ImageButton bt_musicplayer;
    ImageButton bt_geniemusic;
    ImageButton bt_youtube;
    ImageButton bt_twitch;
    ImageButton bt_dc;
    ImageButton img_face;

    // ?????? ?????? ??????
    ImageButton bt_setting;

    // ????????? ??? ??????
    ImageView bt_emotion; // ?????? ?????? ????????????

    ImageView img_background; // ????????? ?????? ??????????????? ????????????


    //????????? ?????? ????????? ?????? ????????????
    ImageView img_month10;
    ImageView img_month0;

    ImageView img_day10;
    ImageView img_day0;

    //ImageView img_week; ?????? ????????? ????????? ????????? ?????? ?????? ?????? ??????.

    ImageView img_hour10;
    ImageView img_hour0;

    ImageView img_min10;
    ImageView img_min0;

    ImageView img_sec10;
    ImageView img_sec0;

    ImageView img_week;

    // ????????? ???????????? ?????? ?????? ????????? ????????????
    ImageView img_speak_ready;

    //????????? ???????????? ?????? ??? ?????? ???????????? ??????
    ImageView img_face_speak;

    //????????? ?????? ??? ????????? ?????? ?????????
    ImageView img_face_blink;

    ImageView img_dark_back;
    ImageView img_dark_back_kokkoro;
    ImageView img_dark_back_kokkoro_face;
    ImageView img_dark_back_kokkoro_blink;
    ImageView img_dark_back_kokkoro_lip;
    ImageView img_dark_back_kokkoro_lip_mode;
    //????????? ?????? ??? ????????? ???????????? ???????????? ????????? ?????????
    ImageView img_dark_back_kokkoro_speak;
    ImageView img_dark_back_kokkoro_speak_text;

    //?????? ?????????
    ImageView weather_now;

    ImageView weather_kokkoro_speak_text;
    ImageView weather_kokkoro_speak_eye;
    ImageView weather_kokkoro_speak_mouth;

    ImageView weather_kokkoro;
    ImageView img_tempView;
    ImageView img_humView;
    ImageView img_weatherpanlel;
    ImageView img_stamp;

    TextView dateView;

    TextView tempView;

    TextView humView;

    TextView weatherText;

    static RequestQueue requestQueue;

    static RequestQueue iftttQueue;

    double currentLatitude;
    double currentLongitude;

    //????????????

    TextView speaktext;

    MediaPlayer mediaPlayer;



    int year;
    int month;
    int date;

    int woy;
    int wom;

    int doy;
    int dom;
    int dow;

    int hour12;
    static int hour24 = 0;
    int minute;
    int second;

    int milliSecond;
    int timeZone;
    int lastDate;

    int character_int;

    final int PERMISSION = 1;

    // ????????? ?????? ?????? ??????
    int kokkoro_voice = Constants.kokkoro_voice_off;
    int kokkoro_count = Constants.kokkoro_conunt_ready;

    // ????????? ???????????????
    int move_to_voice=Constants.move_to_voice_null;

    // sex ?????? ?????? ????????? ??????
    int sex_count = Constants.sex_off;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_kokkoro);

        // ?????? ????????? ?????????
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if(permissionCheck == PackageManager.PERMISSION_DENIED){ //?????? ?????? ??????

            //?????? ?????? ??????
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }



        // ???????????? ????????? ???????????? ????????? ?????? ????????? ??????.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // ??????????????? 6.0?????? ???????????? ???????????? ????????? ??????
        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO}, PERMISSION);
        }


        // ????????? ???????????? ?????? ?????? ??????

        img_speak_ready = (ImageView) findViewById(R.id.speakready);

        //????????? ????????? ???????????? ??????
        img_face_speak =(ImageView)findViewById(R.id.img_speak);

        Glide.with(this).load(R.drawable.speak_ready_lip).apply(new RequestOptions()
                .signature(new ObjectKey("signature string"))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
        ).into(img_face_speak);

        // ????????? ??? ????????? ?????? ?????????

        img_face_blink =(ImageView)findViewById(R.id.btxml_face_blink);

        Glide.with(this).load(R.drawable.kokkoro_face_blink).apply(new RequestOptions()
                .signature(new ObjectKey("signature string"))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
        ).fitCenter().into(img_face_blink);

        // ????????? ?????? ?????? ??? ?????? ???????????? ???????????? ?????? ??????
        img_dark_back = (ImageView)findViewById(R.id.dark_back);
        img_dark_back_kokkoro = (ImageView)findViewById(R.id.dark_back_kokkoro);
        img_dark_back_kokkoro_speak = (ImageView)findViewById(R.id.dark_back_speak);
        // ??? ?????? ?????????????????? ????????????
        img_dark_back_kokkoro_face = (ImageView)findViewById(R.id.dark_back_kokkoro_face);

        img_dark_back_kokkoro_blink = (ImageView)findViewById(R.id.dark_back_kokkoro_blink);
        Glide.with(this).load(R.drawable.speak_ready_blink).apply(new RequestOptions()
                .signature(new ObjectKey("signature string"))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
        ).apply(new RequestOptions()
                .signature(new ObjectKey("signature string"))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
        ).fitCenter().into(img_dark_back_kokkoro_blink);


        img_dark_back_kokkoro_lip = (ImageView)findViewById(R.id.dark_back_kokkoro_lip);
        Glide.with(this).load(R.drawable.speak_ready_lip)
                .apply(new RequestOptions()
                .signature(new ObjectKey("signature string"))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
        ).
                fitCenter().into(img_dark_back_kokkoro_lip);

        // ????????? ???????????? ?????? ??????????????? ???.
        img_dark_back_kokkoro_lip_mode = (ImageView)findViewById(R.id.dark_back_kokkoro_lip_mode);

        //?????? ???????????? ?????? ??????????????? ???.
        img_dark_back_kokkoro_speak_text = (ImageView)findViewById(R.id.dark_back_speak_text);





        img_face_speak.setVisibility(View.GONE); // ????????? ???????????? ??????.

        // img id ??????
        img_face = (ImageButton) findViewById(R.id.btxml_face);

        //?????? ??????
        bt_emotion = (ImageView) findViewById(R.id.btxml_emotion);


        // ????????? ?????? ???????????? ????????? ?????? ??????
        img_background = (ImageView) findViewById(R.id.character_background);

        //????????? ?????? ????????? ?????? ??????
        img_month10 = (ImageView) findViewById(R.id.time_month10);
        img_month0 = (ImageView) findViewById(R.id.time_month0);

        img_day10 = (ImageView) findViewById(R.id.time_day10);
        img_day0 = (ImageView) findViewById(R.id.time_day0);

        img_week = (ImageView) findViewById(R.id.font_week);

        img_hour10 = (ImageView) findViewById(R.id.time_hour10);
        img_hour0 = (ImageView) findViewById(R.id.time_hour0);

        img_min10 = (ImageView) findViewById(R.id.time_min10);
        img_min0 = (ImageView) findViewById(R.id.time_min0);

        img_sec10 = (ImageView) findViewById(R.id.time_sec10);
        img_sec0 = (ImageView) findViewById(R.id.time_sec0);

        //????????? ????????? ??????

        dateView = findViewById(R.id.dateView);

        weather_kokkoro = findViewById(R.id.weather_kokkoro);

        // ?????? gif ??????
        weather_gif_create();

        tempView = findViewById(R.id.tx_tempView);

        img_tempView = findViewById(R.id.img_tempView);

        weatherText = findViewById(R.id.tx_weather);

        humView = findViewById(R.id.tx_humView);

        img_humView = findViewById(R.id.img_humView);

        img_weatherpanlel = findViewById(R.id.weatherpanlel);

        img_stamp = findViewById(R.id.kokkoro_stamp);


        // ?????? ?????? ?????????
        weather_now = findViewById(R.id.img_weather_now);

        // ??????????????? ????????? ????????? gone ??????
        weather_gone();

        /* ???????????? ?????? ???????????? ?????????
        timer = new Timer(); //???????????? ????????? ?????? ????????? ??????
        timerTask = new TimerTask() {
            @Override
            public void run() {
                timerzone(); // 1?????? ?????? ????????? ?????? ?????????
            }
        };

         */

        //???????????? ??????
        img_face_blink.setVisibility(View.VISIBLE);

        welcome_this_voice();


        Thread timethread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    timerzone(); // 1?????? ?????? ????????? ?????? ?????????
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        timethread.start();


        // fab ??????????????? ??????
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        // fab floating ?????? ??????
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab4 = (FloatingActionButton) findViewById(R.id.fab4);



        // ????????? ????????? ?????? ?????? ??? ?????? ??????
        bt_internet = (ImageButton) findViewById(R.id.btxml_internet);



        // ?????? ?????? id ??????
        bt_setting = (ImageButton) findViewById(R.id.btxml_setting);

        //????????? ?????? ????????? ?????? id ??????
        speaktext = (TextView) findViewById(R.id.tx_speaktext);

        //???????????? ????????? ???????????? ??????.
        final Handler speakhandler = new Handler(Looper.getMainLooper());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            speakhandler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    //????????? ???????????? ?????? ?????? ??????
                    if(speakready=="ready"){
                        img_speak_ready.setImageResource(R.drawable.speak_ready);
                    }

                    //.speaktext.setText(SpeakString);
                    speakfiltering();
                    speakhandler.postDelayed(this, 1000);
                }
            }, 0, 1000);
        }


        //?????? ?????? ????????? ??????
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);
        fab4.setOnClickListener(this);

        //SharedPreferences ????????? ??????
        character_select = getSharedPreferences("UserInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = character_select.edit();
        editor.putInt("ActiveID", 0);
        editor.commit();


        // ????????? ???????????? ???????????? ???????????? ???????????? ????????? ???????????? ?????? ??? ??? ??????.
        // ?????? ??????
        img_face.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                //ACTION_DOWN = ????????? ?????????
                if (action == MotionEvent.ACTION_DOWN) {

                    img_face.setImageResource(R.drawable.char_kokkoro_shy);
                    bt_emotion.setImageResource(R.drawable.char_emotion_shy);
                    Toast.makeText(MainKokkoroActivity.this, "???????????? ????????????", Toast.LENGTH_SHORT).show();

                }

                //ACTION_UP = ????????? ?????????
                else if (action == MotionEvent.ACTION_UP) {
                    // ???????????? ?????? ??????
                }
                return false;
            }
        });


        // ????????? ????????? ?????? ?????? ??? ?????? ??????
        bt_internet.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                //ACTION_DOWN = ????????? ?????????
                if (action == MotionEvent.ACTION_DOWN) {

                    img_face.setImageResource(R.drawable.char_kokkoro_internet);
                    bt_emotion.setImageResource(R.drawable.char_emotion_internet);
                    Toast.makeText(MainKokkoroActivity.this, "????????? ?????? ??????", Toast.LENGTH_SHORT).show();

                }

                //ACTION_UP = ????????? ?????????
                else if (action == MotionEvent.ACTION_UP) {
                    // ???????????? ?????? ??????

                    Intent app_internet = getPackageManager().getLaunchIntentForPackage("com.android.chrome");
                    // ?????? ????????? getActivity ??? ???????????? ??????. (?????? ??????)
                    startActivity(app_internet);
                    finish();
                }
                return false;
            }
        });

        // ??????????????? ????????? ?????? ?????? ??? ?????? ??????
        bt_navi = (ImageButton) findViewById(R.id.btxml_navi);
        bt_navi.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                //ACTION_DOWN = ????????? ?????????
                if (action == MotionEvent.ACTION_DOWN) {

                    img_face.setImageResource(R.drawable.char_kokkoro_navi);
                    bt_emotion.setImageResource(R.drawable.char_emotion_navi);
                    Toast.makeText(MainKokkoroActivity.this, "??????????????? ?????? ??????", Toast.LENGTH_SHORT).show();

                }

                //ACTION_UP = ????????? ?????????
                else if (action == MotionEvent.ACTION_UP) {
                    // ???????????? ?????? ??????

                    Intent app_navi = getPackageManager().getLaunchIntentForPackage("com.skt.tmap.ku");
                    // ?????? ????????? getActivity ??? ???????????? ??????. (?????? ??????)
                    startActivity(app_navi);
                    finish();
                }
                return false;
            }
        });

        // ???????????? ????????? ?????? ?????? ??? ?????? ??????
        bt_bluetooth = (ImageButton) findViewById(R.id.btxml_bluetooth);
        bt_bluetooth.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                //ACTION_DOWN = ????????? ?????????
                if (action == MotionEvent.ACTION_DOWN) {

                    img_face.setImageResource(R.drawable.char_kokkoro_bt);
                    bt_emotion.setImageResource(R.drawable.char_emotion_bluetooth);
                    Toast.makeText(MainKokkoroActivity.this, "???????????? ?????? ??????", Toast.LENGTH_SHORT).show();

                } else if (action == MotionEvent.ACTION_UP) {
                    Intent bluetoothmode = new Intent();
                    bluetoothmode.setComponent(new ComponentName("com.ts.MainUI", "com.ts.bt.BtConnectActivity"));
                    startActivity(bluetoothmode);
                    finish();
                }
                return false;

            }


        });

        // ?????? ???????????? ????????? ?????? ?????? ??? ?????? ??????
        bt_musicplayer = (ImageButton) findViewById(R.id.btxml_player);
        bt_musicplayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                //ACTION_DOWN = ????????? ?????????
                if (action == MotionEvent.ACTION_DOWN) {

                    img_face.setImageResource(R.drawable.char_kokkoro_music);
                    bt_emotion.setImageResource(R.drawable.char_emotion_music);
                    Toast.makeText(MainKokkoroActivity.this, "?????????????????? ?????? ??????", Toast.LENGTH_SHORT).show();

                } else if (action == MotionEvent.ACTION_UP) {
                    Intent app_musicplayer = getPackageManager().getLaunchIntentForPackage("com.massivcode.androidmusicplayer");
                    // ?????? ????????? getActivity ??? ???????????? ??????. (?????? ??????)
                    startActivity(app_musicplayer);
                    finish();
                }
                return false;

            }
        });

        // ???????????? ????????? ?????? ?????? ??? ?????? ??????
        bt_geniemusic = (ImageButton) findViewById(R.id.btxml_genie);
        bt_geniemusic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                //ACTION_DOWN = ????????? ?????????
                if (action == MotionEvent.ACTION_DOWN) {

                    img_face.setImageResource(R.drawable.char_kokkoro_music);
                    bt_emotion.setImageResource(R.drawable.char_emotion_music);
                    Toast.makeText(MainKokkoroActivity.this, "???????????? ?????? ??????", Toast.LENGTH_SHORT).show();

                } else if (action == MotionEvent.ACTION_UP) {
                    Intent app_musicplayer = getPackageManager().getLaunchIntentForPackage("com.ktmusic.geniemusic");
                    // ?????? ????????? getActivity ??? ???????????? ??????. (?????? ??????)
                    startActivity(app_musicplayer);
                    finish();
                }
                return false;

            }
        });

        // ?????? ????????? ?????? ?????? ??? ?????? ??????
        bt_dc = (ImageButton) findViewById(R.id.btxml_dc);
        bt_dc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                //ACTION_DOWN = ????????? ?????????
                if (action == MotionEvent.ACTION_DOWN) {

                    img_face.setImageResource(R.drawable.char_kokkoro_dc);   // ????????? ????????? ?????? ??????
                    bt_emotion.setImageResource(R.drawable.char_emotion_noooo); // ????????? ????????? ?????? ??????
                    Toast.makeText(MainKokkoroActivity.this, "?????? ?????? ??????", Toast.LENGTH_SHORT).show();

                } else if (action == MotionEvent.ACTION_UP) {
                    Intent app_musicplayer = getPackageManager().getLaunchIntentForPackage("com.dcinside.app");
                    // ?????? ????????? getActivity ??? ???????????? ??????. (?????? ??????)
                    startActivity(app_musicplayer);
                    finish();
                }
                return false;

            }
        });

        // ????????? ????????? ?????? ?????? ??? ?????? ??????
        bt_youtube = (ImageButton) findViewById(R.id.btxml_youtube);
        bt_youtube.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                //ACTION_DOWN = ????????? ?????????
                if (action == MotionEvent.ACTION_DOWN) {

                    img_face.setImageResource(R.drawable.char_kokkoro_youtube);   // ???????????? ????????? ?????? ??????
                    Toast.makeText(MainKokkoroActivity.this, "????????? ?????? ??????", Toast.LENGTH_SHORT).show();

                } else if (action == MotionEvent.ACTION_UP) {
                    Intent app_musicplayer = getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
                    // ?????? ????????? getActivity ??? ???????????? ??????. (?????? ??????)
                    startActivity(app_musicplayer);
                    finish();
                }
                return false;

            }
        });

        // ????????? ????????? ?????? ?????? ??? ?????? ??????
        bt_twitch = (ImageButton) findViewById(R.id.btxml_twitch);
        bt_twitch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                //ACTION_DOWN = ????????? ?????????
                if (action == MotionEvent.ACTION_DOWN) {

                    img_face.setImageResource(R.drawable.char_kokkoro_youtube);   // ???????????? ????????? ?????? ??????
                    Toast.makeText(MainKokkoroActivity.this, "????????? ?????? ??????", Toast.LENGTH_SHORT).show();

                } else if (action == MotionEvent.ACTION_UP) {
                    Intent app_musicplayer = getPackageManager().getLaunchIntentForPackage("tv.twitch.android.app");
                    // ?????? ????????? getActivity ??? ???????????? ??????. (?????? ??????)
                    startActivity(app_musicplayer);
                    finish();
                }
                return false;

            }
        });

        // ?????? ????????? ?????? ?????? ??? ?????? ??????
        bt_setting = (ImageButton) findViewById(R.id.btxml_setting);
        bt_setting.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                //ACTION_DOWN = ????????? ?????????
                if (action == MotionEvent.ACTION_DOWN) {
                    Toast.makeText(MainKokkoroActivity.this, "?????? ?????? ??????", Toast.LENGTH_SHORT).show();
                } else if (action == MotionEvent.ACTION_UP) {
                    Intent app_setting = getPackageManager().getLaunchIntentForPackage("com.android.settings");
                    // ?????? ????????? getActivity ??? ???????????? ??????. (?????? ??????)
                    startActivity(app_setting);
                    finish();
                }
                return false;

            }
        });


    }


    // ????????? ?????? ???????????? ????????? ?????? ??? ???????????? ??????????????? ??????.
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) mediaPlayer.release();
    }


    // fab ?????? ????????? ?????? ?????? ?????????
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                anim();
                Toast.makeText(this, "????????? ????????? ???????????? ??????????????????", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab1:
                anim();
                Toast.makeText(this, "????????? ??? ?????? ???????????????.", Toast.LENGTH_SHORT).show();
                Intent char_kokkoro = new Intent(MainKokkoroActivity.this, MainKokkoroActivity.class);
                char_kokkoro.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(char_kokkoro);
                finish();
                overridePendingTransition(0, 0);
                break;
            case R.id.fab2:
                anim();
                Toast.makeText(this, "?????? ??? ?????? ???????????????.", Toast.LENGTH_SHORT).show();
                Intent char_karyl = new Intent(MainKokkoroActivity.this, MainKarylActivity.class);
                char_karyl.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(char_karyl);
                finish();
                overridePendingTransition(0, 0);
                break;
            case R.id.fab3:
                anim();
                Toast.makeText(this, "???????????? ??? ?????? ???????????????.", Toast.LENGTH_SHORT).show();
                Intent char_peco = new Intent(MainKokkoroActivity.this, MainPecoActivity.class);
                char_peco.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(char_peco);
                finish();
                overridePendingTransition(0, 0);
                break;
            case R.id.fab4:
                anim();
                Toast.makeText(this, "3????????? ??????!", Toast.LENGTH_SHORT).show();
                Intent char_3tar = new Intent(MainKokkoroActivity.this, Kokkoro3starActivity.class);
                char_3tar.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(char_3tar);
                finish();
                overridePendingTransition(0, 0);
                break;

        }

    }

    public void speakfiltering() {
        try {
            if (kokkoro_voice == Constants.kokkoro_voice_off) {

                if ((SpeakString.contains("?????????") || SpeakString.contains("?????????")) || SpeakString.contains("??????")) {
                    what_this_voice();
                }
            }

            else if (kokkoro_voice == Constants.kokkoro_voice_on && kokkoro_count < Constants.kokkoro_conunt_max) {

                if (SpeakString.contains("?????????") &&! (SpeakString.contains("??????"))) {
                    move_to_voice=Constants.move_to_voice_youtube;
                }

                else if (SpeakString.contains("??????")) {
                    move_to_voice=Constants.move_to_voice_search;
                }

                else if(SpeakString.contains("?????????") && (SpeakString.contains("??????"))) {
                    move_to_voice=Constants.move_to_voice_youtube_search;
                }

                else if (SpeakString.contains("????????????")) {
                    move_to_voice=Constants.move_to_voice_bluetooth;
                }

                else if (SpeakString.contains("??????") || SpeakString.contains("??????")) {
                    move_to_voice=Constants.move_to_voice_navi;
                }

                else if (SpeakString.contains("??????")) {
                    move_to_voice=Constants.move_to_voice_sex;
                }

                else if (SpeakString.contains("?????????") ||SpeakString.contains("??????") || SpeakString.contains("??????")) {
                    move_to_voice = Constants.not_to_move;
                }

                else if (SpeakString.contains("??????") || SpeakString.contains("??????")) {
                    move_to_voice = Constants.move_to_voice_ride;
                }

                else if (SpeakString.contains("??????")) {
                    move_to_voice = Constants.move_to_voice_weather;
                }

                else if (SpeakString.contains("??????")){

                }

                else if (SpeakString.contains("??????") && SpeakString.contains("???")){
                    move_to_voice = Constants.move_to_living_room_on;
                }

                else if (SpeakString.contains("??????") && SpeakString.contains("???")){
                    move_to_voice = Constants.move_to_living_room_off;
                }

                else if (SpeakString.contains("??????") && SpeakString.contains("???")){
                    move_to_voice = Constants.move_to_air_circulator_on;
                }

                else if (SpeakString.contains("??????") && SpeakString.contains("???")){
                    move_to_voice = Constants.move_to_air_circulator_off;
                }

                else if (SpeakString.contains("?????????")){
                    move_to_voice = Constants.move_to_livingroom_temp;
                }



                //???????????? ?????? ??????
                else {
                    kokkoro_count++;
                }

                if(move_to_voice != Constants.move_to_voice_null){
                    move_to_voice_menu();
                }
                else{}
            }

            // ?????? ??? 10?????? ????????? ??????
            else if (kokkoro_voice == Constants.kokkoro_voice_on && kokkoro_count >= Constants.kokkoro_conunt_max) {
                //else if (kokkoro_voice == Constants.kokkoro_voice_on && kokkoro_count >= 5) { ?????? ???????????? ??? ????????? ????????? ????????? ??????.

                move_to_voice = Constants.not_to_move;
                move_to_voice_menu();

            }


            // ?????? ?????? ???????????? ?????? ????????????????????? ?????????
        } catch (NullPointerException e) {
            System.out.println("????????? ?????? ?????? ???????????? ???????????????.");

            if (kokkoro_voice == Constants.kokkoro_voice_on && kokkoro_count < Constants.kokkoro_conunt_max) {
                kokkoro_count++;
            } else if (kokkoro_voice == Constants.kokkoro_voice_on && kokkoro_count >= Constants.kokkoro_conunt_max) {
                move_to_voice = Constants.not_to_move;
                move_to_voice_menu();
            }

        }

    }

    public void timerzone() {
        // ?????? ?????? ??????
        Calendar today = Calendar.getInstance();

        year = today.get(Calendar.YEAR);
        month = today.get(Calendar.MONTH);
        date = today.get(Calendar.DATE);

        woy = today.get(Calendar.WEEK_OF_YEAR);
        wom = today.get(Calendar.WEEK_OF_MONTH);

        doy = today.get(Calendar.DAY_OF_YEAR);
        dom = today.get(Calendar.DAY_OF_MONTH);
        dow = today.get(Calendar.DAY_OF_WEEK);

        hour12 = today.get(Calendar.HOUR);
        hour24 = today.get(Calendar.HOUR_OF_DAY);
        minute = today.get(Calendar.MINUTE);
        second = today.get(Calendar.SECOND);

        milliSecond = today.get(Calendar.MILLISECOND);
        timeZone = today.get(Calendar.ZONE_OFFSET);
        lastDate = today.getActualMaximum(Calendar.DATE);

        /* ?????? ?????????
        System.out.println("????????? " + year + "??? " + month + 1 + "???" + date + "???");
        System.out.println("????????? ????????? " + woy + "??????, ???????????? " + wom + "??????. " + date + "???");
        System.out.println("????????? ?????? ?????? " + doy + "?????????, ?????? ?????? " + dom + "???. ????????? " + dow + "??? (1:?????????)");
        System.out.println("?????? ????????? " + hour12 + ":" + minute + ":" + second + ", 24???????????? ???????????? " + hour24 + ":" + minute + ":" + second);
        System.out.println("????????? " + year + "??? " + month + 1 + "???" + date + "???");
        System.out.println("1000?????? 1??? (0~999): " + milliSecond);
        System.out.println("timeZone (-12~+12): " + timeZone / (60 * 60 * 1000)); // 1000?????? 1?????? ???????????? ???????????? ?????? 60*60*1000
        System.out.println("??? ?????? ????????? ???: " + lastDate);

         */

        // ????????? ?????? (??? ??? ??????) ????????? ?????? ??????
        if (hour24 >= 20 || hour24 < 6) {
            img_background.setImageResource(R.drawable.bg_500512); //???
            //System.out.println("?????? ?????????");
        } else if (hour24 >= 6 && hour24 < 16) {
            img_background.setImageResource(R.drawable.bg_500510); //??????
            //System.out.println("?????? ????????????");
        } else {
            img_background.setImageResource(R.drawable.bg_500511); //??????
            //System.out.println("?????? ????????????");
        }


        // ?????? ????????? ?????? ????????? ?????? ?????? ????????? ??????

        //??? (10??????)
        if (month >= 9)
            img_month0.setImageResource(R.drawable.font_1);
        else
            img_month0.setImageResource(R.drawable.font_0);

        //??? (1??????)
        switch (month % 10) {
            case 0:
                img_month0.setImageResource(R.drawable.font_1);
                break;
            case 1:
                img_month0.setImageResource(R.drawable.font_2);
                break;
            case 2:
                img_month0.setImageResource(R.drawable.font_3);
                break;
            case 3:
                img_month0.setImageResource(R.drawable.font_4);
                break;
            case 4:
                img_month0.setImageResource(R.drawable.font_5);
                break;
            case 5:
                img_month0.setImageResource(R.drawable.font_6);
                break;
            case 6:
                img_month0.setImageResource(R.drawable.font_7);
                break;
            case 7:
                img_month0.setImageResource(R.drawable.font_8);
                break;
            case 8:
                img_month0.setImageResource(R.drawable.font_9);
                break;
            case 9:
                img_month0.setImageResource(R.drawable.font_0);
                break;
        }

        //??? (10??????)
        switch (date / 10) {
            case 0:
                img_day10.setImageResource(R.drawable.font_0);
                break;
            case 1:
                img_day10.setImageResource(R.drawable.font_1);
                break;
            case 2:
                img_day10.setImageResource(R.drawable.font_2);
                break;
            case 3:
                img_day10.setImageResource(R.drawable.font_3);
                break;
            case 4:
                img_day10.setImageResource(R.drawable.font_4);
                break;
            case 5:
                img_day10.setImageResource(R.drawable.font_5);
                break;
            case 6:
                img_day10.setImageResource(R.drawable.font_6);
                break;
            case 7:
                img_day10.setImageResource(R.drawable.font_7);
                break;
            case 8:
                img_day10.setImageResource(R.drawable.font_8);
                break;
            case 9:
                img_day10.setImageResource(R.drawable.font_9);
                break;
        }

        //??? (1??????)
        switch (date % 10) {
            case 0:
                img_day0.setImageResource(R.drawable.font_0);
                break;
            case 1:
                img_day0.setImageResource(R.drawable.font_1);
                break;
            case 2:
                img_day0.setImageResource(R.drawable.font_2);
                break;
            case 3:
                img_day0.setImageResource(R.drawable.font_3);
                break;
            case 4:
                img_day0.setImageResource(R.drawable.font_4);
                break;
            case 5:
                img_day0.setImageResource(R.drawable.font_5);
                break;
            case 6:
                img_day0.setImageResource(R.drawable.font_6);
                break;
            case 7:
                img_day0.setImageResource(R.drawable.font_7);
                break;
            case 8:
                img_day0.setImageResource(R.drawable.font_8);
                break;
            case 9:
                img_day0.setImageResource(R.drawable.font_9);
                break;
        }


        //??????
        switch (dow % 10) {
            case 1:
                img_week.setImageResource(R.drawable.font_week);
                break;
            case 2:
                img_week.setImageResource(R.drawable.week_mon);
                break;
            case 3:
                img_week.setImageResource(R.drawable.week_tues);
                break;
            case 4:
                img_week.setImageResource(R.drawable.week_wednes);
                break;
            case 5:
                img_week.setImageResource(R.drawable.week_thurs);
                break;
            case 6:
                img_week.setImageResource(R.drawable.week_fri);
                break;
            case 7:
                img_week.setImageResource(R.drawable.week_satur);
                break;
        }


        //?????? (10??????)
        switch (hour24 / 10) {
            case 0:
                img_hour10.setImageResource(R.drawable.font_0);
                break;
            case 1:
                img_hour10.setImageResource(R.drawable.font_1);
                break;
            case 2:
                img_hour10.setImageResource(R.drawable.font_2);
                break;
            case 3:
                img_hour10.setImageResource(R.drawable.font_3);
                break;
            case 4:
                img_hour10.setImageResource(R.drawable.font_4);
                break;
            case 5:
                img_hour10.setImageResource(R.drawable.font_5);
                break;
            case 6:
                img_hour10.setImageResource(R.drawable.font_6);
                break;
            case 7:
                img_hour10.setImageResource(R.drawable.font_7);
                break;
            case 8:
                img_hour10.setImageResource(R.drawable.font_8);
                break;
            case 9:
                img_hour10.setImageResource(R.drawable.font_9);
                break;
        }

        //?????? (1??????)
        switch (hour24 % 10) {
            case 0:
                img_hour0.setImageResource(R.drawable.font_0);
                break;
            case 1:
                img_hour0.setImageResource(R.drawable.font_1);
                break;
            case 2:
                img_hour0.setImageResource(R.drawable.font_2);
                break;
            case 3:
                img_hour0.setImageResource(R.drawable.font_3);
                break;
            case 4:
                img_hour0.setImageResource(R.drawable.font_4);
                break;
            case 5:
                img_hour0.setImageResource(R.drawable.font_5);
                break;
            case 6:
                img_hour0.setImageResource(R.drawable.font_6);
                break;
            case 7:
                img_hour0.setImageResource(R.drawable.font_7);
                break;
            case 8:
                img_hour0.setImageResource(R.drawable.font_8);
                break;
            case 9:
                img_hour0.setImageResource(R.drawable.font_9);
                break;
        }

        //??? (10??????)
        switch (minute / 10) {
            case 0:
                img_min10.setImageResource(R.drawable.font_0);
                break;
            case 1:
                img_min10.setImageResource(R.drawable.font_1);
                break;
            case 2:
                img_min10.setImageResource(R.drawable.font_2);
                break;
            case 3:
                img_min10.setImageResource(R.drawable.font_3);
                break;
            case 4:
                img_min10.setImageResource(R.drawable.font_4);
                break;
            case 5:
                img_min10.setImageResource(R.drawable.font_5);
                break;
            case 6:
                img_min10.setImageResource(R.drawable.font_6);
                break;
            case 7:
                img_min10.setImageResource(R.drawable.font_7);
                break;
            case 8:
                img_min10.setImageResource(R.drawable.font_8);
                break;
            case 9:
                img_min10.setImageResource(R.drawable.font_9);
                break;
        }

        //??? (1??????)
        switch (minute % 10) {
            case 0:
                img_min0.setImageResource(R.drawable.font_0);
                break;
            case 1:
                img_min0.setImageResource(R.drawable.font_1);
                break;
            case 2:
                img_min0.setImageResource(R.drawable.font_2);
                break;
            case 3:
                img_min0.setImageResource(R.drawable.font_3);
                break;
            case 4:
                img_min0.setImageResource(R.drawable.font_4);
                break;
            case 5:
                img_min0.setImageResource(R.drawable.font_5);
                break;
            case 6:
                img_min0.setImageResource(R.drawable.font_6);
                break;
            case 7:
                img_min0.setImageResource(R.drawable.font_7);
                break;
            case 8:
                img_min0.setImageResource(R.drawable.font_8);
                break;
            case 9:
                img_min0.setImageResource(R.drawable.font_9);
                break;
        }

        //??? (10??????)
        switch (second / 10) {
            case 0:
                img_sec10.setImageResource(R.drawable.font_0);
                break;
            case 1:
                img_sec10.setImageResource(R.drawable.font_1);
                break;
            case 2:
                img_sec10.setImageResource(R.drawable.font_2);
                break;
            case 3:
                img_sec10.setImageResource(R.drawable.font_3);
                break;
            case 4:
                img_sec10.setImageResource(R.drawable.font_4);
                break;
            case 5:
                img_sec10.setImageResource(R.drawable.font_5);
                break;
            case 6:
                img_sec10.setImageResource(R.drawable.font_6);
                break;
            case 7:
                img_sec10.setImageResource(R.drawable.font_7);
                break;
            case 8:
                img_sec10.setImageResource(R.drawable.font_8);
                break;
            case 9:
                img_sec10.setImageResource(R.drawable.font_9);
                break;
        }

        //??? (1??????)
        switch (second % 10) {
            case 0:
                img_sec0.setImageResource(R.drawable.font_0);
                break;
            case 1:
                img_sec0.setImageResource(R.drawable.font_1);
                break;
            case 2:
                img_sec0.setImageResource(R.drawable.font_2);
                break;
            case 3:
                img_sec0.setImageResource(R.drawable.font_3);
                break;
            case 4:
                img_sec0.setImageResource(R.drawable.font_4);
                break;
            case 5:
                img_sec0.setImageResource(R.drawable.font_5);
                break;
            case 6:
                img_sec0.setImageResource(R.drawable.font_6);
                break;
            case 7:
                img_sec0.setImageResource(R.drawable.font_7);
                break;
            case 8:
                img_sec0.setImageResource(R.drawable.font_8);
                break;
            case 9:
                img_sec0.setImageResource(R.drawable.font_9);
                break;
        }

    }
    public void what_face_visible(){
        // ?????? ????????? ???????????? ???????????? ???????????? ??????
        img_dark_back.setVisibility(View.VISIBLE);
        img_dark_back_kokkoro.setVisibility(View.VISIBLE);
        img_dark_back_kokkoro_blink.setVisibility(View.VISIBLE);
        img_dark_back_kokkoro_lip.setVisibility(View.VISIBLE);
        img_dark_back_kokkoro_face.setImageResource(R.drawable.char_kokkoro_princess_surprised);
        img_dark_back_kokkoro_face.setVisibility(View.VISIBLE);
        img_dark_back_kokkoro_speak.setVisibility(View.VISIBLE);
        Glide.with(this).load(R.drawable.speak_test).apply(new RequestOptions()
                .signature(new ObjectKey("signature string"))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
        ).fitCenter().into(img_dark_back_kokkoro_speak_text);
        img_dark_back_kokkoro_speak_text.setVisibility(View.VISIBLE);
    }

    public void end_face_visible() {
        Glide.with(this).load(R.drawable.speak_end).apply(new RequestOptions()
                .signature(new ObjectKey("signature string"))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
        ).fitCenter().into(img_dark_back_kokkoro_speak_text);
        img_dark_back_kokkoro_lip_mode.setImageResource(R.drawable.kokkoro_princess_happy_lip);
        img_dark_back_kokkoro_face.setImageResource(R.drawable.char_kokkoro_princess_smile);
    }

    public void error_face_visual(){
        //??????
        Glide.with(this).load(R.drawable.speak_error).apply(new RequestOptions()
                .signature(new ObjectKey("signature string"))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
        ).fitCenter().into(img_dark_back_kokkoro_speak_text);
        //??????
        img_dark_back_kokkoro_lip_mode.setImageResource(R.drawable.char_kokkoro_princess_lip_sad);
        //??????
        img_dark_back_kokkoro_face.setImageResource(R.drawable.char_kokkoro_princess_sad);

    }

    public void sex_face_visible(){
        //????????? ????????? ????????? ????????? ???????????????.
        //????????? ???????????? ????????? ?????? ?????? ?????? ????????? ?????????????????????.
        img_dark_back_kokkoro_speak_text.setVisibility(View.VISIBLE);
        img_dark_back_kokkoro_face.setImageResource(R.drawable.char_kokkoro_princess_shy);
    }


    public void all_face_invisible(){
        // ?????? ????????? ???????????? ???????????? ???????????? ??????
        img_dark_back.setVisibility(View.GONE);
        img_dark_back_kokkoro.setVisibility(View.GONE);
        img_dark_back_kokkoro_blink.setVisibility(View.GONE);
        img_dark_back_kokkoro_face.setVisibility(View.GONE);
        img_dark_back_kokkoro_speak.setVisibility(View.GONE);
        img_dark_back_kokkoro_speak_text.setVisibility(View.GONE);
        img_dark_back_kokkoro_lip.setVisibility(View.GONE);
    }

    public void welcome_this_voice(){
        // ?????? ??????????????? ?????????
        mediaPlayer = MediaPlayer.create(MainKokkoroActivity.this, R.raw.welcome_voice);
        mediaPlayer.start();
        mediaPlayer.setLooping(false);
        img_face.setImageResource(R.drawable.char_kokkoro_shy); // ????????? ?????? ????????? ??????
        bt_emotion.setImageResource(R.drawable.char_emotion_shy);
        img_face_speak.setVisibility(View.VISIBLE); // ????????? ????????? ??????.

        //?????? ?????? ?????? ?????? ???????????? ????????? ??????????????? ??????.
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                img_face_speak.setVisibility(View.GONE);
                img_face.setVisibility(View.GONE);
                bt_emotion.setVisibility(View.GONE);
                startService(new Intent(MainKokkoroActivity.this,Recognition.class)); // ???????????? ????????? ???
            }
        });
    }


    public void what_this_voice(){
        stopService(new Intent(MainKokkoroActivity.this,Recognition.class)); // ???????????? ????????? ???

        SpeakString = null;
        System.out.println("?????? ??????");

        //?????? ?????? ??????
        bt_emotion.setImageResource(R.drawable.char_emotion_question);

        mediaPlayer = MediaPlayer.create(MainKokkoroActivity.this, R.raw.what_this_voice);
        mediaPlayer.start();
        mediaPlayer.setLooping(false);

        what_face_visible();


        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                startService(new Intent(MainKokkoroActivity.this,Recognition.class)); // ???????????? ????????? ???
                img_dark_back_kokkoro_lip.setVisibility(View.GONE); // ????????? ???????????? ??????.
            }

        });
        //????????? ?????? ??????
        kokkoro_voice = Constants.kokkoro_voice_on;



    }

    public void move_to_voice_menu(){
        stopService(new Intent(MainKokkoroActivity.this,Recognition.class)); // ???????????? ????????? ???

        // ?????? ?????? ???????????? ?????? ??????????????? ???????????? ?????? ??????
        if((move_to_voice != Constants.move_to_voice_search) && (move_to_voice != Constants.move_to_voice_youtube_search)) {
            SpeakString = null;
        }
        kokkoro_voice = Constants.kokkoro_voice_off;
        kokkoro_count = Constants.kokkoro_conunt_ready;

        Glide.with(this).load(R.drawable.speak_ready_lip)
                .apply(new RequestOptions()
                        .signature(new ObjectKey("signature string"))
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                ).
                fitCenter().into(img_dark_back_kokkoro_lip);

        img_dark_back_kokkoro_lip.setVisibility(View.VISIBLE);

        bt_emotion.setImageResource(R.drawable.char_emotion_answer); // ??????????????? ???????????????


        // ?????? ?????? ????????? ??????
        switch(move_to_voice){
            case Constants.move_to_voice_sex:
                if(sex_count==Constants.sex_off) {

                    sex_count = Constants.sex_on;
                    mediaPlayer = MediaPlayer.create(MainKokkoroActivity.this, R.raw.i_kill_you);
                    mediaPlayer.start();
                    mediaPlayer.setLooping(false);

                    Glide.with(this).load(R.drawable.sex)
                            .apply(new RequestOptions()
                            .signature(new ObjectKey("signature string"))
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                    ).fitCenter().into(img_dark_back_kokkoro_speak_text);

                    img_dark_back_kokkoro_face.setImageResource(R.drawable.char_kokkoro_princess_angry);
                    img_dark_back_kokkoro_lip_mode.setImageResource(R.drawable.char_kokkoro_princess_lip_angry);

                }
                break;

            case Constants.not_to_move:
                mediaPlayer = MediaPlayer.create(MainKokkoroActivity.this, R.raw.error_voice);
                break;

            default:
                mediaPlayer = MediaPlayer.create(MainKokkoroActivity.this, R.raw.end_voice);
                break;
        }


        //?????? ??????
        if(move_to_voice!=Constants.move_to_voice_sex) {
            mediaPlayer.start();
            mediaPlayer.setLooping(false);
        }



        //?????????????????? ?????????????????? ????????? ????????? ???????????? ?????? ??????
        switch(move_to_voice) {

            case Constants.not_to_move :
                error_face_visual();
                break;

            case Constants.move_to_voice_null:
                error_face_visual();
                break;

            case Constants.move_to_voice_sex:
                break;


            default:
                end_face_visible();
                break;
        }

        // ??? ????????? ?????????????????? iot??? ???????????? ?????? ????????????
        if(move_to_voice >= Constants.move_to_living_room_on && move_to_voice <= Constants.move_to_livingroom_temp)
        {

            iftttmode();

        }

        //????????? ?????? ?????? ?????? ??????????????? ??????????????????.

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                if(move_to_voice == Constants.move_to_voice_sex) {
                    System.exit(0);
                }
                else
                voice_end_to_start();

            }
        });




    }

    public void voice_end_to_start(){
        //????????? ?????? ??? ?????? ?????? ????????? ?????? ???????????? ????????????.

                all_face_invisible();
                startService(new Intent(MainKokkoroActivity.this,Recognition.class)); // ???????????? ????????? ???

                switch(move_to_voice){
                    case Constants.move_to_voice_youtube :
                        Intent app_musicplayer = getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
                        // ?????? ????????? getActivity ??? ???????????? ??????. (?????? ??????)
                        startActivity(app_musicplayer);
                        //System.exit(0);
                        break;

                    case Constants.move_to_voice_search:
                        Intent websearch = new Intent(Intent.ACTION_WEB_SEARCH);
                        websearch.putExtra(QUERY, SpeakString);
                        startActivity(websearch);
                        SpeakString = null;
                        //System.exit(0);
                        break;

                    case Constants.move_to_voice_youtube_search:
                        Intent youtubesearch = new Intent(Intent.ACTION_SEARCH);
                        youtubesearch.setPackage("com.google.android.youtube");
                        // ???????????? ????????? ????????? ??????
                        youtubesearch.putExtra(QUERY, SpeakString);
                        SpeakString = null;
                        try {
                            startActivity(youtubesearch);
                        }catch(ActivityNotFoundException e){}

                        //System.exit(0);
                        break;

                    case Constants.move_to_voice_bluetooth:
                        Intent bluetoothmode = new Intent();
                        bluetoothmode.setComponent(new ComponentName("com.ts.MainUI", "com.ts.bt.BtConnectActivity"));
                        startActivity(bluetoothmode);
                        //System.exit(0);
                        break;

                    case Constants.move_to_voice_navi:
                        Intent app_navi = getPackageManager().getLaunchIntentForPackage("com.skt.tmap.ku");
                        // ?????? ????????? getActivity ??? ???????????? ??????. (?????? ??????)
                        startActivity(app_navi);
                        //System.exit(0);
                        break;

                    case Constants.move_to_voice_ride:
                        Intent app_navi2 = getPackageManager().getLaunchIntentForPackage("com.skt.tmap.ku");
                        Intent bluetoothmode2 = new Intent();
                        bluetoothmode2.setComponent(new ComponentName("com.ts.MainUI", "com.ts.bt.BtMusicActivity"));
                        startActivity(bluetoothmode2);
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        startActivity(app_navi2);
                        System.exit(0);
                        break;

                    case Constants.move_to_voice_weather :
                        weather_on();

                    }

                move_to_voice=Constants.move_to_voice_null;


    }

    //?????? ????????? ?????? ??????
    public void weather_gone() {
        weather_kokkoro_speak_text.setVisibility(View.GONE);
        weather_kokkoro_speak_eye.setVisibility(View.GONE);
        weather_kokkoro_speak_mouth.setVisibility(View.GONE);
        weather_kokkoro.setVisibility(View.GONE);
        img_tempView.setVisibility(View.GONE);
        img_humView.setVisibility(View.GONE);
        weather_now.setVisibility(View.GONE);
        img_weatherpanlel.setVisibility(View.GONE);
        img_stamp.setVisibility(View.GONE);
        dateView.setVisibility(View.GONE);
        tempView.setVisibility(View.GONE);
        humView.setVisibility(View.GONE);
        weatherText.setVisibility(View.GONE);
    }

    public void weather_visible(){
        weather_kokkoro_speak_text.setVisibility(View.VISIBLE);
        weather_kokkoro_speak_eye.setVisibility(View.VISIBLE);
        weather_kokkoro_speak_mouth.setVisibility(View.VISIBLE);
        weather_kokkoro.setVisibility(View.VISIBLE);
        img_tempView.setVisibility(View.VISIBLE);
        img_humView.setVisibility(View.VISIBLE);
        weather_now.setVisibility(View.VISIBLE);
        img_weatherpanlel.setVisibility(View.VISIBLE);
        img_stamp.setVisibility(View.VISIBLE);
        dateView.setVisibility(View.VISIBLE);
        tempView.setVisibility(View.VISIBLE);
        humView.setVisibility(View.VISIBLE);
        weatherText.setVisibility(View.VISIBLE);
    }

    public void weather_on(){
        //?????? ?????? ??? ?????? ???
        weather_visible();

        // ????????? ???????????? ????????? ????????????
        mediaPlayer = MediaPlayer.create(MainKokkoroActivity.this, R.raw.weather_kokkoro_voice);

        // ?????? ??????
        mediaPlayer.start();
        mediaPlayer.setLooping(false);

        GpsTracker gpsTracker = new GpsTracker(MainKokkoroActivity.this);

        currentLatitude = gpsTracker.getLatitude();
        currentLongitude = gpsTracker.getLongitude();

        System.out.println("?????? : "+currentLatitude);
        System.out.println("?????? : "+currentLongitude);


        //volley??? ??? ??? ?????? ??????????????? ????????? ??? ????????????
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }


        CurrentCall();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                weather_gone();
            }

        });

    }



    private void CurrentCall(){

        String url = "http://api.openweathermap.org/data/2.5/weather?lat="+currentLatitude+"&lon="+currentLongitude+"&appid={API}&lang=kr";

        System.out.println(url);


        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {

                try {

                    //api??? ?????? ?????? jsonobject??? ????????? ?????? ??????
                    JSONObject jsonObject = new JSONObject(response);


                    //?????? ?????? ??????
                    String city = jsonObject.getString("name");



                    //System??? ?????? ??????(???,???,???,???,???,?????????)???????????? Date??? ????????????
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);

                    //???, ???, ??? ????????????. ???,???,??? ???????????? ??????????????? String??? ???????????? ??????
                    SimpleDateFormat simpleDateFormatDay = new SimpleDateFormat("yyyy??? MM??? dd???");
                    String getDay = simpleDateFormatDay.format(date);

                    //getDate??? ????????? ????????? ???????????? ?????? ??? dateView??? text??????
                    String getDate = getDay + " " + city + "??? ??????????";

                    dateView.setText(getDate);


                    //?????? ?????? ??????
                    JSONArray weatherJson = jsonObject.getJSONArray("weather");
                    JSONObject weatherObj = weatherJson.getJSONObject(0);

                    //weather ????????? ????????? ?????? ????????? ?????? ?????? ??????.
                    String weather = weatherObj.getString("main");

                    weatherText.setText(weather);


                    // ?????? ????????? ????????? ??????

                    if(weather.contains("Thunderstorm")) {
                        weather_now.setImageResource(R.drawable.weather_thunder);
                        weatherText.setTextColor(Color.parseColor("#ea788e"));
                    }
                    else if(weather.contains("Drizzle")) {
                        weather_now.setImageResource(R.drawable.weather_drizzly);
                        weatherText.setTextColor(Color.parseColor("#8d4173"));
                    }
                    else if(weather.contains("Rain")) {
                        weather_now.setImageResource(R.drawable.weather_rainy);
                        weatherText.setTextColor(Color.parseColor("#3463d7"));
                    }
                    else if(weather.contains("Snow")) {
                        weather_now.setImageResource(R.drawable.weather_snow);
                        weatherText.setTextColor(Color.parseColor("#ff447c"));
                    }
                    else if(weather.contains("Mist") || weather.contains("Smoke") || weather.contains("Haze") || weather.contains("Dust") || weather.contains("Fog") || weather.contains("Sand") || weather.contains("Ash") || weather.contains("Squall") || weather.contains("Tornado")) {
                        weather_now.setImageResource(R.drawable.weather_mist);
                        weatherText.setTextColor(Color.parseColor("#8bb96d"));
                    }
                    else if(weather.contains("Clear")) {
                        weather_now.setImageResource(R.drawable.weather_sunny);
                        weatherText.setTextColor(Color.parseColor("#ff7243"));
                    }
                    else if(weather.contains("Clouds")) {
                        weather_now.setImageResource(R.drawable.weather_cloudy);
                        weatherText.setTextColor(Color.parseColor("#3260da"));
                    }
                    // ?????? ?????? ?????? ??????
                    weather_now.setVisibility(View.VISIBLE);

                    //?????? ?????? ??????
                    JSONObject tempK = new JSONObject(jsonObject.getString("main"));

                    //?????? ?????? ?????? ????????? ?????? ????????? ??????
                    double tempDo = (Math.round((tempK.getDouble("temp")-273.15)*100)/100.0);
                    tempView.setText(tempDo +  "??C");

                    //?????? ?????? ?????? ????????? ?????? ????????? ??????
                    double humDo = (Math.round((tempK.getDouble("humidity"))*1)/1);
                    humView.setText(humDo +  "%");








                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }

        };

        request.setShouldCache(false);
        requestQueue.add(request);
    }

    public void weather_gif_create(){
        weather_kokkoro_speak_text = (ImageView)findViewById(R.id.weather_kokkoro_speak);
        Glide.with(this).load(R.drawable.weather_speak).apply(new RequestOptions()
                .signature(new ObjectKey("signature string"))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
        ).apply(new RequestOptions()
                .signature(new ObjectKey("signature string"))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
        ).fitCenter().into(weather_kokkoro_speak_text);


        weather_kokkoro_speak_eye = (ImageView)findViewById(R.id.weather_kokkoro_eye);
        Glide.with(this).load(R.drawable.weather_kokkoro_blink).apply(new RequestOptions()
                .signature(new ObjectKey("signature string"))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
        ).apply(new RequestOptions()
                .signature(new ObjectKey("signature string"))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
        ).fitCenter().into(weather_kokkoro_speak_eye);

        weather_kokkoro_speak_mouth = (ImageView)findViewById(R.id.weather_kokkoro_mouth);
        Glide.with(this).load(R.drawable.weather_kokkoro_speak_mouth).apply(new RequestOptions()
                .signature(new ObjectKey("signature string"))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
        ).apply(new RequestOptions()
                .signature(new ObjectKey("signature string"))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
        ).fitCenter().into(weather_kokkoro_speak_mouth);
    }

    public void iftttmode(){


        //volley??? ??? ??? ?????? ??????????????? ????????? ??? ????????????
        if(iftttQueue == null){
            iftttQueue = Volley.newRequestQueue(getApplicationContext());}


            // ??? ????????? ?????? ???????????? ???????????? ??????.
        String iftttapi = "{API}";
        String startIot ="null";

        // ??? ????????? ???????????? ???????????? ???????????? ??????
        switch(move_to_voice){
            case Constants.move_to_living_room_on :
                startIot="switch_livingroom_on";
                break;
            case Constants.move_to_living_room_off :
                startIot="switch_livingroom_off";
                break;
            case Constants.move_to_air_circulator_on :
                startIot="air_circulator_on";
                break;
            case Constants.move_to_air_circulator_off :
                startIot="air_circulator_off";
                break;
            case Constants.move_to_livingroom_temp :
                startIot="livingroom_temp";
                break;
        }

        System.out.println("?????? ????????? iot ??? : "+startIot);

        String ifttturl="https://maker.ifttt.com/trigger/"+startIot+"/with/key/"+iftttapi;

        System.out.println("?????? ????????? ifttturl ??? : "+ifttturl);

        StringRequest iftttrequest = new StringRequest(Request.Method.GET, ifttturl, new Response.Listener<String>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                System.out.println("??????????????? ???????????? ?????? : " + ifttturl);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("???????????? ?????? : " + ifttturl);

            }
        });

        iftttrequest.setShouldCache(false);
        iftttQueue.add(iftttrequest);


    }



    // fab ??????????????? ?????? ?????????
    public void anim() {

        if (isFabOpen) {
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fab4.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            fab4.setClickable(false);
            isFabOpen = false;
        } else {
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab4.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            fab4.setClickable(true);
            isFabOpen = true;
        }
    }
}

