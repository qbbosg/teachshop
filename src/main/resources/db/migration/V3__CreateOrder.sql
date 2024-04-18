create table course_order
(
    id         serial primary key,
    trade_no   varchar(255),
    course_id  int,
    member_id  int,
    price      int,                                   -- cent unit
    number     int,
    status     varchar(10) not null default 'UNPAID', -- UNPAID, PAID, DELETED, TIMEOUT, REFUND
    created_at timestamp not null default now(),
    updated_at timestamp   not null default now()
);