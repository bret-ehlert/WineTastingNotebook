package com.oenoz.winetastingnotebook.db.schema;

public class RegionTable extends ReferenceItemBaseColumns {
    public static final String TABLE_NAME = "region";
    public static final String COLUMN_PARENT = "parentRegion";

    public static String getCreateSql() {
        return getCreateSql(TABLE_NAME,
                COLUMN_PARENT + " " + ID_DATATYPE,
                " FOREIGN KEY (" + COLUMN_PARENT + ") REFERENCES " + RegionTable.TABLE_NAME + "(" + RegionTable._ID + ")"
        );
    }
}
