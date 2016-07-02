package com.oenoz.winetastingnotebook.db.schema;

import android.provider.BaseColumns;

public class WineTable implements BaseColumns {
    public static final String TABLE_NAME = "wine";
    public static final String COLUMN_WINERY = WineTable.TABLE_NAME;
    public static final String COLUMN_REGION = RegionTable.TABLE_NAME;
    public static final String COLUMN_VARIETY = VarietyTable.TABLE_NAME;
    public static final String COLUMN_VINTAGE = "vintage";
    public static final String COLUMN_NAME = "name";

    public static String getCreateSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
        sql.append(_ID).append(" INTEGER PRIMARY KEY, ");
        sql.append(COLUMN_WINERY).append(" INTEGER NOT NULL, ");
        sql.append(COLUMN_REGION).append(" INTEGER NOT NULL, ");
        sql.append(COLUMN_VARIETY).append(" INTEGER NOT NULL, ");
        sql.append(COLUMN_VINTAGE).append(" INTEGER, ");
        sql.append(COLUMN_NAME).append(" TEXT, ");
        sql.append(" FOREIGN KEY (").append(COLUMN_WINERY).append(") REFERENCES ").append(WineryTable.TABLE_NAME).append("(").append(WineryTable._ID).append("), ");
        sql.append(" FOREIGN KEY (").append(COLUMN_REGION).append(") REFERENCES ").append(RegionTable.TABLE_NAME).append("(").append(RegionTable._ID).append("), ");
        sql.append(" FOREIGN KEY (").append(COLUMN_VARIETY).append(") REFERENCES ").append(VarietyTable.TABLE_NAME).append("(").append(VarietyTable._ID).append(")");
        sql.append(");");
        return sql.toString();
    }
}
