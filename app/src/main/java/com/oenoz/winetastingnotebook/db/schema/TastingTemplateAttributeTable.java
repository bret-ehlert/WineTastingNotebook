package com.oenoz.winetastingnotebook.db.schema;

public class TastingTemplateAttributeTable extends TastingTemplateBaseColumns {
    public static final String TABLE_NAME = "tastingTemplateAttribute";
    public static final String COLUMN_GROUP = TastingTemplateGroupTable.TABLE_NAME;

    public static String GetCreateSql() {
        return GetCreateSql(TABLE_NAME,
                COLUMN_GROUP + " " + ID_DATATYPE + " NOT NULL",
                " FOREIGN KEY (" + COLUMN_GROUP + ") REFERENCES " + TastingTemplateGroupTable.TABLE_NAME + "(" + TastingTemplateGroupTable._ID + ")"
        );
    }
}
