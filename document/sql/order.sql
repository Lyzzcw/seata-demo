CREATE TABLE `order`  (
                            `id` bigint(11) NOT NULL AUTO_INCREMENT,
                            `product_id` bigint(11) NULL DEFAULT NULL COMMENT '商品Id',
                            `num` bigint(11) NULL DEFAULT NULL COMMENT '数量',
                            `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户唯一Id',
                            `create_time` datetime(0) NULL DEFAULT NULL,
                            `status` int(1) NULL DEFAULT NULL COMMENT '订单状态 1 未付款 2 已付款 3 已完成',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

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