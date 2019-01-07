/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.100.2
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : 192.168.100.2:3306
 Source Schema         : MS

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 07/01/2019 21:53:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ms_goods
-- ----------------------------
DROP TABLE IF EXISTS `ms_goods`;
CREATE TABLE `ms_goods`  (
  `goods_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `goods_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商品名称',
  `goods_title` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商品标题',
  `goods_img` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商品图片',
  `goods_detail` longtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '商品详情',
  `goods_price` decimal(10, 2) NOT NULL DEFAULT 0.00,
  `seckill_price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '秒杀价',
  `goods_stock` int(11) DEFAULT 0 COMMENT '商品库存，-1表示没有限制',
  `start_date` datetime(0) NOT NULL COMMENT '秒杀开始时间',
  `end_date` datetime(0) NOT NULL COMMENT '秒杀结束时间',
  `version` int(11) NOT NULL COMMENT '并发版本控制',
  PRIMARY KEY (`goods_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ms_goods
-- ----------------------------
INSERT INTO `ms_goods` VALUES (1, 'iphoneX', 'IPhone', '/img/iphonex.png', 'Apple', 7788.00, 1.00, 100, '2018-12-01 00:00:00', '2020-12-30 00:00:00', 1);
INSERT INTO `ms_goods` VALUES (2, '华为 Mate 10', 'Huawei/华为 Mate 10 6G+128G 全网通4G智能手机', '/img/meta10.png', 'Huawei/华为 Mate 10 6G+128G 全网通4G智能手机', 4199.00, 1.00, 100, '2018-12-01 00:00:00', '2020-12-30 00:00:00', 1);

-- ----------------------------
-- Table structure for ms_order
-- ----------------------------
DROP TABLE IF EXISTS `ms_order`;
CREATE TABLE `ms_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `goods_id` bigint(20) NOT NULL,
  `goods_count` int(11) NOT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '订单状态，0新建未支付，1已支付，2已发货，3已收货，4已退款，5已完成',
  `create_date` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sk_user
-- ----------------------------
DROP TABLE IF EXISTS `sk_user`;
CREATE TABLE `sk_user`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '用户id',
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '昵称',
  `password` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'MD5(MD5(pass明文+固定salt)+salt',
  `salt` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '混淆盐',
  `head` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '头像，云存储的ID',
  `register_date` datetime(0) DEFAULT NULL COMMENT '注册时间',
  `last_login_date` datetime(0) DEFAULT NULL COMMENT '上次登录时间',
  `login_count` int(11) DEFAULT NULL COMMENT '登录次数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sk_user
-- ----------------------------
INSERT INTO `sk_user` VALUES (13000000000, 'user0', 'b7797cce01b4b131b433b6acf4add449', '1a2b3c', NULL, '2018-12-03 06:54:49', NULL, 1);
INSERT INTO `sk_user` VALUES (18181818181, 'jesper', 'b7797cce01b4b131b433b6acf4add449', '1a2b3c4d', NULL, '2018-05-21 21:10:21', '2018-05-21 21:10:25', 1);
INSERT INTO `sk_user` VALUES (18217272828, 'jesper', 'b7797cce01b4b131b433b6acf4add449', '1a2b3c4d', NULL, '2018-05-21 21:10:21', '2018-05-21 21:10:25', 1);

SET FOREIGN_KEY_CHECKS = 1;
