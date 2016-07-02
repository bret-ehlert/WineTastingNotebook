package com.oenoz.winetastingnotebook.db.schema;

public class WineryTable extends ReferenceItemBaseColumns {
    public static final String TABLE_NAME = "winery";

    public static String getCreateSql() {
        return getCreateSql(TABLE_NAME);
    }
}
