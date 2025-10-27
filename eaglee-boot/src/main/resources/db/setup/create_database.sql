-- 鹰眼监控系统数据库创建脚本
-- 用于初始化数据库和用户

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `eaglee_db` 
DEFAULT CHARACTER SET utf8mb4 
DEFAULT COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS `eaglee_dev` 
DEFAULT CHARACTER SET utf8mb4 
DEFAULT COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS `eaglee_test` 
DEFAULT CHARACTER SET utf8mb4 
DEFAULT COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS `eaglee_prod` 
DEFAULT CHARACTER SET utf8mb4 
DEFAULT COLLATE utf8mb4_unicode_ci;

-- 创建用户（如果不存在）
CREATE USER IF NOT EXISTS 'eaglee'@'%' IDENTIFIED BY 'eaglee123';
CREATE USER IF NOT EXISTS 'eaglee_dev'@'%' IDENTIFIED BY 'eaglee_dev';
CREATE USER IF NOT EXISTS 'eaglee_test'@'%' IDENTIFIED BY 'eaglee_test';
CREATE USER IF NOT EXISTS 'eaglee_prod'@'%' IDENTIFIED BY 'eaglee_prod_secure_password';

-- 授权
GRANT ALL PRIVILEGES ON `eaglee_db`.* TO 'eaglee'@'%';
GRANT ALL PRIVILEGES ON `eaglee_dev`.* TO 'eaglee_dev'@'%';
GRANT ALL PRIVILEGES ON `eaglee_test`.* TO 'eaglee_test'@'%';
GRANT ALL PRIVILEGES ON `eaglee_prod`.* TO 'eaglee_prod'@'%';

-- 刷新权限
FLUSH PRIVILEGES;

-- 显示创建的数据库
SHOW DATABASES LIKE 'eaglee%';