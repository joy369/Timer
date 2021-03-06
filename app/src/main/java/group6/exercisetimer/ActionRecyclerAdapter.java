package group6.exercisetimer;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;


public class ActionRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemMoveSwipeListener {
    public ArrayList<ActionComponent> mActionList;
    public Context context;
    private static final int TYPE_DEFAULT_ACT = 0;
    private static final int TYPE_REPEAT = 1;
    private static final int TYPE_CUSTOM = 2;

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mActionList, fromPosition, toPosition);

        notifyItemMoved(fromPosition, toPosition);
//        IMPORTANT to call ! OR get WRONG position later
        notifyItemChanged(fromPosition);
        notifyItemChanged(toPosition);

        return true;
    }

    @Override
    public void onItemSwipe(int position) {
        mActionList.remove(position);
        notifyItemRemoved(position);
//        IMPORTANT to call ! OR get WRONG position later
        notifyItemRangeChanged(position, getItemCount());
    }

    public class UnediableViewHolder extends RecyclerView.ViewHolder {
        TextView title, action_unit, counter;
        EditText short_comment, second;
        ImageView action_figue;

        public UnediableViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.d_action_name);
            counter = (TextView) itemView.findViewById(R.id.d_action_counter);
            action_unit = (TextView) itemView.findViewById(R.id.d_action_unit);
            short_comment = (EditText) itemView.findViewById(R.id.d_action_comment);
            second = (EditText) itemView.findViewById(R.id.d_action_time);
            action_figue = (ImageView) itemView.findViewById(R.id.d_action_image);
        }
    }

    public class CycleViewHolder extends RecyclerView.ViewHolder {
        TextView title, action_unit, counter;
        EditText short_comment, second, item_num;
        ImageView action_figue;

        public CycleViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.r_action_name);
            counter = (TextView) itemView.findViewById(R.id.r_action_counter);
            action_unit = (TextView) itemView.findViewById(R.id.r_action_unit);
            short_comment = (EditText) itemView.findViewById(R.id.r_action_comment);
            second = (EditText) itemView.findViewById(R.id.r_action_time);
//            item_num = (EditText) itemView.findViewById(R.id.r_item_num);
            action_figue = (ImageView) itemView.findViewById(R.id.r_action_image);
        }
    }

    public class EditableViewHolder extends RecyclerView.ViewHolder {
        TextView action_unit, counter;
        EditText custom_name, short_comment, second;
        ImageView action_figue;

        public EditableViewHolder(View itemView) {
            super(itemView);
            custom_name = (EditText) itemView.findViewById(R.id.e_action_name);
            counter = (TextView) itemView.findViewById(R.id.e_action_counter);
            action_unit = (TextView) itemView.findViewById(R.id.e_action_unit);
            short_comment = (EditText) itemView.findViewById(R.id.e_action_comment);
            second = (EditText) itemView.findViewById(R.id.e_action_time);
            action_figue = (ImageView) itemView.findViewById(R.id.e_action_image);
        }
    }

    public ActionRecyclerAdapter(ArrayList<ActionComponent> data) {
        mActionList = data;
    }

    public ActionRecyclerAdapter(Context context, ArrayList<ActionComponent> data) {
        mActionList = data;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return mActionList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mActionList.get(position).getItemType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_DEFAULT_ACT:
                View unedit_itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.default_action, parent, false);
                UnediableViewHolder unedit_viewHolder = new UnediableViewHolder(unedit_itemView);
                return unedit_viewHolder;
            case TYPE_REPEAT:
                View cycle_itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cycle_action, parent, false);
                CycleViewHolder cycle_viewHolder = new CycleViewHolder(cycle_itemView);
                return cycle_viewHolder;

            case TYPE_CUSTOM:
                View edit_itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.custom_action, parent, false);
                EditableViewHolder edit_viewHolder = new EditableViewHolder(edit_itemView);
                return edit_viewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ActionComponent a_item = mActionList.get(position);
        switch (holder.getItemViewType()) {
            case TYPE_DEFAULT_ACT:
//                setIsRecyclable is one way to handle wrong position, but might not be helpful here
//                ((UnediableViewHolder) holder).setIsRecyclable(false);
                ((UnediableViewHolder) holder).title.setText(a_item.getItemName());
                ((UnediableViewHolder) holder).counter.setText(String.valueOf(position+1));
                ((UnediableViewHolder) holder).action_unit.setText("Second");
                ((UnediableViewHolder) holder).action_figue.setImageResource(a_item.getItemFigureID());

//                To make text inside the item appear properly.
//                TextWatcher for second
                if (((UnediableViewHolder) holder) instanceof UnediableViewHolder) {
                    if (((UnediableViewHolder) holder).second.getTag() instanceof TextWatcher) {
                        ((UnediableViewHolder) holder).second
                                .removeTextChangedListener((TextWatcher) ((UnediableViewHolder) holder).second.getTag());
                    }
                }
                ((UnediableViewHolder) holder).second.setText(Integer.toString(a_item.getItemTime()));
                TextWatcher watcher_second_0 = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (TextUtils.isEmpty(s.toString())) {
//                            ((UnediableViewHolder) holder).second.setText("0");
                            mActionList.get(position).time = Integer.parseInt("0");
                        } else mActionList.get(position).time = (int) Float.parseFloat(s.toString());
                    }
                };
                ((UnediableViewHolder) holder).second.addTextChangedListener(watcher_second_0);
                ((UnediableViewHolder) holder).second.setTag(watcher_second_0);

