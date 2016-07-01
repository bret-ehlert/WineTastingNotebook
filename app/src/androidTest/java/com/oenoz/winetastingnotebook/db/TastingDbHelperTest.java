package com.oenoz.winetastingnotebook.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.test.AndroidTestCase;
import android.test.MoreAsserts;

import com.oenoz.winetastingnotebook.db.schema.TastingTemplateAttributeTable;
import com.oenoz.winetastingnotebook.db.schema.TastingTemplateGroupTable;

import java.util.ArrayList;

public class TastingDbHelperTest extends AndroidTestCase
{
    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(TastingDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new TastingDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        checkTastingTemplateGroups(db);
        checkTastingTemplateAttributes(db);
    }

    void checkTastingTemplateGroups(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT " + TastingTemplateGroupTable.COLUMN_NAME + " FROM " + TastingTemplateGroupTable.TABLE_NAME + " ORDER BY " + TastingTemplateGroupTable.COLUMN_SEQUENCE, null);
        ArrayList<String> groups = new ArrayList<>();
        while (cursor.moveToNext()) {
            groups.add(cursor.getString(0));
        }
        cursor.close();
        MoreAsserts.assertContentsInOrder(groups, "Appearance", "Nose", "Palate", "Conclusion");
    }

    void checkTastingTemplateAttributes(SQLiteDatabase db) {
        checkTastingTemplateAttributes(db, "Appearance", "Clarity", "Intensity", "Color");
        checkTastingTemplateAttributes(db, "Nose", "Condition", "Intensity", "Development", "Aromas");
        checkTastingTemplateAttributes(db, "Palate", "Sweetness", "Acidity", "Tannin", "Body", "Intensity", "Flavors", "Alcohol", "Length");
        checkTastingTemplateAttributes(db, "Conclusion", "Quality", "Maturity");
    }

    void checkTastingTemplateAttributes(SQLiteDatabase db, String group, String... expectedAttributes) {
        Cursor cursor = db.rawQuery(
                "SELECT " + TastingTemplateAttributeTable.TABLE_NAME + "." + TastingTemplateAttributeTable.COLUMN_NAME + " FROM " + TastingTemplateAttributeTable.TABLE_NAME +
                SqlHelper.InnerJoin(TastingTemplateGroupTable.TABLE_NAME, TastingTemplateAttributeTable.TABLE_NAME, TastingTemplateAttributeTable.COLUMN_GROUP) +
                " WHERE " + TastingTemplateGroupTable.TABLE_NAME + "." + TastingTemplateGroupTable.COLUMN_NAME + " = ?" +
                " ORDER BY " + TastingTemplateAttributeTable.TABLE_NAME + "." + TastingTemplateAttributeTable.COLUMN_SEQUENCE, new String[] { group });
        ArrayList<String> groups = new ArrayList<>();
        while (cursor.moveToNext()) {
            groups.add(cursor.getString(0));
        }
        cursor.close();
        MoreAsserts.assertContentsInOrder(groups, expectedAttributes);
    }

    void checkTastingTemplateAttibuteValues(SQLiteDatabase db, String group, String attribute, String... expectedValues) {

    }

    void checkTastingTemplateAttibuteGroupValues(SQLiteDatabase db, String group, String attribute, String attributeGroup, String... expectedValues) {

    }
}
