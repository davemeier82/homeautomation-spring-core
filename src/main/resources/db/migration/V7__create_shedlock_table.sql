create table if not exists shedlock
(
    name       varchar(64) not null
        constraint shedlock_pk primary key,
    lock_until timestamp(3),
    locked_at  timestamp(3),
    locked_by  varchar(255)
);