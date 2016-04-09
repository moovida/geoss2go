package com.hydrologis.geoss2go.core;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrea Antonello
 */
public class Profile implements Parcelable {
    public String name = "new profile";
    public String description = "new profile description";
    public String creationdate = "";
    public boolean active = false;
    public String color = "#FFFFFF";
    public String tagsPath = "";
    public List<String> basemapsList = new ArrayList<>();
    public List<String> spatialiteList = new ArrayList<>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Profile)) {
            return false;
        }
        Profile p = (Profile) o;
        if (p.name != null && name != null && !p.name.equals(name)) {
            return false;
        }
        if (p.description != null && description != null && !p.description.equals(description)) {
            return false;
        }
        if (p.creationdate != null && creationdate != null && !p.creationdate.equals(creationdate)) {
            return false;
        }
        return true;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(creationdate);
        dest.writeBooleanArray(new boolean[]{active});
        dest.writeString(color);
        dest.writeString(tagsPath);
        dest.writeList(basemapsList);
        dest.writeList(spatialiteList);
    }


    @SuppressWarnings("javadoc")
    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @SuppressWarnings("unchecked")
        public Profile createFromParcel(Parcel in) {
            Profile profile = new Profile();
            profile.name = in.readString();
            profile.description = in.readString();
            profile.creationdate = in.readString();

            boolean[] activeArray = new boolean[1];
            in.readBooleanArray(activeArray);
            profile.active = activeArray[0];

            profile.color = in.readString();
            profile.tagsPath = in.readString();
            profile.basemapsList = in.readArrayList(String.class.getClassLoader());
            profile.spatialiteList = in.readArrayList(String.class.getClassLoader());

            return profile;
        }

        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };
}
