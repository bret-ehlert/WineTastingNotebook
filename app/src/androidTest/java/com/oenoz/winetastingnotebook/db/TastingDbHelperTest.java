package com.oenoz.winetastingnotebook.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteOpenHelper;
import android.test.AndroidTestCase;
import android.test.MoreAsserts;

import com.oenoz.winetastingnotebook.db.schema.TastingTemplateAttributeGroupTable;
import com.oenoz.winetastingnotebook.db.schema.TastingTemplateAttributeTable;
import com.oenoz.winetastingnotebook.db.schema.TastingTemplateAttributeValueTable;
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
        checkTastingTemplateAttributeGroups(db);
        checkTastingTemplateAttributeValues(db);
    }

    // TODO check no goups without attributes, no attributes without values, not attribute groups without values, etc.

    void checkTastingTemplateGroups(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT " + TastingTemplateGroupTable.COLUMN_NAME + " FROM " + TastingTemplateGroupTable.TABLE_NAME + " ORDER BY " + TastingTemplateGroupTable.COLUMN_SEQUENCE, null);
        assertCusorContents(cursor, "Appearance", "Nose", "Palate", "Conclusion");
    }

    void checkTastingTemplateAttributes(SQLiteDatabase db) {
        checkTastingTemplateAttributes(db, "Appearance", "Clarity", "Intensity", "Color");
        checkTastingTemplateAttributes(db, "Nose", "Condition", "Intensity", "Development", "Aromas");
        checkTastingTemplateAttributes(db, "Palate", "Sweetness", "Acidity", "Tannin", "Body", "Intensity", "Flavors", "Alcohol", "Length");
        checkTastingTemplateAttributes(db, "Conclusion", "Quality", "Maturity");
    }

    void checkTastingTemplateAttributeGroups(SQLiteDatabase db) {
        checkTastingTemplateAttibuteGroups(db, "Appearance", "Color", "White", "Red");
        // TODO checkTastingTemplateAttibuteGroups(db, "Nose", "Aromas", ...);
        // TODO checkTastingTemplateAttibuteGroups(db, "Palate", "Flavors", ...);
    }

    void checkTastingTemplateAttributeValues(SQLiteDatabase db) {
        checkTastingTemplateAttibuteValues(db, "Appearance", "Clarity", "Clear", "Dull", "Cloudy", "Sediment", "Bubbles");
        checkTastingTemplateAttibuteGroupValues(db, "Appearance", "Color", "White", "Colorless", "Straw", "Yellow", "Gold", "Amber");
        checkTastingTemplateAttibuteGroupValues(db, "Appearance", "Color", "Red", "Tawny", "Garnet", "Ruby", "Purple");
        // TODO checkTastingTemplateAttibuteGroupValues(db, "Nose", "Aromas", ...)
        checkTastingTemplateAttibuteValues(db, "Palate", "Tannin", "None", "Light", "Medium", "High");
        // TODO checkTastingTemplateAttibuteGroupValues(db, "Palate", "Flavors", ...)
        checkTastingTemplateAttibuteValues(db, "Palate", "Length", "Short", "Medium", "Long");
        checkTastingTemplateAttibuteValues(db, "Conclusion", "Maturity", "Immature", "Drink now or later", "Drink now", "Tired");
    }

    void checkTastingTemplateAttributes(SQLiteDatabase db, String group, String... expectedAttributes) {
        Cursor cursor = db.rawQuery(
                "SELECT " + TastingTemplateAttributeTable.TABLE_NAME + "." + TastingTemplateAttributeTable.COLUMN_NAME + " FROM " + TastingTemplateAttributeTable.TABLE_NAME +
                SqlHelper.InnerJoin(TastingTemplateGroupTable.TABLE_NAME, TastingTemplateAttributeTable.TABLE_NAME, TastingTemplateAttributeTable.COLUMN_GROUP) +
                " WHERE " + TastingTemplateGroupTable.TABLE_NAME + "." + TastingTemplateGroupTable.COLUMN_NAME + " = ?" +
                " ORDER BY " + TastingTemplateAttributeTable.TABLE_NAME + "." + TastingTemplateAttributeTable.COLUMN_SEQUENCE, new String[] { group });
        assertCusorContents(cursor, expectedAttributes);
    }

    void checkTastingTemplateAttibuteValues(SQLiteDatabase db, String group, String attribute, String... expectedValues) {
        Cursor cursor = db.rawQuery(
                "SELECT " + TastingTemplateAttributeValueTable.TABLE_NAME + "." + TastingTemplateAttributeValueTable.COLUMN_NAME + " FROM " + TastingTemplateAttributeValueTable.TABLE_NAME +
                        SqlHelper.InnerJoin(TastingTemplateAttributeTable.TABLE_NAME, TastingTemplateAttributeValueTable.TABLE_NAME, TastingTemplateAttributeValueTable.COLUMN_ATTRIBUTE) +
                        SqlHelper.InnerJoin(TastingTemplateGroupTable.TABLE_NAME, TastingTemplateAttributeTable.TABLE_NAME, TastingTemplateAttributeTable.COLUMN_GROUP) +
                        " WHERE " + TastingTemplateGroupTable.TABLE_NAME + "." + TastingTemplateGroupTable.COLUMN_NAME + " = ?" +
                        " AND " + TastingTemplateAttributeTable.TABLE_NAME + "." + TastingTemplateAttributeTable.COLUMN_NAME + " = ? " +
                        " AND " + TastingTemplateAttributeValueTable.TABLE_NAME + "." + TastingTemplateAttributeValueTable.COLUMN_ATTRIBUTEGROUP + " IS NULL " +
                        " ORDER BY " + TastingTemplateAttributeValueTable.TABLE_NAME + "." + TastingTemplateAttributeValueTable.COLUMN_SEQUENCE, new String[] { group, attribute });
        assertCusorContents(cursor, expectedValues);
    }

    void checkTastingTemplateAttibuteGroups(SQLiteDatabase db, String group, String attribute, String... expectedAttributeGroups) {
        Cursor cursor = db.rawQuery(
                "SELECT " + TastingTemplateAttributeGroupTable.TABLE_NAME + "." + TastingTemplateAttributeGroupTable.COLUMN_NAME + " FROM " + TastingTemplateAttributeGroupTable.TABLE_NAME +
                        SqlHelper.InnerJoin(TastingTemplateAttributeTable.TABLE_NAME, TastingTemplateAttributeGroupTable.TABLE_NAME, TastingTemplateAttributeGroupTable.COLUMN_ATTRIBUTE) +
                        SqlHelper.InnerJoin(TastingTemplateGroupTable.TABLE_NAME, TastingTemplateAttributeTable.TABLE_NAME, TastingTemplateAttributeTable.COLUMN_GROUP) +
                        " WHERE " + TastingTemplateGroupTable.TABLE_NAME + "." + TastingTemplateGroupTable.COLUMN_NAME + " = ?" +
                        " AND " + TastingTemplateAttributeTable.TABLE_NAME + "." + TastingTemplateAttributeTable.COLUMN_NAME + " = ? " +
                        " ORDER BY " + TastingTemplateAttributeGroupTable.TABLE_NAME + "." + TastingTemplateAttributeGroupTable.COLUMN_SEQUENCE, new String[] { group, attribute });
        assertCusorContents(cursor, expectedAttributeGroups);
    }

    void checkTastingTemplateAttibuteGroupValues(SQLiteDatabase db, String group, String attribute, String attributeGroup, String... expectedValues) {
        Cursor cursor = db.rawQuery(
                "SELECT " + TastingTemplateAttributeValueTable.TABLE_NAME + "." + TastingTemplateAttributeValueTable.COLUMN_NAME + " FROM " + TastingTemplateAttributeValueTable.TABLE_NAME +
                        SqlHelper.InnerJoin(TastingTemplateAttributeTable.TABLE_NAME, TastingTemplateAttributeValueTable.TABLE_NAME, TastingTemplateAttributeValueTable.COLUMN_ATTRIBUTE) +
                        SqlHelper.InnerJoin(TastingTemplateAttributeGroupTable.TABLE_NAME, TastingTemplateAttributeValueTable.TABLE_NAME, TastingTemplateAttributeValueTable.COLUMN_ATTRIBUTEGROUP) +
                        SqlHelper.InnerJoin(TastingTemplateGroupTable.TABLE_NAME, TastingTemplateAttributeTable.TABLE_NAME, TastingTemplateAttributeTable.COLUMN_GROUP) +
                        " WHERE " + TastingTemplateGroupTable.TABLE_NAME + "." + TastingTemplateGroupTable.COLUMN_NAME + " = ?" +
                        " AND " + TastingTemplateAttributeTable.TABLE_NAME + "." + TastingTemplateAttributeTable.COLUMN_NAME + " = ? " +
                        " AND " + TastingTemplateAttributeGroupTable.TABLE_NAME + "." + TastingTemplateAttributeGroupTable.COLUMN_NAME + " = ? " +
                        " ORDER BY " + TastingTemplateAttributeValueTable.TABLE_NAME + "." + TastingTemplateAttributeValueTable.COLUMN_SEQUENCE, new String[] { group, attribute, attributeGroup });
        assertCusorContents(cursor, expectedValues);
    }

    void assertCusorContents(Cursor cursor, String... expected) {
        ArrayList<String> actual = new ArrayList<>();
        while (cursor.moveToNext()) {
            actual.add(cursor.getString(0));
        }
        cursor.close();
        MoreAsserts.assertContentsInOrder(actual, expected);
    }
}
