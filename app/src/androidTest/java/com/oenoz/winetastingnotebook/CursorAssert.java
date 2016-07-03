package com.oenoz.winetastingnotebook;

import android.database.Cursor;
import android.test.MoreAsserts;

import java.util.ArrayList;

public class CursorAssert {
    public static void assertContents(Cursor cursor, String... expected) {
        assertContents(cursor, new ToString() {
            @Override
            public String toString(Cursor cursor) {
                return cursor.getString(0);
            }
        }, expected);
    }

    public static void assertContents(Cursor cursor, ToString tostring, String... expected) {
        ArrayList<String> actual = new ArrayList<>();
        while (cursor.moveToNext()) {
            actual.add(tostring.toString(cursor));
        }
        cursor.close();
        MoreAsserts.assertContentsInOrder(actual, expected);
    }

    public interface ToString {
        public String toString(Cursor cursor);
    }
}
