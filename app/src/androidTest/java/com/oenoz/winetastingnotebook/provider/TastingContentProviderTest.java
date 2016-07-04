package com.oenoz.winetastingnotebook.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.oenoz.winetastingnotebook.CursorAssert;

public class TastingContentProviderTest extends AndroidTestCase {

    public void testTastingSections() {
        Uri tastingUri = mContext.getContentResolver().insert(
                TastingContentUri.forTasting(),
                new ContentValues()
        );
        Cursor cursor = mContext.getContentResolver().query(
                TastingContentUri.forTastingSections(tastingUri),
                new String[] { TastingContentSchema.NAME },
                null,
                null,
                TastingContentSchema.SEQUENCE);
        CursorAssert.assertContents(cursor, "Appearance", "Nose", "Palate", "Conclusion");
    }

    public void testTastingSectionAttributes() {
        Uri tastingUri = mContext.getContentResolver().insert(
                TastingContentUri.forTasting(),
                new ContentValues()
        );
        Cursor sectionsCursor = mContext.getContentResolver().query(
                TastingContentUri.forTastingSections(tastingUri),
                new String[] { TastingContentSchema.ID, TastingContentSchema.NAME },
                null,
                null,
                TastingContentSchema.SEQUENCE);

        assertTrue(sectionsCursor.moveToNext());
        Cursor cursor = getTastingSectionAttributes(tastingUri, sectionsCursor.getLong(0));
        CursorAssert.assertContents(cursor, "Clarity", "Intensity", "Color");

        assertTrue(sectionsCursor.moveToNext());
        cursor = getTastingSectionAttributes(tastingUri, sectionsCursor.getLong(0));
        CursorAssert.assertContents(cursor, "Condition", "Intensity", "Development", "Aromas");

        assertTrue(sectionsCursor.moveToNext());
        cursor = getTastingSectionAttributes(tastingUri, sectionsCursor.getLong(0));
        CursorAssert.assertContents(cursor, "Sweetness", "Acidity", "Tannin", "Body", "Intensity", "Flavors", "Alcohol", "Length");

        assertTrue(sectionsCursor.moveToNext());
        cursor = getTastingSectionAttributes(tastingUri, sectionsCursor.getLong(0));
        CursorAssert.assertContents(cursor, "Quality", "Maturity");
    }

    Cursor getTastingSectionAttributes(Uri tastingUri, long sectionId) {
        return mContext.getContentResolver().query(
                TastingContentUri.forTastingSectionAttributes(TastingContentUri.forTastingSection(tastingUri, sectionId)),
                new String[] { TastingContentSchema.NAME },
                null,
                null,
                TastingContentSchema.SEQUENCE);
    }
}
