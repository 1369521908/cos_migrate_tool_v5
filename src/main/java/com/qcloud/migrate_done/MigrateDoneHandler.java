package com.qcloud.migrate_done;

import cn.hutool.core.date.DateUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.nosql.redis.RedisDS;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.Setting;
import com.qcloud.cos_migrate_tool.record.RecordElement;
import redis.clients.jedis.Jedis;

import java.sql.SQLException;

public class MigrateDoneHandler {

    public static final String TAG = "migrate-done-handle";

    private static String cacheKey;

    private static String machine;

    private static Boolean doneFlag;

    private static Boolean writeDb;

    private static Jedis jedis;

    private static class MigrateDone {
        // 是否已迁移完成
        private boolean done = false;
        // 迁移完成时间
        private String time;

        public boolean isDone() {
            return done;
        }

        public void setDone(boolean done) {
            this.done = done;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

    private static Db db;

    // 初始化必要的信息
    public static void init() {
        //读取classpath下的XXX.setting，不使用变量
        Setting setting = new Setting("config/migrate.setting");
        cacheKey = setting.getByGroup("cacheKey", "migrate");
        machine = setting.getByGroup("machine", "migrate");
        doneFlag = setting.getBool("doneFlag", "migrate");
        writeDb = setting.getBool("writeDb", "migrate");
        RedisDS redisDS = RedisDS.create();
        jedis = redisDS.getJedis();
        // 测试是否读取到正确的配置
        jedis.set(cacheKey + ":test:" + machine, "test machine " + machine);
        System.out.println("init config redis" + redisDS.getJedis().toString());
        System.out.println("init config setting" + setting.toString());

        // 测试是否读取到正确的配置
        db = Db.use();
        try {
            Entity entity = db.queryOne("select 1");
            System.out.println("entity" + JSONUtil.toJsonStr(entity));
            System.out.println("init config mysql success " + db.getConnection().getMetaData().getURL());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 迁移完成后的处理
    public static void done(String opStatus) {
        System.out.println("migrate status " + opStatus);

        // 迁移指标
        // TaskStatics instance = TaskStatics.instance;

        // 所有都不成功
        if (!"ALL_OK".equals(opStatus)) {
            System.out.println("not all done doing nothing");
            return;
        }

        // 写入迁移成功标记
        if (doneFlag) {
            String migrateKey = String.format("%s:%s", cacheKey, machine);
            MigrateDone migrateDone = new MigrateDone();
            migrateDone.setDone(true);
            String now = DateUtil.formatDateTime(DateUtil.date());
            migrateDone.setTime(now);
            jedis.set(migrateKey, JSONUtil.toJsonStr(migrateDone));
            System.out.println("migrate all done set redis " + jedis.toString() + " key " + migrateKey);
        }
    }

    // 迁移的新文件保存一份到mysql fastdfs_cos 数据库
    public static void save(RecordElement recordElement) {
        if (!writeDb) {
            return;
        }

        try {
            RecordElementInfo save = recordElement.buildRecord();
            db.insert(Entity.create("record_element_info")
                    .set("recordType", save.getRecordType().toString())
                    .set("createTime", DateUtil.date())
                    .set("bucketName", save.getBucketName())
                    .set("localPath", save.getLocalPath())
                    // 保存修正后的文件名
                    .set("cosPath", save.getCosPathFix())
                    .set("host", machine)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
