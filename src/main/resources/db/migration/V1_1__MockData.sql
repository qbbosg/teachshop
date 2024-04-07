insert into member (username, encrypt_password) values ( 'li', 'pwd1' );
insert into member (username, encrypt_password) values ( 'wu', 'pwd1' );
insert into member (username, encrypt_password) values ( 'liu', 'pwd1' );
insert into member (username, encrypt_password) values ( 'xu', 'pwd1' );
insert into member (username, encrypt_password) values ( 'lei', 'pwd1' );

insert into role (name) values ( 'student' );
insert into role (name) values ( 'teacher' );
insert into role (name) values ( 'admin' );
insert into role (name) values ( 'root' );

insert into permission (name, role_id) values ( 'login', 1 );
insert into permission (name, role_id) values ( 'login', 2 );
insert into permission (name, role_id) values ( 'upload', 2 );
insert into permission (name, role_id) values ( 'mod_user', 3 );
insert into permission (name, role_id) values ( 'upload', 3 );
insert into permission (name, role_id) values ( 'mod_user', 4 );
insert into permission (name, role_id) values ( 'upload', 4 );
insert into permission (name, role_id) values ( 'mod_admin', 4 );

insert into member_role (member_id, role_id) values ( 1, 1 );
insert into member_role (member_id, role_id) values ( 2, 1 );
insert into member_role (member_id, role_id) values ( 3, 2 );
insert into member_role (member_id, role_id) values ( 4, 3 );
insert into member_role (member_id, role_id) values ( 5, 4 );