//                TextWatcher for short_comment
                if (((UnediableViewHolder) holder) instanceof UnediableViewHolder) {
                    if (((UnediableViewHolder) holder).short_comment.getTag() instanceof TextWatcher) {
                        ((UnediableViewHolder) holder).short_comment
                                .removeTextChangedListener((TextWatcher) ((UnediableViewHolder) holder).short_comment.getTag());
                    }
                }
                ((UnediableViewHolder) holder).short_comment.setText(a_item.getItemComment());
                TextWatcher watcher_short_comment_0 = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        mActionList.get(position).comment = s.toString();

                    }

                };
                ((UnediableViewHolder) holder).short_comment.addTextChangedListener(watcher_short_comment_0);
                ((UnediableViewHolder) holder).short_comment.setTag(watcher_short_comment_0);
                break;

            case TYPE_REPEAT:
//                setIsRecyclable is one way to handle wrong position, but might not be helpful here
//                ((CycleViewHolder) holder).setIsRecyclable(false);
                ((CycleViewHolder) holder).title.setText(a_item.getItemName());
                ((CycleViewHolder) holder).counter.setText(String.valueOf(position+1));
                ((CycleViewHolder) holder).action_unit.setText("time(s)");
                ((CycleViewHolder) holder).action_figue.setImageResource(a_item.getItemFigureID());
                //                To make text inside the item appear properly.
//                TextWatcher for second
                if (((CycleViewHolder) holder) instanceof CycleViewHolder) {
                    if (((CycleViewHolder) holder).second.getTag() instanceof TextWatcher) {
                        ((CycleViewHolder) holder).second
                                .removeTextChangedListener((TextWatcher) ((CycleViewHolder) holder).second.getTag());
                    }
                }
                ((CycleViewHolder) holder).second.setText(Integer.toString(a_item.getItemTime()));
                TextWatcher watcher_second_1 = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (TextUtils.isEmpty(s.toString())) {
//                            ((CycleViewHolder) holder).second.setText("");
                            mActionList.get(position).time = Integer.parseInt("0");
                        } else mActionList.get(position).time = (int)Float.parseFloat(s.toString());
                    }
                };
                ((CycleViewHolder) holder).second.addTextChangedListener(watcher_second_1);
                ((CycleViewHolder) holder).second.setTag(watcher_second_1);

//                TextWatcher for short_comment
                if (((CycleViewHolder) holder) instanceof CycleViewHolder) {
                    if (((CycleViewHolder) holder).short_comment.getTag() instanceof TextWatcher) {
                        ((CycleViewHolder) holder).short_comment
                                .removeTextChangedListener((TextWatcher) ((CycleViewHolder) holder).short_comment.getTag());
                    }
                }
                ((CycleViewHolder) holder).short_comment.setText(a_item.getItemComment());
                TextWatcher watcher_short_comment_1 = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        mActionList.get(position).comment = s.toString();
                    }
                };
                ((CycleViewHolder) holder).short_comment.addTextChangedListener(watcher_short_comment_1);
                ((CycleViewHolder) holder).short_comment.setTag(watcher_short_comment_1);

