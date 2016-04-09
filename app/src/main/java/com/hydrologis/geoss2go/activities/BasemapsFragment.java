package com.hydrologis.geoss2go.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.hydrologis.geoss2go.R;
import com.hydrologis.geoss2go.core.Profile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import eu.geopaparazzi.library.core.ResourcesManager;
import eu.geopaparazzi.library.core.activities.DirectoryBrowserActivity;
import eu.geopaparazzi.library.database.ANote;
import eu.geopaparazzi.library.util.GPDialogs;
import eu.geopaparazzi.library.util.LibraryConstants;

public class BasemapsFragment extends Fragment {
    private static final String ARG_PROFILE = "profile";
    public static final int RETURNCODE_BROWSE = 666;


    private List<String> mBasemapsList = new ArrayList<>();
    private ListView listView;
    private ArrayAdapter<String> mArrayAdapter;

    private String[] supportedExtensions = {"mapurl", "map", "sqlite", "mbtiles"};

    public BasemapsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static BasemapsFragment newInstance(Profile profile) {
        BasemapsFragment fragment = new BasemapsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PROFILE, profile);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profilesettings_basemaps, container, false);

        listView = (ListView) rootView.findViewById(R.id.basemapsList);

        Profile profile = getArguments().getParcelable(ARG_PROFILE);
        mBasemapsList.clear();
        mBasemapsList.addAll(profile.basemapsList);

        FloatingActionButton addFormButton = (FloatingActionButton) rootView.findViewById(R.id.addFormButton);
        addFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    File sdcardDir = ResourcesManager.getInstance(getContext()).getSdcardDir();
                    Intent browseIntent = new Intent(getContext(), DirectoryBrowserActivity.class);
                    browseIntent.putExtra(DirectoryBrowserActivity.EXTENSIONS, supportedExtensions);
                    browseIntent.putExtra(DirectoryBrowserActivity.STARTFOLDERPATH, sdcardDir.getAbsolutePath());
                    startActivityForResult(browseIntent, RETURNCODE_BROWSE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        refreshList();

        return rootView;
    }

    private void refreshList() {
        mArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.fragment_profilesettings_basemaps_row, mBasemapsList) {
            class ViewHolder {
                TextView nameView;
                TextView pathView;
            }

            @Override
            public View getView(int position, View rowView, ViewGroup parent) {

                ViewHolder holder;
                // Recycle existing view if passed as parameter
                if (rowView == null) {
                    LayoutInflater inflater = getLayoutInflater(null);
                    rowView = inflater.inflate(R.layout.fragment_profilesettings_basemaps_row, parent, false);
                    holder = new ViewHolder();
                    holder.nameView = (TextView) rowView.findViewById(R.id.basemapName);
                    holder.pathView = (TextView) rowView.findViewById(R.id.basemapPath);

                    rowView.setTag(holder);
                } else {
                    holder = (ViewHolder) rowView.getTag();
                }

                final String currentBasemap = mBasemapsList.get(position);
                File basemapFile = new File(currentBasemap);

                holder.nameView.setText(basemapFile.getName());
                holder.pathView.setText(basemapFile.getAbsolutePath());

                return rowView;
            }

        };


        listView.setAdapter(mArrayAdapter);
        listView.setClickable(true);
        listView.setLongClickable(true);
        listView.setFocusable(true);
        listView.setFocusableInTouchMode(true);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final String path = mBasemapsList.get(position);
                File file = new File(path);
                GPDialogs.yesNoMessageDialog(getActivity(), "Do you want to remove: " + file.getName(), new Runnable() {
                    @Override
                    public void run() {
                        mBasemapsList.remove(position);
                        ProfileSettingsActivity activity = (ProfileSettingsActivity) getActivity();
                        activity.onBasemapRemoved(path);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refreshList();
                            }
                        });

                    }
                }, null);

                return true;
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (RETURNCODE_BROWSE): {
                if (resultCode == Activity.RESULT_OK) {
                    String path = data.getStringExtra(LibraryConstants.PREFS_KEY_PATH);
                    if (path != null && new File(path).exists()) {
                        if (!mBasemapsList.contains(path)) {
                            mBasemapsList.add(path);
                            ProfileSettingsActivity activity = (ProfileSettingsActivity) getActivity();
                            activity.onBasemapAdded(path);
                            refreshList();
                        }
                    }
                }
            }
        }
    }
}