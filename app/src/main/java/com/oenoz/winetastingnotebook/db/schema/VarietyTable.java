package com.oenoz.winetastingnotebook.db.schema;

public class VarietyTable extends ReferenceItemBaseColumns {
    public static final String TABLE_NAME = "variety";

    public static String getCreateSql() {
        return getCreateSql(TABLE_NAME);
    }
}
