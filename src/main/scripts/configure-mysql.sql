# docker run --name mysqldb -p 3306:3306 -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -d mysql

# connect to mysql and run as root user
#Create Databases
CREATE DATABASE ppm_dev;
CREATE DATABASE ppm_prod;

#Create database service accounts
CREATE USER 'ppm_dev_user'@'localhost' IDENTIFIED BY 'ppmv1';
CREATE USER 'ppm_prod_user'@'localhost' IDENTIFIED BY 'ppmv1';
CREATE USER 'ppm_dev_user'@'%' IDENTIFIED BY 'ppmv1';
CREATE USER 'ppm_prod_user'@'%' IDENTIFIED BY 'ppmv1';

#Database grants
GRANT SELECT ON ppm_dev.* to 'ppm_dev_user'@'localhost';
GRANT INSERT ON ppm_dev.* to 'ppm_dev_user'@'localhost';
GRANT DELETE ON ppm_dev.* to 'ppm_dev_user'@'localhost';
GRANT UPDATE ON ppm_dev.* to 'ppm_dev_user'@'localhost';
GRANT SELECT ON ppm_prod.* to 'ppm_prod_user'@'localhost';
GRANT INSERT ON ppm_prod.* to 'ppm_prod_user'@'localhost';
GRANT DELETE ON ppm_prod.* to 'ppm_prod_user'@'localhost';
GRANT UPDATE ON ppm_prod.* to 'ppm_prod_user'@'localhost';
GRANT SELECT ON ppm_dev.* to 'ppm_dev_user'@'%';
GRANT INSERT ON ppm_dev.* to 'ppm_dev_user'@'%';
GRANT DELETE ON ppm_dev.* to 'ppm_dev_user'@'%';
GRANT UPDATE ON ppm_dev.* to 'ppm_dev_user'@'%';
GRANT SELECT ON ppm_prod.* to 'ppm_prod_user'@'%';
GRANT INSERT ON ppm_prod.* to 'ppm_prod_user'@'%';
GRANT DELETE ON ppm_prod.* to 'ppm_prod_user'@'%';
GRANT UPDATE ON ppm_prod.* to 'ppm_prod_user'@'%';