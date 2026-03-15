CREATE TABLE `chat_message` (
  `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '消息ID',
  `sender_id` INT UNSIGNED NOT NULL COMMENT '发送者ID',
  `receiver_id` INT UNSIGNED NOT NULL COMMENT '接收者ID',
  `content` TEXT NOT NULL COMMENT '消息内容',
  `message_type` TINYINT DEFAULT 1 COMMENT '消息类型：1-文本，2-图片，3-文件',
  `is_read` TINYINT DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  INDEX `idx_sender_receiver` (`sender_id`, `receiver_id`),
  INDEX `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='聊天消息表';
