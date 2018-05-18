package group6.exercisetimer;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class ActionComponent implements Parcelable{
    public String name;
    public String comment;
    public int time;
    public int figure_ID;
    public int type_ID;
    public int item_num;

    public ActionComponent() {
        this.name = "Action";
        this.comment = "Add description";
        this.time = 0;
        this.figure_ID= R.mipmap.ic_launcher;
        this.type_ID = 0;
        this.item_num = 0;
    }

    public ActionComponent(String name, String comment, int time, int figure_ID, int type_ID) {
        this.name = name;
        this.comment = comment;
        this.time = time;
        this.figure_ID = figure_ID;
        this.type_ID = type_ID;
        this.item_num = 0;
    }

    public ActionComponent(String name, String comment, int time, int figure_ID, int item_num, int type_ID) {
        this.name = name;
        this.comment = comment;
        this.time = time;
        this.figure_ID = figure_ID;
        this.type_ID = type_ID;
        this.item_num =item_num;
    }

    public String getItemName() {
        return this.name;
    }

    public String getItemComment() {
        return this.comment;
    }

    public int getItemTime() {
        return this.time;
    }

    public int getItemFigureID() {
        return this.figure_ID;
    }

    public int getItemType() {
        return this.type_ID;
    }

    public int getItemItemNum(){return this.item_num;}

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ActionComponent)) {
            return false;
        }

        ActionComponent action_2 = (ActionComponent) o;
        return action_2.name.equals(name) && action_2.comment.equals(comment)
                && action_2.time == time && action_2.figure_ID ==figure_ID
                && action_2.item_num == item_num;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    protected ActionComponent(Parcel in) {
        name = in.readString();
        comment = in.readString();
        time = in.readInt();
        figure_ID = in.readInt();
        type_ID = in.readInt();
        item_num = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(comment);
        dest.writeInt(time);
        dest.writeInt(figure_ID);
        dest.writeInt(type_ID);
        dest.writeInt(item_num);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ActionComponent> CREATOR = new Parcelable.Creator<ActionComponent>() {
        @Override
        public ActionComponent createFromParcel(Parcel in) {
            return new ActionComponent(in);
        }

        @Override
        public ActionComponent[] newArray(int size) {
            return new ActionComponent[size];
        }
    };
}