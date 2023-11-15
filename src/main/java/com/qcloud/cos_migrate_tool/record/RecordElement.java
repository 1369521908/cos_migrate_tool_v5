package com.qcloud.cos_migrate_tool.record;

import com.qcloud.cos_migrate_tool.config.MigrateType;
import com.qcloud.migrate_done.RecordElementInfo;

public abstract class RecordElement {
    protected MigrateType recordType;

    public RecordElement(MigrateType recordType) {
        super();
        this.recordType = recordType;
    }

    public abstract String buildKey();

    public abstract String buildValue();

    public abstract RecordElementInfo buildRecord();

    @Override
    public String toString() {
        String str = String.format("[record_type: %s], [key: %s], [value: %s]",
                recordType.toString(), buildKey(), buildValue());
        return str;
    }
}
