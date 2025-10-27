-- 鹰眼监控系统数据库初始化脚本
-- 在MySQL容器启动时自动执行

-- 设置字符集
SET NAMES utf8mb4;

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS `eaglee_prod` 
DEFAULT CHARACTER SET utf8mb4 
DEFAULT COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS `eaglee_dev` 
DEFAULT CHARACTER SET utf8mb4 
DEFAULT COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS `eaglee_test` 
DEFAULT CHARACTER SET utf8mb4 
DEFAULT COLLATE utf8mb4_unicode_ci;

-- 创建用户（如果不存在）
CREATE USER IF NOT EXISTS 'eaglee_prod'@'%' IDENTIFIED BY 'eaglee_prod_secure_password';
CREATE USER IF NOT EXISTS 'eaglee_dev'@'%' IDENTIFIED BY 'eaglee_dev';
CREATE USER IF NOT EXISTS 'eaglee_test'@'%' IDENTIFIED BY 'eaglee_test';

-- 授权
GRANT ALL PRIVILEGES ON `eaglee_prod`.* TO 'eaglee_prod'@'%';
GRANT ALL PRIVILEGES ON `eaglee_dev`.* TO 'eaglee_dev'@'%';
GRANT ALL PRIVILEGES ON `eaglee_test`.* TO 'eaglee_test'@'%';

-- 刷新权限
FLUSH PRIVILEGES;

-- 显示创建的数据库
SELECT 'Database initialization completed!' AS message;
SHOW DATABASES LIKE 'eaglee%';