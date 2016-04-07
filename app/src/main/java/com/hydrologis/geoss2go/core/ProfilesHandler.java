package com.hydrologis.geoss2go.core;

import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrea Antonello
 */
public enum ProfilesHandler {
    INSTANCE;

    public static final String KEY_PROFILES_PREFERENCES = "KEY_PROFILES_PREFERENCES";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String CREATIONDATE = "creationdate";
    public static final String AUTHOR = "author";

    public List<Profile> getProfilesFromPreferences(SharedPreferences preferences) throws JSONException {
        String profilesJson = preferences.getString(KEY_PROFILES_PREFERENCES, "");
        List<Profile> profilesList = new ArrayList<>();

        if (profilesJson.trim().length() == 0)
            return profilesList;

        JSONObject root = new JSONObject(profilesJson);
        JSONArray profilesArray = root.getJSONArray("profiles");
        for (int i = 0; i < profilesArray.length(); i++) {
            JSONObject profileObject = profilesArray.getJSONObject(i);
            Profile profile = new Profile();
            if (profileObject.has(NAME))
                profile.name = profileObject.getString(NAME);
            if (profileObject.has(DESCRIPTION)) {
                profile.description = profileObject.getString(DESCRIPTION);
            }
            if (profileObject.has(CREATIONDATE)) {
                profile.creationdate = profileObject.getString(CREATIONDATE);
            }
            if (profileObject.has(AUTHOR)) {
                profile.author = profileObject.getString(AUTHOR);
            }
            profilesList.add(profile);
        }
        return profilesList;
    }


}
