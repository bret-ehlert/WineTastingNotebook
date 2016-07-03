package com.oenoz.winetastingnotebook.ui;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.oenoz.winetastingnotebook.provider.TastingContentSchema;
import com.oenoz.winetastingnotebook.provider.TastingContentUri;

public class TastingPagerAdapter extends FragmentStatePagerAdapter {

    private final Uri mTastingUri;
    private final Cursor mTastingSectionsCursor;
    private int mCurrentGroupIndex = 0;

    public TastingPagerAdapter(Context context, FragmentManager fm, Uri tastingUri) {
        super(fm);
        mTastingUri = tastingUri;
        mTastingSectionsCursor = context.getContentResolver().query(
                TastingContentUri.forTastingSections(tastingUri),
                null, null, null, null);
    }

    @Override
    public int getCount() {
        mTastingSectionsCursor.moveToPosition(mCurrentGroupIndex);
        String currentGroup = getGroupNameFromCursor();
        int count = 1;
        while (mTastingSectionsCursor.moveToNext() && getGroupNameFromCursor().equals(currentGroup)) {
            count++;
        }
        return count + 1;
    }

    @Override
    public Fragment getItem(int position) {
        mTastingSectionsCursor.moveToPosition(position);
        Uri sectionUri = TastingContentUri.forTastingSection(mTastingUri, mTastingSectionsCursor.getLong(mTastingSectionsCursor.getColumnIndex(TastingContentSchema.ID)));
        return TastingAttributeFragment.newInstance(sectionUri);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        mTastingSectionsCursor.moveToPosition(mCurrentGroupIndex);
        String currentGroup = getGroupNameFromCursor();
        mTastingSectionsCursor.moveToPosition(position);
        String positionGroup = getGroupNameFromCursor();
        if (!currentGroup.equals(positionGroup)) {
            return "> " + getGroupNameFromCursor();
        }
        else {
            return getAttributeNameFromCursor();
        }
    }

    public String getSectionTitle(int position) {
        mTastingSectionsCursor.moveToPosition(position);
        return getGroupNameFromCursor();
    }

    private String getAttributeNameFromCursor() {
        return mTastingSectionsCursor.getString(mTastingSectionsCursor.getColumnIndex(TastingContentSchema.TASTING_SECTION_ATTRIBUTE_NAME));
    }

    private String getGroupNameFromCursor() {
        return mTastingSectionsCursor.getString(mTastingSectionsCursor.getColumnIndex(TastingContentSchema.TASTING_SECTION_GROUP_NAME));
    }
}
