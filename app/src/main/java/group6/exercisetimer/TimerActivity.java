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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TimerActivity extends AppCompatActivity {
    private Context context = this;
    private static final long SECOND = 1000;
    private static final String ORANGE = "#F39C12";
    private static final String GREEN = "#28B463";
    private static final String BLUE = "#3498DB";
    private static final String RED = "#e74c3c";
    private static final String PROJECT_BLUE = "#FF3F51B5";
    private ValueAnimator colorAnimation;
    private List<String> aciton_names, action_commnets, action_backbrounds;
    private int[] action_timers, timer_soundIds;
    private int totall_time, soundId;
    private int timer_index = 0;
    private long remaing_time;
    private boolean isPaused = false;
    private boolean by_button = false;
    private Bundle from_ACS_bundle;
    private ActionBar actionBar;
    private TextView action_name_now, action_comment_now, action_time_now, left_action_view;
    private ImageView timer_start, timer_reset, timer_back, timer_next, timer_pause;
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
        timer_soundIds = new int[action_timers.length];
//        make the BG list and sound array for the counter
        makeBGnSoundIDList();

//        Link veiw
        timer_activity_layout = (ConstraintLayout) findViewById(R.id.timer_activity_layout);
        action_name_now = (TextView) findViewById(R.id.timer_title_now);
        left_action_view = (TextView) findViewById(R.id.left_item);
        action_time_now = (TextView) findViewById(R.id.timer_time_now);
        action_comment_now = (TextView) findViewById(R.id.timer_comment_now);
        progressBarCircle = (ProgressBar) findViewById(R.id.time_progress);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(action_name_now, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(action_time_now, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(left_action_view, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        timer_next = (ImageView) findViewById(R.id.click_next);
        timer_back = (ImageView) findViewById(R.id.click_back);
        timer_reset = (ImageView) findViewById(R.id.reset_timer);
        timer_start = (ImageView) findViewById(R.id.start);
        timer_pause = (ImageView) findViewById(R.id.pause);
        timer_start.setOnClickListener(clickListener);
        timer_pause.setOnClickListener(clickListener);
        timer_next.setOnClickListener(clickListener);
        timer_back.setOnClickListener(clickListener);
        timer_reset.setOnClickListener(clickListener);

//        Disable the action bar, it's useless here
        actionBar = getSupportActionBar();
        actionBar.hide();

//            Initial the timer view by the first action item
        action_name_now.setText(aciton_names.get(0));
        action_comment_now.setText(action_commnets.get(0));
        timer_activity_layout.setBackgroundColor(Color.parseColor(action_backbrounds.get(0)));
        left_action_view.setText(String.valueOf(timer_index + 1) + "/" + String.valueOf(aciton_names.size()));
        setProgressBarValues(action_timers[0]);

//        Start timers
        createTimers();

    }

    //    Create and startsequence timers
    private void createTimers() {
        if (timer_index != action_timers.length) {

            setupview();
//            check for pause
            int timer_input_time;
            if (isPaused) {
                timer_input_time = (int) (remaing_time / SECOND);
                progressBarCircle.setMax(action_timers[timer_index]);
                progressBarCircle.setProgress((int) (remaing_time / SECOND));
            } else {
                timer_input_time = action_timers[timer_index];
                setProgressBarValues(action_timers[timer_index]);
            }
            countDownTimer = new CountDownTimer((long) timer_input_time * SECOND, 10) {
                public void onTick(long millisUntilFinished) {
//                +1 for showing the precise number
                    action_time_now.setText(String.valueOf(1 + millisUntilFinished / SECOND));
                    progressBarCircle.setProgress((int) (TimeUnit.MILLISECONDS).toSeconds(millisUntilFinished));
                    remaing_time = millisUntilFinished;
                }

                public void onFinish() {
                    action_time_now.setText(String.valueOf(0));
                    countDownTimer.cancel();
                    timer_index++;
                    createTimers();

                }
            }.start();
        } else {
            allFinish();
        }
    }

    //    Set up the view for each timer
    private void setupview() {
        action_name_now.setText(aciton_names.get(timer_index));
        action_comment_now.setText(action_commnets.get(timer_index));
        left_action_view.setText(String.valueOf(timer_index + 1) + "/" + String.valueOf(aciton_names.size()));
        if (timer_index == 0) {
            timer_activity_layout.setBackgroundColor(Color.parseColor(action_backbrounds.get(timer_index)));
        } else {
            changeBG(action_backbrounds.get(timer_index - 1), action_backbrounds.get(timer_index));
        }
//        When getinto here by reset, back and next, do not make soud
        if (by_button) by_button = false;
            //        Check isPuased, so that timer timer won't be refresh and make too much sound
        else if (!isPaused) {
            playSound(timer_soundIds[timer_index]);
            progressBarCircle.setMax(action_timers[timer_index]);
            progressBarCircle.setProgress(action_timers[timer_index]);
        }
    }

    //    Chnage color of background
    private void changeBG(String from_color, String to_color) {
        colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator()
                , Color.parseColor(from_color), Color.parseColor(to_color));
        colorAnimation.setDuration(400); // milliseconds
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

    //    Initialize progressbar
    private void setProgressBarValues(int timer_duration) {
        progressBarCircle.setMax(timer_duration);
        progressBarCircle.setProgress(timer_duration);
    }

    //    Set finish view
    private void allFinish() {
        setProgressBarValues(0);
        soundId = R.raw.finishbell;
        if (by_button) by_button = false;
        else playSound(soundId);
        if (countDownTimer != null) countDownTimer.cancel();
        action_name_now.setText("Finish");
        action_time_now.setText("0");
        changeBG(action_backbrounds.get(action_timers.length - 1), PROJECT_BLUE);
//        Reassign index so that if user click back will start from the last timer
        timer_index = action_timers.length;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.start:
//                    this animation make user can notice the button is clicked
                    v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.image_onclick));

                    if (isPaused) {
                        countDownTimer.cancel();
                        createTimers();
                        isPaused = false;
                    } else {
                    }
                    break;
                case R.id.pause:
                    v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.image_onclick));
                    countDownTimer.cancel();
                    if (isPaused) {
                    } else {
                        isPaused = true;
                    }
                    break;
                case R.id.reset_timer:
                    v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.image_onclick));
                    countDownTimer.cancel();
                    if (isPaused) {
                        isPaused = false;
                    }
                    by_button = true;
                    createTimers();
                    break;
                case R.id.click_next:
                    v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.image_onclick));
                    by_button = true;
                    countDownTimer.cancel();
                    if (isPaused) {
                        isPaused = false;
                    }
                    if (timer_index >= action_timers.length - 1) {
                        allFinish();
                        break;
                    } else timer_index++;
                    createTimers();
                    break;
                case R.id.click_back:
                    v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.image_onclick));
                    countDownTimer.cancel();
                    if (isPaused) {
                        isPaused = false;
                    }
                    if (timer_index != 0) timer_index--;
                    by_button = true;
                    createTimers();
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


    //    make the background color and sondID list for the sequence timer
    private void makeBGnSoundIDList() {
        for (int i = 0; i < aciton_names.size(); i++) {
            if (aciton_names.get(i).equals("Work")) {
                action_backbrounds.add(RED);
                timer_soundIds[i] = R.raw.whistle;
            } else if (aciton_names.get(i).equals("Rest")) {
                action_backbrounds.add(BLUE);
                timer_soundIds[i] = R.raw.rest_ding;
            } else if (aciton_names.get(i).equals("Prepare")) {
                action_backbrounds.add(ORANGE);
                timer_soundIds[i] = R.raw.rest_ding;
            } else {
                action_backbrounds.add(GREEN);
                timer_soundIds[i] = R.raw.whistle;
            }
        }
    }


}
