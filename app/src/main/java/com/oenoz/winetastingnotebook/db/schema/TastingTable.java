package com.oenoz.winetastingnotebook.db.schema;

import android.provider.BaseColumns;

public class TastingTable implements BaseColumns {
    public static final String TABLE_NAME = "tasting";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_WINE = "wine";
    public static final String COLUMN_COMMNETS = "comments";

    public static String getCreateSql(){
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
        sql.append(_ID).append(" INTEGER PRIMARY KEY, ");
        sql.append(COLUMN_DATE).append(" INTEGER NOT NULL, ");
        sql.append(COLUMN_LOCATION).append(" TEXT, ");
        sql.append(COLUMN_IMAGE).append(" TEXT, ");
        sql.append(COLUMN_WINE).append(" INTEGER NOT NULL, ");
        sql.append(COLUMN_COMMNETS).append(" TEXT, ");
        sql.append(" FOREIGN KEY (").append(COLUMN_WINE).append(") REFERENCES ").append(WineTable.TABLE_NAME).append("(").append(WineTable._ID).append(")");
        sql.append(");");
        return sql.toString();
    }
}
