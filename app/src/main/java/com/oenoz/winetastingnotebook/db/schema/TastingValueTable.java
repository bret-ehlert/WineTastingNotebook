package com.oenoz.winetastingnotebook.db.schema;

import android.provider.BaseColumns;

public class TastingValueTable implements BaseColumns {
    public static final String TABLE_NAME = "tastingValue";
    public static final String COLUMN_TASTING = "tasting";
    public static final String COLUMN_VALUE = "value";

    public static String getCreateSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
        sql.append(_ID).append(" INTEGER PRIMARY KEY, ");
        sql.append(COLUMN_TASTING).append(" INTEGER NOT NULL, ");
        sql.append(COLUMN_VALUE).append(" INTEGER NOT NULL, ");
        sql.append(" FOREIGN KEY (").append(COLUMN_TASTING).append(") REFERENCES ").append(TastingTable.TABLE_NAME).append("(").append(TastingTable._ID).append("), ");
        sql.append(" FOREIGN KEY (").append(COLUMN_VALUE).append(") REFERENCES ").append(TastingTemplateAttributeValueTable.TABLE_NAME).append("(").append(TastingTemplateAttributeValueTable._ID).append(")");
        sql.append(");");
        return sql.toString();
    }
}
