package com.oenoz.winetastingnotebook.db.schema;

public class TastingTemplateAttributeValueTable extends TastingTemplateBaseColumns {
    public static final String TABLE_NAME = "tastingTemplateAttributeValue";
    public static final String COLUMN_ATTRIBUTE = TastingTemplateAttributeTable.TABLE_NAME;
    public static final String COLUMN_ATTRIBUTEGROUP = TastingTemplateAttributeGroupTable.TABLE_NAME;

    public static String GetCreateSql() {
        return GetCreateSql(TABLE_NAME,
                COLUMN_ATTRIBUTE + " " + ID_DATATYPE + " NOT NULL",
                COLUMN_ATTRIBUTEGROUP + " " + ID_DATATYPE,
                " FOREIGN KEY (" + COLUMN_ATTRIBUTE + ") REFERENCES " + TastingTemplateAttributeTable.TABLE_NAME + "(" + TastingTemplateAttributeTable._ID + ")",
                " FOREIGN KEY (" + COLUMN_ATTRIBUTEGROUP + ") REFERENCES " + TastingTemplateAttributeGroupTable.TABLE_NAME + "(" + TastingTemplateAttributeGroupTable._ID + ")"
        );
    }
}
