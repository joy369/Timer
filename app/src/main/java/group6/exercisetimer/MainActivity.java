package group6.exercisetimer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.support.design.widget.FloatingActionButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends AppCompatActivity {
    private ArrayList<TrainingList> init_list = new ArrayList<TrainingList>();
    private ArrayList<TrainingList> loaded_list;
    private Context context = this;
    private ListView training_list_view;
    private FloatingActionButton add_list_button;
    private TrainingListAdapter mTLA;
    private SharedPreferences preferences;
    public static Type training_list_type = new TypeToken<List<TrainingList>>() {
    }.getType();
    public static Type action_Component_type = new TypeToken<List<ActionComponent>>() {
    }.getType();

    //    Refresh layout when setup a list
    @Override
    public void onRestart() {
        super.onRestart();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        loaded_list = new Gson().fromJson(preferences.getString("TRAINING_LIST", ""), training_list_type);
        if (loaded_list != null) init_list = (ArrayList<TrainingList>) loaded_list.clone();
        mTLA = new TrainingListAdapter(this, init_list);
        training_list_view.setAdapter(mTLA);
        mTLA.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Initialize View, which are training list view, FA button
        training_list_view = (ListView) findViewById(R.id.training_list);
        add_list_button = (FloatingActionButton) findViewById(R.id.floatingActionButton1);

//        Initialize data loader(preferences), and Load previous training lists
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        loaded_list = new Gson().fromJson(preferences.getString("TRAINING_LIST", ""), training_list_type);

        if (loaded_list != null) {
            init_list = (ArrayList<TrainingList>) loaded_list.clone();
        }
//        Initialize training list adapter
        mTLA = new TrainingListAdapter(this, init_list);
        training_list_view.setAdapter(mTLA);

//        Function for add new item (training list) by FA button
        add_list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Add new Items to List
                String new_list_name = "Training List" + Integer.toString(init_list.size() + 1);
                TrainingList new_list = new TrainingList(new_list_name, "0", "0", "0");
                init_list.add(new_list);
                mTLA.notifyDataSetChanged();

//                Save current setting
                String str_TLs = new Gson().toJson(init_list, training_list_type);
                preferences.edit().putString("TRAINING_LIST", str_TLs).apply();
            }
        });

//        Function for access to item (a training list)
        training_list_view.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                Toast.makeText(MainActivity.this, "The ith item i = " + position + "l = " + id, Toast.LENGTH_SHORT).show();

//                 Initialize Intent to jump to Action Setting Activity
                Intent toActionSettingIntent = new Intent(MainActivity.this, ActionSettingActivity.class);

//                Send the needed data (Total training list and the clicked item/a specific training list)
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("bundled_TLs", init_list);
                bundle.putInt("current_item_index", position);
                toActionSettingIntent.putExtras(bundle);
                startActivity(toActionSettingIntent);
            }
        });
    }
}
