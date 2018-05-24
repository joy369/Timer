package group6.exercisetimer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;

import static group6.exercisetimer.MainActivity.action_Component_type;
import static group6.exercisetimer.MainActivity.training_list_type;

public class ActionSettingActivity extends AppCompatActivity {
    private static final int TYPE_REPEAT = 1;
    private Context context = this;
    private RecyclerView action_recycler_view;
    private ActionRecyclerAdapter mACA;
    private int training_list_index;
    private String ith_ACL;
    private ArrayList<List<String>> decoded_data;
    private ArrayList<ActionComponent> action_list = new ArrayList<ActionComponent>();
    private ArrayList<ActionComponent> loaded_action_list = new ArrayList<ActionComponent>();
    private ArrayList<ActionComponent> original_list = new ArrayList<ActionComponent>();
    private ArrayList<TrainingList> trainingLists;
    private ActionComponent a_action;
    private SharedPreferences preferences;
    private Bundle from_main_bundle;
    private int action_icon;
    private Button start_timer;
    private ItemTouchHelper itemTouchHelper;
    private com.github.clans.fab.FloatingActionButton add_work, add_rest, add_cycle, add_self_define, add_prepare;
    private com.github.clans.fab.FloatingActionMenu action_menu;
    private final static int name_index = 0;
    private final static int comment_insex = 1;
    private final static int time_index = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_setting);
//         Disable the go back button and set action bar
        setTitle("Set up timer");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

//        Initialize data loader(preference)
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

//        Get bundle from main_activity
        from_main_bundle = getIntent().getExtras();
        training_list_index = from_main_bundle.getInt("current_item_index");
        trainingLists = from_main_bundle.getParcelableArrayList("bundled_TLs");

//        Load the action list in the last time (if not null)
//        Besides, save loaded_action_list which is used for checking any change
        ith_ACL = "ACL_" + Integer.toString(training_list_index);
        loaded_action_list = new Gson().fromJson(preferences.getString(ith_ACL, ""), action_Component_type);

//        Save original copy (the reason is reference, data inside object is not deep cloned )
        String str_ACs = new Gson().toJson(loaded_action_list, action_Component_type);
        preferences.edit().putString("ORIGINAL_LIST", str_ACs).apply();

//        Load original copy
        original_list = new Gson().fromJson(preferences.getString("ORIGINAL_LIST", ""), action_Component_type);

//        Finally, load previous data to action_list
        if (loaded_action_list != null)
            action_list = (ArrayList<ActionComponent>) loaded_action_list.clone();

//        Initialize view
        action_menu = (FloatingActionMenu) findViewById(R.id.menu_add_action);
        action_menu.setClosedOnTouchOutside(true);
        add_prepare = (FloatingActionButton) findViewById(R.id.addprepare);
        add_work = (FloatingActionButton) findViewById(R.id.addwork);
        add_rest = (FloatingActionButton) findViewById(R.id.addrest);
        add_cycle = (FloatingActionButton) findViewById(R.id.addcycle);
        add_self_define = (FloatingActionButton) findViewById(R.id.addselfdefine);
        add_prepare.setOnClickListener(clickListener);
        add_work.setOnClickListener(clickListener);
        add_rest.setOnClickListener(clickListener);
        add_cycle.setOnClickListener(clickListener);
        add_self_define.setOnClickListener(clickListener);
        start_timer = (Button) findViewById(R.id.go_timer_activity);
        start_timer.setOnClickListener(clickListener);
        action_recycler_view = (RecyclerView) findViewById(R.id.action_recycle);
        action_recycler_view.setHasFixedSize(true);
        action_recycler_view.setItemAnimator(new DefaultItemAnimator());
        action_recycler_view.setLayoutManager(new LinearLayoutManager(context));

//        Link recycler view with adapter
        mACA = new ActionRecyclerAdapter(context, action_list);
        action_recycler_view.setAdapter(mACA);

//         initializr ItemTouchHelper and connect RecycleView
        itemTouchHelper = new ItemTouchHelper(new ActionItemTouchHelperCallback(mACA));
        itemTouchHelper.attachToRecyclerView(action_recycler_view);


