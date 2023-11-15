# 迁移工具

## fastdfs迁移说明
- 如果需要单独记录下来迁移的文件到数据库请先建库(fastdfs_cos)后执行sql
- 迁移到cos的文件默认添加了/group1/M00文件夹前缀 如果不需要

## docker 部署说明
- 创建目录
```shell
mkdir /cos_migrate_tool_v5
```
- 编译后拷贝整工程目录后解压

- 构建docker镜像(如果你的服务器中已经有jdk8基础镜像 可以修改Dockerfile中的基础镜像)
```shell
docker-compose build
```

- 修改docker-compose中的fastdfs目录为你自己服务器中fastdfs的data目录
```
/xxx/fastdfs/storage/data/:/xxx/fastdfs/storage/data/
```

- [可选]如果迁移后的key需要跟原来fastdfs的fileid一致 
请修改conf/config.ini的cosPath为/group1/M00
其中M00为你fastdfs机器的tracker(机器) 需要根据自己fastdfs服务情况进行判断
```
cosPath=/group1/M00
```

- [可选]如果迁移后需要往业务服务的redis中写入

- 其他需要修改的配置参考原文档即可

- 启动容器
```shell
docker-compose up -d
```

## 功能说明

迁移工具集成了有关COS数据迁移的功能, 目前支持以下四大类迁移
- 本地数据迁移到COS, 功能同之前的本地同步工具
- 友商数据迁移到COS, 目前支持aws s3, 阿里云oss, 七牛存储, 又拍云存储
- 根据url下载列表进行下载迁移
- COS的bucket数据相互复制, 支持跨账号跨地域的数据复制

## 运行依赖
- JDK1.8或以上, 有关JDK的安装请参考[JAVA安装与配置](https://cloud.tencent.com/document/product/436/10865)
- linux或windows环境, 推荐linux

## 打包方式：
如果需要修改源码，重新打包，需要先安装maven并配置环境变量，确保maven可用。
- 在windows环境下，进入opbin目录，直接双击运行rebuild.bat，新生成的cos_migrate_tool-1.4.8-jar-with-dependencies.jar会复制至dep目录下；
- 在Linux环境下，进入opbin目录，直接`sh rebuild.sh`，新生成的cos_migrate_tool-1.4.8-jar-with-dependencies.jar会复制至dep目录下；

# 使用范例
1. 配置全部通过配置文件读入
sh start_migrate.sh
2. 指定部分配置项以命令行为主.
sh start_migrate.sh -DmigrateLocal.localPath=/test_data/aaa/ -Dcommon.cosPath=/aaa
sh start_migrate.sh -DmigrateAws.prefix=/test_data/bbb/ -Dcommon.cosPath=/bbb

## 迁移机制

迁移工具是有状态的，已经迁移成功的会记录在db目录下，以KV的形式存储在leveldb文件中. 
每次迁移前对要迁移的路径, 先查找下DB中是否存在, 如果存在，且属性和db中存在的一致, 则跳过迁移, 否则进行迁移。这里的属性根据迁移类型的不同而不同，对于本地迁移，会判断mtime。对于友商与bucket复制，会判断源文件的etag和长度是否与db一致。
因此，我们参照的db中是否有过迁移成功的记录，而不是查找COS，如果绕过了迁移工具，通过别的方式(比如coscmd或者控制台)删除修改了文件，那么运行迁移工具由于不会察觉到这种变化，是不会重新迁移的。

## 其他
请参照COS迁移工具[官网文档](https://cloud.tencent.com/document/product/436/15392)
