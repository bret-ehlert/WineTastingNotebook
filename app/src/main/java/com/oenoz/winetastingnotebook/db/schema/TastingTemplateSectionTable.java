package com.oenoz.winetastingnotebook.db.schema;

public class TastingTemplateSectionTable extends TastingTemplateBaseColumns {
    public static final String TABLE_NAME = "tastingTemplateSection";

    public static String GetCreateSql() {
        return GetCreateSql(TABLE_NAME);
    }
}