//        Toast.makeText(context, String.valueOf(context==this), Toast.LENGTH_SHORT).show();

    }

    //    Click for add action and to next timer page
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.addprepare:
                    action_icon = getResources().getIdentifier("prepare", "drawable", context.getPackageName());
                    a_action = new ActionComponent("Prepare", "", 10, action_icon, 0);
                    action_list.add(a_action);
                    mACA.notifyDataSetChanged();
                    action_recycler_view.smoothScrollToPosition(action_list.size() - 1);
                    break;
                case R.id.addwork:
                    action_icon = getResources().getIdentifier("work", "drawable", context.getPackageName());
                    a_action = new ActionComponent("Work", "", 20, action_icon, 0);
                    action_list.add(a_action);
                    mACA.notifyDataSetChanged();
                    action_recycler_view.smoothScrollToPosition(action_list.size() - 1);
                    break;
                case R.id.addrest:
                    action_icon = getResources().getIdentifier("rest", "drawable", context.getPackageName());
                    a_action = new ActionComponent("Rest", "", 10, action_icon, 0);
                    action_list.add(a_action);
                    mACA.notifyDataSetChanged();
                    action_recycler_view.smoothScrollToPosition(action_list.size() - 1);
                    break;
                case R.id.addcycle:
                    action_icon = getResources().getIdentifier("cycle", "drawable", context.getPackageName());
                    a_action = new ActionComponent("Repeat", "", 1, action_icon, 1);
//                    A kinder setting for user, but will messup by Recycler view
//                    if (action_list.size() < 1) a_action = new ActionComponent("Repeat", "", 1, action_icon, 0, 1);
//                    else a_action = new ActionComponent("Repeat", "", 1, action_icon, action_list.size(), 1);
                    action_list.add(a_action);
                    mACA.notifyDataSetChanged();
                    action_recycler_view.smoothScrollToPosition(action_list.size() - 1);
                    break;
                case R.id.addselfdefine:
                    action_icon = getResources().getIdentifier("clock", "drawable", context.getPackageName());
                    a_action = new ActionComponent("Custom Action", "", 30, action_icon, 2);
                    action_list.add(a_action);
                    mACA.notifyDataSetChanged();
                    action_recycler_view.smoothScrollToPosition(action_list.size() - 1);
                    break;
                case R.id.go_timer_activity:
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
//                    When haven't save
                    if (!compareActionLists(original_list, mACA.mActionList)) {
                        alertDialog.setTitle("You haven't save!");
                        alertDialog.setMessage("Please go back and save");
                        alertDialog.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();
                        break;
                    }
//
//                    When totall time = 0
                    decoded_data = decodeList(mACA.mActionList);
                    int time_array[] = new int[decoded_data.get(time_index).size()];

                    int current_time = 0;
                    for (int i = 0; i < decoded_data.get(time_index).size(); i++){
                        current_time = current_time + Integer.valueOf(decoded_data.get(time_index).get(i));
                        time_array[i] = Integer.valueOf(decoded_data.get(time_index).get(i));
                    }
//                    for (String s : decoded_data.get(time_index)){
//                        time_list.add(Integer.valueOf(s));
//                        current_time = current_time + Integer.valueOf(s);
//                    }
                    if (current_time == 0) {
                        alertDialog.setTitle("The total time of this timer is zero");
                        alertDialog.setMessage("Please go back and set again");
                        alertDialog.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();
                        break;
                    }
//                    Send decoded data
//                 Initialize Intent to jump to Timer Activity
                    Intent toTimerIntent = new Intent(ActionSettingActivity.this, TimerActivity.class);

//                Send the needed data (Total name, comment and time sequence)
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("name_sequence", (ArrayList<String>) decoded_data.get(name_index));
                    bundle.putStringArrayList("comment_sequence", (ArrayList<String>) decoded_data.get(comment_insex));
                    bundle.putIntArray("time_sequence", time_array);
                    bundle.putInt("total_time_now", current_time);
                    toTimerIntent.putExtras(bundle);
                    startActivity(toTimerIntent);

                    break;

            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.act_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.currnet_decoded_data:
//                Get current information
                decoded_data = decodeList(mACA.mActionList);
                int current_time, work_num, prepare_num, rest_num, custom_clock_num;
                current_time = work_num = prepare_num = rest_num = custom_clock_num = 0;
//                Get totalltime
                for (String s : decoded_data.get(time_index))
                    current_time = current_time + (Integer.valueOf(s));
                String HMS_now = toHMSform(current_time).get(0) + ":" + toHMSform(current_time).get(1)
                        + ":" + toHMSform(current_time).get(2);
//                Count action name number
                work_num = Collections.frequency(decoded_data.get(name_index), "Work");
                prepare_num = Collections.frequency(decoded_data.get(name_index), "Prepare");
                rest_num = Collections.frequency(decoded_data.get(name_index), "Rest");
                custom_clock_num = decoded_data.get(name_index).size() - work_num - prepare_num - rest_num;

