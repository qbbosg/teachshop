create table member (
    id serial primary key,
    username varchar(50) not null,
    encrypt_password varchar(50) not null,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    status varchar(10) not null default 'OK'
);
create table role (
    id serial primary key,
    name varchar(50) not null,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    status varchar(10) not null default 'OK'
);
create table permission (
    id serial primary key,
    role_id integer not null,
    name varchar(50) not null,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    status varchar(10) not null default 'OK'
);

create table member_role (
    id serial primary key,
    member_id integer not null ,
    role_id integer not null
)
