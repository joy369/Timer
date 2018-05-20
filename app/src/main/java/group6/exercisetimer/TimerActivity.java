package group6.exercisetimer;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
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
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class TimerActivity extends AppCompatActivity {
    private Context context = this;
    private static final long SECOND = 1000;
    private static final String ORANGE = "#F39C12";
    private static final String GREEN = "#28B463";
    private static final String BLUE = "#3498DB";
    private static final String RED = "#e74c3c";
    private ValueAnimator colorAnimation;
    private List<String> aciton_names, action_commnets, action_backbrounds;
    private int[] action_timers, soundIds;
    private int totall_time, soundId;
    private int timer_index = 0;
    private long remaing_time;
    private boolean isPaused;
    private Bundle from_ACS_bundle;
    private ActionBar actionBar;
    private TextView action_title_now, action_comment_now, action_time_now, left_item_view;
    private ImageView timer_start_pause, timer_reset, timer_back, timer_next;
    private ConstraintLayout timer_activity_layout;
    private CountDownTimer countDownTimer;
    private ProgressBar progressBarCircle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
//        load and set up the needed data
        from_ACS_bundle = getIntent().getExtras();
        aciton_names = from_ACS_bundle.getStringArrayList("name_sequence");
        action_commnets = from_ACS_bundle.getStringArrayList("comment_sequence");
        action_timers = from_ACS_bundle.getIntArray("time_sequence");
        totall_time = from_ACS_bundle.getInt("total_time_now");
        action_backbrounds = new ArrayList<String>();
        soundIds = new int[action_timers.length];
//        make the BG list and sound array for the counter
        makeBGnSoundIDList();

//        Link veiw
        timer_activity_layout = (ConstraintLayout) findViewById(R.id.timer_activity_layout);
        action_title_now = (TextView) findViewById(R.id.timer_title_now);
        left_item_view = (TextView) findViewById(R.id.left_item);
        action_time_now = (TextView) findViewById(R.id.timer_time_now);
        action_comment_now = (TextView) findViewById(R.id.timer_comment_now);
        progressBarCircle = (ProgressBar) findViewById(R.id.time_progress);
        action_title_now.setText("SO HARD");
        TextViewCompat.setAutoSizeTextTypeWithDefaults(action_title_now, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(action_time_now, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(left_item_view, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        timer_next = (ImageView) findViewById(R.id.click_next);
        timer_back = (ImageView) findViewById(R.id.click_back);
        timer_reset = (ImageView) findViewById(R.id.reset_timer);
        timer_start_pause = (ImageView) findViewById(R.id.start_pause);
        timer_start_pause.setOnClickListener(clickListener);
        timer_next.setOnClickListener(clickListener);
        timer_back.setOnClickListener(clickListener);
        timer_reset.setOnClickListener(clickListener);

//        Disable the action bar, it's useless here
        actionBar = getSupportActionBar();
        actionBar.hide();

//            Initial the timer view by the first action item
        action_title_now.setText(aciton_names.get(0));
        action_comment_now.setText(action_commnets.get(0));
        timer_activity_layout.setBackgroundColor(Color.parseColor(action_backbrounds.get(0)));
        left_item_view.setText(String.valueOf(timer_index+1)+"/"+String.valueOf(aciton_names.size()));



//        Set currnet maximun
        setProgressBarValues(action_timers[0]);
        countDownTimer = new CountDownTimer((long) totall_time * SECOND, 10) {
            public void onTick(long millisUntilFinished) {
                long have_run = totall_time * SECOND - millisUntilFinished;
                Log.i("have_run", String.valueOf(have_run / SECOND));
//                Change the view for the next action
                if (timer_index != action_timers.length - 1 && (int) (have_run / SECOND) >= action_timers[timer_index]) {
                    timer_index++;
                    action_title_now.setText(aciton_names.get(timer_index));
                    action_comment_now.setText(action_commnets.get(timer_index));
//                    note the + 1 here because user's initial point is 1 but not 0
                    left_item_view.setText(String.valueOf(timer_index+1)+"/"+String.valueOf(aciton_names.size()));
                    changeBG(action_backbrounds.get(timer_index),action_backbrounds.get(timer_index));
                    playSound(soundIds[timer_index]);
                }
                if (millisUntilFinished <=1*SECOND) millisUntilFinished = 10*SECOND;
//                +1 for showing the precise number
                action_time_now.setText(String.valueOf(1 + millisUntilFinished / SECOND));
//                progressBarCircle.setProgress((int)(TimeUnit.MILLISECONDS).toSeconds(millisUntilFinished));

            }

            public void onFinish() {
//                Set the sound
//                countDownTimer.cancel();
//                soundId = R.raw.finishbell;
//                playSound(soundId);
//                progressBarCircle.setVisibility(View.GONE);
//                action_comment_now.setVisibility(View.GONE);
//                action_title_now.setVisibility(View.GONE);
//                timer_back.setVisibility(View.GONE);
//                timer_next.setVisibility(View.GONE);
//                timer_reset.setVisibility(View.GONE);
//                timer_start_pause.setVisibility(View.GONE);
//                left_item_view.setVisibility(View.GONE);
//
//                action_time_now.setText("Finish!");
            }
        }.start();

    }

    //    Chnage color of background
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

    //    According to the name, set the sound and play
    private void playSound(int soundId) {
        SoundPool mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        mSoundPool.load(context, soundId, 1);
        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(sampleId, 1.0f, 1.0f, 0, 0, 1.0f);
            }
        });
    }

    private void setProgressBarValues(int timer_duration) {
        progressBarCircle.setMax(timer_duration);
        progressBarCircle.setProgress(timer_duration);
    }

    private void startCountDownTimer(long timer_duration, String timer_type, int sound_now) {

        countDownTimer = new CountDownTimer(timer_duration * SECOND, SECOND) {
            @Override
            public void onTick(long millisUntilFinished) {
//                Set up timer and progresscircle
                action_time_now.setText(String.valueOf(millisUntilFinished / SECOND));
//                progressBarCircle.setProgress((int) (millisUntilFinished / SECOND));
            }

            @Override
            public void onFinish() {
            }

        }.start();
        countDownTimer.start();
    }

    private void start_pause(long timer_duration, String timer_type, int sound_now) {
        if (isPaused == true) {
//
//
//            // call to initialize the progress bar values
//            setProgressBarValues(countDownTimer.onTick());
//            // showing the reset icon
//            imageViewReset.setVisibility(View.VISIBLE);
//            // changing play icon to stop icon
//            imageViewStartStop.setImageResource(R.drawable.icon_stop);
//            // making edit text not editable
//            editTextMinute.setEnabled(false);
//            // changing the timer status to started
//            timerStatus = TimerStatus.STARTED;
//            // call to start the count down timer
            startCountDownTimer(timer_duration, timer_type, sound_now);

        } else {
            // changing stop icon to start icon
//            stopCountDownTimer();

        }

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.start_pause:
                    v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.image_onclick));

                    break;
                case R.id.reset_timer:
                    v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.image_onclick));
//                reset();
                    break;
                case R.id.click_next:
                    v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.image_onclick));
                    break;
                case R.id.click_back:
                    v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.image_onclick));
                    break;
            }
        }
    };

    //    Cancell the timer when click back key
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        countDownTimer.cancel();
    }


    private void makeBGnSoundIDList() {
        for (int i = 0; i < aciton_names.size(); i++) {
            if (aciton_names.get(i).equals("Work")) {
                action_backbrounds.add(RED);
                soundIds[i] = R.raw.whistle;
            } else if (aciton_names.get(i).equals("Rest")) {
                action_backbrounds.add(BLUE);
                soundIds[i] = R.raw.rest_ding;
            } else if (aciton_names.get(i).equals("Prepare")) {
                action_backbrounds.add(ORANGE);
                soundIds[i] = R.raw.rest_ding;
            } else {
                action_backbrounds.add(GREEN);
                soundIds[i] = R.raw.whistle;
            }
        }
    }


}
