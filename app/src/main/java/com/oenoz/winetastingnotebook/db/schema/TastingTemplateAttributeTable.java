package com.oenoz.winetastingnotebook.db.schema;

public class TastingTemplateAttributeTable extends TastingTemplateBaseColumns {
    public static final String TABLE_NAME = "tastingTemplateAttribute";
    public static final String COLUMN_SECTION = TastingTemplateSectionTable.TABLE_NAME;

    public static String GetCreateSql() {
        return GetCreateSql(TABLE_NAME,
                COLUMN_SECTION + " " + ID_DATATYPE + " NOT NULL",
                " FOREIGN KEY (" + COLUMN_SECTION + ") REFERENCES " + TastingTemplateSectionTable.TABLE_NAME + "(" + TastingTemplateSectionTable._ID + ")"
        );
    }
}
