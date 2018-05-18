package group6.exercisetimer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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

//         new 1個ItemTouchHelper並與RecycleView做關聯
        itemTouchHelper = new ItemTouchHelper(new ActionItemTouchHelperCallback(mACA));
        itemTouchHelper.attachToRecyclerView(action_recycler_view);


//        Toast.makeText(context, String.valueOf(context==this), Toast.LENGTH_SHORT).show();

    }

    //    Click for add
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.go_timer_activity:
//                    Toast.makeText(context, String.valueOf(totall_t), Toast.LENGTH_SHORT).show();
                    break;
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
                    a_action = new ActionComponent("Repeat", "", 1, action_icon, 0, 1);
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

                return true;
            case R.id.save_action_list:
                saveCurrentList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!compareActionLists(mACA.mActionList, original_list)) {
            if (keyCode == KeyEvent.KEYCODE_BACK) { // Let goback HOLD ON
                new AlertDialog.Builder(context)
                        .setTitle("You haven't saved!")
                        .setMessage("Do you want to save the current setting?")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        saveCurrentList();
                                        finish();
                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                }).show();
            }
        } else finish();
        return true;
    }

    public void saveCurrentList() {
        int totall_t = getHMS(mACA.mActionList);
        ArrayList<String> hms_form = toHMSform(totall_t);
        String str_ACs = new Gson().toJson(action_list, action_Component_type);
        trainingLists.get(training_list_index).hour = hms_form.get(0);
        trainingLists.get(training_list_index).minute = hms_form.get(1);
        trainingLists.get(training_list_index).second = hms_form.get(2);
        String str_TLs = new Gson().toJson(trainingLists, training_list_type);
        preferences.edit().putString("TRAINING_LIST", str_TLs).apply();
        preferences.edit().putString("ACL_" + String.valueOf(training_list_index), str_ACs).apply();
        Toast.makeText(context, "List saved!", Toast.LENGTH_SHORT).show();
    }

    public boolean compareActionLists(ArrayList<ActionComponent> a_list1,
                                      ArrayList<ActionComponent> a_list2) {
        if (a_list1.size() != a_list2.size()) return false;
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
        for (int i = 0; i < current_list.size(); i++) {
//            Deal the repeat component
            if (current_list.get(i).getItemType() == TYPE_REPEAT) {

            } else {
                decoded_name.add(current_list.get(i).getItemName());
                decoded_time.add(String.valueOf(current_list.get(i).getItemTime()));
                decoded_time.add(current_list.get(i).getItemComment());
            }
        }
        decoded_data.add(decoded_name);
        decoded_data.add(decoded_time);
        decoded_data.add(decoded_comment);
        return decoded_data;
    }

    public static int getHMS(ArrayList<ActionComponent> current_list) {
        int totall_time = 0;

        for (int i = 0; i < current_list.size(); i++) {
            if (current_list.get(i).name.equals("Repeat"))
                totall_time = totall_time * (current_list.get(i).time + 1);
            else
                totall_time = totall_time + current_list.get(i).time;
        }
        return totall_time;
    }

    public static ArrayList<String> toHMSform(int totall_time) {
        ArrayList<String> HMSform = new ArrayList<String>();
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
