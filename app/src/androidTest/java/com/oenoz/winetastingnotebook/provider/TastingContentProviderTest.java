package com.oenoz.winetastingnotebook.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.oenoz.winetastingnotebook.CursorAssert;
import com.oenoz.winetastingnotebook.db.schema.TastingTemplateGroupTable;

public class TastingContentProviderTest extends AndroidTestCase {

    public void testTastingSections() {
        Uri tastingUri = mContext.getContentResolver().insert(
                TastingContentUri.forTasting(),
                new ContentValues()
        );
        Cursor cursor = mContext.getContentResolver().query(
                TastingContentUri.forTastingSections(tastingUri),
                new String[] { TastingContentSchema.TASTING_SECTION_GROUP_NAME, TastingContentSchema.TASTING_SECTION_ATTRIBUTE_NAME },
                null,
                null,
                null);
        CursorAssert.assertContents(cursor, new CursorAssert.ToString() {
            @Override
            public String toString(Cursor cursor) {
                return cursor.getString(0) + " / " + cursor.getString(1);
            }},
                "Appearance / Clarity",
                "Appearance / Intensity",
                "Appearance / Color",
                "Nose / Condition",
                "Nose / Intensity",
                "Nose / Development",
                "Nose / Aromas",
                "Palate / Sweetness",
                "Palate / Acidity",
                "Palate / Tannin",
                "Palate / Body",
                "Palate / Intensity",
                "Palate / Flavors",
                "Palate / Alcohol",
                "Palate / Length",
                "Conclusion / Quality",
                "Conclusion / Maturity"
        );
    }
}
