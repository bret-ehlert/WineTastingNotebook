package com.oenoz.winetastingnotebook.db.schema;

public class TastingTemplateGroupTable extends TastingTemplateBaseColumns {
    public static final String TABLE_NAME = "tastingTemplateGroup";

    public static String GetCreateSql() {
        return GetCreateSql(TABLE_NAME);
    }
}
