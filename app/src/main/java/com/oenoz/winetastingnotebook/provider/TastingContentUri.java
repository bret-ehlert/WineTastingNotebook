package com.oenoz.winetastingnotebook.provider;

import android.content.ContentUris;
import android.net.Uri;

public class TastingContentUri {
    public static final String PATH_TASTING = "tasting";
    public static final String PATH_TASTING_SECTIONS = "sections";
    public static final String PATH_TASTING_SECTION = "section";

    public static Uri forTasting() {
        return TastingContentProvider.BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASTING).build();
    }

    public static Uri forTasting(long tastingId) {
        return ContentUris.appendId(forTasting().buildUpon(), tastingId).build();
    }

    public static Uri forTastingSections(Uri tasting) {
        return tasting.buildUpon().appendPath(PATH_TASTING_SECTIONS).build();
    }

    public static Uri forTastingSection(Uri tasting, long sectionId) {
        return ContentUris.appendId(tasting.buildUpon().appendPath(PATH_TASTING_SECTION), sectionId).build();
    }
}
