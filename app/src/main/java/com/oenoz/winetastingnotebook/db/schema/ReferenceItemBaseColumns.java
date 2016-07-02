package com.oenoz.winetastingnotebook.db.schema;

import android.content.ContentValues;
import android.provider.BaseColumns;

public abstract class ReferenceItemBaseColumns implements BaseColumns {
    public static final String COLUMN_NAME = "name";

    protected static String ID_DATATYPE = "INTEGER";

    protected static String getCreateSql(String tableName, String... elements) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE ").append(tableName).append(" (");
        sql.append(_ID).append(" ").append(ID_DATATYPE).append(" PRIMARY KEY, ");
        sql.append(COLUMN_NAME).append(" TEXT NOT NULL");
        if (elements.length > 0) {
            sql.append(", ");
            for (int i = 0; i < elements.length - 1; i++) {
                sql.append(elements[i]).append(", ");
            }
            sql.append(elements[elements.length - 1]);
        }
        sql.append(");");
        return sql.toString();
    }

    public static ContentValues getContentValues(String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        return contentValues;
    }
}
