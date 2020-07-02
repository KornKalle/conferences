create table api_key (id bigint not null auto_increment, version bigint not null, last_connected_at datetime, created_at datetime not null, comment varchar(255), valid_until datetime, type varchar(255) not null, `key` varchar(255) not null, primary key (id)) engine=InnoDB;
create table role (id bigint not null auto_increment, version bigint not null, authority varchar(255) not null, primary key (id)) engine=InnoDB;
create table room (id bigint not null auto_increment, version bigint not null, pin integer not null, created_at datetime not null, jitsi_room_name varchar(255) not null, opening_at datetime, created_by_uid varchar(255), type varchar(255) not null, closing_at datetime, link varchar(255) not null, primary key (id)) engine=InnoDB;
create table user (id bigint not null auto_increment, version bigint not null, password_expired bit not null, username varchar(255) not null, account_locked bit not null, `password` varchar(255) not null, account_expired bit not null, enabled bit not null, primary key (id)) engine=InnoDB;
create table user_role (user_id bigint not null, role_id bigint not null, primary key (user_id, role_id)) engine=InnoDB;
alter table role add constraint UK_irsamgnera6angm0prq1kemt2 unique (authority);
alter table user add constraint UK_sb8bbouer5wak8vyiiy4pf2bx unique (username);
alter table user_role add constraint FK859n2jvi8ivhui0rl0esws6o foreign key (user_id) references user (id);
alter table user_role add constraint FKa68196081fvovjhkek5m97n3y foreign key (role_id) references role (id);

insert into role (id, version, authority)
values
(1,0,'ROLE_USER'),
(2,0,'ROLE_ADMIN');

insert into user (id, version, password_expired, username, account_locked, password, account_expired, enabled)
values
(1,0,b'0','admin',b'0','{bcrypt}$2a$10$zm6w7hSrMpIvG4xTaT3bN.Wgku4b3tXJUJh7inCEEV1PgUz37dAPa',b'0',b'1');

insert into user_role (user_id, role_id)
values
(1,2);

insert into api_key (version, created_at, type, `key`) values
(0, '2020-01-01 00:00:00', 'CALLSERVER', '9TNuDtfG2lVqipz4Y2yM6Op0N9Zf8HoFXFaCdRrPKvqYmzPcGZ6YQdxhZSls3JJj');

insert into api_key (version, created_at, type, `key`) values
(0, '2020-01-01 00:00:00', 'RESERVATIONSYSTEM', 'mv9RAzjifJzuXQgL81viw9hmpEr9fy93oCglCw3eOlo8ZqJoNTwNsJyAOK3yVsph');

insert into room (version, pin, created_at, jitsi_room_name, type, link) values
(0, 1234 ,'2020-01-01 00:00:00', 'testRoom', 'PERMANENT', 'https://meet.example.com/testRoom');