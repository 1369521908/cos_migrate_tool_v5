/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.9.58
 Source Server Type    : MySQL
 Source Server Version : 80021 (8.0.21)
 Source Host           : 192.168.9.58:3306
 Source Schema         : fastdfs_cos

 Target Server Type    : MySQL
 Target Server Version : 80021 (8.0.21)
 File Encoding         : 65001

 Date: 15/11/2023 16:19:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for record_element_info
-- ----------------------------
DROP TABLE IF EXISTS `record_element_info`;
CREATE TABLE `record_element_info`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `createTime` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `recordType` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '迁移类型',
  `bucketName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '桶名称',
  `localPath` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '本地路径',
  `cosPath` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'cos路径',
  `host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'fastdfs服务器ip',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3806 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
