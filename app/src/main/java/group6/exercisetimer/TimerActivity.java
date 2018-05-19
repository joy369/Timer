package group6.exercisetimer;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.Image;
import android.media.SoundPool;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.List;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TimerActivity extends AppCompatActivity {
    private static final int SECOND = 1000;
    private static final String ORANGE = "#234567";
    private static final String GREEN = "#674531";
    private static final String BLUE = "#912345";
    private ValueAnimator colorAnimation;
    private List<String> aciton_names, action_commnets;
    private int[] action_timers;
    private int totall_time;
    private Bundle from_ACS_bundle;
    private ActionBar actionBar;
    private TextView action_title_now, action_comment_now, action_time_now, action_left_time;
    private ImageView timer_start, timer_pause, timer_reset, timer_back, timer_next;
    private ConstraintLayout timer_activity_layout;
    private SoundPool sp = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
    private MediaPlayer mPlayer;
    private int soundId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

//        Link veiw
        timer_activity_layout = (ConstraintLayout) findViewById(R.id.timer_activity_layout);
        action_title_now = (TextView) findViewById(R.id.action_title_now);
        action_left_time = (TextView) findViewById(R.id.left_item);
        action_time_now = (TextView) findViewById(R.id.current_time) ;
        TextViewCompat.setAutoSizeTextTypeWithDefaults(action_title_now, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(action_time_now, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(action_left_time, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);

//        Disable the action bar, it's useless here
        actionBar = getSupportActionBar();
        actionBar.hide();
//        load the needed data
        from_ACS_bundle = getIntent().getExtras();
        aciton_names = from_ACS_bundle.getStringArrayList("name_sequence");
        action_commnets = from_ACS_bundle.getStringArrayList("comment_sequence");
        action_timers = from_ACS_bundle.getIntArray("time_sequence");
        totall_time = from_ACS_bundle.getInt("total_time_now");

        changeBG(GREEN,ORANGE);
        soundId = sp.load(this, R.raw.whistle, 1); // in 2nd param u have to pass your desire ringtone
        playSound(soundId);

    }

    private void changeBG(String from_color, String to_color) {
        colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator()
                , Color.parseColor(from_color), Color.parseColor(to_color));
        colorAnimation.setDuration(800); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                timer_activity_layout.setBackgroundColor((int) animator.getAnimatedValue());
            }
        });
        colorAnimation.start();
    }

    private void playSound(int soundId) {
        sp.play(soundId, 1, 1, 0, 0, 1);
        mPlayer = MediaPlayer.create(this, R.raw.whistle); // in 2nd param u have to pass your desire ringtone
        //mPlayer.prepare();
        mPlayer.start();

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

}
