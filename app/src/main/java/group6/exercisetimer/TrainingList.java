package group6.exercisetimer;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class TrainingList implements Cloneable, Parcelable {

    public String name;
    public String hour;
    public String minute;
    public String second;


    public TrainingList() {
        this.name = "Training List";
        this.hour = "0";
        this.minute = "0";
        this.second = "0";

    }

    public TrainingList(String name, String hour, String minute, String second) {
        this.name = name;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public String getItemName() {
        return this.name;
    }

    public String getItemTime() {
        return this.hour + ":" + this.minute + ":" + this.second;
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    protected TrainingList(Parcel in) {
        name = in.readString();
        hour = in.readString();
        minute = in.readString();
        second = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(hour);
        dest.writeString(minute);
        dest.writeString(second);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TrainingList> CREATOR = new Parcelable.Creator<TrainingList>() {
        @Override
        public TrainingList createFromParcel(Parcel in) {
            return new TrainingList(in);
        }

        @Override
        public TrainingList[] newArray(int size) {
            return new TrainingList[size];
        }
    };
}