package com.hydrologis.geoss2go.activities;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hydrologis.geoss2go.Geoss2GoActivity;
import com.hydrologis.geoss2go.R;
import com.hydrologis.geoss2go.core.Profile;
import com.hydrologis.geoss2go.core.ProfilesHandler;

import org.json.JSONException;

import java.util.List;

public class ProfileSettingsActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private int mSelectedProfileIndex;
    private SharedPreferences mPeferences;
    private List<Profile> mProfileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);


        Bundle extras = getIntent().getExtras();
        Profile selectedProfile = extras.getParcelable(Geoss2GoActivity.KEY_SELECTED_PROFILE);

        mPeferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            mProfileList = ProfilesHandler.INSTANCE.getProfilesFromPreferences(mPeferences);
        } catch (JSONException e) {
            Log.e("GEOS2GO", "", e);
        }

        mSelectedProfileIndex = mProfileList.indexOf(selectedProfile);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_settings, menu);
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
    protected void onPause() {
        super.onPause();

        if (mProfileList != null) {
            Profile profile = mProfileList.get(mSelectedProfileIndex);


            try {
                ProfilesHandler.INSTANCE.saveProfilesToPreferences(mPeferences, mProfileList);
            } catch (JSONException e) {
                Log.e("GEOS2GO", "Error saving profiles", e);
            }
        }
    }

    public void onProfileInfoChanged(String name, String description) {
        Profile profile = mProfileList.get(mSelectedProfileIndex);
        profile.name = name;
        profile.description = description;
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ProfileInfoFragment.newInstance(mProfileList.get(mSelectedProfileIndex));
                case 1:
                    return ProfileInfoFragment.newInstance(mProfileList.get(mSelectedProfileIndex));
                case 2:
                    return ProfileInfoFragment.newInstance(mProfileList.get(mSelectedProfileIndex));
                case 3:
                    return ProfileInfoFragment.newInstance(mProfileList.get(mSelectedProfileIndex));
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Profile info";
                case 1:
                    return "Basemaps";
                case 2:
                    return "Spatialite Databases";
                case 3:
                    return "Forms";
            }
            return null;
        }
    }
}
