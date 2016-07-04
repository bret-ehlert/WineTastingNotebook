package com.oenoz.winetastingnotebook.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.oenoz.winetastingnotebook.CursorAssert;
import com.oenoz.winetastingnotebook.db.schema.RegionTable;
import com.oenoz.winetastingnotebook.db.schema.TastingTable;
import com.oenoz.winetastingnotebook.db.schema.TastingTemplateAttributeGroupTable;
import com.oenoz.winetastingnotebook.db.schema.TastingTemplateAttributeTable;
import com.oenoz.winetastingnotebook.db.schema.TastingTemplateAttributeValueTable;
import com.oenoz.winetastingnotebook.db.schema.TastingTemplateSectionTable;
import com.oenoz.winetastingnotebook.db.schema.VarietyTable;

import java.util.Date;

public class TastingDbHelperTest extends AndroidTestCase
{
    SQLiteDatabase db;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mContext.deleteDatabase(TastingDbHelper.DATABASE_NAME);
        db = new TastingDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
    }

    // TODO check no goups without attributes, no attributes without values, not attribute groups without values, etc.

    public void testTastingTemplateGroups() {
        Cursor cursor = db.rawQuery("SELECT " + TastingTemplateSectionTable.COLUMN_NAME + " FROM " + TastingTemplateSectionTable.TABLE_NAME + " ORDER BY " + TastingTemplateSectionTable.COLUMN_SEQUENCE, null);
        CursorAssert.assertContents(cursor, "Appearance", "Nose", "Palate", "Conclusion");
    }

    public void testTastingTemplateAttributes() {
        assertTastingTemplateAttributes("Appearance", "Clarity", "Intensity", "Color");
        assertTastingTemplateAttributes("Nose", "Condition", "Intensity", "Development", "Aromas");
        assertTastingTemplateAttributes("Palate", "Sweetness", "Acidity", "Tannin", "Body", "Intensity", "Flavors", "Alcohol", "Length");
        assertTastingTemplateAttributes("Conclusion", "Quality", "Maturity");
    }

    public void testTastingTemplateAttributeGroups() {
        assertTastingTemplateAttibuteGroups("Appearance", "Color", "White", "Red");
        // TODO checkTastingTemplateAttibuteGroups(db, "Nose", "Aromas", ...);
        // TODO checkTastingTemplateAttibuteGroups(db, "Palate", "Flavors", ...);
    }

    public void testTastingTemplateAttributeValues() {
        assertTastingTemplateAttibuteValues("Appearance", "Clarity", "Clear", "Dull", "Cloudy", "Sediment", "Bubbles");
        assertTastingTemplateAttibuteGroupValues("Appearance", "Color", "White", "Colorless", "Straw", "Yellow", "Gold", "Amber");
        assertTastingTemplateAttibuteGroupValues("Appearance", "Color", "Red", "Tawny", "Garnet", "Ruby", "Purple");
        // TODO checkTastingTemplateAttibuteGroupValues(db, "Nose", "Aromas", ...)
        assertTastingTemplateAttibuteValues("Palate", "Tannin", "None", "Light", "Medium", "High");
        // TODO checkTastingTemplateAttibuteGroupValues(db, "Palate", "Flavors", ...)
        assertTastingTemplateAttibuteValues("Palate", "Length", "Short", "Medium", "Long");
        assertTastingTemplateAttibuteValues("Conclusion", "Maturity", "Immature", "Drink now or later", "Drink now", "Tired");
    }

    public void testRegions() {
        assertRegion("Australia");
        assertRegion("Australia", "New South Wales");
        assertRegion("Australia", "New South Wales", "Hunter Valley");
        assertRegion("Australia", "Tasmania", "Tamar Valley");
        assertRegion("Australia", "South Australia", "Barossa", "Barossa Valley");
        assertRegion("Australia", "South Australia", "Barossa", "Eden Valley");
        assertRegion("France", "Bordeaux", "Haut-Médoc");
        assertRegion("France", "Bordeaux", "Médoc");
        assertRegion("France", "Bordeaux", "Margaux");
        assertRegion("France", "Bordeaux", "Saint-Émilion");
        assertRegion("France", "Rhône", "Châteauneuf-du-Pape");
        assertRegion("France", "Rhône", "Crozes-Hermitage");
        assertRegion("United States", "California", "Napa Valley");
        assertRegion("Italy", "Tuscany", "Chianti");
    }

    public void testVarieties() {
        assertVariety("Pinot Noir");
        assertVariety("Shiraz");
        assertVariety("Cabernet Sauvignon");
        assertVariety("Grenache");
        assertVariety("Tempranillo");
        assertVariety("Sangiovese");
        assertVariety("Nebbiolo");
        assertVariety("Riesling");
        assertVariety("Chardonnay");
        assertVariety("Sauvignon Blanc");
    }

    public void testNewTastingTime() {
        long id = db.insert(TastingTable.TABLE_NAME, TastingTable.COLUMN_WINE, new ContentValues());
        Cursor cursor = db.rawQuery("SELECT " + TastingTable.COLUMN_DATE + " FROM " + TastingTable.TABLE_NAME + " WHERE " + TastingTable._ID + " = ?", new String[] { Long.toString(id) });
        assertTrue(cursor.moveToNext());
        long dateDiff = new Date().getTime() - TastingTable.parseDate(cursor.getString(0)).getTime();
        assertTrue(dateDiff < 1000);
        cursor.close();
    }

    void assertTastingTemplateAttributes(String group, String... expectedAttributes) {
        Cursor cursor = db.rawQuery(
                "SELECT " + TastingTemplateAttributeTable.TABLE_NAME + "." + TastingTemplateAttributeTable.COLUMN_NAME + " FROM " + TastingTemplateAttributeTable.TABLE_NAME +
                SqlHelper.innerJoin(TastingTemplateSectionTable.TABLE_NAME, TastingTemplateAttributeTable.TABLE_NAME, TastingTemplateAttributeTable.COLUMN_SECTION) +
                " WHERE " + TastingTemplateSectionTable.TABLE_NAME + "." + TastingTemplateSectionTable.COLUMN_NAME + " = ?" +
                " ORDER BY " + TastingTemplateAttributeTable.TABLE_NAME + "." + TastingTemplateAttributeTable.COLUMN_SEQUENCE, new String[] { group });
        CursorAssert.assertContents(cursor, expectedAttributes);
    }

    void assertTastingTemplateAttibuteValues(String group, String attribute, String... expectedValues) {
        Cursor cursor = db.rawQuery(
                "SELECT " + TastingTemplateAttributeValueTable.TABLE_NAME + "." + TastingTemplateAttributeValueTable.COLUMN_NAME + " FROM " + TastingTemplateAttributeValueTable.TABLE_NAME +
                        SqlHelper.innerJoin(TastingTemplateAttributeTable.TABLE_NAME, TastingTemplateAttributeValueTable.TABLE_NAME, TastingTemplateAttributeValueTable.COLUMN_ATTRIBUTE) +
                        SqlHelper.innerJoin(TastingTemplateSectionTable.TABLE_NAME, TastingTemplateAttributeTable.TABLE_NAME, TastingTemplateAttributeTable.COLUMN_SECTION) +
                        " WHERE " + TastingTemplateSectionTable.TABLE_NAME + "." + TastingTemplateSectionTable.COLUMN_NAME + " = ?" +
                        " AND " + TastingTemplateAttributeTable.TABLE_NAME + "." + TastingTemplateAttributeTable.COLUMN_NAME + " = ? " +
                        " AND " + TastingTemplateAttributeValueTable.TABLE_NAME + "." + TastingTemplateAttributeValueTable.COLUMN_ATTRIBUTEGROUP + " IS NULL " +
                        " ORDER BY " + TastingTemplateAttributeValueTable.TABLE_NAME + "." + TastingTemplateAttributeValueTable.COLUMN_SEQUENCE, new String[] { group, attribute });
        CursorAssert.assertContents(cursor, expectedValues);
    }

    void assertTastingTemplateAttibuteGroups(String group, String attribute, String... expectedAttributeGroups) {
        Cursor cursor = db.rawQuery(
                "SELECT " + TastingTemplateAttributeGroupTable.TABLE_NAME + "." + TastingTemplateAttributeGroupTable.COLUMN_NAME + " FROM " + TastingTemplateAttributeGroupTable.TABLE_NAME +
                        SqlHelper.innerJoin(TastingTemplateAttributeTable.TABLE_NAME, TastingTemplateAttributeGroupTable.TABLE_NAME, TastingTemplateAttributeGroupTable.COLUMN_ATTRIBUTE) +
                        SqlHelper.innerJoin(TastingTemplateSectionTable.TABLE_NAME, TastingTemplateAttributeTable.TABLE_NAME, TastingTemplateAttributeTable.COLUMN_SECTION) +
                        " WHERE " + TastingTemplateSectionTable.TABLE_NAME + "." + TastingTemplateSectionTable.COLUMN_NAME + " = ?" +
                        " AND " + TastingTemplateAttributeTable.TABLE_NAME + "." + TastingTemplateAttributeTable.COLUMN_NAME + " = ? " +
                        " ORDER BY " + TastingTemplateAttributeGroupTable.TABLE_NAME + "." + TastingTemplateAttributeGroupTable.COLUMN_SEQUENCE, new String[] { group, attribute });
        CursorAssert.assertContents(cursor, expectedAttributeGroups);
    }

    void assertTastingTemplateAttibuteGroupValues(String group, String attribute, String attributeGroup, String... expectedValues) {
        Cursor cursor = db.rawQuery(
                "SELECT " + TastingTemplateAttributeValueTable.TABLE_NAME + "." + TastingTemplateAttributeValueTable.COLUMN_NAME + " FROM " + TastingTemplateAttributeValueTable.TABLE_NAME +
                        SqlHelper.innerJoin(TastingTemplateAttributeTable.TABLE_NAME, TastingTemplateAttributeValueTable.TABLE_NAME, TastingTemplateAttributeValueTable.COLUMN_ATTRIBUTE) +
                        SqlHelper.innerJoin(TastingTemplateAttributeGroupTable.TABLE_NAME, TastingTemplateAttributeValueTable.TABLE_NAME, TastingTemplateAttributeValueTable.COLUMN_ATTRIBUTEGROUP) +
                        SqlHelper.innerJoin(TastingTemplateSectionTable.TABLE_NAME, TastingTemplateAttributeTable.TABLE_NAME, TastingTemplateAttributeTable.COLUMN_SECTION) +
                        " WHERE " + TastingTemplateSectionTable.TABLE_NAME + "." + TastingTemplateSectionTable.COLUMN_NAME + " = ?" +
                        " AND " + TastingTemplateAttributeTable.TABLE_NAME + "." + TastingTemplateAttributeTable.COLUMN_NAME + " = ? " +
                        " AND " + TastingTemplateAttributeGroupTable.TABLE_NAME + "." + TastingTemplateAttributeGroupTable.COLUMN_NAME + " = ? " +
                        " ORDER BY " + TastingTemplateAttributeValueTable.TABLE_NAME + "." + TastingTemplateAttributeValueTable.COLUMN_SEQUENCE, new String[] { group, attribute, attributeGroup });
        CursorAssert.assertContents(cursor, expectedValues);
    }

    void assertRegion(String... heirarchy) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM " ).append(RegionTable.TABLE_NAME).append(" AS r0 ");
        for(int i = 1; i < heirarchy.length; i++) {
            sql.append("INNER JOIN ").append(RegionTable.TABLE_NAME).append(" AS r").append(i).append(" ON r").append(i).append(".").append(RegionTable.COLUMN_PARENT).append(" = ").append("r").append(i - 1).append(".").append(RegionTable._ID).append(" ");
        }
        sql.append("WHERE r0.").append(RegionTable.COLUMN_NAME).append(" = ? ");
        for(int i = 1; i < heirarchy.length; i++) {
            sql.append("AND r").append(i).append(".").append(RegionTable.COLUMN_NAME).append(" = ? ");
        }
        Cursor cursor = db.rawQuery(sql.toString(), heirarchy);
        assertTrue(cursor.moveToNext());
        assertEquals(1, cursor.getInt(0));
        cursor.close();
    }

    void assertVariety(String variety) {
        Cursor cursor = db.rawQuery("SELECT " + VarietyTable.COLUMN_NAME + " FROM " + VarietyTable.TABLE_NAME + " WHERE " + VarietyTable.COLUMN_NAME + " = ? COLLATE NOCASE", new String[] { variety });
        CursorAssert.assertContents(cursor, variety);
    }
}
