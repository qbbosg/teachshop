create table course
(
    id         serial primary key,
    title      varchar(200) not null,
    describe   text,
    price      int,                                -- cent unit
    status      varchar(100) not null default 'OK', -- OK, NO
    created_at timestamp    not null default now(),
    updated_at timestamp    not null default now()
);
create table video
(
    id         serial primary key,
    title      varchar(200) not null,
    describe   text,
    link       varchar(200) not null,
    price      int,                                -- cent unit
    course_id  int,
    status      varchar(100) not null default 'OK', -- OK, NO
    created_at timestamp    not null default now(),
    updated_at timestamp    not null default now()
);