//                Launch note
                new AlertDialog.Builder(context)
                        .setTitle("Current Setting")
                        .setMessage("Totall time: " + HMS_now + "\n"
                                + "Prepare:" + prepare_num + "\n"
                                + "Work: " + work_num + "\n"
                                + "Rest: " + prepare_num + "\n"
                                + "Custom clock: " + custom_clock_num)

                        .show();
                return true;
            case R.id.save_action_list:
                saveCurrentList();
                return true;
            case R.id.reset_timer:
                new AlertDialog.Builder(context)
                        .setMessage("Do you want to recover to the latest saving?")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        action_list.clear();
                                        action_list.addAll(original_list);
                                        mACA.notifyDataSetChanged();
                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Handle the crash leaded by null reference
        boolean one_is_null =false;

        if (mACA.mActionList.size() == 0 && original_list != null) { one_is_null=true;}
        else if (mACA.mActionList.size() > 0 && original_list == null) { one_is_null=true;}
        else if (mACA.mActionList.size() == 0 && original_list == null) { one_is_null=false;}
//        else if (!compareActionLists(mACA.mActionList, original_list)) {
        if (!compareActionLists(mACA.mActionList, original_list ) || one_is_null) {
            // HOLD ON! goback
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                new AlertDialog.Builder(context)
                        .setTitle("Are you sure to leave?")
                        .setMessage("You will lose the changed data")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void saveCurrentList() {
        int totall_t = getTotallSecond(mACA.mActionList);
        List<String> hms_form = toHMSform(totall_t);
        String str_ACs = new Gson().toJson(action_list, action_Component_type);
        trainingLists.get(training_list_index).hour = hms_form.get(0);
        trainingLists.get(training_list_index).minute = hms_form.get(1);
        trainingLists.get(training_list_index).second = hms_form.get(2);
        String str_TLs = new Gson().toJson(trainingLists, training_list_type);
        original_list = (ArrayList<ActionComponent>) mACA.mActionList.clone();
        preferences.edit().putString("ORIGINAL_LIST", str_ACs).apply();
        preferences.edit().putString("TRAINING_LIST", str_TLs).apply();
        preferences.edit().putString("ACL_" + String.valueOf(training_list_index), str_ACs).apply();
        final Toast toast = new Toast(getApplicationContext());
        Toast.makeText(context, "List saved!", Toast.LENGTH_SHORT).show();
    }

    public boolean compareActionLists(ArrayList<ActionComponent> a_list1,
                                      ArrayList<ActionComponent> a_list2) {
        if (a_list1 == null) a_list1 = new ArrayList<ActionComponent>();
        if (a_list2 == null) a_list2 = new ArrayList<ActionComponent>();
        else if (a_list1.size() != a_list2.size()) return false;
        for (int i = 0; i < a_list1.size(); i++) {
            if (!a_list1.get(i).equals(a_list2.get(i))) return false;
        }
        return true;
    }

    public static ArrayList<List<String>> decodeList(ArrayList<ActionComponent> current_list) {
        ArrayList<List<String>> decoded_data = new ArrayList<List<String>>();

        List<String> decoded_name = new ArrayList<String>();
        List<String> decoded_time = new ArrayList<String>();
        List<String> decoded_comment = new ArrayList<String>();
//        Loop the action list
        for (int i = 0; i < current_list.size(); i++) {
//            Deal the repeat action
            if (current_list.get(i).getItemType() == TYPE_REPEAT) {
//                Deal the input equals 0
                if (current_list.get(i).getItemTime() == 0) {
                } else {
                    List<String> temp_name = new ArrayList<String>(decoded_name);
                    List<String> temp_time = new ArrayList<String>(decoded_time);
                    List<String> temp_comment = new ArrayList<String>(decoded_comment);
                    for (int repeat_count = 0; repeat_count < current_list.get(i).getItemTime(); repeat_count++) {
                        decoded_name.addAll(temp_name);
                        decoded_time.addAll(temp_time);
                        decoded_comment.addAll(temp_comment);
                    }
                }
            } // End of deal repeat action
//            The item is not repeat
            else {
                if (current_list.get(i).getItemTime() != 0) {
                    decoded_name.add(current_list.get(i).getItemName());
                    decoded_time.add(String.valueOf(current_list.get(i).getItemTime()));
                    decoded_comment.add(current_list.get(i).getItemComment());
                }
            }
        }
        decoded_data.add(decoded_name);
        decoded_data.add(decoded_comment);
        decoded_data.add(decoded_time);
        return decoded_data;

    }


    public static int getTotallSecond(ArrayList<ActionComponent> current_list) {
        int totall_time = 0;

        for (int i = 0; i < current_list.size(); i++) {
            if (current_list.get(i).name.equals("Repeat"))
                totall_time = totall_time * (current_list.get(i).time + 1);
            else
                totall_time = totall_time + current_list.get(i).time;
        }
        return totall_time;
    }

    public static List<String> toHMSform(int totall_time) {
        List<String> HMSform = new ArrayList<String>();
        int h, m, s;
        h = totall_time / 3600;
        m = totall_time / 60 % 60;
        s = totall_time % 60;
        HMSform.add(String.valueOf(h));
        HMSform.add(String.valueOf(m));
        HMSform.add(String.valueOf(s));
        return HMSform;
    }

}
