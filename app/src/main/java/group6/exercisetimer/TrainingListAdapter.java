package group6.exercisetimer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static group6.exercisetimer.MainActivity.action_Component_type;
import static group6.exercisetimer.MainActivity.training_list_type;

public class TrainingListAdapter extends BaseAdapter implements View.OnClickListener {

    private Context context;
    private LayoutInflater li;
    private ArrayList<TrainingList> data;
    private SharedPreferences preferences;
    private ArrayList<TrainingList> loaded_list;
    private ArrayList<ActionComponent> loaded_action_list;

    public TrainingListAdapter(Context context, ArrayList<TrainingList> data) {
        this.context = context;
        this.data = data;
        this.li = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        Object obj = data.get(i);
        return data.indexOf(obj);
    }

    @Override
    public void onClick(View view) {
        final int TL_item_position_now = (int) view.getTag(R.id.TL_dots);
        switch (view.getId()) {
            case R.id.TL_dots:
                PopupMenu popup = new PopupMenu(context, view);
                popup.inflate(R.menu.tl_menu);
                popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (R.id.TL_delete == (item.getItemId())) {
                            notifyDataSetChanged();
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Are you sure to delete this list?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
//                                    Load training lists to delete item and then save again
                                    preferences = PreferenceManager.getDefaultSharedPreferences(context);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    loaded_list = new Gson().fromJson(preferences.getString("TRAINING_LIST", ""), training_list_type);

                                    if (loaded_list.size() == 1 || TL_item_position_now == (loaded_list.size() - 1)) {
//                                        Delete training list, save and Change the layout
                                        data.remove(TL_item_position_now);
                                        String str_TLs = new Gson().toJson(data, training_list_type);
                                        preferences.edit().putString("TRAINING_LIST", str_TLs).apply();
                                        notifyDataSetChanged();
//                                        Delete action components of this training list
                                        editor.remove("ACL_" + Integer.toString(TL_item_position_now));
                                        editor.apply();
                                    } else {
//                                        When delete, we have to move every list below the deleted list 1 position
                                        for (int i = TL_item_position_now; i < loaded_list.size() - 1; i++) {
                                            loaded_action_list = new Gson().fromJson(preferences.getString("ACL_" + Integer.toString(TL_item_position_now + 1), ""), action_Component_type);
                                            String str_ACs = new Gson().toJson(loaded_action_list, action_Component_type);
                                            preferences.edit().putString("ACL_" + Integer.toString(i), str_ACs).apply();
                                        }
//                                        Delete training list, save and Change the layout
                                        data.remove(TL_item_position_now);
                                        String str_TLs = new Gson().toJson(data, training_list_type);
                                        preferences.edit().putString("TRAINING_LIST", str_TLs).apply();
                                        notifyDataSetChanged();
                                        editor.remove("ACL_" + Integer.toString(loaded_list.size() - 1));
                                        editor.apply();
                                    }
                                }

                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                            builder.show();
                        } else if (R.id.TL_copy == (item.getItemId())) {
                            TrainingList temp_copy = (TrainingList) new TrainingList();
                            try {
                                temp_copy = (TrainingList) data.get(TL_item_position_now).clone();
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                            temp_copy.name = data.get(TL_item_position_now).name + " (Copy)";
//                            Load training lists to add item and then save again
                            preferences = PreferenceManager.getDefaultSharedPreferences(context);
                            loaded_list = new Gson().fromJson(preferences.getString("TRAINING_LIST", ""), training_list_type);
                            loaded_action_list = new Gson().fromJson(preferences.getString("ACL_" + Integer.toString(TL_item_position_now), ""), action_Component_type);
                            loaded_list.add(temp_copy);
                            String str_TLs = new Gson().toJson(loaded_list, training_list_type);
                            String str_ACs = new Gson().toJson(loaded_action_list, action_Component_type);
                            preferences.edit().putString("TRAINING_LIST", str_TLs).apply();
                            preferences.edit().putString("ACL_" + Integer.toString(loaded_list.size() - 1), str_ACs).apply();
//                            Change the layout
                            data.add(temp_copy);
                            notifyDataSetChanged();
                        } else if (R.id.TL_rename == (item.getItemId())) {

                            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
                            View mView = layoutInflaterAndroid.inflate(R.layout.rename_input_dialog_box, null);
                            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
                            alertDialogBuilderUserInput.setView(mView);

                            final EditText rename_edittext = (EditText) mView.findViewById(R.id.rename_edittext);
                            alertDialogBuilderUserInput
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialogBox, int id) {
//                                            Load training lists to rename item and then save again.
                                            preferences = PreferenceManager.getDefaultSharedPreferences(context);
                                            loaded_list = new Gson().fromJson(preferences.getString("TRAINING_LIST", ""), training_list_type);
                                            loaded_list.get(TL_item_position_now).name = rename_edittext.getText().toString();
                                            String str_TLs = new Gson().toJson(loaded_list, training_list_type);
                                            preferences.edit().putString("TRAINING_LIST", str_TLs).apply();
//                                            Change the layout
                                            data.get(TL_item_position_now).name = rename_edittext.getText().toString();
                                            notifyDataSetChanged();
                                        }
                                    })

                                    .setNegativeButton("Cancel",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialogBox, int id) {
                                                    dialogBox.cancel();
                                                }
                                            });
                            AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                            alertDialogAndroid.show();
                        }
                        return true;
                    }
                });
                popup.show();
                break;
        }
    }

    private static class ViewHolder {
        TextView h_name;
        TextView h_time;
        ImageView h_dots;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = li.inflate(R.layout.training_list, viewGroup, false);
            holder = new ViewHolder();
            holder.h_name = (TextView) view.findViewById(R.id.TL_name);
            holder.h_time = (TextView) view.findViewById(R.id.TL_time);
            holder.h_dots = (ImageView) view.findViewById(R.id.TL_dots);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        String item_name = data.get(i).getItemName();
        String item_time = data.get(i).getItemTime();
        holder.h_name.setText(item_name);
        holder.h_time.setText(item_time);
        holder.h_dots.setOnClickListener(this);
        holder.h_dots.setTag(R.id.TL_dots, i);
        return view;
    }

}
