package com.oenoz.winetastingnotebook.ui;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.oenoz.winetastingnotebook.provider.TastingContentSchema;
import com.oenoz.winetastingnotebook.provider.TastingContentUri;

public class TastingAttributePageAdapter extends TastingPageAdapter {

    private final Uri mTastingSectionUri;

    public TastingAttributePageAdapter(Context context, FragmentManager fm, Uri tastingSectionUri) {
        super(context, fm, TastingContentUri.forTastingSectionAttributes(tastingSectionUri));
        mTastingSectionUri = tastingSectionUri;
    }

    @Override
    public Fragment getItem(int position) {
        Uri sectionAttributeUri = TastingContentUri.forTastingSectionAttribute(mTastingSectionUri, getElementId(position));
        return TastingAttributeFragment.newInstance(sectionAttributeUri);
    }
}