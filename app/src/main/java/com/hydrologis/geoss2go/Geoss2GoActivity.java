package com.hydrologis.geoss2go;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hydrologis.geoss2go.core.Profile;
import com.hydrologis.geoss2go.core.ProfilesHandler;
import com.hydrologis.geoss2go.dialogs.NewProfileDialogFragment;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Geoss2GoActivity extends AppCompatActivity implements NewProfileDialogFragment.INewProfileCreatedListener {

    private LinearLayout profilesContainer;
    private LinearLayout emptyFiller;
    private SharedPreferences mPeferences;

    private List<Profile> profileList = new ArrayList<>();

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
            try {
                ProfilesHandler.INSTANCE.saveProfilesToPreferences(mPeferences, profileList);
            } catch (JSONException e) {
                Log.e("GEOS2GO", "Error saving profiles", e);
            }
        }
    }

    private void loadProfiles() {
        profilesContainer.removeAllViews();

        if (profileList.size() != 0)
            emptyFiller.setVisibility(View.GONE);

        for (Profile profile : profileList) {
            View newProjectDialogView = getLayoutInflater().inflate(R.layout.profile_cardlayout, null);
            TextView profileNameText = (TextView) newProjectDialogView.findViewById(R.id.profileNameText);
            profileNameText.setText(profile.name);
            TextView profileDescriptionText = (TextView) newProjectDialogView.findViewById(R.id.profileDescriptionText);
            profileDescriptionText.setText(profile.description);

            profilesContainer.addView(newProjectDialogView);
        }

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
}
