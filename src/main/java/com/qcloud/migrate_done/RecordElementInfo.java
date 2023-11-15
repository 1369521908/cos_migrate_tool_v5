package com.qcloud.migrate_done;

import com.qcloud.cos_migrate_tool.config.MigrateType;

public class RecordElementInfo {

    private MigrateType recordType;

    private String bucketName;

    private String localPath;

    private String cosPath;

    public RecordElementInfo() {
    }

    public RecordElementInfo(MigrateType recordType, String bucketName, String localPath, String cosPath) {
        this.recordType = recordType;
        this.bucketName = bucketName;
        this.localPath = localPath;
        this.cosPath = cosPath;
    }

    public MigrateType getRecordType() {
        return recordType;
    }

    public void setRecordType(MigrateType recordType) {
        this.recordType = recordType;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getCosPath() {
        return cosPath;
    }

    /**
     * 返回修正后的cos path 只是为了和业务系统保持一致
     * 其实只是相差一个开头的 "/" 因为迁移工具必须 "/" 开头
     * @return
     */
    public String getCosPathFix() {
        if (cosPath.startsWith("/")) {
            String cosPath1 = cosPath.substring(1);
            return cosPath1;
        }

        return cosPath;
    }

    public void setCosPath(String cosPath) {
        this.cosPath = cosPath;
    }
}
