CREATE DATABASE db_barbershop;

USE db_barbershop;

CREATE TABLE usuario(
id bigint primary key auto_increment,
nome varchar(75) not null,
usuario varchar(50) not null unique,
senha varchar(100) not null,
perfil varchar(10) not null default 'PADRÃO',
estado boolean not null default true,
data_hora_criacao datetime default current_timestamp,
ultimo_login datetime default '0001-01-01 00:00:00'
);
