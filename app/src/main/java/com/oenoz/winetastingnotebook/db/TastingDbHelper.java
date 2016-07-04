package com.oenoz.winetastingnotebook.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.oenoz.winetastingnotebook.R;
import com.oenoz.winetastingnotebook.db.schema.ReferenceItemBaseColumns;
import com.oenoz.winetastingnotebook.db.schema.RegionTable;
import com.oenoz.winetastingnotebook.db.schema.TastingTable;
import com.oenoz.winetastingnotebook.db.schema.TastingTemplateAttributeGroupTable;
import com.oenoz.winetastingnotebook.db.schema.TastingTemplateAttributeTable;
import com.oenoz.winetastingnotebook.db.schema.TastingTemplateAttributeValueTable;
import com.oenoz.winetastingnotebook.db.schema.TastingTemplateBaseColumns;
import com.oenoz.winetastingnotebook.db.schema.TastingTemplateSectionTable;
import com.oenoz.winetastingnotebook.db.schema.TastingValueTable;
import com.oenoz.winetastingnotebook.db.schema.VarietyTable;
import com.oenoz.winetastingnotebook.db.schema.WineTable;
import com.oenoz.winetastingnotebook.db.schema.WineryTable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TastingDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "tasting.db";
    private static final String LOG_TAG = TastingDbHelper.class.getSimpleName();

    Context mContext;

    public TastingDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TastingTemplateSectionTable.GetCreateSql());
        db.execSQL(TastingTemplateAttributeTable.GetCreateSql());
        db.execSQL(TastingTemplateAttributeGroupTable.GetCreateSql());
        db.execSQL(TastingTemplateAttributeValueTable.GetCreateSql());
        populateTemplate(db);

        db.execSQL(RegionTable.getCreateSql());
        populateRegions(db);

        db.execSQL(VarietyTable.getCreateSql());
        populateVarieties(db);

        db.execSQL(WineryTable.getCreateSql());
        db.execSQL(WineTable.getCreateSql());
        db.execSQL(TastingTable.getCreateSql());
        db.execSQL(TastingValueTable.getCreateSql());
    }

    void populateTemplate(SQLiteDatabase db) {
        InputStream stream = mContext.getResources().openRawResource(R.raw.tasting_template);
        try {
            db.beginTransaction();

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            byte templateGroupSequence = 0;
            byte templateAttributeSequence = 0;
            byte templateAttributeGroupSequence = 0;
            byte templateAttributeValueSequence = 0;
            long templateGroupId = -1;
            long templateAttributeId = -1;
            long templateAttributeGroupId = -1;
            while ((line = reader.readLine()) != null) {
                int tabs = line.lastIndexOf("\t");
                if (tabs == -1) {
                    ContentValues contentValues = TastingTemplateBaseColumns.getContentValues(line.trim(), templateGroupSequence++, true);
                    templateGroupId = db.insert(TastingTemplateSectionTable.TABLE_NAME, null, contentValues);
                    templateAttributeSequence = 0;
                    templateAttributeId = -1;
                    templateAttributeGroupSequence = 0;
                    templateAttributeGroupId = -1;
                }
                else if(tabs == 0) {
                    ContentValues contentValues = TastingTemplateBaseColumns.getContentValues(line.trim(), templateAttributeSequence++, true);
                    contentValues.put(TastingTemplateAttributeTable.COLUMN_SECTION, templateGroupId);
                    templateAttributeId = db.insert(TastingTemplateAttributeTable.TABLE_NAME, null, contentValues);
                    templateAttributeGroupSequence = 0;
                    templateAttributeGroupId = -1;
                    templateAttributeValueSequence = 0;
                }
                else if(tabs == 1) {
                    ContentValues contentValues = TastingTemplateBaseColumns.getContentValues(line.trim(), templateAttributeGroupSequence++, true);
                    contentValues.put(TastingTemplateAttributeGroupTable.COLUMN_ATTRIBUTE, templateAttributeId);
                    templateAttributeGroupId = db.insert(TastingTemplateAttributeGroupTable.TABLE_NAME, null, contentValues);
                    templateAttributeValueSequence = 0;
                }
                else {
                    ContentValues contentValues = TastingTemplateBaseColumns.getContentValues(line.trim(), templateAttributeValueSequence++, true);
                    contentValues.put(TastingTemplateAttributeValueTable.COLUMN_ATTRIBUTE, templateAttributeId);
                    if (templateAttributeGroupId > -1) {
                        contentValues.put(TastingTemplateAttributeValueTable.COLUMN_ATTRIBUTEGROUP, templateAttributeGroupId);
                    }
                    db.insert(TastingTemplateAttributeValueTable.TABLE_NAME, null, contentValues);
                }
            }

            db.setTransactionSuccessful();
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "populateTemplate failed", e);
        }
        finally {
            db.endTransaction();
            try {
                stream.close();
            }
            catch (IOException e) {
                Log.w(LOG_TAG, "error closing tasting template stream", e);
            }
        }
    }

    void populateRegions(SQLiteDatabase db) {
        InputStream stream = mContext.getResources().openRawResource(R.raw.regions);
        try {
            db.beginTransaction();

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            long[] ids = new long[5];
            while ((line = reader.readLine()) != null) {
                int level = line.lastIndexOf("\t") + 1;
                ContentValues contentValues = ReferenceItemBaseColumns.getContentValues(line.trim());
                if(level > 0) {
                    contentValues.put(RegionTable.COLUMN_PARENT, ids[level - 1]);
                }
                ids[level] = db.insert(RegionTable.TABLE_NAME, null, contentValues);
                for (int i = level + 1; i < ids.length; i++) {
                    ids[i] = -1;
                }
            }

            db.setTransactionSuccessful();
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "populateRegions failed", e);
        }
        finally {
            db.endTransaction();
            try {
                stream.close();
            }
            catch (IOException e) {
                Log.w(LOG_TAG, "error closing regions file stream", e);
            }
        }
    }

    void populateVarieties(SQLiteDatabase db) {
        InputStream stream = mContext.getResources().openRawResource(R.raw.varieties);
        try {
            db.beginTransaction();

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) {
                ContentValues contentValues = ReferenceItemBaseColumns.getContentValues(line.trim());
                db.insert(VarietyTable.TABLE_NAME, null, contentValues);
            }

            db.setTransactionSuccessful();
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "populateVarieties failed", e);
        }
        finally {
            db.endTransaction();
            try {
                stream.close();
            }
            catch (IOException e) {
                Log.w(LOG_TAG, "error closing varieties file stream", e);
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
