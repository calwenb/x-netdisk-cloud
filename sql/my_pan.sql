/*
 Navicat Premium Data Transfer

 Source Server         : 服务器
 Source Server Type    : MySQL
 Source Server Version : 50734
 Source Host           : XXX:3306
 Source Schema         : my_pan

 Target Server Type    : MySQL
 Target Server Version : 50734
 File Encoding         : 65001

 Date: 20/02/2022 16:22:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for file_folder
-- ----------------------------
DROP TABLE IF EXISTS `file_folder`;
CREATE TABLE `file_folder`  (
  `file_folder_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文件夹ID',
  `file_folder_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件夹名称',
  `parent_folder_id` int(11) NULL DEFAULT 0 COMMENT '父文件夹ID',
  `file_store_id` int(11) NULL DEFAULT NULL COMMENT '所属文件仓库ID',
  `file_folder_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件夹路径',
  PRIMARY KEY (`file_folder_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 236 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_folder
-- ----------------------------

-- ----------------------------
-- Table structure for file_store
-- ----------------------------
DROP TABLE IF EXISTS `file_store`;
CREATE TABLE `file_store`  (
  `file_store_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文件仓库ID',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '主人ID',
  `current_size` bigint(20) NULL DEFAULT 0 COMMENT '当前容量（单位KB）',
  `max_size` bigint(20) NULL DEFAULT 1048576 COMMENT '最大容量（单位KB）',
  PRIMARY KEY (`file_store_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 106 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_store
-- ----------------------------


-- ----------------------------
-- Table structure for my_file
-- ----------------------------
DROP TABLE IF EXISTS `my_file`;
CREATE TABLE `my_file`  (
  `my_file_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文件ID',
  `my_file_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名',
  `file_store_id` int(11) NULL DEFAULT NULL COMMENT '文件仓库ID',
  `my_file_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '/' COMMENT '文件存储路径',
  `download_count` int(11) NULL DEFAULT 0 COMMENT '下载次数',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更改时间',
  `parent_folder_id` int(11) NULL DEFAULT NULL COMMENT '父文件夹ID',
  `size` bigint(20) NULL DEFAULT NULL COMMENT '文件大小（字节）',
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件类型',
  PRIMARY KEY (`my_file_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1479 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of my_file
-- ----------------------------

-- ----------------------------
-- Table structure for temp_file
-- ----------------------------
DROP TABLE IF EXISTS `temp_file`;
CREATE TABLE `temp_file`  (
  `temp_file_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文件ID',
  `temp_file_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名',
  `temp_file_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '/' COMMENT '文件存储路径',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `download_count` int(11) NULL DEFAULT 0 COMMENT '下载次数',
  `update_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `size` int(11) NULL DEFAULT NULL COMMENT '文件大小',
  `type` int(11) NULL DEFAULT NULL COMMENT '文件类型',
  PRIMARY KEY (`temp_file_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 204 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of temp_file
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `user_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '昵称',
  `login_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录账号',
  `pass_word` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录密码',
  `user_type` int(11) NOT NULL COMMENT '用户类型 0:超级管理员,1:管理员,2:普通用户',
  `phone_number` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话号码',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像路径',
  `register_time` datetime NOT NULL COMMENT '注册时间',
  `token` int(11) NOT NULL COMMENT '用户令牌',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `login_name`(`login_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 93 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
