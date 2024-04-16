##1 db 생성
create database apidb;

##2 사용자 생성      identified by   'password'
create user 'apidbuser'@'localhost' identified by 'apidbuser';
## 외부에서 연결 가능한 계정 생성
create user 'apidbuser'@'%' identified by 'apidbuser';

##3 권한 생성
grant all privileges on apidb.* to 'apidbuser'@'localhost';
grant all privileges on apidb.* to 'apidbuser'@'%';
