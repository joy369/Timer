package group6.exercisetimer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TimerActivity extends AppCompatActivity {
    private List<String> aciton_names;
    private List<String> action_commnets;
    private int[] action_timers;
    private Bundle from_ACS_bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        from_ACS_bundle = getIntent().getExtras();
        aciton_names = from_ACS_bundle.getStringArrayList("name_sequence");
        action_commnets = from_ACS_bundle.getStringArrayList("comment_sequence");
        action_timers = from_ACS_bundle.getIntArray("time_sequence");
    }
}
