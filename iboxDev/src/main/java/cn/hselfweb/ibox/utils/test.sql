/*
 Navicat Premium Data Transfer

 Source Server         : ibox
 Source Server Type    : MySQL
 Source Server Version : 50722
 Source Host           : hselfweb.cn:3306
 Source Schema         : ibox

 Target Server Type    : MySQL
 Target Server Version : 50722
 File Encoding         : 65001

 Date: 20/03/2019 21:22:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for family
-- ----------------------------
DROP TABLE IF EXISTS `family`;
CREATE TABLE `family`  (
  `fid` int(15) NOT NULL AUTO_INCREMENT COMMENT '家庭id',
  `uid` int(15) NOT NULL COMMENT '管理员id',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '家庭名称',
  `role` int(11) NULL DEFAULT NULL COMMENT '是否是管理员 1为管理员 0为非管理元',
  PRIMARY KEY (`fid`, `uid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2446 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '家庭' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of family
-- ----------------------------
INSERT INTO `family` VALUES (2345, 234, 'hello', 1);
INSERT INTO `family` VALUES (2436, 2, 'Gavin', 1);
INSERT INTO `family` VALUES (2437, 3, 'test1', 0);
INSERT INTO `family` VALUES (2438, 3, 'whatcani', 1);
INSERT INTO `family` VALUES (2439, 4, '{111}', 1);

-- ----------------------------
-- Table structure for food
-- ----------------------------
DROP TABLE IF EXISTS `food`;
CREATE TABLE `food`  (
  `food_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '食物id',
  `food_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '食物名称',
  `food_time` int(11) NULL DEFAULT NULL COMMENT '冷冻或冷冻时间',
  `food_url` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '食物图片',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型 0为冷藏 1为冷冻',
  `comment` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '食物三级食材备注',
  `food_weight` double NULL DEFAULT 0 COMMENT '食物重量',
  `food_parent` int(11) NULL DEFAULT 0 COMMENT '食物所属一级食材id',
  `percent` double(20, 5) NULL DEFAULT NULL COMMENT '保质期占比',
  PRIMARY KEY (`food_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 122333 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '食物表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of food
-- ----------------------------
INSERT INTO `food` VALUES (12, '牛肉', 32, 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544092859262&di=f98cd3b9b4226d36688b6188e566a6e8&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F0eb30f2442a7d933738af5baa74bd11372f001dc.jpg', 1, '牛腿', 23, 43, 0.00000);
INSERT INTO `food` VALUES (13, '蔬菜', 22, 'https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1544429767&di=05fe2a1bffaf5753c3153fc66d1386ed&src=http://pic119.nipic.com/file/20161228/17520916_101205248000_2.jpg', 1, '白菜', 123, 0, 0.20000);
INSERT INTO `food` VALUES (14, '猪肉', 321, 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544092814790&di=2125a2225addf212a8909f720ccaad8b&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3D29a66c66d0b44aed4d43b6a7db75ed74%2F2f738bd4b31c87010e35ddca2d7f9e2f0708ff1f.jpg', 2, '肘子', 12, 0, 0.10000);
INSERT INTO `food` VALUES (15, '海鲜', 10, 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544439929559&di=b376724a2d25e168e12018204a72c917&imgtype=0&src=http%3A%2F%2Ffile16.zk71.com%2FFile%2FCorpEditInsertImages%2F2017%2F11%2F21%2Fwuliu_3501_20171121152520.jpg', 2, '螃蟹', 234, 0, 0.30000);
INSERT INTO `food` VALUES (122332, '111', 123, '9999', 11, 'hhhh', 1123, 111, 5.50000);

-- ----------------------------
-- Table structure for icebox
-- ----------------------------
DROP TABLE IF EXISTS `icebox`;
CREATE TABLE `icebox`  (
  `ice_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '冰箱id  冰箱8位mac地址 底端真实 手机端虚拟(f开头）',
  `fid` int(11) NOT NULL COMMENT '家庭id',
  `ice_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '冰箱昵称',
  PRIMARY KEY (`ice_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '冰箱' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of icebox
-- ----------------------------
INSERT INTO `icebox` VALUES ('123456', 2345, 'hellowoerld');
INSERT INTO `icebox` VALUES ('242a497756bb4d079c4dab8b335beb8d', 2437, NULL);
INSERT INTO `icebox` VALUES ('345362', 2437, 'dfghdf');
INSERT INTO `icebox` VALUES ('89ef2450690f4379b7ff9841cc4b361a', 2439, 'hello');
INSERT INTO `icebox` VALUES ('89f3218e64df440aa2a556fd2e5aabc1', 2438, 'hello');
INSERT INTO `icebox` VALUES ('92aefd913f354fd0b1b9dc35313a440c', 2440, 'hello');
INSERT INTO `icebox` VALUES ('9d1382ecc08649a8b67a60b9c93dc081', 2436, 'myfridge');
INSERT INTO `icebox` VALUES ('c1ae374b853548068de242596813a457', 2441, '你好世界');
INSERT INTO `icebox` VALUES ('f0a908eb2d2e4cc4b5fbe72f1ccd34fa', 2436, 'hello');

-- ----------------------------
-- Table structure for officialcard
-- ----------------------------
DROP TABLE IF EXISTS `officialcard`;
CREATE TABLE `officialcard`  (
  `uuid` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '卡片id',
  `foodid` int(11) NULL DEFAULT NULL COMMENT '食物id',
  PRIMARY KEY (`uuid`) USING BTREE,
  INDEX `uuid`(`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '官方NFC卡' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of officialcard
-- ----------------------------
INSERT INTO `officialcard` VALUES ('111', 12);
INSERT INTO `officialcard` VALUES ('123', 13);
INSERT INTO `officialcard` VALUES ('124', 14);
INSERT INTO `officialcard` VALUES ('234', 15);

-- ----------------------------
-- Table structure for record
-- ----------------------------
DROP TABLE IF EXISTS `record`;
CREATE TABLE `record`  (
  `uuid` int(11) NOT NULL COMMENT 'NFC卡id',
  `ice_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '冰箱id',
  `fid` int(11) NOT NULL COMMENT '家庭id',
  `opflag` int(11) NULL DEFAULT NULL COMMENT '操作0存入 1取出 2续存',
  `opdate` timestamp(6) NULL DEFAULT NULL COMMENT '操作时间',
  `tareweight` int(11) NULL DEFAULT NULL COMMENT '皮重',
  `foodweight` int(11) NULL DEFAULT NULL COMMENT '食物重',
  `foodphoto` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`uuid`, `ice_id`, `fid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of record
-- ----------------------------
INSERT INTO `record` VALUES (111, '345362', 2347, 1, '2018-11-07 20:47:41.000000', 231, 123, NULL);
INSERT INTO `record` VALUES (123, '89f3218e64df440aa2a556fd2e5aabc1', 2324, 2, '2018-11-06 23:41:54.000000', 23, 123, NULL);
INSERT INTO `record` VALUES (124, '345362', 2347, 1, '2018-12-21 15:59:28.000000', 322, 232, NULL);
INSERT INTO `record` VALUES (234, '89f3218e64df440aa2a556fd2e5aabc1', 345, 1, '2018-11-22 20:39:46.000000', 123, 234, 'www.baidu.com');

-- ----------------------------
-- Table structure for unofficialcard
-- ----------------------------
DROP TABLE IF EXISTS `unofficialcard`;
CREATE TABLE `unofficialcard`  (
  `uuid` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'NFC卡的ID',
  `fid` int(11) NULL DEFAULT NULL COMMENT '家庭id',
  `food_url` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图地址片',
  `type` int(11) NULL DEFAULT NULL COMMENT '存入类型',
  `food_time` int(11) NULL DEFAULT NULL COMMENT '存入时间',
  `food_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '食物名称',
  `food_weight` int(11) NULL DEFAULT NULL COMMENT '食物重量',
  `percent` double(255, 5) NULL DEFAULT NULL COMMENT '保质期占比',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '非官方卡' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of unofficialcard
-- ----------------------------
INSERT INTO `unofficialcard` VALUES ('234', NULL, '234234', 1, 345, '乌龟', 34, 0.20000);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `uid` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `user_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `tel` varchar(50) CHARACTER SET utf32 COLLATE utf32_general_ci NULL DEFAULT NULL COMMENT '电话/账号',
  `info` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户信息',
  `head_url` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  PRIMARY KEY (`uid`) USING BTREE,
  INDEX `电话号码`(`tel`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 653 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (2, 'whatever', 'Gavin', '1234450', '北京北京', 'www.baidu.com');
INSERT INTO `user` VALUES (3, '123456', 'Gavin', '18846833759', '517084', 'www.myheadurl.com');
INSERT INTO `user` VALUES (4, '123', '123', '123', '123', 'wer');
INSERT INTO `user` VALUES (652, 'azaz9031', 'lg32', '18846791324', '没有男性', 'helloworld');

-- ----------------------------
-- Table structure for validation
-- ----------------------------
DROP TABLE IF EXISTS `validation`;
CREATE TABLE `validation`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '验证码id',
  `identity` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '验证码',
  `tel` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `duedate` timestamp(6) NULL DEFAULT NULL COMMENT '过期时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 47 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of validation
-- ----------------------------
INSERT INTO `validation` VALUES (20, '141892', '17645109385', '2018-12-03 21:51:03.843000');
INSERT INTO `validation` VALUES (46, '778476', '17645109385', '2019-02-05 18:14:57.296000');

SET FOREIGN_KEY_CHECKS = 1;
