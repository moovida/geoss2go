package com.hydrologis.geoss2go;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hydrologis.geoss2go.core.Profile;
import com.hydrologis.geoss2go.core.ProfilesHandler;
import com.hydrologis.geoss2go.dialogs.NewProfileDialogFragment;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.geopaparazzi.library.core.dialogs.ColorStrokeDialogFragment;
import eu.geopaparazzi.library.style.ColorStrokeObject;
import eu.geopaparazzi.library.style.ColorUtilities;
import eu.geopaparazzi.library.util.GPDialogs;

public class Geoss2GoActivity extends AppCompatActivity implements NewProfileDialogFragment.INewProfileCreatedListener, ColorStrokeDialogFragment.IColorStrokePropertiesChangeListener {

    private LinearLayout profilesContainer;
    private LinearLayout emptyFiller;
    private SharedPreferences mPeferences;

    private List<Profile> profileList = new ArrayList<>();
    private CardView currentColorCardView;
    private Profile currentProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geoss2_go);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        assert collapsingToolbarLayout != null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.TransparentText);
        } else {
            collapsingToolbarLayout.setExpandedTitleColor(Color.argb(0, 0, 0, 0));
        }

        profilesContainer = (LinearLayout) findViewById(R.id.profiles_container);
        emptyFiller = (LinearLayout) findViewById(R.id.empty_fillers);
        TextView licenseText = (TextView) findViewById(R.id.license_text_on_empty);
        licenseText.setText(Html.fromHtml(getString(R.string.license_text)));
        licenseText.setMovementMethod(LinkMovementMethod.getInstance());

        mPeferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            profileList = ProfilesHandler.INSTANCE.getProfilesFromPreferences(mPeferences);
        } catch (JSONException e) {
            Log.e("GEOS2GO", "", e);
        }

        loadProfiles();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewProfileDialogFragment newProfileDialogFragment = NewProfileDialogFragment.newInstance(null, null);
                newProfileDialogFragment.show(getSupportFragmentManager(), "New Profile Dialog");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (profileList != null) {
            saveProfiles();
        }
    }

    private void loadProfiles() {
        profilesContainer.removeAllViews();

        if (profileList.size() != 0) {
            emptyFiller.setVisibility(View.GONE);
        } else {
            emptyFiller.setVisibility(View.VISIBLE);
        }

        for (final Profile profile : profileList) {
            final CardView newProjectCardView = (CardView) getLayoutInflater().inflate(R.layout.profile_cardlayout, null);
            TextView profileNameText = (TextView) newProjectCardView.findViewById(R.id.profileNameText);
            profileNameText.setText(profile.name);
            TextView profileDescriptionText = (TextView) newProjectCardView.findViewById(R.id.profileDescriptionText);
            profileDescriptionText.setText(profile.description);

            ImageButton settingsButton = (ImageButton) newProjectCardView.findViewById(R.id.settingsButton);
            settingsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            ImageButton deleteButton = (ImageButton) newProjectCardView.findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String msg = String.format("Are you sure you want to remove the profile: '%s'? This can't be undone.", profile.name);
                    GPDialogs.yesNoMessageDialog(Geoss2GoActivity.this, msg, new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    profileList.remove(profile);
                                    saveProfiles();
                                    loadProfiles();
                                }
                            });
                        }
                    }, null);

                }
            });
            ImageButton colorButton = (ImageButton) newProjectCardView.findViewById(R.id.colorButton);
            colorButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    currentColorCardView = newProjectCardView;
                    currentProfile = profile;
                    int color = ColorUtilities.toColor(profile.color);
                    ColorStrokeObject colorStrokeObject = new ColorStrokeObject();
                    colorStrokeObject.hasFill = true;
                    colorStrokeObject.hasStroke = false;
                    colorStrokeObject.fillColor = color;

                    ColorStrokeDialogFragment colorStrokeDialogFragment = ColorStrokeDialogFragment.newInstance(colorStrokeObject);
                    colorStrokeDialogFragment.show(getSupportFragmentManager(), "Color Dialog");
                }
            });

            String color = profile.color;
            setCardviewColor(newProjectCardView, color);
//            newProjectCardView.setBackgroundColor(backgroundColor);

            profilesContainer.addView(newProjectCardView);
        }

    }

    private void setCardviewColor(CardView newProjectCardView, String color) {
        int backgroundColor = ColorUtilities.toColor(color);
        newProjectCardView.setCardBackgroundColor(backgroundColor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_geoss2_go, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNewProfileCreated(String name, String description) {
        Profile p = new Profile();
        p.name = name;
        p.description = description;
        p.creationdate = new Date().toString();
        profileList.add(p);
        loadProfiles();
    }

    @Override
    public void onPropertiesChanged(ColorStrokeObject newColorStrokeObject) {
        if (newColorStrokeObject != null && newColorStrokeObject.hasFill && currentColorCardView != null && currentProfile != null) {
            String newColor = ColorUtilities.getHex(newColorStrokeObject.fillColor);
            currentProfile.color = newColor;
            setCardviewColor(currentColorCardView, newColor);

            // and save it to disk
            saveProfiles();
        }
    }

    private void saveProfiles() {
        try {
            ProfilesHandler.INSTANCE.saveProfilesToPreferences(mPeferences, profileList);
        } catch (JSONException e) {
            Log.e("GEOS2GO", "Error saving profiles", e);
        }
    }
}
