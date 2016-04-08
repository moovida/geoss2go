package com.hydrologis.geoss2go.core;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

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
    public static final String COLOR = "color";
    public static final String ACTIVE = "active";
    public static final String TAGS_PATH = "tagsPath";
    public static final String BASEMAPS = "basemaps";
    public static final String PATH = "path";
    public static final String SPATIALITE = "spatialitedbs";
    public static final String PROFILES = "profiles";

    /**
     * Get the list of stored profiles from the preferences.
     *
     * @param preferences the preferences object.
     * @return the list of Profile objects.
     * @throws JSONException
     */
    public List<Profile> getProfilesFromPreferences(SharedPreferences preferences) throws JSONException {
        String profilesJson = preferences.getString(KEY_PROFILES_PREFERENCES, "");
        return getProfilesFromJson(profilesJson);
    }

    /**
     * Save the list of profiles to preferences.
     *
     * @param preferences the preferences object.
     * @return the list of Profile objects.
     * @throws JSONException
     */
    public void saveProfilesToPreferences(SharedPreferences preferences, List<Profile> profilesList) throws JSONException {
        String jsonProfilesString = getJsonFromProfilesList(profilesList, false);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_PROFILES_PREFERENCES, jsonProfilesString);
        editor.apply();
    }

    /**
     * Get the list of profiles from a json text.
     *
     * @param profilesJson the json text.
     * @return the list of Profile objects.
     * @throws JSONException
     */
    @NonNull
    private List<Profile> getProfilesFromJson(String profilesJson) throws JSONException {
        List<Profile> profilesList = new ArrayList<>();

        if (profilesJson.trim().length() == 0)
            return profilesList;

        JSONObject root = new JSONObject(profilesJson);
        JSONArray profilesArray = root.getJSONArray(PROFILES);
        for (int i = 0; i < profilesArray.length(); i++) {
            JSONObject profileObject = profilesArray.getJSONObject(i);
            Profile profile = new Profile();
            if (profileObject.has(NAME)) {
                profile.name = profileObject.getString(NAME);
            }
            if (profileObject.has(DESCRIPTION)) {
                profile.description = profileObject.getString(DESCRIPTION);
            }
            if (profileObject.has(CREATIONDATE)) {
                profile.creationdate = profileObject.getString(CREATIONDATE);
            }
            if (profileObject.has(COLOR)) {
                profile.color = profileObject.getString(COLOR);
            }
            if (profileObject.has(ACTIVE)) {
                profile.active = profileObject.getBoolean(ACTIVE);
            }
            if (profileObject.has(TAGS_PATH)) {
                profile.tagsPath = profileObject.getString(TAGS_PATH);
            }
            if (profileObject.has(BASEMAPS)) {
                JSONArray basemapsArray = profileObject.getJSONArray(BASEMAPS);
                for (int j = 0; j < basemapsArray.length(); j++) {
                    JSONObject baseMapObject = basemapsArray.getJSONObject(j);
                    if (baseMapObject.has(PATH)) {
                        String path = baseMapObject.getString(PATH);
                        profile.basemapsList.add(path);
                    }
                }
            }
            if (profileObject.has(SPATIALITE)) {
                JSONArray spatialiteDbsArray = profileObject.getJSONArray(SPATIALITE);
                for (int j = 0; j < spatialiteDbsArray.length(); j++) {
                    JSONObject spatialiteDbsObject = spatialiteDbsArray.getJSONObject(j);
                    if (spatialiteDbsObject.has(PATH)) {
                        String path = spatialiteDbsObject.getString(PATH);
                        profile.spatialiteList.add(path);
                    }
                }
            }
            profilesList.add(profile);
        }
        return profilesList;
    }


    /**
     * Get the json text from a list of profiles.
     *
     * @param profilesList the list of profiles.
     * @param indent       optional json indenting flag.
     * @return the json string.
     * @throws JSONException
     */
    public String getJsonFromProfilesList(List<Profile> profilesList, boolean indent) throws JSONException {
        JSONObject root = new JSONObject();
        JSONArray profilesArray = new JSONArray();
        root.put(PROFILES, profilesArray);

        for (Profile profile : profilesList) {
            JSONObject profileObject = new JSONObject();

            profileObject.put(NAME, profile.name);
            profileObject.put(DESCRIPTION, profile.description);
            profileObject.put(CREATIONDATE, profile.creationdate);
            profileObject.put(COLOR, profile.color);
            profileObject.put(ACTIVE, profile.active);
            profileObject.put(TAGS_PATH, profile.tagsPath);

            if (profile.basemapsList.size() > 0) {
                JSONArray basemapsArray = new JSONArray();
                for (int j = 0; j < profile.basemapsList.size(); j++) {
                    JSONObject baseMapObject = new JSONObject();
                    baseMapObject.put(PATH, profile.basemapsList.get(j));
                    basemapsArray.put(j, baseMapObject);
                }
                profileObject.put(BASEMAPS, basemapsArray);
            }

            if (profile.spatialiteList.size() > 0) {
                JSONArray spatialiteDbsArray = new JSONArray();
                for (int j = 0; j < profile.spatialiteList.size(); j++) {
                    JSONObject spatialiteDbObject = new JSONObject();
                    spatialiteDbObject.put(PATH, profile.spatialiteList.get(j));
                    spatialiteDbsArray.put(j, spatialiteDbObject);
                }
                profileObject.put(SPATIALITE, spatialiteDbsArray);
            }
        }
        if (indent)
            return root.toString(2);
        else
            return root.toString();

    }
}
