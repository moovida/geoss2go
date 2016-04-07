package com.hydrologis.geoss2go;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hydrologis.geoss2go.dialogs.NewProfileDialogFragment;

public class Geoss2GoActivity extends AppCompatActivity implements NewProfileDialogFragment.INewProfileCreatedListener {

    private LinearLayout profilesContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geoss2_go);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.TransparentText);
        } else {
//        collapsingToolbarLayout.setTitle("Create Delivery Personnel");
            collapsingToolbarLayout.setExpandedTitleColor(Color.argb(0, 0, 0, 0));
//        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.rgb(0, 0, 0));
        }

        profilesContainer = (LinearLayout) findViewById(R.id.profiles_container);

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
        View newProjectDialogView = getLayoutInflater().inflate(R.layout.profile_cardlayout, null);
        TextView profileNameText = (TextView) newProjectDialogView.findViewById(R.id.profileNameText);
        profileNameText.setText(name);
        TextView profileDescriptionText = (TextView) newProjectDialogView.findViewById(R.id.profileDescriptionText);
        profileDescriptionText.setText(description);

        profilesContainer.addView(newProjectDialogView);
    }
}
