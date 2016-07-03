package com.oenoz.winetastingnotebook.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.oenoz.winetastingnotebook.db.SqlHelper;
import com.oenoz.winetastingnotebook.db.TastingDbHelper;
import com.oenoz.winetastingnotebook.db.schema.TastingTable;
import com.oenoz.winetastingnotebook.db.schema.TastingTemplateAttributeGroupTable;
import com.oenoz.winetastingnotebook.db.schema.TastingTemplateAttributeTable;
import com.oenoz.winetastingnotebook.db.schema.TastingTemplateGroupTable;

public class TastingContentProvider extends ContentProvider {
    public static final String CONTENT_AUTHORITY = "com.oenoz.winetastingnotebook";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private class UrlTypes {
        public static final int TASTING = 100;
        public static final int TASTING_SECTIONS = 101;
        public static final int TASTING_SECTION = 102;
    }

    private TastingDbHelper mDbHelper;

    static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(CONTENT_AUTHORITY, TastingContentUri.PATH_TASTING, UrlTypes.TASTING);
        String tastingItemPath = TastingContentUri.PATH_TASTING + "/#/";
        uriMatcher.addURI(CONTENT_AUTHORITY, tastingItemPath + TastingContentUri.PATH_TASTING_SECTIONS, UrlTypes.TASTING_SECTIONS);
        uriMatcher.addURI(CONTENT_AUTHORITY, tastingItemPath + TastingContentUri.PATH_TASTING_SECTION + "/#/", UrlTypes.TASTING_SECTION);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new TastingDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final int urlType = sUriMatcher.match(uri);

        switch (urlType) {
            case UrlTypes.TASTING_SECTIONS:
                return queryTastingSections(uri, projection, selection, selectionArgs, sortOrder);

            default:
                throw new UnsupportedOperationException("Query operation not supported: " + uri);
        }
    }

    Cursor queryTastingSections(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        long tastingId = Long.parseLong(uri.getPathSegments().get(1));
        return mDbHelper.getReadableDatabase().query(
                "(SELECT " + TastingTemplateAttributeTable.TABLE_NAME + "." + TastingTemplateAttributeTable._ID + " AS " + TastingContentSchema.ID + ", " +
                            TastingTemplateGroupTable.TABLE_NAME + "." + TastingTemplateGroupTable.COLUMN_NAME + " AS " + TastingContentSchema.TASTING_SECTION_GROUP_NAME + ", " +
                            TastingTemplateAttributeTable.TABLE_NAME + "." + TastingTemplateAttributeTable.COLUMN_NAME + " AS " + TastingContentSchema.TASTING_SECTION_ATTRIBUTE_NAME + " " +
                "FROM " + TastingTemplateAttributeTable.TABLE_NAME + " " +
                SqlHelper.innerJoin(TastingTemplateGroupTable.TABLE_NAME, TastingTemplateAttributeTable.TABLE_NAME, TastingTemplateAttributeTable.COLUMN_GROUP) + " " +
                " ORDER BY " + TastingTemplateGroupTable.TABLE_NAME + "." + TastingTemplateGroupTable.COLUMN_SEQUENCE + ", " + TastingTemplateAttributeTable.TABLE_NAME + "." + TastingTemplateAttributeTable.COLUMN_SEQUENCE + ")",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int urlType = sUriMatcher.match(uri);
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Uri returnUri;

        switch (urlType) {
            case UrlTypes.TASTING: {
                long id = db.insert(TastingTable.TABLE_NAME, TastingTable.COLUMN_WINE, values);
                returnUri = TastingContentUri.forTasting(id);
                break;
            }

            default:
                throw new UnsupportedOperationException("Insert operation not supported: " + uri);
        }

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
