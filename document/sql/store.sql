CREATE TABLE `store`  (
                          `id` bigint(11) NOT NULL AUTO_INCREMENT,
                          `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                          `num` bigint(11) NULL DEFAULT NULL COMMENT '数量',
                          `create_time` datetime(0) NULL DEFAULT NULL,
                          `price` bigint(10) NULL DEFAULT NULL COMMENT '单价，单位分',
                          PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

INSERT INTO `store` VALUES (1, 'lzy', 1000, '2021-10-15 22:32:40', 100);


CREATE TABLE `undo_log`  (
                             `branch_id` bigint(20) NOT NULL COMMENT 'branch transaction id',
                             `xid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'global transaction id',
                             `context` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'undo_log context,such as serialization',
                             `rollback_info` longblob NOT NULL COMMENT 'rollback info',
                             `log_status` int(11) NOT NULL COMMENT '0:normal status,1:defense status',
                             `log_created` datetime(6) NOT NULL COMMENT 'create datetime',
                             `log_modified` datetime(6) NOT NULL COMMENT 'modify datetime',
                             UNIQUE INDEX `ux_undo_log`(`xid`, `branch_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'AT transaction mode undo table' ROW_FORMAT = Compact;