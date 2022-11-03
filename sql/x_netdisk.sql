/*
 Navicat Premium Data Transfer

 Source Server         : wen腾讯云
 Source Server Type    : MySQL
 Source Server Version : 50738
 Source Host           : 1.117.95.71:3306
 Source Schema         : x_netdisk

 Target Server Type    : MySQL
 Target Server Version : 50738
 File Encoding         : 65001

 Date: 31/08/2022 22:58:40
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
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 536 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `user_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '昵称',
  `login_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录账号',
  `pass_word` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录密码',
  `user_type` int(11) NOT NULL DEFAULT 2 COMMENT '用户类型 -1:超级管理员,1:管理员,2:普通用户',
  `phone_number` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话号码',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '/#' COMMENT '头像路径',
  `register_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '注册时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `login_name`(`login_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 940462908 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
