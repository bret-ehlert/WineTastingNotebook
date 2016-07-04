package com.oenoz.winetastingnotebook.ui;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.oenoz.winetastingnotebook.provider.TastingContentSchema;

public abstract class TastingPageAdapter extends FragmentStatePagerAdapter {

    protected final int CIDX_ID = 0;
    protected final int CIDX_NAME = 1;
    protected final String[] CURSOR_PROJECTION = new String[] { TastingContentSchema.ID, TastingContentSchema.NAME };

    private final Cursor mCursor;

    public TastingPageAdapter(Context context, FragmentManager fm, Uri contentUri) {
        super(fm);
        mCursor = context.getContentResolver().query(
                contentUri,
                new String[] {TastingContentSchema.ID, TastingContentSchema.NAME },
                null, null, null);
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    protected long getElementId(int position) {
        mCursor.moveToPosition(position);
        return mCursor.getLong(CIDX_ID);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        mCursor.moveToPosition(position);
        return getNameFromCursor();
    }

    private String getNameFromCursor() {
        return mCursor.getString(CIDX_NAME);
    }
}