////                TextWatcher for item_num
//                if (((CycleViewHolder) holder) instanceof CycleViewHolder) {
//                    if (((CycleViewHolder) holder).item_num.getTag() instanceof TextWatcher) {
//                        ((CycleViewHolder) holder).item_num
//                                .removeTextChangedListener((TextWatcher) ((CycleViewHolder) holder).item_num.getTag());
//                    }
//                }
//                ((CycleViewHolder) holder).item_num.setText(Integer.toString(a_item.getItemItemNum()));
////                Set range of item num
////                ((CycleViewHolder) holder).item_num.setFilters(
////                        new InputFilter[]{new MinMaxFilter("0", String.valueOf(position))});
//                TextWatcher watcher_item_num_1 = new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//                        if (TextUtils.isEmpty(s.toString())) {
////                            ((CycleViewHolder) holder).item_num.setText("0");
//                            mActionList.get(position).item_num = Integer.parseInt("0");
//                        } else mActionList.get(position).item_num = Integer.parseInt(s.toString());
//
//                    }
//                };
//                ((CycleViewHolder) holder).item_num.addTextChangedListener(watcher_item_num_1);
//                ((CycleViewHolder) holder).item_num.setTag(watcher_item_num_1);
                break;

            case TYPE_CUSTOM:
//                setIsRecyclable is one way to handle wrong position, but might not be helpful here
//                ((EditableViewHolder) holder).setIsRecyclable(false);
                ((EditableViewHolder) holder).counter.setText(String.valueOf(position+1));
                ((EditableViewHolder) holder).action_figue.setImageResource(a_item.getItemFigureID());
                ((EditableViewHolder) holder).action_unit.setText("Second");
//                TextWatcher for custom name
                if (((EditableViewHolder) holder) instanceof EditableViewHolder) {
                    if (((EditableViewHolder) holder).custom_name.getTag() instanceof TextWatcher) {
                        ((EditableViewHolder) holder).custom_name
                                .removeTextChangedListener((TextWatcher) ((EditableViewHolder) holder).custom_name.getTag());
                    }
                }
                ((EditableViewHolder) holder).custom_name.setText(a_item.getItemName());
                TextWatcher watcher_custom_name_2 = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        mActionList.get(position).name = s.toString();
                    }
                };
                ((EditableViewHolder) holder).custom_name.addTextChangedListener(watcher_custom_name_2);
                ((EditableViewHolder) holder).custom_name.setTag(watcher_custom_name_2);

//                To make text inside the item appear properly.
//                TextWatcher for second
                if (((EditableViewHolder) holder) instanceof EditableViewHolder) {
                    if (((EditableViewHolder) holder).second.getTag() instanceof TextWatcher) {
                        ((EditableViewHolder) holder).second
                                .removeTextChangedListener((TextWatcher) ((EditableViewHolder) holder).second.getTag());
                    }
                }
                ((EditableViewHolder) holder).second.setText(Integer.toString(a_item.getItemTime()));
                TextWatcher watcher_second_2 = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (TextUtils.isEmpty(s.toString())) {
//                            ((EditableViewHolder) holder).second.setText("0");
                            mActionList.get(position).time = Integer.parseInt("0");
                        } else mActionList.get(position).time = (int) Float.parseFloat(s.toString());
                    }
                };
                ((EditableViewHolder) holder).second.addTextChangedListener(watcher_second_2);
                ((EditableViewHolder) holder).second.setTag(watcher_second_2);

//                TextWatcher for short_comment
                if (((EditableViewHolder) holder) instanceof EditableViewHolder) {
                    if (((EditableViewHolder) holder).short_comment.getTag() instanceof TextWatcher) {
                        ((EditableViewHolder) holder).short_comment
                                .removeTextChangedListener((TextWatcher) ((EditableViewHolder) holder).short_comment.getTag());
                    }
                }
                ((EditableViewHolder) holder).short_comment.setText(a_item.getItemComment());
                TextWatcher EditableViewHolder = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        mActionList.get(position).comment = s.toString();
                    }
                };
                ((EditableViewHolder) holder).short_comment.addTextChangedListener(EditableViewHolder);
                ((EditableViewHolder) holder).short_comment.setTag(EditableViewHolder);

                break;
        }
    }


}
