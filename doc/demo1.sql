/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80015
 Source Host           : localhost:3306
 Source Schema         : demo1

 Target Server Type    : MySQL
 Target Server Version : 80015
 File Encoding         : 65001

 Date: 17/07/2019 22:55:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `sys_oauth_client_details`;
CREATE TABLE `sys_oauth_client_details`  (
  `client_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `resource_ids` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `client_secret` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `scope` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `authorized_grant_types` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `authorities` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additional_information` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `autoapprove` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '终端信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oauth_client_details
-- ----------------------------
INSERT INTO `sys_oauth_client_details` VALUES ('api', null, 'api', 'user', 'client_credentials', null, null, null, null, '{\"limit_level\":2}', 'true');
INSERT INTO `sys_oauth_client_details` VALUES ('app', null, 'app', 'server', 'password,refresh_token', null, null, null, null, null, 'true');
INSERT INTO `sys_oauth_client_details` VALUES ('cloudx', null, 'cloudx', 'server', 'password,refresh_token,client_credentials,authorization_code', 'http://baidu.com', null, null, null, null, 'true');

-- ----------------------------
-- Table structure for sys_rate_limiter
-- ----------------------------
DROP TABLE IF EXISTS `sys_rate_limiter`;
CREATE TABLE `sys_rate_limiter`  (
  `limit_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `level` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '等级',
  `level_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '等级名称（描述）',
  `replenish_rate` int(11) NOT NULL COMMENT '流速',
  `burst_capacity` int(11) NOT NULL COMMENT '桶容量',
  `limit_type` int(10) NOT NULL COMMENT '单位 1:秒，2:分钟，3:小时，4:天',
  PRIMARY KEY (`limit_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_rate_limiter
-- ----------------------------
INSERT INTO `sys_rate_limiter` VALUES (1, '1', '1级', 10, 10, 1);
INSERT INTO `sys_rate_limiter` VALUES (2, '2', '2级', 20, 20, 1);
INSERT INTO `sys_rate_limiter` VALUES (3, '3', '3级', 30, 30, 2);
INSERT INTO `sys_rate_limiter` VALUES (4, '4', '4级', 40, 40, 1);
INSERT INTO `sys_rate_limiter` VALUES (5, '5', '5级', 500, 500, 1);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `salt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '随机盐',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '简介',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '头像',
  `create_time` timestamp(0) DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `lock_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '0-正常，9-锁定',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '0-正常，1-删除',
  `wx_openid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '微信openid',
  `qq_openid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'QQ openid',
  `limit_level` int(10) DEFAULT NULL COMMENT '限流等级',
  PRIMARY KEY (`user_id`) USING BTREE,
  INDEX `user_wx_openid`(`wx_openid`) USING BTREE,
  INDEX `user_qq_openid`(`qq_openid`) USING BTREE,
  INDEX `user_idx1_username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$HykYYbnn/y9IajyHmSmoOe78dIzrypa6m0cSieoA2KzVTqv840S6W', NULL, '13800000000', NULL, '2019-07-11 07:15:18', '2019-07-12 09:52:00', '0', '0', NULL, NULL, 1);
INSERT INTO `sys_user` VALUES (3, 'test0', '$2a$10$GqATgNUo/6BhcbHPp1DBkO2hD6v1bjWr9db1ffKc8W7lU5vi28XZi', NULL, NULL, NULL, '2019-07-12 09:52:15', NULL, '0', '0', NULL, NULL, 1);
INSERT INTO `sys_user` VALUES (4, 'test1', '$2a$10$OR34pxHtsUDT3QFCV5F0.eKdV6ct4AI8ngL35U.Fh2/12SOWawrH2', NULL, NULL, NULL, '2019-07-12 09:52:30', NULL, '0', '0', NULL, NULL, 1);
INSERT INTO `sys_user` VALUES (5, 'test2', '$2a$10$/vTwhZy0rtq6Tr3H3VQ66u93XsB3ayB2xN.gVcamEO5E4fhQKsZ4C', NULL, NULL, NULL, '2019-07-12 09:52:32', '2019-07-12 15:20:21', '0', '0', NULL, NULL, 3);
INSERT INTO `sys_user` VALUES (6, 'test3', '$2a$10$5ADM2gHUV7.K9x9cJidwde9aZP3UAK01OV07i2wvgl2uOMw3ISh7e', NULL, NULL, NULL, '2019-07-12 09:52:32', '2019-07-12 15:18:12', '0', '0', NULL, NULL, 3);
INSERT INTO `sys_user` VALUES (7, 'test4', '$2a$10$A/j8wGtVo/JkQkD0zeNxkuEVBHXZl/zx0NYgX9mjWkRsVtfmz1Nb6', NULL, NULL, NULL, '2019-07-12 09:52:33', NULL, '0', '0', NULL, NULL, 1);

SET FOREIGN_KEY_CHECKS = 1;
