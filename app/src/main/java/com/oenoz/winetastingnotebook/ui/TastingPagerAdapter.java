package com.oenoz.winetastingnotebook.ui;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.oenoz.winetastingnotebook.provider.TastingContentSchema;
import com.oenoz.winetastingnotebook.provider.TastingContentUri;

public class TastingPagerAdapter extends FragmentStatePagerAdapter {

    private final Uri mTastingUri;
    private final Cursor mTastingSectionsCursor;

    public TastingPagerAdapter(Context context, FragmentManager fm, Uri tastingUri) {
        super(fm);
        mTastingUri = tastingUri;
        mTastingSectionsCursor = context.getContentResolver().query(
                TastingContentUri.forTastingSections(tastingUri),
                null, null, null, null);
    }

    @Override
    public int getCount() {
        return mTastingSectionsCursor.getCount();
    }

    @Override
    public Fragment getItem(int position) {
        mTastingSectionsCursor.moveToPosition(position);
        Uri sectionUri = TastingContentUri.forTastingSection(mTastingUri, mTastingSectionsCursor.getLong(mTastingSectionsCursor.getColumnIndex(TastingContentSchema.ID)));
        return TastingAttributeFragment.newInstance(sectionUri);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        mTastingSectionsCursor.moveToPosition(position);
        /*
        return mTastingSectionsCursor.getString(mTastingSectionsCursor.getColumnIndex(TastingContentSchema.TASTING_SECTION_GROUP_NAME)) + " / " +
                mTastingSectionsCursor.getString(mTastingSectionsCursor.getColumnIndex(TastingContentSchema.TASTING_SECTION_ATTRIBUTE_NAME));*/
        return mTastingSectionsCursor.getString(mTastingSectionsCursor.getColumnIndex(TastingContentSchema.TASTING_SECTION_ATTRIBUTE_NAME));
    }
}
