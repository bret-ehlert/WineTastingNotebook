package com.oenoz.winetastingnotebook.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.oenoz.winetastingnotebook.db.TastingDbHelper;
import com.oenoz.winetastingnotebook.db.schema.TastingTable;
import com.oenoz.winetastingnotebook.db.schema.TastingTemplateAttributeTable;
import com.oenoz.winetastingnotebook.db.schema.TastingTemplateSectionTable;

public class TastingContentProvider extends ContentProvider {
    public static final String CONTENT_AUTHORITY = "com.oenoz.winetastingnotebook";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private class UrlTypes {
        public static final int TASTING = 100;
        public static final int TASTING_SECTIONS = 101;
        public static final int TASTING_SECTION_ATTRIBUTES = 102;
    }

    private class IdSegments {
        public static final int TASTING = 1;
        public static final int TASTING_SECTION = 3;
    }

    private TastingDbHelper mDbHelper;

    static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(CONTENT_AUTHORITY, TastingContentUri.PATH_TASTING, UrlTypes.TASTING);
        String tastingItemPath = TastingContentUri.PATH_TASTING + "/#/";
        uriMatcher.addURI(CONTENT_AUTHORITY, tastingItemPath + TastingContentUri.PATH_TASTING_SECTIONS, UrlTypes.TASTING_SECTIONS);
        uriMatcher.addURI(CONTENT_AUTHORITY, tastingItemPath + TastingContentUri.PATH_TASTING_SECTION + "/#/" + TastingContentUri.PATH_TASTING_SECTION_ATTRIBUTES, UrlTypes.TASTING_SECTION_ATTRIBUTES);
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

            case UrlTypes.TASTING_SECTION_ATTRIBUTES:
                return queryTastingSectionAttributes(uri, projection, selection, selectionArgs, sortOrder);

            default:
                throw new UnsupportedOperationException("Query operation not supported: " + uri);
        }
    }

    Cursor queryTastingSections(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // TODO long tastingId = Long.parseLong(uri.getPathSegments().get(IdSegments.TASTING));
        return mDbHelper.getReadableDatabase().query(TastingTemplateSectionTable.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                TastingTemplateSectionTable.COLUMN_SEQUENCE);
    }

    Cursor queryTastingSectionAttributes(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String sectionId = uri.getPathSegments().get(IdSegments.TASTING_SECTION);
        return mDbHelper.getReadableDatabase().query(TastingTemplateAttributeTable.TABLE_NAME,
                projection,
                TastingTemplateAttributeTable.COLUMN_SECTION + " = ?",
                new String[] { sectionId },
                null,
                null,
                TastingTemplateSectionTable.COLUMN_SEQUENCE);
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
