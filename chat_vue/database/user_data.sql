INSERT INTO `user` (`username`, `password`, `email`, `phone`, `nickname`, `avatar`, `status`, `is_online`, `last_login_time`, `last_login_ip`) VALUES
('zhangsan', '$2b$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'zhangsan@example.com', '13800138001', '张三', 'https://api.dicebear.com/7.x/avataaars/svg?seed=zhangsan', 1, 1, NOW(), '192.168.1.100'),
('lisi', '$2b$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'lisi@example.com', '13800138002', '李四', 'https://api.dicebear.com/7.x/avataaars/svg?seed=lisi', 1, 0, NOW(), '192.168.1.101'),
('wangwu', '$2b$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'wangwu@example.com', '13800138003', '王五', 'https://api.dicebear.com/7.x/avataaars/svg?seed=wangwu', 1, 1, NOW(), '192.168.1.102'),
('admin', '$2b$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'admin@example.com', '13800138000', '管理员', 'https://api.dicebear.com/7.x/avataaars/svg?seed=admin', 1, 0, NOW(), '192.168.1.1');
