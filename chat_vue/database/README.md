# 数据库说明

## user 表结构

### 表名
`user` - 用户表

### 字段说明

| 字段名 | 类型 | 说明 | 备注 |
|--------|------|------|------|
| id | INT UNSIGNED | 用户ID | 主键，自增 |
| username | VARCHAR(50) | 用户名 | 唯一，不能为空 |
| password | VARCHAR(255) | 密码 | 加密存储，不能为空 |
| email | VARCHAR(100) | 邮箱 | 唯一 |
| phone | VARCHAR(20) | 手机号 | - |
| nickname | VARCHAR(50) | 昵称 | - |
| avatar | VARCHAR(255) | 头像URL | - |
| status | TINYINT | 状态 | 0-禁用，1-启用，默认1 |
| is_online | TINYINT | 在线状态 | 0-离线，1-在线，默认0 |
| last_login_time | DATETIME | 最后登录时间 | - |
| last_login_ip | VARCHAR(50) | 最后登录IP | - |
| created_at | DATETIME | 创建时间 | 默认当前时间 |
| updated_at | DATETIME | 更新时间 | 自动更新 |

### 索引
- `idx_username` - 用户名索引
- `idx_email` - 邮箱索引
- `idx_status` - 状态索引

## 使用方法

### 1. 创建数据库
```sql
CREATE DATABASE chat_platform DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
USE chat_platform;
```

### 2. 创建 user 表
```bash
mysql -u root -p chat_platform < database/user.sql
```

或者在 MySQL 客户端中执行：
```sql
source d:/study/social_media_platform/chat_vue/database/user.sql
```

### 3. 插入测试数据（可选）
```bash
mysql -u root -p chat_platform < database/user_data.sql
```

或者在 MySQL 客户端中执行：
```sql
source d:/study/social_media_platform/chat_vue/database/user_data.sql
```

## 测试账号

| 用户名 | 密码 | 邮箱 | 昵称 |
|--------|------|------|------|
| zhangsan | 123456 | zhangsan@example.com | 张三 |
| lisi | 123456 | lisi@example.com | 李四 |
| wangwu | 123456 | wangwu@example.com | 王五 |
| admin | 123456 | admin@example.com | 管理员 |

注意：测试数据中的密码是使用 bcrypt 加密的，原始密码都是 `123456`

## 注意事项

1. 密码字段建议使用 bcrypt 或其他安全的加密算法进行加密存储
2. 生产环境中应该使用环境变量配置数据库连接信息
3. 建议定期备份数据库
4. 根据实际需求可以添加更多字段，如性别、生日、地址等
