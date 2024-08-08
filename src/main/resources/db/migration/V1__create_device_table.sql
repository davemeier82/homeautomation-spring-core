create table if not exists device
(
    id           uuid not null
        constraint device_id_pk primary key,
    device_id    text not null,
    device_type  text not null,
    display_name text,
    constraint device_id_uk unique (device_id, device_type)
);

