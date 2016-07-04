package com.oenoz.winetastingnotebook.ui;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.oenoz.winetastingnotebook.provider.TastingContentUri;

public class TastingSectionPagerAdapter extends TastingPageAdapter {

    private final Uri mTastingUri;

    public TastingSectionPagerAdapter(Context context, FragmentManager fm, Uri tastingUri) {
        super(context, fm, TastingContentUri.forTastingSections(tastingUri));
        mTastingUri = tastingUri;
    }

    @Override
    public Fragment getItem(int position) {
        Uri sectionUri = TastingContentUri.forTastingSection(mTastingUri, getElementId(position));
        return TastingSectionFragment.newInstance((String)getPageTitle(position), sectionUri);
    }
}
