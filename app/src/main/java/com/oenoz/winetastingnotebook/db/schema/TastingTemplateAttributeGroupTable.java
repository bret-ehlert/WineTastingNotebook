package com.oenoz.winetastingnotebook.db.schema;

public class TastingTemplateAttributeGroupTable extends TastingTemplateBaseColumns {
    public static final String TABLE_NAME = "tastingTemplateAttributeGroup";
    public static final String COLUMN_ATTRIBUTE = TastingTemplateAttributeTable.TABLE_NAME;

    public static String GetCreateSql() {
        return GetCreateSql(TABLE_NAME,
                COLUMN_ATTRIBUTE + " " + ID_DATATYPE + " NOT NULL",
                " FOREIGN KEY (" + COLUMN_ATTRIBUTE + ") REFERENCES " + TastingTemplateAttributeTable.TABLE_NAME + "(" + TastingTemplateAttributeTable._ID + ")"
        );
    }
}
