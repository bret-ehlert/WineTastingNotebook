package com.oenoz.winetastingnotebook.ui;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.oenoz.winetastingnotebook.R;
import com.oenoz.winetastingnotebook.provider.TastingContentUri;

public class TastingActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, TastingSectionFragment.OnFragmentInteractionListener, TastingAttributeFragment.OnFragmentInteractionListener {

    private TastingSectionPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Uri tastingUri;
        String tastingUrl = intent.getStringExtra(Intent.EXTRA_TEXT);
        if(tastingUrl == null) {
            tastingUri = getContentResolver().insert(TastingContentUri.forTasting(), new ContentValues());
            intent.putExtra(Intent.EXTRA_TEXT, tastingUri.toString());
        }
        else {
            tastingUri = Uri.parse(tastingUrl);
        }
        mSectionsPagerAdapter = new TastingSectionPagerAdapter(this, getSupportFragmentManager(), tastingUri);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.tastingSectionsViewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(this);
        onPageSelected(0);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
        //Setting tabs from adpater
        //tabLayout.setTabsFromPagerAdapter (mSectionsPagerAdapter);

        final ActionBar actionBar = getActionBar();

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
        getMenuInflater().inflate(R.menu.menu_tasting, menu);
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
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPageSelected(int position) {
        //setTitle(mSectionsPagerAdapter.getSectionTitle(position));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
