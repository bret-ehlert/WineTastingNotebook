package com.oenoz.winetastingnotebook.db;

import android.provider.BaseColumns;

public abstract class SqlHelper {
    public static String innerJoin(String toTable, String fromTable, String fromColumn) {
        return " INNER JOIN " + toTable + " ON " + toTable + "." + BaseColumns._ID + " = " + fromTable + "." + fromColumn;
    }
}
