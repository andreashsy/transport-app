drop database if exists bus_users;

create database bus_users;

use bus_users;

create table users(
    username varchar(64) not null,
    password varchar(512) not null,
    notification_token varchar(512),
    telegram_username varchar(64),

    primary key (username)
);

create table bus_stops_favourites(
    bus_stop_id char(5) not null,
    username varchar(64) not null,

    primary key (bus_stop_id, username),
    constraint fk_username
        foreign key (username)
        references users(username)
);

create table notifications(
    task_id char(16) not null,
    username varchar(64) not null,
    cron_time varchar(20) not null,
    bus_stop_id char(5) not null,
    primary key (task_id),
    constraint fk_username_notification
        foreign key (username)
        references users(username)
);

